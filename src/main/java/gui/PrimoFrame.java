package gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionListener.*;

public class PrimoFrame extends JFrame {



    private JButton buttonR;

    private JButton buttonL;

    private JPanel panelRL;
private GridBagConstraints c;
    public PrimoFrame(){


        super("Schermata di Accesso");
        panelRL = new JPanel();

        buttonL= new JButton("Login");
        buttonR= new JButton("Registrati");




        add(panelRL);
        panelRL.setPreferredSize(new Dimension(200,200));
        panelRL.setBackground(Color.RED);
        panelRL.setLayout(new GridBagLayout());
 c= new GridBagConstraints();
c.insets=new Insets (0 , 10, 0, 0);
        panelRL.add(buttonR,c);
c.ipadx=20;
        panelRL.add(buttonL,c);

        buttonL.setPreferredSize(new Dimension(100,20));

setUpButtonListener();


        setSize(800,500);

        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setVisible(true);







    }

public void setUpButtonListener(){

    ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonL)
            System.out.println("hai cliccato il button L");

        }
    };
   buttonL.addActionListener(buttonListener);
}

}
