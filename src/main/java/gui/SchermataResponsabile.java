// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

/**Questa GUI rapressenta la gui di responsabile dove può creare le lezioni, gestire gli insegnamenti attivi
 * e anche gestire le richieste di spostamento fatte dai docenti.
 */
public class SchermataResponsabile {
    JFrame frame;
    private JPanel panel1;
    private JButton visualizzaRichiesteButton;
    private JButton creaLezioneButton;
    private JButton insegnamentiAttiviButton;
    private JButton logoutButton;

    public SchermataResponsabile(Controller controller, JFrame frameChiamante) {
        frame = new JFrame("Responsabile");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);

        /* Controllo che il bottone si inizializzato corettamente*/
        if(creaLezioneButton != null) {
            creaLezioneButton.addActionListener(e -> {
                CrealezioneDialog d = new CrealezioneDialog(controller, frame);
                d.dialog.setVisible(true);
            });
        }
        if(insegnamentiAttiviButton != null) {
            insegnamentiAttiviButton.addActionListener(e -> {
                InsegnamentiDialog d = new InsegnamentiDialog(controller, frame);
                d.dialog.setVisible(true);
            });

        }
        if(logoutButton != null) {
            logoutButton.addActionListener(e -> {
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }
        if(visualizzaRichiesteButton != null) {
            visualizzaRichiesteButton.addActionListener(e -> {
               VisualizzaRichiestaDialog d= new VisualizzaRichiestaDialog(controller,frame);

            });
        }
    }

}