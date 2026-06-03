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
        richiestaTable.setSelectionBackground(Color.BLUE);
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
       Object[][] data=controller.ottieniRichiesteInviate();
        richiestaTable.setModel(new DefaultTableModel(data,
                new String[]{"Orario Lezione","Orario Nuovo","Motivo","Stato"} ) );
    }

}
