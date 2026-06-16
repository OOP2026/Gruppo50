package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SchermataInsegnamentiDocente {
    JFrame frame;
    private JPanel panelAddInsegnamenti;
    private JTable tabellaInsegnamenti;
    private JPanel panelButtons;
    private JButton aggiungiButton;
    private JButton indietroButton;
    private JComboBox insegnamentiBox;
    private JPanel panel1;
    private Controller controller;
    private JFrame frameChiamante;
    public SchermataInsegnamentiDocente(Controller c, JFrame f) {
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Schermata Insegnamenti");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        caricaEvents();
        creaTable();

    }

    private void caricaEvents(){

        if(indietroButton != null) {
            indietroButton.addActionListener(e -> {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();

            });
        }
        if(aggiungiButton!=null){
        aggiungiButton.addActionListener(e->{
            if(insegnamentiBox.getSelectedIndex()==0){
                JOptionPane.showMessageDialog(frame,"Non hai selezionato un'insegnamento.");
                return;
            }

            insegnamentiBox.setSelectedIndex(0);
            JOptionPane.showMessageDialog(frame,"Insegnamento aggiunto.");

        });
        }

    }
    private void creaTable(){
        Object[][] data=null;
        tabellaInsegnamenti.setModel(new DefaultTableModel(data,
                new String[]{"Nome","Cfu","Anno"} ) );
        //Per centrare il testo delle righe
        DefaultTableCellRenderer centerRender= new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tabellaInsegnamenti.getColumnCount();i++){
            tabellaInsegnamenti.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
        //non rende possibile modificare le righe all'utente
        tabellaInsegnamenti.setDefaultEditor(Object.class, null);
        //quando le righe vengono cliccati diventano grigio chiaro
        tabellaInsegnamenti.setSelectionBackground(Color.LIGHT_GRAY);
    }
}
