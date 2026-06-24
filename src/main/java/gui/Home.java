package gui;

import controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class Home {
	private static final Controller controller=new Controller(new ArrayList<>());
	public static void main(String[] args) {
		try {
			controller.apriConnessioneDatabase();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Impossibile connettersi al database:\n" + e.getMessage(),
					"Errore di connessione", JOptionPane.ERROR_MESSAGE);
			return; // senza database non avviamo l'applicazione
		}


		Controller controller = new Controller(new ArrayList<>());
	PrimoFrame primoFrame = new PrimoFrame(controller);
		primoFrame.frame.setVisible(true);

	}

}


