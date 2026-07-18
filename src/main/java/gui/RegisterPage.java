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
    private JComboBox Ruolocombobox;
    private static final String TITOLO_ERRORE_REGISTRAZIONE = "Errore nella registrazione";

    public RegisterPage(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Registrati");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        //Controllo che il bottone si iniziallizzi corettamente
        if (annullaButton != null) {
            annullaButton.addActionListener(e -> {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
        //Controllo che il bottone si iniziallizzi corettamente
        if (confermaButton != null) {

            confermaButton.addActionListener(e -> {
                String nome = nomeText.getText();
                String cognome = cognomeText.getText();
                String ruolo = (String) Ruolocombobox.getSelectedItem();
                String email = emailText.getText();
                String username = usernameText.getText();
                String password = new String(passwordText.getPassword());

                // Solo controllo input
                if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty()
                        || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Compila tutti i campi", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
                    return;
                }
if(!mailValidazione()) {
                    JOptionPane.showMessageDialog(frame, "Email non valida.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String matricola=null;
                // Controllo ruolo e richiesta matricola ciclica
                if ("Studente".equals(ruolo)) {
                    boolean matricolaValida = false;

                    while (!matricolaValida) {
                        matricola = JOptionPane.showInputDialog(frame,
                                "Inserisci la tua matricola\n(Formato richiesto: DE1 seguito da 7 numeri, es. DE1234567):",
                                "Richiesta Matricola",
                                JOptionPane.QUESTION_MESSAGE);

                        // Se l'utente preme "Annulla" o chiude la finestra con la 'X'
                        if (matricola == null) {
                            JOptionPane.showMessageDialog(frame, "La matricola è obbligatoria per completare la registrazione.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
                            return; // Interrompe l'intera registrazione
                        }

                        matricola = matricola.trim(); // Rimuove eventuali spazi inseriti per sbaglio

                        // Controllo il formato tramite Regex:
                        // ^ = inizio stringa, DE1 = testo fisso, \d{7} = esattamente 7 numeri, $ = fine stringa
                        if (matricola.matches("^DE1\\d{7}$")) {
                            if (controller.matricolaEsiste(matricola)) {
                                // Mostra l'errore e il ciclo while ripartirà chiedendo di nuovo l'input
                                JOptionPane.showMessageDialog(frame,
                                        "Matricola già registrata!\nInserisci una matricola diversa.",
                                        "Matricola già in uso",
                                        JOptionPane.WARNING_MESSAGE);
                            } else {
                                matricolaValida = true; // Esce dal ciclo while
                            }
                        } else {
                            // Mostra l'errore e il ciclo while ripartirà chiedendo di nuovo l'input
                            JOptionPane.showMessageDialog(frame,
                                    "Formato non valido!\nLa matricola deve iniziare con 'DE1' ed essere seguita da esattamente 7 cifre.",
                                    "Errore Formato",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }

                if (!controller.registra(nome, cognome, email, username, password, ruolo,matricola)) {
                    JOptionPane.showMessageDialog(frame, "Username o Email già in uso.", TITOLO_ERRORE_REGISTRAZIONE, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(frame, "Registrazione completata!");
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
    }

    private boolean mailValidazione(){
        //un modo per vedere se il pattern della mail è corretto attraverso regex
       String  emailRegex="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
       Pattern emailPattern= Pattern.compile(emailRegex);
       Matcher matcher=emailPattern.matcher(emailText.getText());
       return matcher.matches();
    }
}