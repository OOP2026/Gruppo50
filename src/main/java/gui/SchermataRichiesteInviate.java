package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    }
    private void creaTable(){

        richiestaTable.setModel(new DefaultTableModel(null,
                new String[]{"Giorno Lezione","Orario Lezione","Giorno nuovo","Orario Nuovo","Motivo","Stato"} ) );
    }

}
