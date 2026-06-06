package gui;

import controller.Controller;
import javax.swing.*;

public class LoginPage {
    JFrame frame;
    private JPanel panel1;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JButton accediButton;
    private JButton annullaButton;

    public LoginPage(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Login");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    JOptionPane.showMessageDialog(frame, "Compila tutti i campi", "Errore nel login", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Delega al controller
                if (!controller.accedi(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Credenziali non valide.", "Errore nel login", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Apri la schermata giusta in base al ruolo
                String ruolo = controller.getRuolo();
                if (ruolo == null) {
                    JOptionPane.showMessageDialog(frame, "Errore: ruolo non riconosciuto.", "Errore nel login", JOptionPane.ERROR_MESSAGE);
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
                }
                frame.setVisible(false);
            });
        }
    }
}