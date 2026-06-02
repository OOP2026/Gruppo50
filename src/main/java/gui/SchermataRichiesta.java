package gui;

import javax.swing.*;
import controller.Controller;
public class SchermataRichiesta {
    JFrame frame;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox comboBox1;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JComboBox comboBox2;
    private JPanel panel1;
    private JPanel panelRichiesta;
    private JPanel panelLezioneDaSpostare;
    private JPanel panelLezioneProposta;
    private JPanel panelButtons;
    private JPanel panelMotivo;
    private JButton inviaButton;
    private JButton indietroButton;
    private JTextArea motivoText;
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
