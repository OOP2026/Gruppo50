package gui;

import controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class Home {
	private static final Controller controller=new Controller(new ArrayList<>());
	public static void main(String[] args) {
		try {
			controller.apriConnessioneDatabase();
			controller.online();
		} catch (Exception e) {
			controller.offline();
			JOptionPane.showMessageDialog(null,
					"Impossibile connettersi al database.\n" +
							"L'applicazione funzionerà solo in memoria: i dati non verranno salvati.\n\n" + e.getMessage(),
					"Database non disponibile", JOptionPane.WARNING_MESSAGE);
		}

		// Carica una sola volta dal DB utenti, insegnamenti e lezioni
		// (se il DB non è disponibile il metodo non fa nulla)
		controller.caricaDatiIniziali();

		PrimoFrame primoFrame = new PrimoFrame(controller);
		primoFrame.frame.setVisible(true);

	}

}


