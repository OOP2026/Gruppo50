package gui;
import controller.Controller;
import javax.swing.*;
import javax.swing.JComboBox;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;

/**
 * Finestra di dialogo (GUI) dedicata alla creazione e programmazione di una nuova lezione.
 * <p>
 * Questa schermata che può essere usata solo dal responsabile fornisce un modulo per inserire tutti i dettagli
 * necessari alla creazione. L'interfaccia é formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Nome Insegnamento:</b> Un menu a tendina per scegliere l'insegnamento.</li>
 * <li><b>Email docente:</b> Campo di testo per inserire la mail del professore.</li>
 * <li><b>Nome aula:</b> Un menu a tendina dinamico per la selezione dell'aula.</li>
 * <li><b>Giorno:</b> Menu a scorrimento per scegliere il giorno della settimana.</li>
 * <li><b>Fascia Oraria:</b> Campi di testo dedicati all'inserimento di ore e minuti.</li>
 * <li><b>Bottoni di Azione:</b> Conferma o annulla l'operazione.</li>
 * </ul>
 */

public class CrealezioneDialog {
    JDialog dialog;
    private JPanel panel1;
    private JButton annullaButton;
    private JButton confermaButton;
    private JComboBox<String> giornoCombo;
    private JComboBox<String> nomeInsField;
    private JLabel labelErrore;
    private JTextField emailDocField;
    private JComboBox<String> nomeAulaField;
    private JTextField oraIField;
    private JTextField minIField;
    private JTextField oraFField;
    private JTextField minFField;
    private final  JTextField textComboBoxIns;
    private final JTextField textComboBoxAula;
    private final  Controller controller;


    /**
     * Costruisce il dialog per creare una nuova lezione. Prepara le combo
     * dinamiche per insegnamenti e aule, registra i listener e valida i campi
     * prima di delegare la creazione al controller.
     * @param controller controller dell'applicazione
     * @param frameChiamante frame genitore usato per centrare il dialog
     */
    public CrealezioneDialog(Controller controller, JFrame frameChiamante) {
        dialog = new JDialog(frameChiamante, "Crea Nuova Lezione", true);
        this.controller = controller;
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
caricaAule();
caricaInsegnamenti();
        //verifico che il frame chiamante non sia null
        if (frameChiamante != null) {
            dialog.setLocationRelativeTo(frameChiamante);
        }

        textComboBoxIns=  textFieldComboBoxIns();
        textComboBoxAula=textFieldComboBoxAula();
        caricaInsegnamentiBox("");
        caricaAuleBox("");
        caricaEvents();
        //Controllo che il bottone si crei correttamente.
        JButton annulla = annullaButton;
        if (annulla != null) {
            annulla.addActionListener(e -> dialog.dispose());
        }
        JButton conferma = confermaButton;
        if(conferma != null) {
            conferma.addActionListener(e -> {
                labelErrore.setText("");

                String nomeIns = Objects.requireNonNull(nomeInsField.getSelectedItem()).toString();
                String emailDoc = emailDocField.getText().trim();
                String nomeAula = Objects.requireNonNull(nomeAulaField.getSelectedItem()).toString();
                String giorno = (String) giornoCombo.getSelectedItem();

                if (nomeIns.isEmpty() || emailDoc.isEmpty() || nomeAula.isEmpty()
                        || oraIField.getText().trim().isEmpty()
                        || minIField.getText().trim().isEmpty()
                        || oraFField.getText().trim().isEmpty()
                        || minFField.getText().trim().isEmpty()) {
                    labelErrore.setText("Compila tutti i campi.");
                    return;
                }

                try {
                    int oraI = Integer.parseInt(oraIField.getText().trim());
                    int minI = Integer.parseInt(minIField.getText().trim());
                    int oraF = Integer.parseInt(oraFField.getText().trim());
                    int minF = Integer.parseInt(minFField.getText().trim());
int [] orario= {oraI,minI,oraF,minF};
                    String errore = controller.creaLezione(
                            nomeIns,
                            emailDoc,
                            nomeAula,
                            giorno, orario);

                    if (errore != null) {
                        labelErrore.setText(errore);
                        return;
                    }

                    JOptionPane.showMessageDialog(dialog, "Lezione creata con successo!");
                    dialog.dispose();

                } catch (NumberFormatException ex) {
                    labelErrore.setText("Gli orari devono essere numeri interi.");
                }
            });
        }
    }

    /**
     * Aggiorna la JComboBox degli insegnamenti filtrandola per la stringa
     * fornita dall'utente e mantenendo l'editor testuale sincronizzato.
     * @param materia filtro testuale da applicare
     */
    private void caricaInsegnamentiBox(String materia){
        //Rimuove tutti gli items precedenti
        nomeInsField.removeAllItems();
        //richiede gli insegnamenti in base alla string materia
        List<String> data= controller.getInsegnamentiRegistrati(materia.toLowerCase());
        for(String insegnamento:data){
            nomeInsField.addItem(insegnamento);
        }

        nomeInsField.setMaximumRowCount(4);
        nomeInsField.setSelectedIndex(-1);
        // revalidate e repaint servono per ricalibrare la JComboBox dopo aver rimosso e aggiunto items
        nomeInsField.revalidate();
        nomeInsField.repaint();
        //Questo if ha lo scopo di chiudere il popup della JComboBox se è aperto
        // e poi riaprirlo solo se ci sono items da mostrare. Questo evita che il popup rimanga aperto con una lista vuota.
        if(nomeInsField.isPopupVisible()){
            nomeInsField.setPopupVisible(false);
            if(nomeInsField.getItemCount()>0)
                nomeInsField.setPopupVisible(true);
        }else if(nomeInsField.getItemCount()>0 && !materia.equalsIgnoreCase(""))
        {
            nomeInsField.setPopupVisible(true);
        }
        //rimette il testo scritto dall'utente nella textfield della combobox
        textComboBoxIns.setText(materia);
    }
    /**
     * Restituisce la JTextField dell'editor della JComboBox 'nomeInsField'.
     * @throws IllegalStateException se l'editor non è un JTextField, indicando che la JComboBox non è editabile.
     */
    public JTextField textFieldComboBoxIns() {
        if(nomeInsField == null) {
            throw new NullPointerException("nomeInsField è null");
        }
        Component editor = nomeInsField.getEditor().getEditorComponent();



        if (editor instanceof JTextField) {
            return (JTextField) editor;
        }
        // Se non è un JTextField, interrompiamo con un messaggio d'errore chiaro
        // invece di passare un null che causerebbe danni più avanti.
        throw new IllegalStateException("L'editor della JComboBox 'nomeInsField' non è un JTextField. Assicurati che sia editabile.");
    }
    /**
     * Aggiorna la JComboBox delle aule filtrandola per il testo inserito
     * dall'utente e sincronizza l'editor testuale.
     * @param nomeAula filtro testuale da applicare
     */
    private void caricaAuleBox(String nomeAula){
        //rimuove tutti gli items precedenti
        nomeAulaField.removeAllItems();
        //richiede le aule in base alla stringa nomeAula
        List<String> data= controller.getAule(nomeAula.toLowerCase());
        for(String aula:data){
            nomeAulaField.addItem(aula);
        }

        nomeAulaField.setMaximumRowCount(4);
        nomeAulaField.setSelectedIndex(-1);
        //revalidate e repaint servono per ricalibrare la JComboBox dopo aver rimosso e aggiunto items
        nomeAulaField.revalidate();
        nomeAulaField.repaint();
        //Questo if ha lo scopo di chiudere il popup della JComboBox se è aperto
        // e poi riaprirlo solo se ci sono items da mostrare.
        // Questo evita che il popup rimanga aperto con una lista vuota.
        if(nomeAulaField.isPopupVisible()){
            nomeAulaField.setPopupVisible(false);

            if(nomeAulaField.getItemCount()>0)
                nomeAulaField.setPopupVisible(true);
        }else{
            if(nomeAulaField.getItemCount()>0 && !nomeAula.equalsIgnoreCase(""))
                nomeAulaField.setPopupVisible(true);
        }
        //Rimette il testo digitato dall'utente nella textfield della ComboBox
        textComboBoxAula.setText(nomeAula);
    }
    /**
     * Restituisce la JTextField dell'editor della JComboBox 'nomeAulaField'.
     * @throws IllegalStateException se l'editor non è un JTextField, indicando che la JComboBox non è editabile.
     */
    public JTextField textFieldComboBoxAula() {
        if(nomeAulaField == null) {
            throw new NullPointerException("nomeAulaField è null");
        }
        Component editor = nomeAulaField.getEditor().getEditorComponent();

        if (editor instanceof JTextField) {
            return (JTextField) editor;
        }
        // Se non è un JTextField, interrompiamo con un messaggio d'errore chiaro
        // invece di passare un null che causerebbe danni più avanti.
        throw new IllegalStateException("L'editor della JComboBox 'nomeAulaField' non è un JTextField. Assicurati che sia editabile.");

    }


    /**
     * Registra i listener per le textfield degli editor delle combo in modo
     * da eseguire il filtering al rilascio di un tasto.
     */
    private void caricaEvents() {
        textComboBoxIns.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                String testoAttuale= textComboBoxIns.getText();
                caricaInsegnamentiBox(testoAttuale);
            }

        });
        textComboBoxAula.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                String testoAttuale= textComboBoxAula.getText();
                caricaAuleBox(testoAttuale);
            }

        });
    }

    /**
     * Richiama il controller per caricare gli insegnamenti dal database e
     * mostra un messaggio all'utente se si verifica un errore.
     */
    private void caricaInsegnamenti(){
        String msg= controller.caricaInsegnamentiDaDB();
        if(msg!=null){
            JOptionPane.showMessageDialog(dialog, msg, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Richiama il controller per caricare le aule dal database e mostra un
     * messaggio all'utente se si verifica un errore.
     */
    private void caricaAule(){
        String ex= controller.caricaAuleDaDB();
        if(ex!=null){
            JOptionPane.showMessageDialog(dialog, ex, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}