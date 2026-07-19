package gui;

import controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Classe di avvio dell'applicazione.
 * Inizializza il {@link Controller}, tenta la connessione al database
 * e apre la finestra principale {@link PrimoFrame}.
 */
public class Home {
	private static final Controller controller=new Controller(new ArrayList<>());

	/**
	 * Punto di ingresso dell'applicazione.
	 * Apre la connessione al database e passa in modalità online; se la connessione
	 * fallisce, passa in modalità offline (solo in memoria, dati non salvati) avvisando
	 * l'utente con un dialogo. Carica poi i dati iniziali e mostra il frame principale
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
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


