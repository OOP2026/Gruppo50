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


public class  Controller {
	private static final Logger logger = Logger.getLogger(Controller.class.getName());
	private static final String RESPONSABILE_RUOLO= "RESPONSABILE";
	private static final String STUDENTE_RUOLO="STUDENTE";
	private static final String DOCENTE_RUOLO="DOCENTE";
	private Studente studente;
	private Responsabile responsabile;
	private Responsabile responsabileTemp;
	private Docente docente;
	@SuppressWarnings("unused")
	private Utente utente;
	/**E' una lista di tipo {@link Utente} che contiene tutti gli utenti che si sono registrati.*/
	private final List<Utente> utentiRegistrati;
	private OrarioLezioni orarioLezioni = new OrarioLezioni();
	private List<Insegnamento> insegnamentiRegistrati = new ArrayList<>();
	private List<Aula> aule=new ArrayList<>();
	/**
	 * Costruisce il controller dell'applicazione (pattern BCE).
	 * @param utentiRegistrati la lista condivisa di tutti gli {@link Utente utenti} registrati,
	 * che verrà popolata anche dal caricamento dal database all'avvio dell'applicazione.
	 */
	public Controller(List<Utente> utentiRegistrati) {
		this.utentiRegistrati = utentiRegistrati;

	}
	/**Questo metodo permette al programma di funzionare offline senza mandare query al database
	 * permettendo al programma di lavorare solo in memoria*/
	public void offline(){
		ConnessioneDatabase.setStatus(false);
	}
	/**Questo metodo permette al programma di funzionare online mandando query al database
	 * permettendo al programma di lavorare sia tramite database e memoria
	 * però se l'inserimento nel database non va, non funzionera nemmeno quello in memoria*/
	public void online(){
		ConnessioneDatabase.setStatus(true);
	}
	/**
	 * Apre la connessione al database inizializzando il singleton {@link ConnessioneDatabase}.
	 * @throws SQLException se la connessione al database non può essere stabilita.
	 */
	public void apriConnessioneDatabase() throws SQLException {
		ConnessioneDatabase.getInstance();
	}
	/** Azzera i riferimenti precedenti per evitare bug tra un login e l'altro.*/
	public void logout(){
		//Logout
		this.studente=null;
		this.docente=null;
		this.responsabile=null;
		this.responsabileTemp=null;
		this.utente= null;
	}
	/**
	 * Carica dal database utenti, insegnamenti e lezioni.
	 * <p>Da chiamare una sola volta all'avvio dell'applicazione (vedi {@code Home}),
	 * così i dati delle sessioni precedenti sono disponibili sia per il login
	 * sia per i controlli di unicità in fase di registrazione.</p>
	 */
	public void caricaDatiIniziali() {
		if (ConnessioneDatabase.getStatus()) {
			caricaUtentiDaDB();
			caricaInsegnamentiDaDB();
			caricaLezioniDaDB();
		}
	}

	/**
	 * Esegue il login di un utente a partire dalle credenziali inserite nella GUI.
	 * <p>
	 * Se le credenziali corrispondono a un utente registrato, ne identifica il
	 * tipo ({@link Studente}, {@link Docente} o {@link Responsabile}) e lo
	 * imposta come utente loggato. Gli utenti vengono caricati dal database
	 * una sola volta all'avvio tramite {@link #caricaDatiIniziali()}.
	 * </p>
	 * @param username lo username inserito.
	 * @param password la password inserita.
	 * @return {@code true} se il login ha avuto successo, {@code false} altrimenti.
	 */
	public boolean accedi(String username, String password) {

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
	/**
	 * Questo metodo controlla se l'utente è uno studente.
	 * @return  Ritorna un valore booleano.
	 */
	public boolean isStudente(Utente u){
		if (u instanceof Studente) {
			this.studente = (Studente) u;
			return true;
		}
		return false;
	}
	/**
	 * Questo metodo controlla se l'utente è un responsabile.
	 * @return Ritorna un valore booleano.
	 */
	public boolean isResponsabile(Utente u){
		if (u instanceof Responsabile) {
			this.responsabile = (Responsabile) u;

			// Carica le richieste in attesa così il responsabile le vede in GUI
			String erroreRichieste = caricaRichiesteResponsabileDaDB();
			if (erroreRichieste != null){
				String errore= "Errore caricamento richieste: "+ erroreRichieste;
				logger.info(errore);
			}
			return true;
		}
		return false;
	}
	/**
	 * Questo metodo controlla se l'utente è un docente
	 * @return Ritorna un valore booleano.
	 */
	public boolean isDocente(Utente u){
		if (u instanceof Docente) {
			this.docente = (Docente) u;
			putResponsabile();
			// Carica le richieste inviate così il docente le vede in GUI
			String erroreRichieste = caricaRichiesteDocenteDaDB();
			if (erroreRichieste != null) {
				String errore ="Errore caricamento richieste: " + erroreRichieste;
				logger.info(errore);}
			// Carica le materie del docente così le rivede anche nelle sessioni successive
			String erroreMaterie = caricaMaterieDocenteDaDB();
			if (erroreMaterie != null) {
				String errore = "Errore caricamento materie: " + erroreMaterie;
				logger.info(errore);}
			return true;
		}
		return false;
	}
	/**
	 * Restituisce il ruolo dell'utente attualmente loggato.
	 * @return {@code "RESPONSABILE"}, {@code "DOCENTE"} o {@code "STUDENTE"};
	 * {@code null} se nessun utente è loggato.
	 */
	public String getRuolo() {
		if (responsabile != null) return RESPONSABILE_RUOLO;
		if (docente != null) return DOCENTE_RUOLO;
		if (studente != null) return STUDENTE_RUOLO;
		return null;
	}
	/**Imposta il responsabileTemp che serve per mandare le richieste al responsabile*/
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
	/**Controlla se non è null {@code responsabileTemp} se lo è lancia una {@link Exception}
	 * @throws NullPointerException se il responsabile è null lancia un eccezione mostrando un errore senza far crushare il programma.
	 */
	public void checkResponsabileTemp(){
		if(responsabileTemp==null){
			throw new NullPointerException("Non è presente ancora un responsabile");
		}
	}



	//Responsabile rifiuta lo spostamento


	/**
	 * Permette al {@link Responsabile responsabile} di creare una nuova lezione.
	 * <p>
	 * L'insegnamento deve già esistere tra quelli registrati (la lezione non ne
	 * crea uno nuovo) e l'email indicata deve essere quella del docente titolare.
	 * La lezione viene prima inserita in memoria nel Model (che valida orario e
	 * disponibilità del docente) e poi salvata sul database; se il salvataggio
	 * fallisce, la lezione resta solo in memoria e l'errore è segnalato a console.
	 * </p>
	 * @param nomeInsegnamento il nome dell'insegnamento registrato a cui associare la lezione.
	 * @param emailDocente l'email del docente titolare dell'insegnamento.
	 * @param nomeAula il nome dell'aula in cui si terrà la lezione.
	 * @param giorno il giorno della settimana della lezione.
	 * @param orarioIn è un array con dentro [0] l'ora di inizio , [1] il minuto d'inizio, [2] l'ora di fine e [3] il minuto di fine
	 * @return {@code null} se la creazione ha avuto successo, altrimenti una
	 * {@code String} con il messaggio di errore da mostrare in GUI.
	 */
	public String creaLezione(
			String nomeInsegnamento,
			String emailDocente,
			String nomeAula,
			String giorno, int[] orarioIn) {
		Aula aula = stringToAula(nomeAula);
		if (aula == null) {
			return "Errore si sta provando a creare una lezione con un aula non esistente";
		}
		//Verifico che l'insegnamento inserito esista tra quelli registrati (confronto per nome).
		//La lezione NON crea un nuovo insegnamento: riusa quello già registrato.
		Insegnamento insegnamento = stringToInsegnamento(nomeInsegnamento);
		if (insegnamento == null) {
			return "Errore si sta provando a creare una lezione per un insegnamento non esistente";
		}
		if(insegnamento.getDocente()==null){
			return "Questo insegnamento non ha un docente titolare";
		}
		//Cerco se il docente a cui il responsabile assegna la lezione esista davvero
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
		//L'email deve essere quella del docente titolare dell'insegnamento registrato
		if (!insegnamento.getDocente().getmail().equals(emailDocente)) {
			return "Il docente indicato non è il titolare dell'insegnamento.";
		}

		// 1) Inserimento in memoria nel Model (valida orario e disponibilità del docente).
		//    Se questa parte fallisce è un errore vero: si interrompe e si mostra il messaggio.
		try {
			Orario orario = new Orario(giorno, orarioIn[0], orarioIn[1], orarioIn[2], orarioIn[3]);
			Lezione lezione = new Lezione(insegnamento, aula, orario);
			this.docente= docenteTrovato;


			//Gestire meglio questo metodo o permettere al db di confrontare i vincoli con l'orario della lezione
			if(ConnessioneDatabase.getStatus()) {
				caricaVincoliDocenteDaDB();
				if(!responsabile.verificaDisponibilita(this.docente.getVincoli(),lezione.getOrario())){
					throw new SQLException("Non è disponibile il docente");
				}
				LezioneDAO lezioneDAO= new LezionePostgresDao();
				lezioneDAO.salvaLezioneDB(
						insegnamento.getNome(), insegnamento.getAnnoCorso(),
						emailDocente,
						nomeAula, giorno,
						orarioIn);
			}
			responsabile.inserisciLezione(lezione, orarioLezioni);


		}catch (SQLException e) {
			logger.warning(e.getMessage());
			return "Impossibile salvare la lezione nel database";
		}
		catch (IllegalArgumentException e) {
			// Errore di validazione del Model (es. orario non valido, docente non disponibile)
			this.docente=null;
			return e.getMessage();
		}finally {
			this.docente=null;
		}



		return null;
	}
	/**Permette al docente di aggiungere un Insegnamento che può insegnare.
	 * <p>L'associazione viene anche salvata nella tabella {@code docenteinsegnamento}
	 * tramite {@link DocenteDAO#salvaMateriaDocenteDB}, sia in fase di registrazione
	 * sia quando la materia viene aggiunta in un secondo momento, così da renderla
	 * persistente tra le sessioni.</p>
	 * @param materia il nome della materia da aggiungere al docente.
	 * @return {@code null} se tutto ok, altrimenti una {@code String} con l'errore.
	 */
	public String addInsegnamentoDocente(String materia){
		try{
			Insegnamento insegnamento = stringToInsegnamento(materia);
			if(ConnessioneDatabase.getStatus() && insegnamento != null){
				DocenteDAO docenteDAO = new DocentePostgresDao();
				docenteDAO.salvaMateriaDocenteDB(docente.getmail(), insegnamento.getNome());
			}
			docente.addInsegnamento(insegnamento);
		}catch(SQLException e){
			logger.warning(e.getMessage());
			return "Impossibile salvare la materia del docente nel database.";
		}catch(Exception e){
			return e.getMessage();
		}
		return null;
	}
	/**
	 * Questo metodo permette al {@link Docente docente} di rimuovere una materia che insegna,
	 * 	serve solo inserire come parametro il nome dell'{@link Insegnamento insegnamento} da rimuovere.
	 * @param materia la materia che il docente preferisce rimuovere dagli insegnamenti.
	 * @return Restituisce una {@code String} o {@code null}.
	 */
	public String removeInsegnamentoDocente(String materia){
		try{
			Insegnamento insegnamento = stringToInsegnamento(materia);
			if(ConnessioneDatabase.getStatus() && insegnamento != null){
				DocenteDAO docenteDAO = new DocentePostgresDao();
				docenteDAO.rimuoviMateriaDocenteDB(docente.getmail(), insegnamento.getNome());
			}
			docente.removeInsegnamento(insegnamento);
		}catch(SQLException e){
			logger.warning(e.getMessage());
			return "Impossibile rimuovere la materia del docente dal database.";
		}catch(Exception ex){
			return ex.getMessage();
		}
		return null;
	}
	/**Carica dal database le materie associate al docente loggato (tabella
	 * {@code docenteinsegnamento}) e le mette nella lista in memoria del docente,
	 * sostituendo quella precedente per evitare duplicati tra un login e l'altro.
	 * I nomi letti vengono risolti negli {@link Insegnamento insegnamenti}
	 * registrati (caricati all'avvio con {@link #caricaInsegnamentiDaDB()});
	 * eventuali materie non più registrate vengono ignorate.
	 * Viene invocato al login del docente.
	 * @return Restituisce una {@code String} con l'errore o {@code null} se tutto ok.
	 */
	public String caricaMaterieDocenteDaDB() {
		try {
			if(!ConnessioneDatabase.getStatus()){return null;}
			DocenteDAO docenteDAO = new DocentePostgresDao();
			ArrayList<String> nomiMaterie = new ArrayList<>();
			docenteDAO.leggiMaterieDocenteDB(docente.getmail(), nomiMaterie);

			List<Insegnamento> materie = new ArrayList<>();
			for (String nome : nomiMaterie) {
				Insegnamento insegnamento = stringToInsegnamento(nome);
				if (insegnamento != null) {
					materie.add(insegnamento);
				}
			}
			docente.caricaInsegnamentiInDocente(materie);
		} catch (SQLException e) {
			logger.warning(e.getMessage());
			return "Impossibile caricare le materie del docente dal database.";
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}
	/**
	 * Ritorna un  {@link Insegnamento insegnamento} solo se esiste nell'elenco degli insegnamenti attivi.
	 * @param materia è il nome dell'insegnamento.
	 * @return Restituisce un oggetto di tipo {@link Insegnamento}.
	 */
	private Insegnamento stringToInsegnamento(String materia){
		for(Insegnamento insegnamento:insegnamentiRegistrati){
			if(insegnamento.getNome().equalsIgnoreCase(materia)){
				return insegnamento;
			}
		}
		return null;
	}
	/**
	 * Ritorna gli insegnamenti registrati meno quelli del docente però solo il nome
	 * @return Restituisce una lista di tipo {@code String}.
	 */
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
			if(insegnamento.getNome().toLowerCase().startsWith(materia) && insegnamento.getDocente()!=null)
				data.add(insegnamento.getNome());
		}
		return data;
	}
	/**
	 * Ritorna gli insegnamenti del docente.
	 * @return Restituisce un array di tipo {@code Object[][]}.
	 */
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
	/**
	 * Permette al docente di aggiungere un {@link Vincolo} max 3.
	 * Docente indica i il giorno e una fascia oraria in cui non può fare lezione.
	 * @param giorno giorno indicato dal docente in cui non è disponibile.
	 * @param oraInizio ora di inizio del vincolo indicato dal docente in cui non è disponibile.
	 * @param minutoInizio minuto di inizio del vincolo indicato dal docente in cui non è disponibile.
	 * @param oraFine ora di fine del vincolo indicato dal docente in cui non è disponibile.
	 * @param minutoFine minuto di fine del vincolo indicato dal docente in cui non è disponibile.
	 * @return Restituisce una {@code String} o {@code null}.
	 */
	public String aggiungiVincolo(String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {
		try{
			Vincolo v= new Vincolo(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
			if(ConnessioneDatabase.getStatus()){
				VincoloDAO vincoloDAO= new VincoloPostgresDao();
				vincoloDAO.salvaVincoloDB(this.docente.getmail(),giorno,oraInizio,minutoInizio,oraFine,minutoFine);
			}

			docente.aggiungiVincolo(v);
		}catch(SQLException e){
			logger.info(e.getMessage());
			return "Impossibile salvare il vincolo nel DataBase.";
		}
		catch(Exception e){
			return e.getMessage();
		}
		return null;
	}
	/**
	 * Permette di rimuovere un {@link Vincolo vincolo} usando la posizione del vincolo che si vuole rimuovere.
	 * @param ind E' la posizione in cui sta il vincolo nella list vincoli del docente.
	 * @return  Restituisce una {@code String} o {@code null}.
	 */
	public String rimuoviVincolo(int ind) {
		try{

			List<Vincolo> vincoli= new ArrayList<>(docente.getVincoli());
			Vincolo v= vincoli.get(ind);
			if(ConnessioneDatabase.getStatus()){
				VincoloDAO vincoloDAO= new VincoloPostgresDao();
				vincoloDAO.rimuoviVincoloDB(this.docente.getmail(),v.getOrario().getGiorno(),v.getOrario().getOraInizio(),v.getOrario().getMinutoInizio(),v.getOrario().getOraFine(),v.getOrario().getMinutoFine());
			}
			docente.rimuoviVincolo(ind);
		} catch (SQLException e) {
			logger.warning(e.getMessage());
			return "Impossibile rimuovere il vincolo dal DataBase: \n";
		}
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
			if(!ConnessioneDatabase.getStatus()){return null;}
			VincoloDAO vincoloDAO = new VincoloPostgresDao();
			List<Vincolo> vs=new ArrayList<>();
			Object[][] vincoli = vincoloDAO.caricaVincoliDB(docente.getmail());
			for (Object[] vincolo : vincoli) {
				vs.add(new Vincolo((String)vincolo[0],(int)vincolo[1],(int)vincolo[2],(int)vincolo[3],(int)vincolo[4]));
			}
			docente.caricaVincoliInDocente(vs);
		}catch(SQLException e){
			logger.warning(e.getMessage());
			return "Impossibile carica i vincoli del docente dal database";
		}catch(Exception e){
			return e.getMessage();
		}
		return null;
	}
	/**Permette al {@link Docente Docente} di richiedere di spostare la lezione indicando il nuovo e il vecchio orario*/
	public void richiestaspostamentoLezione(String motivo, String giornoVecchio, int [] orarioVecchio, String giornoNuovo,
	                                        int [] orarioNuovo) {


		// Persistenza: la richiesta viene salvata nella tabella richiesta (stato
		// di default 'IN_ATTESA'). Se il salvataggio fallisce la richiesta resta
		// solo in memoria e l'errore viene segnalato a console (stesso pattern di
		// creaLezione). L'id generato dal DB viene memorizzato nella richiesta,
		// così approvazione/rifiuto potranno aggiornare la riga corretta.
		try {
			checkResponsabileTemp();
			if(ConnessioneDatabase.getStatus()) {
				RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
				String[] datiTesto = new String[5];
				datiTesto[RichiestaDAO.EMAIL_DOCENTE] = docente.getmail();
				datiTesto[RichiestaDAO.EMAIL_RESPONSABILE] = responsabileTemp.getmail();
				datiTesto[RichiestaDAO.MOTIVO] = motivo;
				datiTesto[RichiestaDAO.GIORNO_INIZIALE] = giornoVecchio;
				datiTesto[RichiestaDAO.GIORNO_PROPOSTO] = giornoNuovo;
				int idGenerato = richiestaDAO.salvaRichiestaDB(datiTesto, orarioVecchio, orarioNuovo);
				List<Richiesta> inviate = docente.getRichiesteInviate();
				inviate.get(inviate.size() - 1).setId(idGenerato);
			}
			docente.richiestaSpostamentoLezione(orarioLezioni,motivo, new Orario(giornoVecchio, orarioVecchio[0], orarioVecchio[1], orarioVecchio[2], orarioVecchio[3]), new Orario(giornoNuovo, orarioNuovo[0], orarioNuovo[1], orarioNuovo[2], orarioNuovo[3]));
		} catch (SQLException e) {
			logger.info("Richiesta non salvata sul database (resta solo in memoria): " + e.getMessage());
		}
		catch (Exception e) {
			logger.info("Richiesta non salvata in memoria : " + e.getMessage());
		}
	}
	/**Carica dal database le richieste inviate dal docente loggato (di qualsiasi
	 * stato: in attesa, approvate e rifiutate) e le mette nella lista in memoria
	 * del docente, così la GUI ({@code SchermataRichiesteInviate}) le vede tramite
	 * {@link #ottieniRichiesteInviate()} anche nelle sessioni successive.
	 * Viene invocato al login del docente.
	 * @return Restituisce una {@code String} con l'errore o {@code null} se tutto ok.
	 */
	public String caricaRichiesteDocenteDaDB() {
		try {
			if(!ConnessioneDatabase.getStatus()){return null;}
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			ArrayList<Integer> id = new ArrayList<>();
			ArrayList<String[]> datiTesto = new ArrayList<>();
			ArrayList<int[]> orarioIniziale = new ArrayList<>();
			ArrayList<int[]> orarioProposto = new ArrayList<>();
			richiestaDAO.leggiRichiesteDocenteDB(docente.getmail(),
					id, datiTesto, orarioIniziale, orarioProposto);

			List<Richiesta> richieste = new ArrayList<>();
			for (int i = 0; i < id.size(); i++) {
				String[] t = datiTesto.get(i);
				int[] oi = orarioIniziale.get(i);
				int[] op = orarioProposto.get(i);
				Richiesta r = new Richiesta(docente, t[RichiestaDAO.TESTO_MOTIVO],
						new Orario(t[RichiestaDAO.TESTO_GIORNO_INIZIALE], oi[0], oi[1], oi[2], oi[3]),
						new Orario(t[RichiestaDAO.TESTO_GIORNO_PROPOSTO], op[0], op[1], op[2], op[3]));
				r.setId(id.get(i));
				r.caricaStatoDaDB(t[RichiestaDAO.TESTO_STATO]);
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
	 * @return Restituisce una {@code String} con l'errore o {@code null} se è tutto ok.
	 */
	public String caricaRichiesteResponsabileDaDB() {
		try {
			if(!ConnessioneDatabase.getStatus()){return null;}
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			ArrayList<Integer> id = new ArrayList<>();
			ArrayList<String[]> datiTesto = new ArrayList<>();
			ArrayList<int[]> orarioIniziale = new ArrayList<>();
			ArrayList<int[]> orarioProposto = new ArrayList<>();
			richiestaDAO.leggiRichiesteInAttesaDB(
					id, datiTesto, orarioIniziale, orarioProposto);

			List<Richiesta> richieste = new ArrayList<>();
			for (int i = 0; i < id.size(); i++) {
				String[] t = datiTesto.get(i);
				int[] oi = orarioIniziale.get(i);
				int[] op = orarioProposto.get(i);
				Docente docenteRichiedente = trovaDocentePerEmail(t[RichiestaDAO.EMAIL_DOCENTE]);
				Richiesta r = new Richiesta(docenteRichiedente, t[RichiestaDAO.MOTIVO],
						new Orario(t[RichiestaDAO.GIORNO_INIZIALE], oi[0], oi[1], oi[2], oi[3]),
						new Orario(t[RichiestaDAO.GIORNO_PROPOSTO], op[0], op[1], op[2], op[3]));
				r.setId(id.get(i));
				// lo stato non viene letto: le richieste estratte sono tutte IN_ATTESA (default)
				richieste.add(r);
			}
			Responsabile.caricaRichiesteSpostamento(richieste);
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}


	/**Aggiorna sul database lo stato corrente (letto dal Model) della richiesta
	 * indicata. Best-effort: se la richiesta non era mai stata salvata sul DB
	 * ({@code id < 0}) o l'aggiornamento fallisce, lo stato resta aggiornato solo
	 * in memoria e l'errore viene segnalato a console.
	 * @param numeroRichiesta posizione della richiesta nella lista del responsabile.
	 */
	private void aggiornaStatoRichiestaSuDB(int numeroRichiesta) {
		try {
			if(!ConnessioneDatabase.getStatus()) {return;}
			Richiesta r = responsabile.getRichiesteSpostamento().get(numeroRichiesta);
			if (r.getId() < 0) return; // richiesta mai salvata sul database
			RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
			richiestaDAO.aggiornaStatoRichiestaDB(r.getId(), responsabile.getStatoRichiesta(numeroRichiesta));
		} catch (Exception e) {
			logger.info("Stato richiesta NON aggiornato sul database: " + e.getMessage());
		}
	}
	/**
	 *  Restituisce un array che contiene le richieste inviate dal docente.
	 * @return Restituisce un array di tipo {@code Object[][]}.
	 */
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
	/**
	 * Il metodo ritorna le lezioni del docente in ordine, prima il giorno e poi l'orario.
	 * @return  Ritorna una array di tipo {@code Object[][]}.
	 */
	public Object[][] getLezioniDocente() {
		List<Lezione> l = docente.getLezioni(orarioLezioni);

		if (l.isEmpty()) {
			return new Object[0][0];
		}

		String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
		List<List<Lezione>> lezioniPerGiorno = new ArrayList<>();

		// Raggruppa le lezioni per giorno
		for (String giorno : giorni) {
			lezioniPerGiorno.add(l.stream()
					.filter(lezione -> lezione.getOrario().getGiorno().equalsIgnoreCase(giorno))
					.collect(Collectors.toList()));
		}

		// Trova il numero massimo di lezioni in un singolo giorno (sarà il numero di righe)
		int maxRighe = lezioniPerGiorno.stream()
				.mapToInt(List::size)
				.max()
				.orElse(0);

		// Inizializza direttamente la matrice finale
		Object[][] data = new Object[maxRighe][giorni.length];

		// Popola la matrice
		for (int riga = 0; riga < maxRighe; riga++) {
			for (int col = 0; col < giorni.length; col++) {
				List<Lezione> lezioniDelGiorno = lezioniPerGiorno.get(col);

				// Se in questo giorno c'è una lezione per la riga corrente, inseriscila, altrimenti metti ""
				if (riga < lezioniDelGiorno.size()) {
					data[riga][col] = lezioniDelGiorno.get(riga).infoLezioneSenzaDocente();
				} else {
					data[riga][col] = "";
				}
			}
		}

		return data;
	}

	/**
	 * Metodo che permette all'utente di registrarsi come Studente o Docente o Responsabile.
	 * @param name Nome di battesimo del utente registrato.
	 * @param cogn Cognome di battesimo del utente registrato
	 * @param email l'email utilizzata in fase di registrazione.
	 * @param login l'username con cui l'Utente accede.
	 * @param pass la password con cui l'Utente accede.
	 * @param ruolo indica che ruolo svolgi all'interno dell'università.
	 * @param matricola la matricola inserita dallo studente in fase di registrazione.
	 * @param annoCorso l'anno di corso (1-3) scelto dallo studente in fase di registrazione.
	 * @return nel caso in cui l'inserimento nel DB fallisce e restituisce FALSE altrimenti torna TRUE.
	 */
	public boolean registra(String name, String cogn, String email, String login, String pass, String ruolo, String matricola, int annoCorso) {
		for (Utente u : utentiRegistrati) {
			if (u.getmail().equals(email) || u.getUsername().equals(login)) {
				return false; // Non possono esistere più user con la stessa mail o username.
			}
		}

		switch (ruolo.toUpperCase()) {
			case RESPONSABILE_RUOLO:
				creaResponsabile(name,cogn,email,login,pass);
				break;

			case DOCENTE_RUOLO:
				creaDocente(name,cogn,email,login,pass);
				break;

			case STUDENTE_RUOLO:
			default:
				creaStudente(name, cogn, email, login, pass, matricola, annoCorso);
				break;
		}

		return true;
	}

	/**
	 * Crea un nuovo {@link Responsabile} nell'applicazione e lo aggiunge alla lista in memoria.
	 * <p>
	 * Il metodo prova prima a salvare il responsabile sul database chiamando
	 * {@link #salvaResponsabileDB(String,String,String,String,String)}; indipendentemente
	 * dall'esito del salvataggio (se la connessione è disabilitata non viene lanciata
	 * un'eccezione), viene poi creata l'istanza in memoria e aggiunta a
	 * {@code utentiRegistrati}. Eventuali errori vengono loggati.
	 * </p>
	 * @param name  nome del responsabile
	 * @param cogn  cognome del responsabile
	 * @param email email del responsabile (usata come identificativo)
	 * @param login username per l'accesso
	 * @param pass  password per l'accesso
	 */
	public void creaResponsabile(String name,String cogn,String email,String login,String pass){

		try {
			salvaResponsabileDB(name, cogn, email, login, pass);
			Responsabile nuovoUtente = new Responsabile(name, cogn, email, login, pass);
			utentiRegistrati.add(nuovoUtente);
		}catch (SQLException e){
			logger.warning("Registrazione del responsabile Impossibile a causa del DB: "+e.getMessage());
		}catch (Exception e){
			logger.warning(e.getMessage());
		}


	}
	/**
	 * Salva un responsabile nel database.
	 * <p>
	 * Se la connessione al database è disabilitata ({@link ConnessioneDatabase#getStatus()} == false)
	 * il metodo ritorna senza effettuare alcuna operazione. In caso di errore SQL
	 * viene rilanciata l'eccezione al chiamante.
	 * </p>
	 * @param name  nome del responsabile
	 * @param cogn  cognome del responsabile
	 * @param email email del responsabile
	 * @param login username del responsabile
	 * @param pass  password del responsabile
	 * @throws SQLException se l'operazione di salvataggio sul database fallisce
	 */
	public void salvaResponsabileDB(String name,String cogn,String email,String login,String pass) throws  SQLException{

		if(!ConnessioneDatabase.getStatus()){return;}
		ResponsabileDAO responsabileDAO = new ResponsabilePostgresDao();
		responsabileDAO.salvaResponsabileDB(name, cogn, email, login, pass);
	}
	/**
	 * Crea un nuovo {@link Docente} nell'applicazione.
	 * <p>
	 * Il metodo prova a salvare il docente nel database tramite
	 * {@link #salvaDocenteDB(String,String,String,String,String)}. Se il salvataggio
	 * riesce (o la connessione è disabilitata) viene creata l'istanza in memoria,
	 * assegnata a {@code this.docente} e aggiunta a {@code utentiRegistrati}, in
	 * modo che la GUI possa subito associare materie al docente appena creato.
	 * Eventuali errori vengono loggati.
	 * </p>
	 * @param name  nome del docente
	 * @param cogn  cognome del docente
	 * @param email email del docente
	 * @param login username per l'accesso
	 * @param pass  password per l'accesso
	 */
	public void creaDocente(String name,String cogn,String email,String login,String pass){
		try {
			salvaDocenteDB(name, cogn, email, login, pass);
			Docente nuovoUtente = new Docente(name, cogn, email, login, pass);
			// Permette alla GUI di aggiungere subito le materie del docente appena registrato
			// (stesso pattern usato per lo studente); la GUI chiama logout() a fine dialog.
			this.docente=nuovoUtente;
			utentiRegistrati.add(nuovoUtente);
		}catch (SQLException e){
			logger.warning("Registrazione del docente impossibile  a causa del DB: "+e.getMessage());
		}catch (Exception e){
			logger.warning(e.getMessage());
		}


	}
	/**
	 * Salva un {@link Docente} nel database.
	 * <p>
	 * Se la connessione al database è disabilitata il metodo non esegue nulla.
	 * In caso di errore SQL l'eccezione viene propagata al chiamante.
	 * </p>
	 * @param name  nome del docente
	 * @param cogn  cognome del docente
	 * @param email email del docente
	 * @param login username del docente
	 * @param pass  password del docente
	 * @throws SQLException se l'operazione di salvataggio sul database fallisce
	 */
	public void salvaDocenteDB(String name,String cogn,String email,String login,String pass) throws  SQLException{

		if(!ConnessioneDatabase.getStatus()){return;}
		DocenteDAO docenteDAO = new DocentePostgresDao();
		docenteDAO.salvaDocDB(name, cogn, email, login, pass);
	}

	/**
	 * Crea un nuovo {@link Studente} nell'applicazione.
	 * <p>
	 * Prova a salvare lo studente sul database tramite
	 * {@link #salvaStudenteDB(String,String,String,String,String,String,int)}; in ogni
	 * caso crea l'istanza in memoria (con l'anno di corso indicato) e la aggiunge
	 * a {@code utentiRegistrati}. Eventuali errori vengono loggati.
	 * </p>
	 * @param name      nome dello studente
	 * @param cogn      cognome dello studente
	 * @param email     email dello studente
	 * @param login     username per l'accesso
	 * @param pass      password per l'accesso
	 * @param matricola matricola dello studente
	 * @param annoCorso anno di corso dello studente (1-3)
	 */
	public void creaStudente(String name,String cogn,String email,String login,String pass,String matricola,int annoCorso){
		try {
			salvaStudenteDB(name, cogn, email, login, pass,matricola,annoCorso);
			Studente nuovoUtente = new Studente(name, cogn, email, login, pass, matricola, annoCorso);
			this.studente=nuovoUtente;
			utentiRegistrati.add(nuovoUtente);
		}catch (SQLException e){
			logger.info("Registrazione dello studente impossibile a causa del DB: "+e.getMessage());
		}catch (Exception e){
			logger.warning(e.getMessage());
		}


	}
	/**
	 * Salva uno {@link Studente} nel database.
	 * <p>
	 * Se la connessione al database è disabilitata il metodo ritorna senza effettuare
	 * operazioni. La matricola e l'anno di corso vengono passati direttamente
	 * al DAO. In caso di errore SQL l'eccezione viene propagata.
	 * </p>
	 * @param name      nome dello studente
	 * @param cogn      cognome dello studente
	 * @param email     email dello studente
	 * @param login     username per l'accesso
	 * @param pass      password per l'accesso
	 * @param matricola matricola dello studente
	 * @param annoCorso anno di corso dello studente (1-3)
	 * @throws SQLException se l'operazione di salvataggio sul database fallisce
	 */
	public void salvaStudenteDB(String name,String cogn,String email,String login,String pass,String matricola,int annoCorso) throws  SQLException{

		if(!ConnessioneDatabase.getStatus()){return;}
		StudenteDAO studenteDAO = new StudentePostgresDao();
		// Passa direttamente la matricola e l'anno di corso ricevuti come parametri al DB
		studenteDAO.salvaStudenteDB(name, cogn, email, login, pass, matricola, annoCorso);
	}
	/**
	 * Verifica se una matricola risulta già registrata nel sistema.
	 * <p>Il controllo avviene sulla lista degli utenti in memoria, che viene
	 * caricata dal database una sola volta all'avvio tramite
	 * {@link #caricaDatiIniziali()}: copre quindi sia gli studenti già presenti
	 * nel DB sia quelli registrati durante la sessione corrente.</p>
	 * @param matricola la matricola da controllare.
	 * @return {@code true} se esiste già uno studente con questa matricola, {@code false} altrimenti.
	 */
	public boolean matricolaEsiste(String matricola) {
		for (Utente u : utentiRegistrati) {
			if (u instanceof Studente && ((Studente) u).getmatricola().equalsIgnoreCase(matricola)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Restituisce la matricola dello {@link Studente studente} attualmente loggato.
	 * @return la matricola dello studente; {@code ""} se nessuno studente è loggato.
	 */
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
			// Confronto case-insensitive: il giorno salvato sul DB potrebbe
			// differire per maiuscole/minuscole dalla chiave della mappa.
			for (String g : giorni) {
				if (g.equalsIgnoreCase(giorno)) {
					mappa.get(g).add(testo);
					break;
				}
			}
		}
		return mappa;
	}
	/**
	 * Permette al {@link Responsabile responsabile} di registrare un nuovo insegnamento.
	 * <p>
	 * se l'insegnamento non è già presente viene aggiunto alla lista in memoria
	 * e salvato sul database tramite {@link InsegnamentoDAO}.
	 * </p>
	 * @param nome il nome dell'insegnamento.
	 * @param cfu il numero di crediti formativi.
	 * @param annoCorso l'anno di corso in cui viene insegnato.
	 * @return {@code null} se la registrazione ha avuto successo, altrimenti una
	 * {@code String} con il messaggio di errore da mostrare in GUI.
	 */
	public String registraInsegnamento(String nome, int cfu, int annoCorso) {


		InsegnamentoDAO insegnamentoDAO= null;
		try {
			Insegnamento candidato = new Insegnamento(nome, cfu, annoCorso);

			if (insegnamentiRegistrati.contains(candidato)) {
				return "Insegnamento già presente.";
			}

			if(ConnessioneDatabase.getStatus()) {
				insegnamentoDAO = new InsegnamentoPostgresDAO();
				insegnamentoDAO.salvaInsegnamento(nome, annoCorso, cfu);
			}
			insegnamentiRegistrati.add(candidato);
		} catch (SQLException e) {
			logger.warning(e.getMessage());
			return "Impossibile salvare l'insegnamento nel database";
		}catch(Exception e){
			return e.getMessage();
		}

		return null;
	}

	/**
	 * Restituisce la lista degli insegnamenti attivi formattata per la JTable.
	 * Ogni Object[] contiene: [Nome, CFU, AnnoCorso, email docente]
	 */
	public List<Object[]> getInsegnamentiAttivi() {
		List<Object[]> righe = new ArrayList<>();
		for (Insegnamento ins : insegnamentiRegistrati) {
			String d="nessuno";
			if(ins.getDocente() != null){d=ins.getDocente().getmail(); }

			righe.add(new Object[]{
					ins.getNome(),
					ins.getNumeroCFU(),
					ins.getAnnoCorso(),
					d
			});
		}
		return righe;
	}
	/**
	 * Metodo che utilizza il get richieste di responsabile
	 * Viene utilizzato dalla gui nella dialog visualizzaRichiesta, per ottenere le richieste in ATTESA per quel responsabile
	 * @return richieste in ATTESA per quel responsabile.
	 */
	public Object[][] getRichiesteSpostamento() {
		List<model.Richiesta> lista = responsabile.getRichiesteSpostamento();
		Object[][] data = new Object[lista.size()][6];
		for (int i = 0; i < lista.size(); i++) {
			model.Richiesta r = lista.get(i);
			data[i][0] = i;
			data[i][1] = r.getDocenteRichiedente().getNome() + " " + r.getDocenteRichiedente().getCognome();
			data[i][2] = r.getOrarioLezioneDaSpostare().getGiorno() + " "
					+ r.getOrarioLezioneDaSpostare().getOrarioCompleto();
			data[i][3] = r.getNuovoOrarioLezione().getGiorno() + " "
					+ r.getNuovoOrarioLezione().getOrarioCompleto();
			data[i][4] = r.getMotivoRichiesta();
			data[i][5] = responsabile.getStatoRichiesta(i);
		}
		return data;
	}
	/**
	 * Metodo usato nella gui dalla dialog Visualizza Richiesta.
	 *  Approva la richiesta dato il numero di richiesta in input.
	 * @param numeroRichiesta il numero di richiesta associato.
	 * @return restituisce null se va tutto bene altrimenti mostra un messaggio di errore.
	 */
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
		if(ConnessioneDatabase.getStatus()){
			aggiornaStatoRichiestaSuDB(numeroRichiesta);
		}
		// spostamentoLezione imposta RIFIUTATA automaticamente in caso di conflitto
		if (!responsabile.getStatoRichiesta(numeroRichiesta).equals(StatoRichiesta.APPROVATA.name())) {
			return "Impossibile spostare la lezione: conflitto di orario. "
					+ "La richiesta è stata rifiutata automaticamente.";
		}


		return null; // successo
	}
	/**
	 * Metodo usato nella gui dalla dialog Visualizza Richiesta.
	 * Rifiuta la richiesta dato il numero di richiesta in input.
	 * @param numeroRichiesta il numero di richiesta associato.
	 */
	public void rifiutaRichiestaSpostamento(int numeroRichiesta) {
		if (!responsabile.isRichiestaInAttesa(numeroRichiesta)) {
			return; // già processata o indice non valido
		}

		responsabile.spostamentoLezione(numeroRichiesta, orarioLezioni, false);   // <-- false
		// Persiste lo stato RIFIUTATA sul database
		if(ConnessioneDatabase.getStatus()){
			aggiornaStatoRichiestaSuDB(numeroRichiesta);
		}
	}
	/**
	 * Permette al {@link Responsabile responsabile} di modificare l'orario proposto
	 * in una richiesta di spostamento ancora in attesa.
	 * <p>
	 * Il nuovo orario viene validato dal Model e poi aggiornato anche sul database
	 * (best-effort: se il salvataggio fallisce la modifica resta solo in memoria).
	 * Se la richiesta è già stata approvata o rifiutata l'orario non è più modificabile.
	 * </p>
	 * @param numeroRichiesta l'indice della richiesta da modificare.
	 * @param giorno il nuovo giorno proposto.
	 * @param oraInizio la nuova ora di inizio.
	 * @param minutoInizio il nuovo minuto di inizio.
	 * @param oraFine la nuova ora di fine.
	 * @param minutoFine il nuovo minuto di fine.
	 * @return {@code null} se la modifica ha avuto successo, altrimenti una
	 * {@code String} con il messaggio di errore da mostrare in GUI.
	 */
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
			if(ConnessioneDatabase.getStatus()){
				Richiesta r = responsabile.getRichiesteSpostamento().get(numeroRichiesta);
				if (r.getId() >= 0) {
					RichiestaDAO richiestaDAO = new RichiestaPostgresDao();
					richiestaDAO.aggiornaOrarioPropostoDB(r.getId(), giorno, oraInizio, minutoInizio, oraFine, minutoFine);
				}
			}

			responsabile.cambiaOrarioRichiesta(numeroRichiesta, nuovoOrario);
			// Persiste il nuovo orario proposto sul database (best-effort)
		}
		catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		catch (SQLException e) {
			logger.warning(e.getMessage());
			logger.info("Orario proposto Impossibile aggiornarlo sul database");
		}
		return null;
	}

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
			if(!ConnessioneDatabase.getStatus()){return;}
			LezioneDAO lezioneDAO = new LezionePostgresDao();
			ArrayList<String> nomiInsegnamento = new ArrayList<>();
			ArrayList<Integer> anniCorso = new ArrayList<>();
			ArrayList<String> emailDocente = new ArrayList<>();
			ArrayList<String> nomeAula = new ArrayList<>();
			ArrayList<String> giorno = new ArrayList<>();
			ArrayList<int[]> orarioLetto = new ArrayList<>();
			lezioneDAO.leggiTutteLezioniDB(nomiInsegnamento, anniCorso, emailDocente,
					nomeAula, giorno, orarioLetto);

			OrarioLezioni orarioCaricato = new OrarioLezioni();
			for (int i = 0; i < nomiInsegnamento.size(); i++) {
				Docente docenteTitolare = trovaDocentePerEmail(emailDocente.get(i));
				Insegnamento insegnamento = trovaInsegnamentoRegistrato(
						nomiInsegnamento.get(i), anniCorso.get(i));
				if (insegnamento == null) {
					insegnamento = new Insegnamento(
							nomiInsegnamento.get(i), 1, anniCorso.get(i), docenteTitolare);
				}
				Aula aula = new Aula(nomeAula.get(i), 200);
				int[] o = orarioLetto.get(i);
				Orario orario = new Orario(giorno.get(i),
						o[0], o[1], o[2], o[3]);
				orarioCaricato.caricaLezioneDaDB(new Lezione(insegnamento, aula, orario));
			}
			this.orarioLezioni = orarioCaricato;
		} catch (Exception e) {
			logger.info("Errore nel caricamento delle lezioni dal database: " + e.getMessage());
		}
	}
	/**
	 * Carica dal database tutti gli insegnamenti salvati e li aggiunge alla
	 * lista in memoria {@code insegnamentiRegistrati}, così che siano visibili
	 * nella schermata del responsabile (dialog "Insegnamenti Attivi") anche
	 * nelle sessioni successive a quella in cui sono stati creati.
	 * <p>
	 * Viene invocato al login, dopo il caricamento degli utenti: per ogni
	 * insegnamento letto si ricerca il docente titolare tra gli utenti
	 * registrati tramite {@link #trovaDocentePerEmail(String)}. Gli
	 * insegnamenti già presenti in memoria non vengono duplicati. Eventuali
	 * errori del database vengono segnalati a console senza interrompere il
	 * login.
	 */
	public String caricaInsegnamentiDaDB() {
		try {
			if(!ConnessioneDatabase.getStatus()){return null;}
			InsegnamentoDAO insegnamentoDAO = new InsegnamentoPostgresDAO();
			List<Insegnamento> i= new ArrayList<>();
			for (Object[] riga : insegnamentoDAO.caricaInsegnamentiDB()) {
				i.add(new Insegnamento((String) riga[0],(int)riga[1],(int)riga[2],trovaDocentePerEmail((String) riga[3])));
			}
			insegnamentiRegistrati= new ArrayList<>(i);

		} catch (Exception e) {
			logger.warning(e.getMessage());
			return "Impossibile caricare gli insegnamenti attivi dal database.";
		}
		return null;
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
		return null;
	}
	/**
	 * Cerca tra gli insegnamenti registrati quello con nome e anno di corso
	 * indicati, così da riusare i CFU reali letti dal database.
	 *
	 * @param nome nome dell'insegnamento
	 * @param annoCorso anno di corso dell'insegnamento
	 * @return l'{@link Insegnamento} corrispondente, o {@code null} se assente
	 */
	private Insegnamento trovaInsegnamentoRegistrato(String nome, int annoCorso) {
		for (Insegnamento ins : insegnamentiRegistrati) {
			if (ins.getNome().equals(nome) && ins.getAnnoCorso() == annoCorso) {
				return ins;
			}
		}
		return null;
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
	private void caricaUtentiDaDB() {
		try {
			if(!ConnessioneDatabase.getStatus()){return;}
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
			logger.warning("Errore nel caricamento degli utenti dal database:\n " + e.getMessage());
		}
	}

	//Metodi sulle Aule

	/**
	 * Carica dal database tutte le {@link Aula aule} tramite {@link AulaDAO}
	 * e ricostruisce da zero la lista in memoria {@code aule}, usata dalla GUI
	 * (es. dialog di creazione lezione) per popolare l'elenco delle aule disponibili.
	 */
	public String caricaAuleDaDB(){
		try {
			if(!ConnessioneDatabase.getStatus()){return null;}
			AulaPostgresDao aulaDao= new AulaPostgresDao();
			List<Aula> a=new ArrayList<>();
			Object[][] dati = aulaDao.caricaAulaDB();
			for (Object[] aula : dati) {
				String nome = (String) aula[0];
				int capienza = (int) aula[1];
				a.add(new Aula(nome, capienza));
			}
			aule = new ArrayList<>(a);
		}catch (Exception e){
			logger.warning(e.getMessage());
			return "Impossibile caricare le Aule dal database:";
		}
		return null;
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
	public Object[][] getAuleData(){
		if(aule.isEmpty()) return new Object[0][0];
		Object[][] data= new Object[aule.size()][2];
		for(int i=0; i<aule.size(); i++){
			data[i][0]=aule.get(i).getNome();
			data[i][1]=aule.get(i).getCapienza();
		}

		return data;
	}
	/**
	 * Registra una nuova aula all'interno del sistema e la salva nel database.
	 * <p>
	 * Questa operazione è riservata esclusivamente ai Responsabili.
	 * Il metodo gestisce sia l'aggiornamento della memoria locale sia la persistenza tramite DAO.
	 * </p>
	 * @param nome l'identificativo dell'aula (es. "A1")
	 * @param capienza il numero massimo di posti disponibili
	 * @return {@code null} se l'operazione va a buon fine, altrimenti una stringa contenente il messaggio di errore
	 */
	public String inserisciAula(String nome, int capienza){
		try{
			Aula aula= new Aula(nome,capienza);
			if(aule.contains(aula)) throw new IllegalArgumentException("Esiste gia un aula di nome: "+nome);

			if(ConnessioneDatabase.getStatus()){
				AulaPostgresDao aulaDAO= new AulaPostgresDao();
				aulaDAO.salvaAulaDB(nome,capienza);
			}

			aule.add(aula);

		}
		catch(SQLException e){
			logger.warning(e.getMessage());
			return "Impossibile salvare salvare l'aula nel DataBase. ";
		}
		catch(Exception e){
			return e.getMessage();
		}
		return null;
	}
	/**
	 * Elimina un'aula dal sistema e dal database.
	 * <p>
	 * Essendo un'operazione distruttiva, è accessibile solo al Responsabile.
	 * La rimozione dell'aula comporta automaticamente l'eliminazione di
	 * tutte le lezioni attualmente programmate al suo interno.
	 * </p>
	 * @param nomeAula il nome dell'aula da eliminare
	 * @return {@code null} se l'operazione va a buon fine, altrimenti una stringa contenente il messaggio di errore
	 */
	public String rimuoviAula(String nomeAula){
		try{
			Aula aulaTemp= stringToAula(nomeAula);
			if(aulaTemp==null) throw new NullPointerException("Non esiste quest'aula che vuoi rimuovere: "+nomeAula);
			if(ConnessioneDatabase.getStatus()) {
				AulaPostgresDao aulaDAO = new AulaPostgresDao();
				aulaDAO.rimuoviAulaDB(nomeAula);
			}
			aule.remove(aulaTemp);
			removeLezioneByAula(aulaTemp);


		} catch (SQLException e) {
			logger.warning(e.getMessage());
			return "Impossibile rimuove l'aula dal database.";
		}
		catch (Exception e){
			return e.getMessage();
		}
		return null;

	}
	/**
	 * Metodo di supporto per cercare un'istanza di Aula partendo dal suo nome.
	 * @param nomeAula il nome dell'aula da cercare
	 * @return l'oggetto {@link Aula} corrispondente se trovato, {@code null} altrimenti
	 */
	public Aula stringToAula(String nomeAula){
		for(Aula aula:aule){
			if(aula.getNome().equals(nomeAula)){
				return aula;
			}
		}
		return null;
	}
	/**
	 * Rimuove dall'orario generale tutte le lezioni associate a un'aula specifica.
	 * <p>
	 * L'operazione viene eseguita utilizzando i permessi del Responsabile.
	 * Il metodo utilizza una lista temporanea per evitare eccezioni di modifica
	 * concorrente durante l'iterazione.
	 * </p>
	 * @param aula l'aula di cui si vogliono cancellare le lezioni
	 */
	private void removeLezioneByAula(Aula aula){
		for(Lezione l:orarioLezioni.getOrarioLezioni()){
			if(l.getAula().equals(aula)){
				responsabile.rimuoviLezione(l,orarioLezioni);
			}
		}
	}
	private void removeLezioneByInsegnamento(Insegnamento ins){
		for(Lezione l:orarioLezioni.getOrarioLezioni()) {
			if (l.getInsegnamento().equals(ins)) {
				responsabile.rimuoviLezione(l, orarioLezioni);
			}
		}
	}

	public String removeInsegnamento(String nome){
		try{
			Insegnamento ins= stringToInsegnamento(nome);
			if(ConnessioneDatabase.getStatus()) {
				InsegnamentoPostgresDAO insDAO= new InsegnamentoPostgresDAO();
				insDAO.rimuoviInsegnamentoDB(nome);
			}

			insegnamentiRegistrati.remove(ins);
			removeLezioneByInsegnamento(ins);

		}
		catch(SQLException e){
			logger.warning(e.getMessage());
			return "Impossibile rimuovere l'insegnamento dal database.";

		}
		catch(Exception e){
			return e.getMessage();
		}
		return null;
	}
	//Viene usato per quando responsabile crea lezione e deve controllare i vincoli
	private void caricaVincoliDocenteDaDB() throws SQLException {
		if(!ConnessioneDatabase.getStatus()) {return;}

		VincoloDAO vincoloDAO = new VincoloPostgresDao();
		List<Vincolo> vs=new ArrayList<>();
		Object[][] vincoli = vincoloDAO.caricaVincoliDB(docente.getmail());
		for (Object[] vincolo : vincoli) {
			vs.add(new Vincolo((String)vincolo[0],(int)vincolo[1],(int)vincolo[2],(int)vincolo[3],(int)vincolo[4]));
		}
		docente.caricaVincoliInDocente(vs);
	}
	public String assegnaDocente(String ins,String email){
		Docente d=null;
		for (Utente u : utentiRegistrati) {
			if (u.getmail().equals(email) && (u instanceof Docente)) {
				d= (Docente)u;
				logger.info(d.getmail());
			}
		}
		if(d==null) {return "Non esiste un docente con questa email";}

		Insegnamento insegnamento = stringToInsegnamento(ins);
		if (insegnamento == null) {
			return "Errore si sta provando ad assegnare ad un insegnamento non esistente";
		}
		try{
			//Controlla se il docente puo insegnare questa materia
		Insegnamento insTemp= new Insegnamento(insegnamento);
    insTemp.setDocente(d);
			if(ConnessioneDatabase.getStatus()) {
				InsegnamentoPostgresDAO insDAO = new InsegnamentoPostgresDAO();
				insDAO.assegnaDocenteTitolare(email,ins);
			}
			insegnamento.setDocente(d);
		}catch(SQLException e){
			logger.warning(e.getMessage());
			return"Impossibile assegnare un docente a questa materia";
		}catch(Exception e){
			e.printStackTrace();
			logger.warning(e.getMessage());
			return e.getMessage();
		}
		return null;
	}

	public String modificaDocenteTitolare(String emailDocente,String nomeIns){
		try {
			Docente d=null;
			for (Utente u : utentiRegistrati) {
				if (u.getmail().equals(emailDocente) && (u instanceof Docente)) {
					d= (Docente)u;
				}
			}
			if(d==null) {return "Non esiste un docente con questa email";}
			Insegnamento ins= stringToInsegnamento(nomeIns);
			if(ins==null) throw new NullPointerException("Impossibile modificare il docente, l'insegnamento non esiste");
			int cfu= ins.getNumeroCFU();
			int annoCorso= ins.getAnnoCorso();
			//Controlla se il docente puo insegnare questa materia
			Insegnamento insTemp= new Insegnamento(ins);
			insTemp.setDocente(d);
            if(ConnessioneDatabase.getStatus()){
			InsegnamentoPostgresDAO insDAO= new InsegnamentoPostgresDAO();
			insDAO.rimuoviInsegnamentoDB(nomeIns);
			insDAO.salvaInsegnamento(nomeIns, annoCorso,cfu);
			insDAO.assegnaDocenteTitolare(emailDocente,nomeIns);
			}

			removeLezioneByInsegnamento(ins);
			ins.setDocente(d);


        } catch (SQLException e) {
			logger.warning(e.getMessage());
            return "Impossibile modificare il docente titolare a causa del DB";
        }catch (Exception e) {
			logger.warning(e.getMessage());
			return e.getMessage();
		}

		return null;
	}

}