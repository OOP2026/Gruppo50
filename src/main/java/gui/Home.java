package gui;

import controller.Controller;
import javax.swing.*;
import java.util.ArrayList;

public class Home {
	public static void main(String[] args) {
		Controller controller = new Controller(new ArrayList<>());
		//PrimoFrame f = new PrimoFrame(controller);
		JFrame frm= new JFrame();
		SchermataDocente f= new SchermataDocente(controller,frm);
		f.frame.setVisible(true);
		
	}
}

