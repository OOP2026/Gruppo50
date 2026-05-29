// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

public class SchermataStudente {
    JFrame frame;
    private JPanel panel1;
    private JTextArea PrimaLezioneLunedi;
    private JTextArea SecondaLezioneLunedi;
    private JTextArea TerzaLezioneLunedi;
    private JTextArea PrimaLezioneMercoledi;
    private JTextArea SecondaLezioneMercoledi;
    private JTextArea PrimaLezioneGiovedi;
    private JTextArea SecondaLezioneGiovedi;
    private JTextArea TerzaLezioneGiovedi;
    private JTextArea textArea2;
    private JTextArea textArea1;

    public SchermataStudente(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Studente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        PrimaLezioneLunedi.setEditable(false);
        SecondaLezioneLunedi.setEditable(false);
        TerzaLezioneLunedi.setEditable(false);
        PrimaLezioneMercoledi.setEditable(false);
        SecondaLezioneMercoledi.setEditable(false);
        PrimaLezioneGiovedi.setEditable(false);
        SecondaLezioneGiovedi.setEditable(false);
        TerzaLezioneGiovedi.setEditable(false);
        textArea2.setEditable(false);
        textArea1.setEditable(false);
        //.setEditable(false);
    }


}