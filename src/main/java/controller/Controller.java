package controller;
import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;
import implementazioneDao.LezionePostgresDao;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Controller {
	private Studente studente;
	private Responsabile responsabile;
	private Responsabile responsabileTemp;
	private Docente docente;
	private Utente utente;
    ///E' una lista di tipo {@link Utente} che contiene tutti gli utenti che si sono registrati
	private List<Utente> utentiRegistrati;
	private OrarioLezioni orarioLezioni = new OrarioLezioni();
	private List<Insegnamento> insegnamentiRegistrati = new ArrayList<>();

	public Controller(List<Utente> utentiRegistrati) {
		this.utentiRegistrati = utentiRegistrati;

	}
	public void apriConnessioneDatabase() throws Exception {
		ConnessioneDatabase.getInstance();
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
					putResponsabile();
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
	///Imposta il responsabileTemp che serve per mandare le richieste al responsabile
	public void putResponsabile(){
		for (Utente u : utentiRegistrati) {
				this.utente = u;

				if (u instanceof Responsabile) {
					this.responsabileTemp = (Responsabile) u;
                    return;
				}
			}
responsabileTemp=null;
	}
    ///Controlla se non è null {@code responsabileTemp} se lo è lancia una {@link Exception}
    /// @exception NullPointerException
    public void checkResponsabileTemp(){
        if(responsabileTemp==null){
            throw new NullPointerException("Non è presente ancora un responsabile");
        }
    }



	//Responsabile rifiuta lo spostamento


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
			// 1) Salva la lezione in memoria nel Model (valida orario e disponibilità del docente)
			responsabile.inserisciLezione(lezione, orarioLezioni);

			// 2) Rende persistente la lezione sul database tramite il DAO (pattern BCE + DAO)
			LezioneDAO lezioneDAO = new LezionePostgresDao();
			lezioneDAO.salvaLezioneDB(
					nomeInsegnamento, annoCorso,
					emailDocente,
					nomeAula, capienza,
					giorno, oraInizio, minutoInizio, oraFine, minutoFine);

			return null;
		} catch (IllegalArgumentException e) {
			// Errore di validazione del Model (es. orario non valido, docente non disponibile)
			return e.getMessage();
		} catch (Exception e) {
			// Errore proveniente dal database (rilanciato dal DAO)
			return e.getMessage();
		}
	}

	//Docente visualizza l'orario delle proprie lezionoùi
	public void visualizzaLezione(OrarioLezioni elencoLezioni) {
		docente.visualizzaOrario(elencoLezioni);
	}
///Permette al docente di aggiungere un Insegnamento che può insegnare
    public void addInsegnamentoDocente(String materia){

                docente.addInsegnamento(stringToInsegnamento(materia));

    }
    ///Questo metodo permette al {@link Docente docente} di rimuovere una materia che insegna,
    /// serve solo inserire come parametro il nome dell'{@link Insegnamento insegnamento} da rimuovere
    ///@return Restituisce una {@code String} o {@code null}
    public String removeInsegnamentoDocente(String materia){
		try{
			docente.removeInsegnamento(stringToInsegnamento(materia));
		}catch(Exception ex){
			return ex.getMessage();
		}
		return null;
    }
    ///Ritorna un  {@link Insegnamento insegnamento} solo se esiste nell'elenco degli insegnamenti attivi
    /// @return Restituisce un oggetto di tipo {@link Insegnamento}
    /// @param materia è il nome dell'insegnamento
    private Insegnamento stringToInsegnamento(String materia){
        for(Insegnamento insegnamento:insegnamentiRegistrati){
            if(insegnamento.Nome.equalsIgnoreCase(materia)){
                 return insegnamento;
            }
        }
        return null;
    }
    ///Ritorna gli insegnamenti registrati meno quelli del docente però solo il nome
    ///@return Restituisce una lista di tipo {@code String}
    public List<String> getInsegnamentiRegistratiDocente(){
        List<String> data= new ArrayList<>();
        List<Insegnamento> a= new ArrayList<>(insegnamentiRegistrati);
        List<Insegnamento> b= docente.getInsegnamenti();
        a.removeAll(b);
        for(Insegnamento insegnamento:a){
            data.add(insegnamento.Nome);
        }
        return data;
    };
    ///Ritorna gli insegnamenti registrati  però solo il nome
    /// @param materia Se materia è {@code ""} il metodo ritorna tutte le materie registrate, se no ritorna le materie che iniziano con la stringa dentro materia
    /// @return Restituisce una lista di tipo {@code String}
    public List<String> getInsegnamentiRegistrati(String materia){
        List<String> data= new ArrayList<>();
        List<Insegnamento> a= new ArrayList<>(insegnamentiRegistrati);

        for(Insegnamento insegnamento:a){
            if(insegnamento.Nome.toLowerCase().startsWith(materia))
            data.add(insegnamento.Nome);
        }
        return data;
    };
    ///Ritorna gli insegnamenti del docente
    ///@return Restituisce un array di tipo {@code Object[][]}
    public Object[][] getInsegnamentiDocente(){
        List<Insegnamento> insegnamenti= docente.getInsegnamenti();
        if(insegnamenti.isEmpty()){ return new Object[0][0];}
        Object[][] data=new Object[insegnamenti.size()][3];
        for(int i=0; i<insegnamenti.size(); i++){
         data[i][0]=insegnamenti.get(i).Nome;
         data[i][1]=insegnamenti.get(i).NumeroCFU;
         data[i][2]=insegnamenti.get(i).AnnoCorso;
        }
        return data;
    };
    ///Permette al docente di aggiungere un {@link Vincolo} max 3.
	///Docente indica i il giorno e una fascia oraria in cui non può fare lezione.
    ///@return Restituisce una {@code String} o {@code null}
	public String aggiungiVincolo(String giorno, int oraInzio, int minutoInzio, int oraFIne, int minutoFine) {
        try{
            docente.aggiungiVincolo(giorno, oraInzio, minutoInzio, oraFIne, minutoFine);
        }catch(Exception e){
            return e.getMessage();
        }
        return null;
	}
///Permette di rimuovere un {@link Vincolo vincolo} usando la posizione del vincolo che si vuole rimuovere
/// @return Restituisce una {@code String} o {@code null}
/// @param ind E' la posizione in cui sta il vincolo nella list vincoli del docente
	public String rimuoviVincolo(int ind) {
        try{
		docente.rimuoviVincolo(ind);}
        catch (Exception e){
            return e.getMessage();
        }
        return null;
	}

	///Un metodo che permette di ottenere i {@link Vincolo vincoli} del docente
    /// @return Restituisce un array di tipo {@code Object[][]}
	public Object[][] ottieniVincoli() {
		List<Vincolo> v = docente.getVincoli();

		Object[][] data = new Object[v.size()][1];
		for (int i = 0; i < v.size(); i++) {
			data[i][0] = v.get(i).orario.giorno + " " + v.get(i).orario.getOrarioCompleto();
		}
		return data;
	}

	///Permette al {@link Docente Docente} di richiedere di spostare la lezione indicando il nuovo e il vecchio orario
	public void richiestaspostamentoLezione(String motivo, String giornoVecchio, int oraInizioVecchio, int minutoInizioVecchio, int oraFineVecchio, int minutoFineVecchio, String giornoNuovo,
	                                        int oraInizioNuovo, int minutoInizioNuovo, int oraFineNuovo, int minutoFineNuovo) {
        checkResponsabileTemp();
		docente.richiestaSpostamentoLezione(orarioLezioni,responsabileTemp,motivo, new Orario(giornoVecchio, oraInizioVecchio, minutoInizioVecchio, oraFineVecchio, minutoFineVecchio), new Orario(giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo));
	}
/// Restituisce un array che contiene le richieste inviate dal docente
/// @return Restituisce un array di tipo {@code Object[][]}
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
///Il metodo ritorna le lezioni del docente in ordine, prima il giorno e poi l'orario
/// @return Ritorna una array di tipo {@code Object[][]}
	public Object[][] getLezioniDocente() {
		List<Lezione> l = docente.getLezioni(orarioLezioni);

		if(l.isEmpty()){

            return new Object[0][0]; }
		List<List<Lezione>> lezioniPerGiorno = new ArrayList<>();
		List<Object[]> data = new ArrayList<>();
		String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
		// raggruppa le lezione per giorno.
		for (String giorno:giorni) {
            //crea un list con lezioni per ogni giorno dentro un'altra list
			lezioniPerGiorno.add(l.stream().filter(lezione -> lezione.orario.giorno.equalsIgnoreCase(giorno)).collect(Collectors.toList()));

        }
		while(true) {
		Object[] row = new Object[5];
		boolean hasLezioni=false;
		// crea la riga con le lezioni del giorno, se non ci sono lezioni per quel giorno mette ""
			for(int g=0; g<giorni.length; g++){
				row[g]=lezioniPerGiorno.get(g).isEmpty()? "":lezioniPerGiorno.get(g).get(0).infoLezione();
			}
			// rimuove la prima lezione di ogni giorno, se non ci sono lezioni per quel giorno non fa nulla
			for(int j=0; j<giorni.length; j++){
				if(!lezioniPerGiorno.get(j).isEmpty()){
					lezioniPerGiorno.get(j).remove(0);
					hasLezioni=true;
				}
			}
            //Se non ci sono piu lezioni in nessuna lista ferma il loop
			if(!hasLezioni) break;
			data.add(row);
		}

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
	public String registraInsegnamento(String nome, int cfu, int annoCorso, String emailDocente) {
		Docente docenteTrovato = null;
		for (Utente u : utentiRegistrati) {
			if (u instanceof Docente && u.getmail().equals(emailDocente)) {
				docenteTrovato = (Docente) u;
				break;
			}
		}
		if (docenteTrovato == null) return "Nessun docente registrato con questa email.";

		Insegnamento candidato = new Insegnamento(nome, cfu, annoCorso, docenteTrovato);
		if (insegnamentiRegistrati.contains(candidato)) {
			return "Insegnamento già presente.";
		}
		insegnamentiRegistrati.add(candidato);
		return null;
	}

	/**
	 * Restituisce la lista degli insegnamenti attivi formattata per la JTable.
	 * Ogni Object[] contiene: [Nome, CFU, AnnoCorso, email docente]
	 */
	public List<Object[]> getInsegnamentiAttivi() {
		List<Object[]> righe = new ArrayList<>();
		for (Insegnamento ins : insegnamentiRegistrati) {
			righe.add(new Object[]{
					ins.Nome,
					ins.NumeroCFU,
					ins.AnnoCorso,
					ins.docente.getmail()
			});
		}
		return righe;
	}
	/// Metodo che utilizza il get richieste di responsabile
	/// Viene utilizzato dalla gui nella dialog visualizzaRichiesta, per ottenere le richieste in ATTESA per quel responsabile
	public Object[][] getRichiesteSpostamento() {
		List<model.Richiesta> lista = responsabile.getRichiesteSpostamento();
		Object[][] data = new Object[lista.size()][6];
		for (int i = 0; i < lista.size(); i++) {
			model.Richiesta r = lista.get(i);
			data[i][0] = i;
			data[i][1] = r.docenteRichiedente.nome + " " + r.docenteRichiedente.cognome;
			data[i][2] = r.orarioLezioneDaSpostare.giorno + " "
					+ r.orarioLezioneDaSpostare.getOrarioCompleto();
			data[i][3] = r.nuovoOrarioLezione.giorno + " "
					+ r.nuovoOrarioLezione.getOrarioCompleto();
			data[i][4] = r.motivoRichiesta;
			data[i][5] = responsabile.getStatoRichiesta(i);
		}
		return data;
	}
	///Metodo usato nella gui dalla dialog Visualizza Richiesta.
	/// Approva la richiesta dato il numero di richiesta in input
	public String approvaRichiestaSpostamento(int numeroRichiesta) {
		if (!responsabile.isRichiestaInAttesa(numeroRichiesta)) {
			String stato = responsabile.getStatoRichiesta(numeroRichiesta);
			if (stato == null) return "Numero richiesta non valido.";
			return "La richiesta è già stata " +
					(stato.equals("APPROVATA") ? "approvata." : "rifiutata.");
		}

		responsabile.spostamentoLezione(numeroRichiesta, orarioLezioni, true);   // <-- true

		// spostamentoLezione imposta RIFIUTATA automaticamente in caso di conflitto
		if (!responsabile.getStatoRichiesta(numeroRichiesta).equals("APPROVATA")) {
			return "Impossibile spostare la lezione: conflitto di orario. "
					+ "La richiesta è stata rifiutata automaticamente.";
		}

		return null; // successo
	}
	///Metodo usato nella gui dalla dialog Visualizza Richiesta.
	/// Rifiuta la richiesta dato il numero di richiesta in input
	public void rifiutaRichiestaSpostamento(int numeroRichiesta) {
		if (!responsabile.isRichiestaInAttesa(numeroRichiesta)) {
			return; // già processata o indice non valido
		}
		responsabile.spostamentoLezione(numeroRichiesta, orarioLezioni, false);   // <-- false
	}
}