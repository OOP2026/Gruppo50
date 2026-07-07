package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
@SuppressWarnings("unused")
public class SchermataInsegnamentiDocente {
    JFrame frame;
    private JPanel panelAddInsegnamenti;
    private JTable tabellaInsegnamenti;
    private JPanel panelButtons;
    private JButton aggiungiButton;
    private JButton indietroButton;
    private JComboBox<String> insegnamentiBox;
    private JPanel panel1;
   final private Controller controller;
   final private JFrame frameChiamante;
    public SchermataInsegnamentiDocente(Controller c, JFrame f) {
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Schermata Insegnamenti");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        if(insegnamentiBox!=null) {insegnamentiBox.setMaximumRowCount(4);}
        caricaEvents();

        caricaInsegnamentiBox();
        if(tabellaInsegnamenti!=null) creaTable();

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
                JOptionPane.showMessageDialog(frame,"Non hai selezionato nessun insegnamento.","Attenzione",JOptionPane.WARNING_MESSAGE);
                return;
            }
            try{
                controller.addInsegnamentoDocente(Objects.requireNonNull(insegnamentiBox.getSelectedItem()).toString());
            }
            catch(Exception ex){
                dialogErrore(ex.getMessage());
            return;
            }
            caricaInsegnamentiBox();
            creaTable();

        });
        }
if(tabellaInsegnamenti != null) {
    tabellaInsegnamenti.getSelectionModel().addListSelectionListener(e -> {
        int riga = tabellaInsegnamenti.getSelectedRow();
        if (riga == -1 || !e.getValueIsAdjusting()) return;
        String materia = tabellaInsegnamenti.getValueAt(riga, 0).toString();
        String motivo = "Vuoi rimuovere come materia che insegni " + materia + "?";
        JTextArea textArea = new JTextArea(motivo);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        //textArea.setBackground(new Color(255, 255, 255, 0) );
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(120, 100));
        int risposta = JOptionPane.showConfirmDialog(frame, scrollPane, "Rimozione Insegnamento", JOptionPane.YES_NO_OPTION);
        eliminaInsegnamento(risposta, materia);

        tabellaInsegnamenti.getSelectionModel().clearSelection();
    });
}
    }

    private void caricaInsegnamentiBox(){
        if(insegnamentiBox==null){
dialogErrore("Errore GUI: insegnamentiBox non inizializzato dal designer, riavvia il programma");
        return;}
        insegnamentiBox.removeAllItems();
        insegnamentiBox.addItem("none");
        List<String> data= controller.getInsegnamentiRegistratiDocente();
        for(String insegnamento:data){
            insegnamentiBox.addItem(insegnamento);
        }
        insegnamentiBox.setSelectedIndex(0);
    }
    private void creaTable(){
        Object[][] data=controller.getInsegnamentiDocente();
        tabellaInsegnamenti.setModel(new DefaultTableModel(data,
                new String[]{"Nome","Cfu","Anno"} ) );
        //Per centrare il testo delle righe
        DefaultTableCellRenderer centerRender= new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<tabellaInsegnamenti.getColumnCount();i++){
            tabellaInsegnamenti.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
        //non rende possibile modificare le righe all'utente
        tabellaInsegnamenti.setDefaultEditor(Object.class, null);
        //quando le righe vengono cliccati diventano grigio chiaro
        tabellaInsegnamenti.setSelectionBackground(Color.LIGHT_GRAY);
    }

    private void eliminaInsegnamento(int risposta, String materia){
        if (risposta == JOptionPane.YES_OPTION) {
            String action = controller.removeInsegnamentoDocente(materia);
            if (action != null) {
                dialogErrore(action);
            }
            caricaInsegnamentiBox();
            creaTable();
        }
    }
    private void dialogErrore(String e){
        JOptionPane.showMessageDialog(frame, e, "Errore", JOptionPane.ERROR_MESSAGE);
    }


}
