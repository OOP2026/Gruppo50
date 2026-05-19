package controller;
import model.*;




public class Controller {
	private Studente studente;
	private Responsabile responsabile;
	private Docente docente;
	private Utente utente;


	public Controller(Utente utente) {
		if(utente instanceof Studente) {
			this.studente = (Studente) utente;
		}
		if(utente instanceof Responsabile) {
			this.responsabile = (Responsabile) utente;
		}
		if(utente instanceof Docente) {
			this.docente = (Docente) utente;
		}
	}



	public boolean accedi(String l, String pass) {
		return utente.login(l, pass);
	}

	public void visualizzaRichiesteSpostamento(Responsabile responsabile) {
		responsabile.VizualizzaRichiesteSpostamento();
	}

	public void apporvaRichiesta (Responsabile responsabile, int numeroRichiesta, OrarioLezioni ElencoLezioni){
		responsabile.SpostamentoLezione(numeroRichiesta,ElencoLezioni);
	}

	public void rifiutarichiesta (Responsabile responsabile, int numeroRichiesta){
		responsabile.rifiutaRichiesta(numeroRichiesta);
	}

	public void creaLezione(Responsabile responsabile,Lezione l, OrarioLezioni ElencoLezioni){
		responsabile.inserisciLezione(l,ElencoLezioni);
	}

	//Docente
}
