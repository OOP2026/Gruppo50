package controller;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Controller {
	private Studente studente;
	private Responsabile responsabile;
	private Docente docente;
	private Utente utente;
	private List<Utente> utentiRegistrati;
	private OrarioLezioni orarioLezioni = new OrarioLezioni();

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
		if (docente != null) return "DOCENTE";
		if (studente != null) return "STUDENTE";
		return null;
	}

	public void visualizzaRichiesteSpostamento() {
		responsabile.visualizzaRichiesteSpostamento();
	}

	public void approvaRichiesta(int numeroRichiesta, OrarioLezioni elencoLezioni) {
		responsabile.spostamentoLezione(numeroRichiesta, elencoLezioni);
	}

	//Responsabile rifiuta lo spostamento
	public void rifiutarichiesta(int numeroRichiesta) {
		responsabile.rifiutaRichiesta(numeroRichiesta);
	}

	//Responsabile crea una lezione
	public String creaLezione(
			String nomeInsegnamento, int cfu, int annoCorso,
			String emailDocente,
			String nomeAula, int capienza,
			String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {

		Docente docenteTrovato = null;
		for (Utente u : utentiRegistrati) {
			if (u instanceof Docente && u.getmail().equals(emailDocente)) {
				docenteTrovato = (Docente) u;
				break;
			}
		}
		if (docenteTrovato == null) {
			return "Nessun docente registrato con questa email.";
		}

		try {
			Insegnamento insegnamento = new Insegnamento(nomeInsegnamento, cfu, annoCorso, docenteTrovato);
			Aula aula = new Aula(nomeAula, capienza);
			Orario orario = new Orario(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
			Lezione lezione = new Lezione(insegnamento, aula, orario);
			responsabile.inserisciLezione(lezione, orarioLezioni);
			return null;
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}

	//Docente visualizza l'orario delle proprie lezionoùi
	public void visualizzaLezione(OrarioLezioni elencoLezioni) {
		docente.visualizzaOrario(elencoLezioni);
	}

	//Docente indica i il giorno e una fascia oraria in cui non può fare lezione.
	public void aggiungiVincolo(String giorno, int oraInzio, int minutoInzio, int oraFIne, int minutoFine) {
		docente.aggiungiVincolo(giorno, oraInzio, minutoInzio, oraFIne, minutoFine);
	}

	public void rimuoviVincolo(int ind) {
		docente.rimuoviVincolo(ind);
	}

	//return vincoli
	public Object[][] ottieniVincoli() {
		List<Vincolo> v = docente.getVincoli();
		Object[][] data = new Object[v.size()][1];
		for (int i = 0; i < v.size(); i++) {
			data[i][0] = v.get(i).orario.giorno + " " + v.get(i).orario.getOrarioCompleto();
		}
		return data;
	}

	//Docente richiede di spostare la lezione indicando il nuovo e il vechio orario)
	public void richiestaspostamentoLezione(String motivo, String giornoVecchio, int oraInizioVecchio, int minutoInizioVecchio, int oraFineVecchio, int minutoFineVecchio, String giornoNuovo,
	                                        int oraInizioNuovo, int minutoInizioNuovo, int oraFineNuovo, int minutoFineNuovo) {
		docente.richiestaSpostamentoLezione(motivo, new Orario(giornoVecchio, oraInizioVecchio, minutoInizioVecchio, oraFineVecchio, minutoFineVecchio), new Orario(giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo));
	}

	public Object[][] ottieniRichiesteInviate() {
		List<Richiesta> r = docente.getRichiesteInviate();
		Object[][] data = new Object[r.size()][4];
		for (int i = 0; i < r.size(); i++) {
			data[i][0] = r.get(i).orarioLezioneDaSpostare.giorno + " " + r.get(i).orarioLezioneDaSpostare.getOrarioCompleto();
			data[i][1] = r.get(i).nuovoOrarioLezione.giorno + " " + r.get(i).nuovoOrarioLezione.getOrarioCompleto();
			data[i][2] = r.get(i).motivoRichiesta;
			data[i][3] = r.get(i).statoRichiesta;
		}
		return data;
	}

	public Object[][] getLezioniDocente() {
		List<Lezione> l = docente.ottieniLezioni(orarioLezioni);
		if(l.isEmpty()) return new Object[0][0];
		List<List<Lezione>> lezioniPerGiorno = new ArrayList<>();
		List<Object[]> data = new ArrayList<>();
		String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
		// raggruppa le lezione per giorno.
		for (String giorno:giorni) {
			lezioniPerGiorno.add(l.stream().filter(lezione -> lezione.orario.giorno.equalsIgnoreCase(giorno)).collect(Collectors.toList()));
		}
		while(true) {
		Object[] row = new Object[5];
		boolean hasLezioni=false;
		// crea la riga con le lezioni del giorno, se non ci sono lezioni per quel giorno mette ""
			for(int g=0; g<giorni.length; g++){
				row[g]=lezioniPerGiorno.get(g).isEmpty()? "":lezioniPerGiorno.get(g).get(0).infoLezione();
			}
			// rimuovi la prima lezione di ogni giorno, se non ci sono lezioni per quel giorno non fa nulla
			for(int j=0; j<giorni.length; j++){
				if(!lezioniPerGiorno.get(j).isEmpty()){
					lezioniPerGiorno.get(j).remove(0);
					hasLezioni=true;
				}
			}
			if(!hasLezioni) break;
			data.add(row);
		}
		Object [][] m=data.toArray(new Object[0][]);
		System.out.println("risultato riga 0 di 0: "+m[0][0]);
		System.out.println("risultato riga 0 di 2: "+m[0][1]);

		return data.toArray(new Object[0][]);
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
				String matricola = "DE"+String.format("%08d",utentiRegistrati.size()+1);
				Studente nuovoStudente = new Studente(name, cogn, email, login, pass, matricola, 1);
				nuovoUtente = nuovoStudente;
				this.studente = nuovoStudente;
				break;
		}
		utentiRegistrati.add(nuovoUtente);
		return true;
	}
	public String getMatricola() {
		//Condizione che verifica che lo studente non sia null altrimenti restituisce ""
		if (studente != null) {
			return studente.getmatricola();
		}
		return "";
	}

	/**
	 * Restituisce le lezioni dello studente attualmente loggato, raggruppate per giorno.
	 * <p>
	 * Le stringhe nella lista dei valori seguono il formato: {@code "HH:mm - HH:mm  |  NomeInsegnamento"}.
	 * Questo metodo viene utilizzato dalla vista (es. {@code SchermataStudente}) per popolare
	 * il tabellone dell'orario in modo dinamico.
	 * </p>
	 *
	 * @return Una {@link java.util.Map} ordinata (LinkedHashMap) dove la chiave è il giorno
	 * della settimana (es. "Lunedì") e il valore è la lista delle lezioni formattate.
	 * Restituisce una mappa vuota se non vi è nessuno studente loggato.
	 */
	public java.util.Map<String, java.util.List<String>> getLezioniStudentePerGiorno() {
		if (studente == null) return new java.util.HashMap<>();

		/*
		 * NOTA DI PROGETTAZIONE:
		 * Le lezioni accessibili allo studente sono filtrate per anno di corso.
		 * OrarioLezioni non espone la lista senza token, ma possiamo appoggiarci al metodo
		 * visualizzaOrarioCompleto(Studente) che già fa il filtraggio. Poiché però abbiamo
		 * bisogno dei dati strutturati (non solo stampa), estendiamo il Controller con
		 * accesso diretto all'orario interno tramite un metodo dedicato.
		 */
		java.util.Map<String, java.util.List<String>> mappa = new java.util.LinkedHashMap<>();
		String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
		for (String g : giorni) mappa.put(g, new java.util.ArrayList<>());

		for (model.Lezione l : orarioLezioni.getLezioniStudente(studente)) {
			String giorno = l.orario.giorno;
			String testo  = String.format("%02d:%02d - %02d:%02d  |  %s",
					l.orario.oraInizio, l.orario.minutoInizio,
					l.orario.oraFine,   l.orario.minutoFine,
					l.insegnamento.Nome);
			mappa.getOrDefault(giorno, new java.util.ArrayList<>()).add(testo);
		}
		return mappa;
	}
}