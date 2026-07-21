package gui;

import controller.Controller;
import javax.swing.*;
/**
 * Finestra di dialogo (GUI) dedicata all'accesso degli utenti già registrati nel sistema.
 * <p>
 * Questa schermata accessibile a tutti fornisce un modulo per inserire le credenziali di accesso.
 * Dopo la verifica col database, reindirizza automaticamente l'utente alla propria
 * schermata personale in base al ruolo. L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Username:</b> Campo di testo per inserire l'username dell'utente.</li>
 * <li><b>Password:</b> Campo di testo per inserire la password.</li>
 * <li><b>Bottoni di Azione:</b> Esegui l'accesso o torna alla schermata precedente.</li>
 * </ul>
 */
public class LoginPage {
    JFrame frame;
    private JPanel panel1;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JButton accediButton;
    private JButton annullaButton;

    /**
     * Costruisce la schermata di login, associa i pulsanti e delega al
     * {@code controller} l'autenticazione. In caso di successo apre la
     * schermata relativa al ruolo restituito dal controller.
     * @param controller controller dell'applicazione
     * @param frameChiamante frame genitore per il posizionamento
     */
    public LoginPage(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Login");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        if (annullaButton != null) {
            annullaButton.addActionListener(e -> {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
        if (accediButton != null) {

            accediButton.addActionListener(e -> {
                String username = usernameText.getText();
                String password = new String(passwordText.getPassword());

                // Solo controllo input
                if (username.isEmpty() || password.isEmpty()) {
                   erroreLogin("Compila tutti i campi");
                    return;
                }

                // Delega al controller
                if (!controller.accedi(username, password)) {
              erroreLogin("Credenziali non valide.");
                    return;
                }

                // Apri la schermata giusta in base al ruolo
                String ruolo = controller.getRuolo();
                if (ruolo == null) {
                    erroreLogin("ruolo non riconosciuto.");
                    return;
                }
                switch (ruolo) {
                    case "RESPONSABILE": {
                        SchermataResponsabile s = new SchermataResponsabile(controller, frame);
                        s.frame.setVisible(true);
                        break;
                    }
                    case "DOCENTE": {
                        SchermataDocente s = new SchermataDocente(controller, frame);
                        s.frame.setVisible(true);
                        break;
                    }
                    case "STUDENTE": {
                        SchermataStudente s = new SchermataStudente(controller, frame);
                        s.frame.setVisible(true);
                        break;

                    }
                    default:{ break; }
                }
                frame.setVisible(false);
            });
        }
    }

    /**
     * Mostra un dialog di errore relativo alla procedura di login.
     * @param e messaggio di errore da visualizzare
     */
    public void erroreLogin(String e){
        JOptionPane.showMessageDialog(frame, "Errore: "+e, "Errore nel login", JOptionPane.ERROR_MESSAGE);
    }
}