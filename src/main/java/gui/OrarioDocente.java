package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class OrarioDocente {
    JFrame frame;
    private Controller controller;
    private JFrame frameChiamante;
    private JPanel panel1;
    private JTable tabellaOrario;
    private JButton indietroButton;

    public OrarioDocente(Controller c, JFrame f){
        frameChiamante=f;
        controller=c;
        frame = new JFrame("Schermata Orario Docente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        if (indietroButton != null) {
            caricaEvents();
        }

        // CORREZIONE 2: Passa la tabella solo se è stata inizializzata
        if (tabellaOrario != null) {
            creaTable(tabellaOrario);
        }
    }


    private void caricaEvents(){
        if(indietroButton != null) {
            indietroButton.addActionListener(e -> {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();

            });
        }
    }
    private void creaTable(JTable table){
       DefaultTableModel model=new DefaultTableModel(null,
               new String[]{"Lunedi","Martedi","Mercoledi","Giovedi","Venerdi"} );
        table.setModel(model);
        //non rende editable la tabella
        table.setDefaultEditor(Object.class, null);
      configuraTable(table);
        //inseriamo manualmente i dati per un test
        model.addRow(new Object[]{"Orario: 15:00-16:00\nAula: 101","Orario: 10:00-11:00\nAula: 102","","",""});

    }
    private void configuraTable(JTable table){
        //TableCellRenderer gestisce la creazione delle celle della tabella
        TableCellRenderer orarioRender= new TableCellRenderer() {
            final JTextArea area= new JTextArea(){{
                //torna a capo automaticamente e non rende editabile l'area di testo
                setLineWrap(true);
                setWrapStyleWord(true);
                setEditable(false);
                setMaximumSize(new Dimension(70,90));
            }};
            @Override
            //questo metodo viene chiamto ogni volta che la tabella crea una cella, e permette di personalizzare il modo in cui viene renderizzata la cella
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    area.setText(value.toString());
                    area.setFont(table.getFont());
                    area.setSize(table.getColumnModel().getColumn(column).getWidth(),table.getRowHeight());
                    area.setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getWidth(),table.getRowHeight()));
                    //ritorna una textare, cosi da poter vedere il testo su piu righe
                    return area;
            }
        };
        //ogni colonna della tabella viene configurata con il render creato in precedenza
        //cosi ogni cella avrà una text are
        for(int i=0;i<table.getColumnCount();i++){
            table.getColumnModel().getColumn(i).setCellRenderer(orarioRender);;
        }
        table.setRowHeight(50);
    }
    private void caricaLezioni(JTable table,Object[][] data){
        DefaultTableModel model= (DefaultTableModel) table.getModel();
        for(Object[] riga:data){
            model.addRow(riga);
        }

    }

}

