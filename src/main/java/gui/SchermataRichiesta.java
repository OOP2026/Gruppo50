package gui;

import javax.swing.*;
import controller.Controller;
public class SchermataRichiesta {
    JFrame frame;
    private JTextField oraIniziaLezioneText;
    private JTextField minutiIniziaLezioneText;
    private JTextField oraFineLezioneText;
    private JTextField minutiFineLezioneText;
    private JComboBox giorniBox;
    private JTextField minutiIniziaNuovaText;
    private JTextField oraFineNuovaText;
    private JTextField minutiFineNuovaText;
    private JComboBox giorniNuoviBox;
    private JPanel panel1;
    private JPanel panelRichiesta;
    private JPanel panelLezioneDaSpostare;
    private JPanel panelLezioneProposta;
    private JPanel panelButtons;
    private JPanel panelMotivo;
    private JButton inviaButton;
    private JButton indietroButton;
    private JTextArea motivoText;
    private JTextField oraIniziaNuovaText;
    JFrame frameChiamante;
    Controller controller;
    public SchermataRichiesta(Controller c, JFrame f){
        frameChiamante=f;
        controller=c;
        frame = new JFrame("Schermata Richiesta");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameChiamante);
 caricaEvents();
    }

    private void caricaEvents(){
        indietroButton.addActionListener(e->{
            frame.setVisible(false);
            frameChiamante.setVisible(true);
            frame.dispose();

        });

    }
}
