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


}

