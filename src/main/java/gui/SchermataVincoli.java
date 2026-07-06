package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.Controller;

import java.awt.*;
import java.util.Objects;

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
    private JTextField oraInizioText;
    private JTextField minutiInizioText;
    private JPanel panelButtons;
    private JPanel panelOrarioInizio;
    private JComboBox<String> giorniBox;
    final private Controller controller;
   final private JFrame frameChiamante;

    public SchermataVincoli(Controller c,JFrame f){
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Schermata Vincoli");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        if (indietroButton != null) {
            caricaEvents();
        }

        //Passa la tabella solo se è stata inizializzata
        if (tabellaVincoli != null) {
            creaTable();
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
        aggiungiButton.addActionListener(e->{
    if(!checkCampi()) return;
    int oraFine=Integer.parseInt( oraFineText.getText());
            int minutiFine=Integer.parseInt( minutiFineText.getText());
            int oraInizio=Integer.parseInt( oraInizioText.getText());
            int minutiInizio=Integer.parseInt( minutiInizioText.getText());
            String giorno= Objects.requireNonNull(giorniBox.getSelectedItem()).toString().toLowerCase();
            String ex=controller.aggiungiVincolo(giorno,oraInizio,minutiInizio,oraFine,minutiFine);
            if(ex!=null){
                JOptionPane.showMessageDialog(frame,ex,"Errore",JOptionPane.ERROR_MESSAGE);
                return;
            }
            resetCampi();
            creaTable();
        });

        rimuoviButton.addActionListener(e->{
            int indice=tabellaVincoli.getSelectedRow();
            if(indice==-1){
                JOptionPane.showMessageDialog(frame,"Seleziona un vincolo da rimuovere","Errore",JOptionPane.ERROR_MESSAGE);
                return;
            }
            String ex=controller.rimuoviVincolo(indice);
                if(ex!=null){
                    JOptionPane.showMessageDialog(frame,ex,"Errore",JOptionPane.ERROR_MESSAGE);
                    return;
                }


                creaTable();
        });


    }
private boolean checkCampi(){
    //controlla se tutti i campi sono stati compilati
    if(oraFineText.getText().isEmpty() || minutiFineText.getText().isEmpty() || oraInizioText.getText().isEmpty() || minutiInizioText.getText().isEmpty()){
        JOptionPane.showMessageDialog(frame,"Compila tutti i campi","Errore",JOptionPane.WARNING_MESSAGE);
        return false;
    }
    try{
        Integer.parseInt( oraFineText.getText());
        Integer.parseInt( minutiFineText.getText());
        Integer.parseInt( oraInizioText.getText());
        Integer.parseInt( minutiInizioText.getText());
    }catch(NumberFormatException ex){
        JOptionPane.showMessageDialog(frame,"Inserisci valori numerici validi per orari e minuti","Errore",JOptionPane.WARNING_MESSAGE);
        return false;
    }

    return true;
}
private void resetCampi(){
    oraFineText.setText("");
    minutiFineText.setText("");
    oraInizioText.setText("");
    minutiInizioText.setText("");
}

    private void creaTable(){
        Object[][] data=controller.ottieniVincoli();
        tabellaVincoli.setModel(new DefaultTableModel(data,
                new String[]{"Vincoli"} ) );
        //Per centrare il testo delle righe
        DefaultTableCellRenderer centerRender= new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tabellaVincoli.getColumnCount();i++){
            tabellaVincoli.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
        //non rende possibile modificare le righe all'utente
        tabellaVincoli.setDefaultEditor(Object.class, null);
        //quando le righe vengono cliccati diventano grigio chiaro
        tabellaVincoli.setSelectionBackground(Color.LIGHT_GRAY);
    }


}
