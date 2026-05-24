package controller;
import model.*;
import java.util.List;
import java.util.ArrayList;



public class Controller {
	private Studente studente;
	private Responsabile responsabile;
	private Docente docente;
	private Utente utente;
	private List<Utente> utentiRegistrati;


	public Controller(List<Utente> utentiRegistrati) {
		this.utentiRegistrati = utentiRegistrati;
	}

	public boolean accedi(String username, String password) {
		// Azzera i riferimenti precedenti per evitare bug tra un login e l'altro
		this.studente = null;
		this.docente = null;
		this.responsabile = null;
		this.utente = null;

		for (Utente u : utentiRegistrati) {
			if (u.login(username, password)) {
				this.utente = u;

				// Identifica il tipo di istanza
				if (u instanceof Responsabile) {
					this.responsabile = (Responsabile) u;
				} else if (u instanceof Docente) {
					this.docente = (Docente) u;
				} else if (u instanceof Studente) {
					this.studente = (Studente) u;
				}
				return true;
			}
		}
		return false;
	}




	public String getRuolo() {
		if (responsabile != null) return "RESPONSABILE";
		if (docente != null)      return "DOCENTE";
		if (studente != null)     return "STUDENTE";
		return null;
	}

	public void visualizzaRichiesteSpostamento() {
		responsabile.visualizzaRichiesteSpostamento();
	}

	public void approvaRichiesta (int numeroRichiesta, OrarioLezioni elencoLezioni){
		responsabile.spostamentoLezione(numeroRichiesta,elencoLezioni);
	}

	public void rifiutarichiesta (int numeroRichiesta){
		responsabile.rifiutaRichiesta(numeroRichiesta);
	}

	public void creaLezione(Lezione l, OrarioLezioni elencoLezioni){
		responsabile.inserisciLezione(l,elencoLezioni);
	}

	//Docente visualizza l'orario delle proprie lezionoùi
	public void visualizzaLezione(OrarioLezioni elencoLezioni){
		docente.visualizzaOrario(elencoLezioni);
	}
	//Docente indica i il giorno e una fascia oraria in cui non può fare lezione.
	public void aggiungiVincolo(String giorno, int oraInzio, int minutoInzio,int oraFIne,int minutoFine){
		docente.aggiungiVincolo(giorno,oraInzio,minutoInzio,oraFIne,minutoFine);
	}

	//Docente richiede di spostare la lezione indicando il nuovo e il vechio orario)
	public void richiestaspostamentoLezione(Responsabile responsabile, String motivo, String giornoVecchio, int oraInizioVecchio, int minutoInizioVecchio, int oraFineVecchio, int minutoFineVecchio, String giornoNuovo,
	                                        int oraInizioNuovo, int minutoInizioNuovo, int oraFineNuovo, int minutoFineNuovo){
		docente.richiestaSpostamentoLezione(responsabile,motivo,new Orario(giornoVecchio,oraInizioVecchio,minutoInizioVecchio,oraFineVecchio,minutoFineVecchio),new Orario(giornoNuovo,oraInizioNuovo,minutoInizioNuovo,oraFineNuovo,minutoFineNuovo));
	}

	//Studente visualizza l'orario delle lezioni del corso.
	public void visualizzaOrarioLezioni(OrarioLezioni elencoLezioni){
		studente.visualizzaOrarioLezioni(elencoLezioni);
	}

	//Utente
	public boolean registra(String name,String cogn, String email,String login, String pass,String ruolo){
		for (Utente u : utentiRegistrati) {
			if (u.getmail().equals(email)) {
				return false; // Non possono esistere più user con la stessa mail.
			}
		}
		Utente nuovoUtente;
		switch (ruolo.toUpperCase()) {
			case "RESPONSABILE":
				nuovoUtente = new Responsabile(name, cogn, email, login, pass);
				break;
			case "DOCENTE":
				nuovoUtente = new Docente(name, cogn, email, login, pass);
				break;
			case "STUDENTE":
			default:
				String matricola="DE00000000";
				nuovoUtente = new Studente(name, cogn, email, login, pass,matricola,1);
				break;
		}
		utentiRegistrati.add(new Utente(name, cogn,  email, login, pass));
		return true;
	}

}