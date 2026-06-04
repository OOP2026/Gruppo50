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
    private Controller controller;
    private JFrame frameChiamante;
    public SchermataDocente(Controller c,JFrame f) {
        controller=c;
        frameChiamante=f;
        frame = new JFrame("Docente");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
        caricaEvents();
    }

    public void caricaEvents(){
       inviaRichiesteButton.addActionListener(e->{
           SchermataRichiesta r= new SchermataRichiesta(controller,this.frame);
           r.frame.setVisible(true);
           this.frame.setVisible(false);
       });
       gestioneVincoliButton.addActionListener(e->{
           SchermataVincoli v= new SchermataVincoli(controller,this.frame);
           v.frame.setVisible(true);
           this.frame.setVisible(false);
       });
       visualizzaRichiesteInviateButton.addActionListener(e->{
           SchermataRichiesteInviate ri= new SchermataRichiesteInviate(controller,this.frame);
           ri.frame.setVisible(true);
           this.frame.setVisible(false);
       });
       visualizzaOrarioButton.addActionListener(e->{
           OrarioDocente o= new OrarioDocente(controller,this.frame);
           o.frame.setVisible(true);
           this.frame.setVisible(false);
       });
    }
}
