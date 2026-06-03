package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    private JTextField oraInizioText;
    private JTextField minutiInizioText;
    private JPanel panelButtons;
    private JPanel panelOrarioInizio;
    private JComboBox giorniBox;
    private JTextField indiceVincoloText;
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
        aggiungiButton.addActionListener(e->{

            int oraFine=Integer.parseInt( oraFineText.getText());
            int minutiFine=Integer.parseInt( minutiFineText.getText());
            int oraInizio=Integer.parseInt( oraInizioText.getText());
            int minutiInizio=Integer.parseInt( minutiInizioText.getText());
            String giorno=giorniBox.getSelectedItem().toString();
            try{
                controller.aggiungiVincolo(giorno,oraInizio,minutiInizio,oraFine,minutiFine);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(frame,ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);
                return;
            }
  creaTable();
        });

        rimuoviButton.addActionListener(e->{
            int indice= Integer.parseInt(indiceVincoloText.getText());
            try{
                controller.rimuoviVincolo(indice-1);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(frame,ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);
                return;
            }
            indiceVincoloText.setText("");
                creaTable();
        });

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
    }


}
