package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.Controller;

import java.awt.*;
import java.util.Objects;
@SuppressWarnings("unused")
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
             dialogErrore_Warning(ex,0);
                return;
            }
            resetCampi();
            creaTable();
        });

        rimuoviButton.addActionListener(e->{
            int indice=tabellaVincoli.getSelectedRow();
            if(indice==-1){
             dialogErrore_Warning("Seleziona un vincolo da rimuovere",1);
                return;
            }
            String ex=controller.rimuoviVincolo(indice);
                if(ex!=null){
                    dialogErrore_Warning(ex,0);
                    return;
                }


                creaTable();
        });


    }
private boolean checkCampi(){
    //controlla se tutti i campi sono stati compilati
    if(oraFineText.getText().isEmpty() || minutiFineText.getText().isEmpty() || oraInizioText.getText().isEmpty() || minutiInizioText.getText().isEmpty()){
dialogErrore_Warning("Compila tutti i campi",1);
        return false;
    }
    try{
        Integer.parseInt( oraFineText.getText());
        Integer.parseInt( minutiFineText.getText());
        Integer.parseInt( oraInizioText.getText());
        Integer.parseInt( minutiInizioText.getText());
    }catch(NumberFormatException ex){
        dialogErrore_Warning("Inserisci valori numerici validi per orari e minuti",1);
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
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<tabellaVincoli.getColumnCount();i++){
            tabellaVincoli.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
        //non rende possibile modificare le righe all'utente
        tabellaVincoli.setDefaultEditor(Object.class, null);
        //quando le righe vengono cliccati diventano grigio chiaro
        tabellaVincoli.setSelectionBackground(Color.LIGHT_GRAY);
    }

    private void dialogErrore_Warning(String e,int tipo){
        switch (tipo){
            case 0:{
                JOptionPane.showMessageDialog(frame,e,"Errore",JOptionPane.ERROR_MESSAGE);
                break;
            }
            case 1:{
                JOptionPane.showMessageDialog(frame,e,"Attenzione",JOptionPane.WARNING_MESSAGE);
                break;
            }
            default:{ break; }
        }

    }


}
