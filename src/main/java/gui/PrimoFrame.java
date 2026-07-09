package gui;

import controller.Controller;
import javax.swing.*;

public class PrimoFrame {
    JFrame frame;
    private JPanel panel1;
    private JButton loginButton;
    private JButton registratiButton;

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