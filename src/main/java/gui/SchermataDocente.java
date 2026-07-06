// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

public class SchermataDocente {
   static JFrame frame;
    private JPanel panel1;
    private JButton schermataRichiesteButton;
    private JButton visualizzaOrarioButton;
    private JButton gestioneVincoliButton;
    private JButton insegnamentiButton;
    private JButton logoutButton;
    final private Controller controller;
    final private JFrame frameChiamante;
    public SchermataDocente(Controller c,JFrame f) {
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Docente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        String mess=controller.caricaVincoliDaDB();
        if(mess!=null){
            JOptionPane.showMessageDialog(frame,mess,"Errore",JOptionPane.ERROR_MESSAGE);
        }
    caricaEvents();

    }

    public void caricaEvents(){
        //Controllo che il bottone invia richieste sia inizializzato corettamente
        if (schermataRichiesteButton != null) {
            schermataRichiesteButton.addActionListener(e -> {
                SchermataRichiesta r = new SchermataRichiesta(controller, frame);
                r.frame.setVisible(true);
                frame.setVisible(false);
            });
        }
        //Controllo che il bottone per gestire i vincoli sia creato
        if (gestioneVincoliButton != null) {
            gestioneVincoliButton.addActionListener(e -> {
                SchermataVincoli v = new SchermataVincoli(controller, frame);
                v.frame.setVisible(true);
                frame.setVisible(false);
            });
        }
        //Controllo che il bottone per visualizzare l'orario sia creato
        if (visualizzaOrarioButton != null) {
            visualizzaOrarioButton.addActionListener(e -> {
                OrarioDocente o = new OrarioDocente(controller, frame);
                o.frame.setVisible(true);
                frame.setVisible(false);
            });
        }
        if(logoutButton != null) {
            logoutButton.addActionListener(e -> {
                controller.logout();
                frame.setVisible(false);
                frameChiamante.setVisible(true);
                frame.dispose();
            });
        }

        if (insegnamentiButton != null) {
            insegnamentiButton.addActionListener(e -> {
                SchermataInsegnamentiDocente i = new SchermataInsegnamentiDocente(controller, frame);
                i.frame.setVisible(true);
                frame.setVisible(false);
            });
        }


    }
}
