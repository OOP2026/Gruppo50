package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")

/**
 * Finestra di dialogo (GUI) dedicata alla visualizzazione e all'aggiunta di nuovi insegnamenti.
 * <p>
 * Questa schermata che può essere usata solo dal responsabile permette di vedere gli insegnamenti attivi
 * e la possibilità di registrarne di nuovi. L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Tabella Insegnamenti:</b> Una tabella che mostra l'elenco dei corsi (Nome, CFU, Anno corso, Email docente).</li>
 * <li><b>Nome Insegnamento:</b> Campo di testo per inserire il nome del corso.</li>
 * <li><b>CFU:</b> Campo di testo per inserire i crediti formativi.</li>
 * <li><b>Anno Corso:</b> Campo di testo per inserire l'anno di corso.</li>
 * <li><b>Email Docente:</b> Campo di testo per inserire l'email del professore.</li>
 * <li><b>Bottoni di Azione:</b> Aggiungi un nuovo insegnamento o chiudi la finestra.</li>
 * </ul>
 */
public class InsegnamentiDialog {
    JDialog dialog;
    private JPanel panel1;
    private JPanel sottoPannello2;
    private JPanel sottoPannello1;
    private JTable tabellaInsegnamenti;
    private JButton chiudiButton;
    private JLabel labelErrore;
    private JTextField nomeInsField;
    private JTextField cfuField;
    private JTextField annoField;
    private JButton aggiungiButton;
private final Controller controller;
    private DefaultTableModel tableModel;

    public InsegnamentiDialog(Controller controller, JFrame frameChiamante) {

        dialog = new JDialog(frameChiamante, "Insegnamenti Attivi", true);
        dialog.setContentPane(panel1);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(frameChiamante);
this.controller= controller;
caricaInsegnamenti();
        // Inizializza il DefaultTableModel e assegnalo alla JTable
        String[] colonne = {"Nome", "CFU", "Anno corso", "Email docente"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        // Assicura a SonarQube che il componente non sia null
        java.util.Objects.requireNonNull(tabellaInsegnamenti, "Errore GUI: tabellaInsegnamenti non inizializzata dal designer");

        tabellaInsegnamenti.setModel(tableModel);
        tabellaInsegnamenti.getTableHeader().setReorderingAllowed(false);
        tabellaInsegnamenti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Carica subito gli insegnamenti già presenti
        aggiornaTabella(controller);

        // ── Listener ────────────────────────────────────────────────────────
        chiudiButton.addActionListener(e -> dialog.dispose());

        aggiungiButton.addActionListener(e -> {
            labelErrore.setText("");

            String nome     = nomeInsField.getText().trim();
            String cfuTxt   = cfuField.getText().trim();
            String annoTxt  = annoField.getText().trim();

            if (nome.isEmpty() || cfuTxt.isEmpty() || annoTxt.isEmpty()) {
                labelErrore.setText("Compila tutti i campi.");
                return;
            }

            try {
                int cfu  = Integer.parseInt(cfuTxt);
                int anno = Integer.parseInt(annoTxt);

                String errore = controller.registraInsegnamento(nome, cfu, anno);
                if (errore != null) {
                    labelErrore.setText(errore);
                    aggiornaTabella(controller);
                    return;
                }

                aggiornaTabella(controller);
                cfuField.setText("");
                annoField.setText("");
                JOptionPane.showMessageDialog(dialog, "Insegnamento aggiunto con successo!");

            } catch (NumberFormatException ex) {
                labelErrore.setText("CFU e anno corso devono essere numeri interi.");
            }
        });
caricaEventTable();


    }

    /**
     * Carica i listener di selezione della tabella degli insegnamenti.
     * <p>
     * Attacca un {@code ListSelectionListener} alla tabella che reagisce quando
     * l'utente seleziona una riga. A seconda che il docente sia assegnato ("nessuno" indica assenza),
     * apre un dialog di gestione per assegnare il docente oppure per rimuovere l'insegnamento.
     * La selezione viene automaticamente deselezionata dopo l'azione.
     * </p>
     */
    private void caricaEventTable(){
        if(tabellaInsegnamenti != null) {
            tabellaInsegnamenti.getSelectionModel().addListSelectionListener(e -> {
                int riga = tabellaInsegnamenti.getSelectedRow();
                if (riga == -1 || !e.getValueIsAdjusting()) {return;}
                String campoDocente = tabellaInsegnamenti.getValueAt(riga, 3).toString();

                    gestioneInsegnamento(riga);




                tabellaInsegnamenti.getSelectionModel().clearSelection();
            });
        }
    }

    /**
     * Rimuove un insegnamento dal sistema dopo conferma dell'utente.
     * <p>
     * Se l'utente ha confermato la rimozione ({@code YES_OPTION}), invoca il controller
     * per eliminare l'insegnamento tramite {@link Controller#removeInsegnamento(String)}.
     * Se la rimozione fallisce, mostra un messaggio di errore.
     * Aggiorna la tabella indipendentemente dall'esito.
     * </p>
     * @param risposta il codice della risposta (JOptionPane.YES_OPTION o JOptionPane.NO_OPTION)
     * @param nomeIns il nome dell'insegnamento da rimuovere
     */
    private void rimuoviInsegnamento(int risposta,String nomeIns){
        if (risposta == JOptionPane.YES_OPTION) {
            String action = controller.removeInsegnamento(nomeIns);
            if (action != null) {
                JOptionPane.showMessageDialog(dialog, action, "Errore nella rimozione", JOptionPane.ERROR_MESSAGE);
            }
            aggiornaTabella(controller);
        }

    }
    private void modificaDocenteTitolare(String emailDocente, String nomeIns){
        if (!emailDocente.trim().isEmpty()) {
            String action = controller.modificaDocenteTitolare(emailDocente,nomeIns);
            if (action != null) {
                JOptionPane.showMessageDialog(dialog, action, "Errore nella modifica del docente titolare", JOptionPane.ERROR_MESSAGE);
            }
            aggiornaTabella(controller);
        }else{
            JOptionPane.showMessageDialog(dialog, "Il campo dell'email non può essere vuoto!", "ATTENZIONE!", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Assegna un docente a un insegnamento.
     * <p>
     * Se l'email fornita non è vuota, invoca il controller per assegnare il docente
     * tramite {@link Controller#assegnaDocente(String, String)}. Se l'operazione ha
     * successo aggiorna la tabella; altrimenti mostra un messaggio di errore.
     * Se l'email è vuota mostra un avviso all'utente.
     * </p>
     * @param nomeIns il nome dell'insegnamento a cui assegnare il docente
     * @param emailDocente l'email del docente da assegnare
     */
    private void assegnaDocente(String nomeIns, String emailDocente){
        if (!emailDocente.trim().isEmpty()) {
            String action = controller.assegnaDocente(nomeIns,emailDocente);
            if (action != null) {
                JOptionPane.showMessageDialog(dialog, action, "Errore nell'assegnazione del docente", JOptionPane.ERROR_MESSAGE);
            }
            aggiornaTabella(controller);
        }else{
            JOptionPane.showMessageDialog(dialog, "Il campo dell'email non può essere vuoto!", "ATTENZIONE", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Aggiorna il contenuto della tabella degli insegnamenti.
     * <p>
     * Pulisce tutte le righe precedenti e ricarica gli insegnamenti attivi
     * dal controller tramite {@link Controller#getInsegnamentiAttivi()}, quindi
     * le aggiunge alla tabella renderizzate come {@code Object[]}.
     * </p>
     * @param controller il controller da cui recuperare gli insegnamenti
     */
    private void aggiornaTabella(Controller controller) {
        tableModel.setRowCount(0);
        for (Object[] riga : controller.getInsegnamentiAttivi()) {
            tableModel.addRow(riga);
        }
    }

    /**
     * Carica gli insegnamenti dal database tramite il controller.
     * <p>
     * Invoca {@link Controller#caricaInsegnamentiDaDB()} e in caso di errore
     * mostra un dialog con il messaggio di errore.
     * </p>
     */
    private void caricaInsegnamenti(){
        String msg= controller.caricaInsegnamentiDaDB();
        if(msg!=null){
            JOptionPane.showMessageDialog(dialog, msg, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Apre un dialog per la gestione di un insegnamento senza docente assegnato.
     * <p>
     * Presenta all'utente tre opzioni:
     * <ul>
     * <li>Assegna docente - apre il panel di assegnazione del docente</li>
     * <li>Rimuovi insegnamento - apre il panel di conferma rimozione</li>
     * <li>Annulla - chiude il dialog senza fare nulla</li>
     * </ul>
     * </p>
     * @param riga l'indice della riga selezionata nella tabella
     */
    private void gestioneInsegnamento(int riga){
        String[] opzioni={"Assegna docente","Rimuovi insegnamento","Annulla"};
        int scelta= JOptionPane.showOptionDialog(dialog,"Cosa desideri fare con questo insegnamento?",
                "Gestione insegnamento",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,opzioni,opzioni[0]);
        switch(scelta){
            case 0:
                assegnaDocentePanel(riga);
                break;

            case 1:
                removePanel(riga);
                break;
            default:
                break;
        }

    }
    /**
     * Mostra un dialog di conferma per la rimozione di un insegnamento.
     * <p>
     * Estrae il nome dell'insegnamento dalla riga selezionata e chiede conferma
     * all'utente con un messaggio di avvertenza che include l'avviso che tutte
     * le lezioni associate all'insegnamento saranno rimosse. Se confermato,
     * chiama {@link #rimuoviInsegnamento(int, String)} con la risposta.
     * </p>
     * @param riga l'indice della riga selezionata nella tabella
     */
    private void removePanel(int riga){
        String nomeInsegnamento = tabellaInsegnamenti.getValueAt(riga, 0).toString();
        String motivo = "Vuoi rimuovere l'insegnamento " + nomeInsegnamento + "? Se rimuovi l'insegnamento verrano rimosse anche le lezioni associate con questo insegnamento";
        JTextArea textArea = new JTextArea(motivo);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(120, 100));
        int risposta = JOptionPane.showConfirmDialog(dialog, scrollPane, "Rimozione Insegnamento", JOptionPane.YES_NO_OPTION);
        rimuoviInsegnamento(risposta, nomeInsegnamento);}
    /**
     * Mostra un dialog input per l'assegnazione di un docente a un insegnamento.
     * <p>
     * Estrae il nome dell'insegnamento dalla riga selezionata e chiede all'utente
     * di inserire l'email del docente. Valida l'email tramite {@link #mailValidazione(String)};
     * se la validazione fallisce mostra un avviso. Se valida, chiama
     * {@link #assegnaDocente(String, String)} per completare l'assegnazione.
     * Se l'utente cancella il dialog, il metodo ritorna senza fare nulla.
     * </p>
     * @param riga l'indice della riga selezionata nella tabella
     */
    private void assegnaDocentePanel(int riga){

        String nomeInsegnamento = tabellaInsegnamenti.getValueAt(riga, 0).toString();
        int numeroCfu= Integer.parseInt(tabellaInsegnamenti.getValueAt(riga,1).toString());
        int annoCorso= Integer.parseInt(tabellaInsegnamenti.getValueAt(riga,2).toString());


        String emailDocente = JOptionPane.showInputDialog(dialog,"Inserisci l'email del docente da assegnare:",
                "Assegnazione Docente",JOptionPane.QUESTION_MESSAGE);
   if(emailDocente==null){return;}
   if(mailValidazione(emailDocente)){
       JOptionPane.showMessageDialog(dialog, "Email non valida.", "ATTENZIONE!", JOptionPane.WARNING_MESSAGE);
return;
   }
        assegnaDocente(nomeInsegnamento,emailDocente.trim());
    }

    /**
     * Valida il formato di un indirizzo email tramite espressione regolare.
     * <p>
     * Verifica che l'email corrisponda al pattern:
     * {@code ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}}
     * </p>
     * @param mail l'indirizzo email da validare
     * @return {@code true} se l'email è valida, {@code false} altrimenti
     */
    private boolean mailValidazione(String mail){
        // un modo per vedere se il pattern della mail è corretto attraverso regex
        String emailRegex="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher matcher = emailPattern.matcher(mail);
        return matcher.matches();
    }
}


