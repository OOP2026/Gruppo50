package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SchermataRichiesteInviate {
    JFrame frame;
    private JPanel panel1;
    private JButton indietroButton;
    private JTable richiestaTable;

    JFrame frameChiamante;
    Controller controller;
    public SchermataRichiesteInviate(Controller c, JFrame f){
        frameChiamante=f;
        controller=c;
        frame = new JFrame("Schermata Richieste inviate");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        caricaEvents();
        creaTable();
    }

    private void caricaEvents(){
        indietroButton.addActionListener(e->{
            frame.setVisible(false);
            frameChiamante.setVisible(true);
            frame.dispose();

        });
        richiestaTable.getSelectionModel().addListSelectionListener(e->{
            int riga= richiestaTable.getSelectedRow();
            if(riga==-1|| !e.getValueIsAdjusting()) return;
            String motivo= richiestaTable.getValueAt(riga,2).toString();
            JTextArea textArea = new JTextArea(motivo);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            //textArea.setBackground(new Color(255, 255, 255, 0) );
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(120, 100));
            JOptionPane.showMessageDialog(frame,scrollPane,"Motivo della richiesta: ",JOptionPane.INFORMATION_MESSAGE);
            richiestaTable.getSelectionModel().clearSelection();
        });
    }
    private void creaTable(){
       Object[][] data=controller.ottieniRichiesteInviate();
        richiestaTable.setModel(new DefaultTableModel(data,
                new String[]{"Orario Lezione","Orario Nuovo","Motivo","Stato"} ) );
       //non rende editable la tabella
        richiestaTable.setDefaultEditor(Object.class, null);
    //
        richiestaTable.setSelectionBackground(Color.LIGHT_GRAY);
    }

}
