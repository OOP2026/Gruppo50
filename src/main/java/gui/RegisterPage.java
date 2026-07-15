package gui;
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
import controller.Controller;
import javax.swing.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
    private JComboBox comboBox1;

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
                String ruolo = (String) comboBox1.getSelectedItem();
                String email = emailText.getText();
                String username = usernameText.getText();
                String password = new String(passwordText.getPassword());

                // Solo controllo input
                if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty()
                        || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Compila tutti i campi", "Errore nella registrazione", JOptionPane.ERROR_MESSAGE);
                    return;
                }
if(!mailValidazione()) {
                    JOptionPane.showMessageDialog(frame, "Email non valida.", "Errore nella registrazione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!controller.registra(nome, cognome, email, username, password, ruolo)) {
                    JOptionPane.showMessageDialog(frame, "Username o Email già in uso.", "Errore nella registrazione", JOptionPane.ERROR_MESSAGE);
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