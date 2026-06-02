package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.Controller;
public class SchermataVincoli {
    JFrame frame;
    private JPanel panel1;
    private JTable tabellaVincoli;
    private JPanel panelGestioneVincoli;
    private JPanel panelOrarioFine;
    private JTextField oraFineText;
    private JTextField minutiFineText;
    private JButton aggiungiButton;
    private JButton rimuoviButton;
    private JButton indietroButton;
    private JTextField oraInizioFIneText;
    private JTextField minutiFIneText;
    private JPanel panelButtons;
    private JPanel panelOrarioInizio;
    private JComboBox giorniBox;
    private Controller controller;
    private JFrame frameChiamante;

    public SchermataVincoli(Controller c,JFrame f){
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Schermata Vincoli");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
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

        tabellaVincoli.setModel(new DefaultTableModel(null,
                new String[]{"Giorno","Orario"} ) );
    }


}
