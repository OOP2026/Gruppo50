package gui;

import controller.Controller;
import javax.swing.*;
/**
 * Finestra di interfaccia grafica (GUI) che serve da schermata iniziale dell'applicazione.
 * <p>
 * Questa schermata accessibile a tutti gli utenti rappresenta il punto di ingresso principale del sistema.
 * L'interfaccia è formata dai seguenti elementi:
 * </p>
 * <ul>
 * <li><b>Bottone Login:</b> Reindirizza l'utente alla schermata per inserire le credenziali di accesso.</li>
 * <li><b>Bottone Registrati:</b> Apre il modulo dedicato alla creazione di un nuovo account all'interno del sistema.</li>
 * </ul>
 */
public class PrimoFrame {
    JFrame frame;
    private JPanel panel1;
    private JButton loginButton;
    private JButton registratiButton;

    /**
     * Costruisce la schermata iniziale dell'applicazione, registrando i
     * listener per navigare verso il login o la registrazione.
     * @param controller controller dell'applicazione
     */
    public PrimoFrame(Controller controller) {
        frame = new JFrame("Schermata di Accesso");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);


        if(loginButton!=null) {
            loginButton.addActionListener(e -> {
                LoginPage loginPage = new LoginPage(controller, frame);
                loginPage.frame.setVisible(true);
                frame.setVisible(false);
            });
        }

        if(registratiButton!=null) {
            registratiButton.addActionListener(e -> {
                RegisterPage registerPage = new RegisterPage(controller, frame);
                registerPage.frame.setVisible(true);
                frame.setVisible(false);
            });
        }
    }
}