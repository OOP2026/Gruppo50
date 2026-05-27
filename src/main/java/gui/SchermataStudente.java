// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

public class SchermataStudente {
    JFrame frame;
    private JPanel panel1;
    private JTextArea a08301030TextArea;
    private JTextArea a11001300TextArea;
    private JTextArea a14001600TextArea;
    private JTextArea a14001600TextArea1;
    private JTextArea a16301830TextArea;
    private JTextArea a08301030TextArea1;
    private JTextArea a11001300TextArea2;
    private JTextArea a14001600TextArea2;
    private JTextArea textArea2;
    private JTextArea textArea1;

    public SchermataStudente(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Studente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        a08301030TextArea.setEditable(false);
        a11001300TextArea.setEditable(false);
        a14001600TextArea.setEditable(false);
        a14001600TextArea1.setEditable(false);
        a16301830TextArea.setEditable(false);
        a08301030TextArea1.setEditable(false);
        a11001300TextArea2.setEditable(false);
        a14001600TextArea2.setEditable(false);
        textArea2.setEditable(false);
        textArea1.setEditable(false);
        //.setEditable(false);
    }


}