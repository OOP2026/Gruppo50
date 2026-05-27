// SchermataStudente.java
package gui;
import controller.Controller;
import javax.swing.*;

public class SchermataDocente {
   static JFrame frame;
    JMenuBar barraStrumenti;
    private JPanel panel1;
    private JButton inviaRichiesteButton;
    private JButton visualizzaRichiesteInviateButton;
    private JButton visualizzaOrarioButton;
    private JButton gestioneVincoliButton;

    public SchermataDocente(Controller controller,JFrame frameChiamante) {
        frame = new JFrame("Docente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
    }

    public JMenuBar creaMenu(){
       JMenuBar barraMenu= new JMenuBar();
       JMenu menuRichieste= new JMenu("Richieste");
       JMenu menuProfilo= new JMenu("Profilo");
       JMenu menuOrario= new JMenu("Visualizza Orario");
      JMenuItem  menuItemInviaRichiesta= new JMenuItem("Invia Richiesta");
       JMenuItem menuItemVediRichieste=new JMenuItem("Vedi Richieste");

        menuRichieste.add(menuItemInviaRichiesta);
        menuRichieste.add(menuItemVediRichieste);
        barraMenu.add(menuProfilo);
        barraMenu.add(menuOrario);
        barraMenu.add(menuRichieste);
        return barraMenu;
    }
}
