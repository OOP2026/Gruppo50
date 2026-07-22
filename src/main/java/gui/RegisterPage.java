package gui;

import controller.Controller;
import javax.swing.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Finestra di interfaccia grafica (GUI) dedicata alla registrazione di un nuovo account.
 * <p>
 * Questa schermata accessibile a tutti, fornisce un modulo completo per inserire
 * i dati anagrafici, le credenziali e scegliere il proprio ruolo all'interno del sistema (Studente, Docente o Responsabile).
 *  L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Nome e Cognome:</b> Campi di testo per inserire nome e cognome dell'utente.</li>
 * <li><b>Scegli il tipo di utente:</b> Menu a tendina per selezionare il ruolo.</li>
 * <li><b>Email:</b> Campo di testo per la mail.</li>
 * <li><b>Username e Password:</b> Campi per inserire username e password.</li>
 * <li><b>Bottoni di Azione:</b> Conferma per inviare i dati al database, oppure annulla per tornare al menu iniziale.</li>
 * </ul>
 */
public class RegisterPage {
    JFrame frame;
    private JPanel panel1;
    private JTextField nomeText;
    private JTextField cognomeText;
    private JTextField emailText;
    private JTextField usernameText;
    private JPasswordField passwordText;
    /** Il bottone di conferma per finalizzare la registrazione dell'Utente. */
    private JButton confermaButton;
    private JButton annullaButton;
    private JComboBox ruolocombobox;
    private static final String TITOLO_ERRORE_REGISTRAZIONE = "Errore nella registrazione";
    private static final String TITOLO_AGGIUNGI_INSEGNAMENTO="Aggiungi insegnamento";

    /**
     * Costruisce la schermata di registrazione e collega i pulsanti di conferma
     * e annulla alla logica del controller. Controlla input di base e gestisce
     * flussi specifici per ruolo (matricola per Studente, insegnamenti per Docente).
     * @param controller controller dell'applicazione
     * @param frameChiamante frame genitore per il posizionamento
     */
    public RegisterPage(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Registrati");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        // Controllo che il bottone si inizializzi correttamente
        if (annullaButton != null) {
            annullaButton.addActionListener(e -> chiudiFinestra(frameChiamante));
        }

        // Controllo che il bottone si inizializzi correttamente
        if (confermaButton != null) {
            confermaButton.addActionListener(e -> processaRegistrazione(controller, frameChiamante));
        }
    }

    /**
     * Metodo estratto per gestire la logica di registrazione e ridurre la Cognitive Complexity.
     */
    private void processaRegistrazione(Controller controller, JFrame frameChiamante) {
        String nome = nomeText.getText();
        String cognome = cognomeText.getText();
        String ruolo = (String) ruolocombobox.getSelectedItem();
        String email = emailText.getText();
        String username = usernameText.getText();
        String password = new String(passwordText.getPassword());

        // Solo controllo input base
        if (!isInputBaseValido(nome, cognome, email, username, password)) {
            return;
        }

        String matricola = null;
        int annoCorso = 1;

        // Controllo ruolo e richiesta matricola ciclica
        if ("Studente".equals(ruolo)) {
            matricola = ottieniMatricolaValida(controller);
            if (matricola == null) {
                return; // Interrompe l'intera registrazione
            }
            annoCorso = chiediAnnoCorso();
            if (annoCorso == -1) {
                return; // Interrompe l'intera registrazione
            }
        }

        if (!controller.registra(nome, cognome, email, username, password, ruolo, matricola, annoCorso)) {
            JOptionPane.showMessageDialog(frame, "Username o Email già in uso.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se si è registrato un docente, chiede ciclicamente se vuole aggiungere materie
        if ("Docente".equals(ruolo)) {
            chiediInsegnamentiDocente(controller);
        }

        JOptionPane.showMessageDialog(frame, "Registrazione completata!");
        chiudiFinestra(frameChiamante);
    }

    /**
     * Valida i campi di input di base per la registrazione.
     */
    private boolean isInputBaseValido(String nome, String cognome, String email, String username, String password) {
        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Compila tutti i campi", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!mailValidazione()) {
            JOptionPane.showMessageDialog(frame, "Email non valida.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Richiede ciclicamente all'utente la matricola fino a quando non fornisce
     * un valore con il formato corretto (DE1 seguito da 7 cifre) e non già
     * presente nel sistema. Mostra messaggi di errore/avviso all'utente.
     * @param controller controller per verificare l'esistenza della matricola
     * @return la matricola valida oppure {@code null} se l'utente annulla
     */
    private String ottieniMatricolaValida(Controller controller) {
        while (true) {
            String matricola = JOptionPane.showInputDialog(frame,
                    "Inserisci la tua matricola\n(Formato richiesto: DE1 seguito da 7 numeri, es. DE1234567):",
                    "Richiesta Matricola",
                    JOptionPane.QUESTION_MESSAGE);

            // Se l'utente preme "Annulla" o chiude la finestra con la 'X'
            if (matricola == null) {
                JOptionPane.showMessageDialog(frame, "La matricola è obbligatoria per completare la registrazione.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
                return null;
            }

            matricola = matricola.trim(); // Rimuove eventuali spazi inseriti per sbaglio

            // Controllo il formato tramite Regex
            if (!matricola.matches("^DE1\\d{7}$")) {
                JOptionPane.showMessageDialog(frame,
                        "Formato non valido!\nLa matricola deve iniziare con 'DE1' ed essere seguita da esattamente 7 cifre.",
                        "Errore Formato",
                        JOptionPane.WARNING_MESSAGE);
            }
            // Controllo esistenza se il formato è valido
            else if (controller.matricolaEsiste(matricola)) {
                JOptionPane.showMessageDialog(frame,
                        "Matricola già registrata!\nInserisci una matricola diversa.",
                        "Matricola già in uso",
                        JOptionPane.WARNING_MESSAGE);
            }
            // Se supera tutti i controlli, la matricola è valida
            else {
                return matricola;
            }
        }
    }

    /**
     * Chiede allo studente, dopo l'inserimento della matricola, a quale anno
     * di corso è iscritto (primo, secondo o terzo) tramite una dialog a scelta.
     * @return l'anno di corso (1, 2 o 3), oppure {@code -1} se l'utente chiude la dialog
     */
    private int chiediAnnoCorso() {
        String[] anni = {"Primo anno", "Secondo anno", "Terzo anno"};
        int scelta = JOptionPane.showOptionDialog(frame,
                "A quale anno di corso sei iscritto?",
                "Anno di corso",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                anni,
                anni[0]);

        // Se l'utente chiude la finestra con la 'X'
        if (scelta == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(frame, "L'anno di corso è obbligatorio per completare la registrazione.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        return scelta + 1; // indice 0..2 -> anno 1..3
    }

    /**
     * Gestisce la chiusura del frame corrente e la riapertura del frame chiamante.
     */
    private void chiudiFinestra(JFrame frameChiamante) {
        frame.setVisible(false);
        frameChiamante.setVisible(true);
        frame.dispose();
    }

    /**
     * Dopo la registrazione di un docente chiede, tramite una dialog ciclica,
     * se vuole aggiungere una materia (insegnamento).
     * <p>
     * Finché il docente preme "Sì" può selezionare una materia tra gli insegnamenti
     * registrati non ancora associati a lui; premendo "No" l'inserimento termina.
     * Le materie aggiunte vengono poi visualizzate nella schermata Insegnamenti del docente.
     * Riusa i metodi del Controller della schermata Insegnamenti: {@code registra}
     * imposta come docente corrente quello appena creato, e a fine dialog viene
     * chiamato {@code logout()} per azzerare lo stato.
     * </p>
     * @param controller il controller dell'applicazione.
     */
    private void chiediInsegnamentiDocente(Controller controller) {
        boolean continuaAggiunta = true;

        while (continuaAggiunta) {
            int risposta = JOptionPane.showConfirmDialog(frame,
                    "Vuoi aggiungere una materia (insegnamento)?",
                    TITOLO_AGGIUNGI_INSEGNAMENTO,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (risposta != JOptionPane.YES_OPTION) {
                continuaAggiunta = false;
            } else {
                // Deleghiamo la logica complessa a un metodo separato.
                // Il metodo restituirà 'false' se non ci sono più materie disponibili.
                continuaAggiunta = gestisciSelezioneMateria(controller);
            }
        }

        // (Ricordati di mantenere l'eventuale controller.logout() qui alla fine se presente)
    }

    /**
     * Mostra una dialog per scegliere un insegnamento da aggiungere al docente
     * corrente. Se non ci sono materie disponibili mostra un messaggio e
     * termina il ciclo di aggiunta.
     * @param controller controller usato per recuperare e aggiungere insegnamenti
     * @return {@code false} se non ci sono più insegnamenti disponibili, altrimenti {@code true}
     */
    private boolean gestisciSelezioneMateria(Controller controller) {
        java.util.List<String> disponibili = controller.getInsegnamentiRegistratiDocente();

        // Controllo disponibilità
        if (disponibili.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Non ci sono altri insegnamenti disponibili da aggiungere.",
                    TITOLO_AGGIUNGI_INSEGNAMENTO,
                    JOptionPane.INFORMATION_MESSAGE);
            return false; // Interrompe il ciclo nel metodo chiamante
        }

        // Input utente
        String materia = (String) JOptionPane.showInputDialog(frame,
                "Seleziona la materia da aggiungere:",
                TITOLO_AGGIUNGI_INSEGNAMENTO,
                JOptionPane.QUESTION_MESSAGE,
                null,
                disponibili.toArray(),
                disponibili.get(0));

        // Inserimento se l'utente non ha premuto Annulla
        if (materia != null) {
            try {
                controller.addInsegnamentoDocente(materia);
                JOptionPane.showMessageDialog(frame, materia + " aggiunta con successo!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

        return true; // La procedura è andata a buon fine, si può continuare ad aggiungere
    }

    /**
     * Valida la sintassi dell'indirizzo email inserito usando una regex.
     * @return {@code true} se la mail rispetta il formato previsto, {@code false} altrimenti
     */
    private boolean mailValidazione(){
        // un modo per vedere se il pattern della mail è corretto attraverso regex
        String emailRegex="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher matcher = emailPattern.matcher(emailText.getText());
        return matcher.matches();
    }
}