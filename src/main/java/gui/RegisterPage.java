package gui;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.awt.*;

public class RegisterPage extends JFrame{
private JPanel registerForm;

public RegisterPage(){
    super("Registrati");
    
    setLayout(new GridBagLayout());
    add(registerForm);
    setSize(800, 500);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

}

}