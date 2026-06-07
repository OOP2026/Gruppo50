// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

public class SchermataResponsabile {
    JFrame frame;
    private JPanel panel1;
    private JButton VIsualizzaRichiesteButton;
    private JButton creaLezioneButton;
    private JButton insegnamentiAttiviButton;

    public SchermataResponsabile(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Responsabile");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        if(creaLezioneButton != null) {
            creaLezioneButton.addActionListener(e -> {
                CrealezioneDialog d = new CrealezioneDialog(controller, frame);
                d.dialog.setVisible(true);
            });
        }
    }


}