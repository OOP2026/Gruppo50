package controller;
import dao.*;
import database_connection.ConnessioneDatabase;
import implementazionedao.*;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;


public class Controller {
	private static final Logger logger = Logger.getLogger(Controller.class.getName());
   private static final String RESPONSABILE_RUOLO= "RESPONSABILE";
   private static final String STUDENTE_RUOLO="STUDENTE";
   private static final String DOCENTE_RUOLO="DOCENTE";
	private Studente studente;
	private Responsabile responsabile;
	private Responsabile responsabileTemp;
	private Docente docente;
	private Utente utente;
	///E' una lista di tipo {@link Utente} che contiene tutti gli utenti che si sono registrati
	private final List<Utente> utentiRegistrati;
	private OrarioLezioni orarioLezioni = new OrarioLezioni();
	private List<Insegnamento> insegnamentiRegistrati = new ArrayList<>();
	private List<Aula> aule=new ArrayList<>();
	public Controller(List<Utente> utentiRegistrati) {
		this.utentiRegistrati = utentiRegistrati;

	}
	public void apriConnessioneDatabase() throws SQLException {
		ConnessioneDatabase.getInstance();
	}
/// Azzera i riferimenti precedenti per evitare bug tra un login e l'altro
	public void logout(){
		//Logout
		this.studente=null;
		this.docente=null;
		this.responsabile=null;
		this.responsabileTemp=null;
		this.utente= null;
	}
    public boolean accedi(String username, String password) {

        // Carica dal database tutti gli utenti registrati (studenti, docenti e
        // responsabili), così è possibile accedere anche a utenti salvati in
        // sessioni precedenti (pattern BCE + DAO).
        caricaUtentiDaDB();
        caricaLezioniDaDB();

        for (Utente u : utentiRegistrati) {
            if (u.login(username, password)) {
                this.utente = u;

                // Identifica il tipo di istanza
                if(isResponsabile(u)){
                    return true;
                }
                if(isDocente(u)){
                    return true;
                }
                if(isStudente(u)){
                    return true;
                }
                break;
            }
        }
        return false;
    }
    /// Questo metodo controlla se l'utente è uno studente
    /// @return Ritorna un valore booleano
    public boolean isStudente(Utente u){
        if (u instanceof Studente) {
            this.studente = (Studente) u;
            return true;
        }
        return false;
    }
    ///Questo metodo controlla se l'utente è un responsabile
    /// @return Ritorna un valore booleano
    public boolean isResponsabile(Utente u){
        if (u instanceof Responsabile) {
            this.responsabile = (Responsabile) u;
            try{caricaAuleDaDB(); }
            catch(Exception e){
                logger.info("Errore caricamento aule: "+e.getMessage());
            }
            // Carica le richieste in attesa così il responsabile le vede in GUI
            String erroreRichieste = caricaRichiesteResponsabileDaDB();
            if (erroreRichieste != null)
                logger.info("Errore caricamento richieste: "+ erroreRichieste);
            return true;
        }
        return false;
    }
    ///Questo metodo controlla se l'utente è un docente
    /// @return Ritorna un valore booleano
    public boolean isDocente(Utente u){
        if (u instanceof Docente) {
            this.docente = (Docente) u;
            putResponsabile();
            // Carica le richieste inviate così il docente le vede in GUI
            String erroreRichieste = caricaRichiesteDocenteDaDB();
            if (erroreRichieste != null)
                logger.info("Errore caricamento richieste: " + erroreRichieste);
            return true;
        }
        return false;
    }


	public String getRuolo() {
		if (responsabile != null) return RESPONSABILE_RUOLO;
		if (docente != null) return DOCENTE_RUOLO;
		if (studente != null) return STUDENTE_RUOLO;
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
	/// @throws NullPointerException
	public void checkResponsabileTemp(){
		if(responsabileTemp==null){
			throw new NullPointerException("Non è presente ancora un responsabile");
		}
	}



	//Responsabile rifiuta lo spostamento


	//Responsabile crea una lezione
	//Responsabile crea una lezione
	public String creaLezione(
			String nomeInsegnamento, int cfu, int annoCorso,
			String emailDocente,
			String nomeAula,
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

		// 1) Inserimento in memoria nel Model (valida orario e disponibilità del docente).
		//    Se questa parte fallisce è un errore vero: si interrompe e si mostra il messaggio.
		try {
			Insegnamento insegnamento = new Insegnamento(nomeInsegnamento, cfu, annoCorso, docenteTrovato);
			Aula aula = new Aula(nomeAula, 200);
			Orario orario = new Orario(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
			Lezione lezione = new Lezione(insegnamento, aula, orario);
			responsabile.inserisciLezione(lezione, orarioLezioni);
		} catch (IllegalArgumentException e) {
			// Errore di validazione del Model (es. orario non valido, docente non disponibile)
			return e.getMessage();
		}

		// 2) Persistenza: la lezione viene salvata nella tabella lezione.
		//    Se il salvataggio fallisce (DB non disponibile, aula inesistente nella
		//    tabella aula, conflitto rilevato dal trigger del DB) la lezione resta
		//    solo in memoria e l'errore viene segnalato a console.
		try {
			LezioneDAO lezioneDAO = new LezionePostgresDao();
			lezioneDAO.salvaLezioneDB(
					nomeInsegnamento, annoCorso,
					emailDocente,
					nomeAula, giorno,
					oraInizio, minutoInizio, oraFine, minutoFine);
		} catch (Exception e) {
			logger.info("Lezione NON salvata sul database (resta solo in memoria): " + e.getMessage());
		}

		return null;
	}
	///Permette al docente di aggiungere un Insegnamento che può insegnare
	public void addInsegnamentoDocente(String materia){

		docente.addInsegnamento(stringToInsegnamento(materia));

	}
	///Questo metodo permette al {@link Docente docente} di rimuovere una materia che insegna,
	/// serve solo inserire come parametro il nome dell'{@link Insegnamento insegnamento} da rimuovere
	///@Returns Restituisce una {@code String} o {@code null}
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
			if(insegnamento.getNome().equalsIgnoreCase(materia)){
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
			data.add(insegnamento.getNome());
		}
		return data;
	}
	/**Ritorna gli insegnamenti registrati  però solo il nome
	 *@param materia <p>Se materia è {@code ""} il metodo ritorna tutte le materie registrate,
	 *se no ritorna le materie che iniziano con la stringa dentro materia.
	 *@return Restituisce una lista di tipo {@code String}
	 */
	public List<String> getInsegnamentiRegistrati(String materia){
		List<String> data= new ArrayList<>();
		List<Insegnamento> a= new ArrayList<>(insegnamentiRegistrati);

		for(Insegnamento insegnamento:a){
			if(insegnamento.getNome().toLowerCase().startsWith(materia))
				data.add(insegnamento.getNome());
		}
		return data;
	}
	///Ritorna gli insegnamenti del docente
	///@return Restituisce un array di tipo {@code Object[][]}
	public Object[][] getInsegnamentiDocente(){
		List<Insegnamento> insegnamenti= docente.getInsegnamenti();
		if(insegnamenti.isEmpty()){ return new Object[0][0];}
		Object[][] data=new Object[insegnamenti.size()][3];
		for(int i=0; i<insegnamenti.size(); i++){
			data[i][0]=insegnamenti.get(i).getNome();
			data[i][1]=insegnamenti.get(i).getNumeroCFU();
			data[i][2]=insegnamenti.get(i).getAnnoCorso();
		}
		return data;
	}
	///Permette al docente di aggiungere un {@link Vincolo} max 3.
	///Docente indica i il giorno e una fascia oraria in cui non può fare lezione.
	///@return Restituisce una {@code String} o {@code null}
	public String aggiungiVincolo(String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {
		try{
            VincoloDAO vincoloDAO= new VincoloPostgresDao();
            vincoloDAO.salvaVincoloDB(this.docente.getmail(),giorno,oraInizio,minutoInizio,oraFine,minutoFine);
			docente.aggiungiVincolo(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
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
			VincoloDAO vincoloDAO= new VincoloPostgresDao();
			List<Vincolo> vincoli= new ArrayList<>(docente.getVincoli());
			Vincolo v= vincoli.get(ind);
			//rimozioni attraverso db
			vincoloDAO.rimuoviVincoloDB(this.docente.getmail(),v.getOrario().getGiorno(),v.getOrario().getOraInizio(),v.getOrario().getMinutoInizio(),v.getOrario().getOraFine(),v.getOrario().getMinutoFine());
			docente.rimuoviVincolo(ind);}
		catch (Exception e){
			return e.getMessage();
		}
		return null;
	}

	/**
	 * Un metodo che permette di ottenere i {@link Vincolo vincoli} del docente
	 * @return Restituisce un array di tipo {@code Object[][]}
	 */
	public Object[][] ottieniVincoli() {
		List<Vincolo> v = docente.getVincoli();

		Object[][] data = new Object[v.size()][1];
		for (int i = 0; i < v.size(); i++) {
			data[i][0] = v.get(i).getOrario().getGiorno() + " " + v.get(i).getOrario().getOrarioCompleto();
		}
		return data;
	}
	// Metodo per i vincoli
	/**Permette al docente di aggiungere un {@link Vincolo} max 3.
	 *Docente indica i il giorno e una fascia oraria in cui non può fare lezione.
	 *@return Restituisce una {@code String} o {@code null}
	 */
	public String caricaVincoliDaDB(){
		try{
			VincoloDAO vincoloDAO = new VincoloPostgresDao();
			List<Vincolo> vs=new ArrayList<>();
			Object[][] vincoli = vincoloDAO.caricaVincoliDB(docente.getmail());
			for (Object[] vincolo : vincoli) {
				vs.add(new Vincolo((String)vincolo[0],(int)vincolo[1],(int)vincolo[2],(int)vincolo[3],(int)vincolo[4]));
			}
			docente.caricaVincoliInDocente(vs);
		}catch(Exception e){
			return e.getMessage();
		}
		return null;
	}

	///Permette al {@link Docente Docente} di richiedere di spostare la lezione indicando il nuovo e il vecchio orario
	public void richiestaspostamentoLezione(String motivo, String giornoVecchio, int oraInizioVecchio, int minutoInizioVecchio, int oraFineVecchio, int minutoFineVecchio, String giornoNuovo,
	                                        int oraInizioNuovo, int minutoInizioNuovo, int oraFineNuovo, int minutoFineNuovo) {
		checkResponsabileTemp();
		docente.richiestaSpostamentoLezione(orarioLezioni,responsabileTemp,motivo, new Orario(giornoVecchio, oraInizioVecchio, minutoInizioVecchio, oraFineVecchio, minutoFineVecchio), new Orario(giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo));

		// Persistenza: la richiesta viene salvata nella tabella richiesta (stato
		// di default 'IN_ATTESA'). Se il salvataggio fallisce la richiesta resta
		// solo in memoria e l'errore viene segnalato a console (stesso pattern di
		// creaLezione). L'id generato dal DB viene memorizzato nella richiesta,
		// così approvazione/rifiuto potranno aggiornare la riga corretta.
		try {
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			int idGenerato = richiestaDAO.salvaRichiestaDB(
					docente.getmail(), responsabileTemp.getmail(), motivo,
					giornoVecchio, oraInizioVecchio, minutoInizioVecchio, oraFineVecchio, minutoFineVecchio,
					giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo);
			List<Richiesta> inviate = docente.getRichiesteInviate();
			inviate.get(inviate.size() - 1).setId(idGenerato);
		} catch (Exception e) {
			logger.info("Richiesta NON salvata sul database (resta solo in memoria): " + e.getMessage());
		}
	}

	/**Carica dal database le richieste inviate dal docente loggato (di qualsiasi
	 * stato: in attesa, approvate e rifiutate) e le mette nella lista in memoria
	 * del docente, così la GUI ({@code SchermataRichiesteInviate}) le vede tramite
	 * {@link #ottieniRichiesteInviate()} anche nelle sessioni successive.
	 * Viene invocato al login del docente.
	 * @return Restituisce una {@code String} con l'errore o {@code null} se tutto ok
	 */
	public String caricaRichiesteDocenteDaDB() {
		try {
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			ArrayList<Integer> id = new ArrayList<>();
			ArrayList<String> emailResponsabile = new ArrayList<>();
			ArrayList<String> motivo = new ArrayList<>();
			ArrayList<String> giornoIniziale = new ArrayList<>();
			ArrayList<Integer> oraInizioIniziale = new ArrayList<>();
			ArrayList<Integer> minutoInizioIniziale = new ArrayList<>();
			ArrayList<Integer> oraFineIniziale = new ArrayList<>();
			ArrayList<Integer> minutoFineIniziale = new ArrayList<>();
			ArrayList<String> giornoProposto = new ArrayList<>();
			ArrayList<Integer> oraInizioProposto = new ArrayList<>();
			ArrayList<Integer> minutoInizioProposto = new ArrayList<>();
			ArrayList<Integer> oraFineProposto = new ArrayList<>();
			ArrayList<Integer> minutoFineProposto = new ArrayList<>();
			ArrayList<String> stato = new ArrayList<>();
			richiestaDAO.leggiRichiesteDocenteDB(docente.getmail(),
					id, emailResponsabile, motivo,
					giornoIniziale, oraInizioIniziale, minutoInizioIniziale, oraFineIniziale, minutoFineIniziale,
					giornoProposto, oraInizioProposto, minutoInizioProposto, oraFineProposto, minutoFineProposto,
					stato);

			List<Richiesta> richieste = new ArrayList<>();
			for (int i = 0; i < id.size(); i++) {
				Richiesta r = new Richiesta(docente, motivo.get(i),
						new Orario(giornoIniziale.get(i), oraInizioIniziale.get(i), minutoInizioIniziale.get(i),
								oraFineIniziale.get(i), minutoFineIniziale.get(i)),
						new Orario(giornoProposto.get(i), oraInizioProposto.get(i), minutoInizioProposto.get(i),
								oraFineProposto.get(i), minutoFineProposto.get(i)));
				r.setId(id.get(i));
				r.caricaStatoDaDB(stato.get(i));
				richieste.add(r);
			}
			docente.caricaRichiesteInviate(richieste);
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}

	/**Carica dal database tutte le richieste ancora in stato {@code IN_ATTESA}
	 * (di qualsiasi docente) e le mette nella lista in memoria del responsabile
	 * loggato, così la GUI ({@code VisualizzaRichiestaDialog}) le vede tramite
	 * {@link #getRichiesteSpostamento()} anche nelle sessioni successive.
	 * Viene invocato al login del responsabile.
	 * @return Restituisce una {@code String} con l'errore o {@code null} se tutto ok
	 */
	public String caricaRichiesteResponsabileDaDB() {
		try {
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			ArrayList<Integer> id = new ArrayList<>();
			ArrayList<String> emailDocente = new ArrayList<>();
			ArrayList<String> emailResponsabile = new ArrayList<>();
			ArrayList<String> motivo = new ArrayList<>();
			ArrayList<String> giornoIniziale = new ArrayList<>();
			ArrayList<Integer> oraInizioIniziale = new ArrayList<>();
			ArrayList<Integer> minutoInizioIniziale = new ArrayList<>();
			ArrayList<Integer> oraFineIniziale = new ArrayList<>();
			ArrayList<Integer> minutoFineIniziale = new ArrayList<>();
			ArrayList<String> giornoProposto = new ArrayList<>();
			ArrayList<Integer> oraInizioProposto = new ArrayList<>();
			ArrayList<Integer> minutoInizioProposto = new ArrayList<>();
			ArrayList<Integer> oraFineProposto = new ArrayList<>();
			ArrayList<Integer> minutoFineProposto = new ArrayList<>();
			richiestaDAO.leggiRichiesteInAttesaDB(
					id, emailDocente, emailResponsabile, motivo,
					giornoIniziale, oraInizioIniziale, minutoInizioIniziale, oraFineIniziale, minutoFineIniziale,
					giornoProposto, oraInizioProposto, minutoInizioProposto, oraFineProposto, minutoFineProposto);

			List<Richiesta> richieste = new ArrayList<>();
			for (int i = 0; i < id.size(); i++) {
				Docente docenteRichiedente = trovaDocentePerEmail(emailDocente.get(i));
				Richiesta r = new Richiesta(docenteRichiedente, motivo.get(i),
						new Orario(giornoIniziale.get(i), oraInizioIniziale.get(i), minutoInizioIniziale.get(i),
								oraFineIniziale.get(i), minutoFineIniziale.get(i)),
						new Orario(giornoProposto.get(i), oraInizioProposto.get(i), minutoInizioProposto.get(i),
								oraFineProposto.get(i), minutoFineProposto.get(i)));
				r.setId(id.get(i));
				// lo stato non viene letto: le richieste estratte sono tutte IN_ATTESA (default)
				richieste.add(r);
			}
			responsabile.caricaRichiesteSpostamento(richieste);
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}

	/**Aggiorna sul database lo stato corrente (letto dal Model) della richiesta
	 * indicata. Best-effort: se la richiesta non era mai stata salvata sul DB
	 * ({@code id < 0}) o l'aggiornamento fallisce, lo stato resta aggiornato solo
	 * in memoria e l'errore viene segnalato a console.
	 * @param numeroRichiesta posizione della richiesta nella lista del responsabile
	 */
	private void aggiornaStatoRichiestaSuDB(int numeroRichiesta) {
		try {
			Richiesta r = responsabile.getRichiesteSpostamento().get(numeroRichiesta);
			if (r.getId() < 0) return; // richiesta mai salvata sul database
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			richiestaDAO.aggiornaStatoRichiestaDB(r.getId(), responsabile.getStatoRichiesta(numeroRichiesta));
		} catch (Exception e) {
			logger.info("Stato richiesta NON aggiornato sul database: " + e.getMessage());
		}
	}
	/// Restituisce un array che contiene le richieste inviate dal docente
	/// @return Restituisce un array di tipo {@code Object[][]}
	public Object[][] ottieniRichiesteInviate() {
		List<Richiesta> r = docente.getRichiesteInviate();
		Object[][] data = new Object[r.size()][4];
		for (int i = 0; i < r.size(); i++) {
			data[i][0] = r.get(i).getOrarioLezioneDaSpostare().getGiorno() + " " + r.get(i).getOrarioLezioneDaSpostare().getOrarioCompleto();
			data[i][1] = r.get(i).getNuovoOrarioLezione().getGiorno() + " " + r.get(i).getNuovoOrarioLezione().getOrarioCompleto();
			data[i][2] = r.get(i).getMotivoRichiesta();
			data[i][3] = r.get(i).getStatoRichiesta();
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
			lezioniPerGiorno.add(l.stream().filter(lezione -> lezione.getOrario().getGiorno().equalsIgnoreCase(giorno)).collect(Collectors.toList()));

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

	/**
	 * Metodo che permette all'utente di registrarsi come Studente o Docente o Responsabile.
	 * @param name Nome di battesimo del utente registrato.
	 * @param cogn Cognome di battesimo del utente registrato
	 * @param email l'email utilizzata in fase di registrazione.
	 * @param login l'username con cui l'Utente accede.
	 * @param pass la password con cui l'Utente accede.
	 * @param ruolo indica che ruolo svolgi all'interno dell'università.
	 * @return nel caso in cui l'inserimento nel DB fallisce e restituisce FALSE altrimenti torna TRUE.
	 */
	public boolean registra(String name,String cogn, String email,String login, String pass,String ruolo){
		for (Utente u : utentiRegistrati) {
			if (u.getmail().equals(email)) {
				return false; // Non possono esistere più user con la stessa mail.
			}
		}
		Utente nuovoUtente;

		switch (ruolo.toUpperCase()) {
			case RESPONSABILE_RUOLO:
				try {
					ResponsabileDAO responsabileDAO = new ResponsabilePostgresDao();
					responsabileDAO.salvaResponsabileDB(name, cogn, email, login, pass);
				} catch (Exception e) {
					logger.info("DB non disponibile, registrazione responsabile solo in memoria: " + e.getMessage());
				}
				nuovoUtente = new Responsabile(name, cogn, email, login, pass);
				break;
			case DOCENTE_RUOLO:
				try {
					DocenteDAO docenteDAO = new DocentePostgresDao();
					docenteDAO.salvaDocDB(name, cogn, email, login, pass);
				} catch (Exception e) {
					logger.info("DB non disponibile, registrazione docente solo in memoria: " + e.getMessage());
				}
				nuovoUtente = new Docente(name, cogn, email, login, pass);
				break;
			case STUDENTE_RUOLO:
			default:
				String matricola;
				try {
					StudenteDAO studenteDAO = new StudentePostgresDao();
					matricola = studenteDAO.generaMatricolaDB();
					studenteDAO.salvaStudenteDB(name, cogn, email, login, pass, matricola, 1);
				} catch (Exception e) {
					logger.info("DB non disponibile, registrazione studente solo in memoria: " + e.getMessage());
					long numStudenti = utentiRegistrati.stream().filter(u -> u instanceof Studente).count();
					matricola = "DE" + String.format("%08d", numStudenti + 1);
				}
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
			String giorno = l.getOrario().getGiorno();
			String testo  = String.format("%02d:%02d - %02d:%02d  |  %s",
					l.getOrario().getOraInizio(), l.getOrario().getMinutoInizio(),
					l.getOrario().getOraFine(),   l.getOrario().getMinutoFine(),
					l.getInsegnamento().getNome());
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
					ins.getNome(),
					ins.getNumeroCFU(),
					ins.getAnnoCorso(),
					ins.getDocente().getmail()
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
			data[i][1] = r.getDocenteRichiedente().nome + " " + r.getDocenteRichiedente().cognome;
			data[i][2] = r.getOrarioLezioneDaSpostare().getGiorno() + " "
					+ r.getOrarioLezioneDaSpostare().getOrarioCompleto();
			data[i][3] = r.getNuovoOrarioLezione().getGiorno() + " "
					+ r.getNuovoOrarioLezione().getOrarioCompleto();
			data[i][4] = r.getMotivoRichiesta();
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
					(stato.equals(StatoRichiesta.APPROVATA.name()) ? "approvata." : "rifiutata.");
		}

		// <-- true
		responsabile.spostamentoLezione(numeroRichiesta, orarioLezioni, true);
		// Persiste il nuovo stato (APPROVATA o RIFIUTATA in caso di conflitto)
		aggiornaStatoRichiestaSuDB(numeroRichiesta);
		// spostamentoLezione imposta RIFIUTATA automaticamente in caso di conflitto
		if (!responsabile.getStatoRichiesta(numeroRichiesta).equals(StatoRichiesta.APPROVATA.name())) {
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
		// Persiste lo stato RIFIUTATA sul database
		aggiornaStatoRichiestaSuDB(numeroRichiesta);
	}
	public String modificaOrarioRichiesta(
			int numeroRichiesta, String giorno, int oraInizio, int minutoInizio,  int oraFine, int minutoFine){

		if (!responsabile.isRichiestaInAttesa(numeroRichiesta)) {
			String stato = responsabile.getStatoRichiesta(numeroRichiesta);
			if (stato == null) return "Numero richiesta non valido.";
			return "La richiesta è già stata " +
					(stato.equals(StatoRichiesta.APPROVATA.name()) ? "approvata." : "rifiutata.") +
					" L'orario non è più modificabile.";
		}
		try {
			Orario nuovoOrario = new Orario(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
			responsabile.cambiaOrarioRichiesta(numeroRichiesta, nuovoOrario);
			// Persiste il nuovo orario proposto sul database (best-effort)
			try {
				Richiesta r = responsabile.getRichiesteSpostamento().get(numeroRichiesta);
				if (r.getId() >= 0) {
					RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
					richiestaDAO.aggiornaOrarioPropostoDB(r.getId(), giorno, oraInizio, minutoInizio, oraFine, minutoFine);
				}
			} catch (Exception e) {
				logger.info("Orario proposto NON aggiornato sul database: " + e.getMessage());
			}
			return null; // successo
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}

	/** <p>Legge tutti gli utenti dal database tramite {@link UtenteDAO} (unico
	 * metodo di caricamento: la tabella {@code utente} contiene studenti, docenti
	 * e responsabili) e li aggiunge alla lista {@code utentiRegistrati}, evitando
	 * duplicati (confronto per email). In base alla colonna {@code ruolo} viene
	 * istanziata la classe corretta del Model. Per docenti e responsabili la
	 * matricola letta dal database viene ignorata: per il momento non viene
	 * visualizzata a schermo. Eventuali errori del database vengono segnalati a
	 * console senza interrompere il login degli utenti già presenti in memoria.</p>
	 */
	/**
	 * Carica dal database tutte le lezioni salvate e ricostruisce l'orario in
	 * memoria, cosi' che vengano visualizzate anche nelle sessioni successive
	 * (in particolare dai docenti). Viene invocato al login, dopo il caricamento
	 * degli utenti: per ogni lezione letta si ricerca il docente titolare tra
	 * gli utenti registrati e si ricostruiscono Insegnamento, Aula e Orario.
	 * L'orario viene ricostruito da zero a ogni login per riflettere fedelmente
	 * il database ed evitare lezioni duplicate. Eventuali errori del database
	 * vengono segnalati a console senza interrompere il login; in tal caso
	 * l'orario in memoria resta invariato.
	 */
	private void caricaLezioniDaDB() {
		try {
			LezioneDAO lezioneDAO = new LezionePostgresDao();
			ArrayList<String> nomiInsegnamento = new ArrayList<>();
			ArrayList<Integer> anniCorso = new ArrayList<>();
			ArrayList<String> emailDocente = new ArrayList<>();
			ArrayList<String> nomeAula = new ArrayList<>();
			ArrayList<String> giorno = new ArrayList<>();
			ArrayList<Integer> oraInizio = new ArrayList<>();
			ArrayList<Integer> minutoInizio = new ArrayList<>();
			ArrayList<Integer> oraFine = new ArrayList<>();
			ArrayList<Integer> minutoFine = new ArrayList<>();
			lezioneDAO.leggiTutteLezioniDB(nomiInsegnamento, anniCorso, emailDocente,
					nomeAula, giorno, oraInizio, minutoInizio, oraFine, minutoFine);

			OrarioLezioni orarioCaricato = new OrarioLezioni();
			for (int i = 0; i < nomiInsegnamento.size(); i++) {
				Docente docenteTitolare = trovaDocentePerEmail(emailDocente.get(i));
				Insegnamento insegnamento = new Insegnamento(
						nomiInsegnamento.get(i), 0, anniCorso.get(i), docenteTitolare);
				Aula aula = new Aula(nomeAula.get(i), 200);
				Orario orario = new Orario(giorno.get(i),
						oraInizio.get(i), minutoInizio.get(i),
						oraFine.get(i), minutoFine.get(i));
				orarioCaricato.caricaLezioneDaDB(new Lezione(insegnamento, aula, orario));
			}
			this.orarioLezioni = orarioCaricato;
		} catch (Exception e) {
			logger.info("Errore nel caricamento delle lezioni dal database: " + e.getMessage());
		}
	}

	/**
	 * Cerca tra gli utenti registrati il docente con l'email indicata. Se non
	 * viene trovato, ne crea uno "leggero" con la sola email valorizzata,
	 * sufficiente ad associare la lezione al docente corretto in fase di
	 * visualizzazione.
	 *
	 * @param email email del docente titolare della lezione
	 * @return il {@link Docente} corrispondente all'email
	 */
	private Docente trovaDocentePerEmail(String email) {
		for (Utente u : utentiRegistrati) {
			if (u instanceof Docente && u.getmail().equals(email)) {
				return (Docente) u;
			}
		}
		return new Docente("", "", email, "", "");
	}

	private void caricaUtentiDaDB() {
		try {
			UtenteDAO utenteDAO = new UtentePostgresDao();
			ArrayList<String> nomi = new ArrayList<>();
			ArrayList<String> cognomi = new ArrayList<>();
			ArrayList<String> emails = new ArrayList<>();
			ArrayList<String> logins = new ArrayList<>();
			ArrayList<String> passwords = new ArrayList<>();
			ArrayList<String> matricole = new ArrayList<>();
			ArrayList<Integer> anniCorso = new ArrayList<>();
			ArrayList<String> ruoli = new ArrayList<>();
			utenteDAO.leggiUtentiDB(nomi, cognomi, emails, logins, passwords, matricole, anniCorso, ruoli);

			for (int i = 0; i < emails.size(); i++) {
				boolean giaPresente = false;
				for (Utente u : utentiRegistrati) {
					if (u.getmail().equals(emails.get(i))) {
						giaPresente = true;
						break;
					}
				}
				if (giaPresente) continue;

				switch (ruoli.get(i).toUpperCase()) {
					case RESPONSABILE_RUOLO:
						utentiRegistrati.add(new Responsabile(nomi.get(i), cognomi.get(i),
								emails.get(i), logins.get(i), passwords.get(i)));
						break;
					case DOCENTE_RUOLO:
						utentiRegistrati.add(new Docente(nomi.get(i), cognomi.get(i),
								emails.get(i), logins.get(i), passwords.get(i)));
						break;
					case STUDENTE_RUOLO:
					default:
						Integer anno = anniCorso.get(i);
						utentiRegistrati.add(new Studente(nomi.get(i), cognomi.get(i),
								emails.get(i), logins.get(i), passwords.get(i),
								matricole.get(i), anno != null ? anno : 1));
						break;
				}
			}
		} catch (Exception e) {
			logger.info("Errore nel caricamento degli utenti dal database: " + e.getMessage());
		}
	}

	//Metodi sulle Aule
	public void caricaAuleDaDB() throws Exception {
		AulaPostgresDao aulaDao= new AulaPostgresDao();
		List<Aula> a=new ArrayList<>();
		Object[][] dati = aulaDao.caricaAulaDB();
		for (Object[] aula : dati) {
			String nome = (String) aula[0];
			int capienza = (int) aula[1];
			a.add(new Aula(nome, capienza));
		}
		aule=new ArrayList<>(a);
	}
	/**Ritorna le aule  però solo il nome
	 *@param nomeAula <p>Se nomeAula è {@code ""} il metodo ritorna tutte le aule,
	 *se non è vuota ritorna le aule che iniziano con la stringa dentro nomeAula.
	 *@return Restituisce una lista di tipo {@code String}
	 */
	public List<String> getAule(String nomeAula){
		List<String> data= new ArrayList<>();
		List<Aula> a= new ArrayList<>(aule);

		for(Aula aula:a){
			if(aula.getNome().toLowerCase().startsWith(nomeAula.trim()))
				data.add(aula.getNome());
		}
		return data;
	}
}

