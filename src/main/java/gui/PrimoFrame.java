package gui;

import controller.Controller;
import javax.swing.*;
import java.util.ArrayList;

public class PrimoFrame {
    JFrame frame;
    private JPanel panel1;
    private JButton loginButton;
    private JButton registratiButton;

    public PrimoFrame(Controller controller) {
        frame = new JFrame("Schermata di Accesso");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        loginButton.addActionListener(e -> {
            LoginPage loginPage = new LoginPage(controller, frame);
            loginPage.frame.setVisible(true);
            frame.setVisible(false);
        });

        registratiButton.addActionListener(e -> {
            RegisterPage registerPage = new RegisterPage(controller, frame);
            registerPage.frame.setVisible(true);
            frame.setVisible(false);
        });
    }
}