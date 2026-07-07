package gui;

import controller.Controller;
import javax.swing.*;

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


                if (!controller.registra(nome, cognome, email, username, password, ruolo)) {
                    JOptionPane.showMessageDialog(frame, "Email già in uso.", "Errore nella registrazione", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(frame, "Registrazione completata!");
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
    }
}