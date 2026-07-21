package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
/**
 * Finestra di interfaccia grafica (GUI) dedicata alla gestione degli insegnamenti del docente.
 * <p>
 * Questa schermata, accessibile solo ai Docenti e ai Responsabili
 * permette di visualizzare, aggiungere e rimuovere le materie
 * che il professore può insegnare.
 * L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Tabella Insegnamenti:</b> Mostra l'elenco dei corsi associati al docente (Nome, CFU, Anno).</li>
 * <li><b>Menu Insegnamenti:</b> Un menu a tendina che elenca tutti i corsi registrati nel sistema non ancora associati al docente.</li>
 * <li><b>Bottone Aggiungi:</b> Associa l'insegnamento selezionato nel menu al profilo del docente.</li>
 * <li><b>Bottone Indietro:</b> Chiude la finestra e riporta l'utente alla schermata di controllo principale.</li>
 * </ul>
 */
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
   private final Controller controller;
   private final JFrame frameChiamante;
    /**
     * Costruisce la schermata per la gestione degli insegnamenti del docente.
     * Popola la tabella, il menu delle materie disponibili e registra i listener
     * per aggiungere o rimuovere insegnamenti.
     * @param c controller dell'applicazione
     * @param f frame chiamante usato per il posizionamento
     */
    public SchermataInsegnamentiDocente(Controller c, JFrame f) {
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Schermata Insegnamenti");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        caricaInsegnamenti();
        if(insegnamentiBox!=null) {insegnamentiBox.setMaximumRowCount(4);}
        caricaEvents();

        caricaInsegnamentiBox();
        if(tabellaInsegnamenti!=null) creaTable();

    }

    /**
     * Registra gli ActionListener per i pulsanti (indietro, aggiungi) e la
     * selezione della tabella che abilita la rimozione di un insegnamento.
     */
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

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(120, 100));
        int risposta = JOptionPane.showConfirmDialog(frame, scrollPane, "Rimozione Insegnamento", JOptionPane.YES_NO_OPTION);
        eliminaInsegnamento(risposta, materia);

        tabellaInsegnamenti.getSelectionModel().clearSelection();
    });
}
    }

    /**
     * Riempie la combo box con gli insegnamenti disponibili che il docente
     * non ha ancora associato. Mostra un messaggio d'errore se la GUI non è inizializzata.
     */
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
    /**
     * Costruisce il modello della tabella degli insegnamenti e applica
     */
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
    /**
     * Richiama il controller per caricare gli insegnamenti dal database e,
     * in caso di errore, visualizza un messaggio all'utente.
     */
    private void caricaInsegnamenti(){
        String msg= controller.caricaInsegnamentiDaDB();
        if(msg!=null){
            dialogErrore(msg);
        }
    }
    /**
     * Rimuove l'insegnamento selezionato dal docente se l'utente conferma
     * l'operazione. Aggiorna la combo e la tabella dopo la rimozione.
     * @param risposta risposta del dialogo di conferma (YES/NO)
     * @param materia nome dell'insegnamento da rimuovere
     */
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
    /**
     * Mostra un dialogo di errore con il messaggio fornito.
     * @param e messaggio di errore
     */
    private void dialogErrore(String e){
        JOptionPane.showMessageDialog(frame, e, "Errore", JOptionPane.ERROR_MESSAGE);
    }


}
