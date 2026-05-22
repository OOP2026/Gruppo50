// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

public class SchermataResponsabile {
    JFrame frame;
    private JPanel panel1;

    public SchermataResponsabile(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Studente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
    }
}