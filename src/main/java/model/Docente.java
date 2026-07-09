package model;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 * La classe Docente rappresenta un professore universitario.
 * <p>
 * Il docente viene salvato come un Utente nel sistema ed egli può
 * visualizzare le proprie lezioni, richiedere di spostarle a un responsabile inoltre
 * può indicare anche dei vincoli(max 3) dove non è reperibile.
 * </p>
 *
 * @see Utente
 * @see Insegnamento
 * @see Vincolo
 * @see Richiesta
 * @see Lezione
 */
public class Docente extends Utente {
    /**L'elenco degli insegnamenti abilitati al docente.*/
    List<Insegnamento> insegnamenti;
    /**Registro delle richieste IN_ATTESA inoltrate dal docente.*/
    private ArrayList<Richiesta> richiesteSpostamentoInviate;
    /** Elenco dei vincoli indicati dal docente indicati come fascia oraria. */
    private ArrayList<Vincolo> vincoli;
    private static final Logger logger = Logger.getLogger(Docente.class.getName());


    /**
     * Crea una nuova lezione associando insegnamento, aula e orario.
     *
     * @param nome primo nome di battesimo del docente.
     * @param cognome secondo nome di battesimo del docente.
     * @param email   email a uso universitario del docente tipicamente(nome.cognome@dominio.estensione).
     * @param login   username utilizzato dal docente per accedere.
     * @param password password utilizzata dal docente per accedere
     */
    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome,cognome,email,login,password);
        richiesteSpostamentoInviate= new ArrayList<>();
        vincoli= new ArrayList<>();
        insegnamenti=new ArrayList<>();
    }

    /**
     * Crea un nuovo Docente copiando i dati di un altro oggetto Docente.
     *
     * @param d il docente da cui copiare le informazioni.
     */
    public Docente(Docente d){
        super(d);

        this.richiesteSpostamentoInviate = new ArrayList<>(d.richiesteSpostamentoInviate);
        this.vincoli = new ArrayList<>(d.vincoli);
        this.insegnamenti = new ArrayList<>(d.insegnamenti);
    }
    /**Permette di aggiungere un insegnamento al docente.
     * @param insegnamento è proprio l'insegnamento che vogliamo aggiugere al docente es(se il professore è abilitato)
     * a insegnare 'Matematica, statistica ecc...'.
     * @throws NullPointerException se l'insegnamento passato è null.
     */
    public void addInsegnamento(Insegnamento insegnamento){
        if(insegnamento==null){
            throw new NullPointerException("Il valore è null non puo essere aggiunto!");
        }
        this.insegnamenti.add(insegnamento);
        logger.info(insegnamento.getNome()+" è stato aggiunto a "+this.nome+" "+this.cognome);
    }
    /**
     * Permette di rimuovere un insegnamento al docente.
     * @param insegnamento l'insegnamento da rimuovere.
     * @throws NullPointerException se l'insegnamento passato è null.
     */
    public void removeInsegnamento(Insegnamento insegnamento){
        if(insegnamento==null){
            throw new NullPointerException("Il valore è null non puo essere aggiunto!");
        }
        this.insegnamenti.remove(insegnamento);
    }
    /**
     * Ritorna gli insegnamenti del docente.
     * @return una nuova lista con gli insegnamenti del docente.
     */
    public List<Insegnamento> getInsegnamenti(){
        return new ArrayList<>(insegnamenti);
    }

    /**
     * Ritorna le lezioni del docente.
     * @param o la lista dell'orario generale delle lezioni.
     * @return una lista con le lezioni del docente.
     */
    public List<Lezione> getLezioni(OrarioLezioni o){
        return o.getDocenteLezioni(this);
    }
    /**
     * Questa funzione invia una richiesta di spostamento al responsabile.
     * @param orario elenco degli orari dove verificare l'esistenza della lezione.
     * @param responsabile il responsabile a cui inviare la richiesta.
     * @param motivo la motivazione della richiesta dello spostamento.
     * @param orarioProposto il nuovo orario richiesto.
     * @param orarioVecchio l'orario attuale che si vuole cambiare.
     * @throws IllegalArgumentException se la lezione della richiesta non esiste.
     */
    public void richiestaSpostamentoLezione(OrarioLezioni orario,Responsabile responsabile,String motivo, Orario orarioVecchio, Orario orarioProposto) {
        //creazione della richiesta
        Richiesta richiesta = new Richiesta(this, motivo, orarioVecchio, orarioProposto);
        if (!checkRichiestaLezione(richiesta, orario))
            throw new IllegalArgumentException("La lezione riferita dalla richiesta non è esistente");
        responsabile.richiesteSpostamento.add(richiesta);
        this.richiesteSpostamentoInviate.add(richiesta);

    }
    /**
     * Stampa le richieste inviate del docente nel terminale.
     */
    protected void visualizzaRichiesteInviate(){
        int numeroRichiesta=1;
        int numeroRichieste=richiesteSpostamentoInviate.size();
        logger.info("Richieste di spostamento inviate:");
        if(numeroRichieste==0){
            logger.info("Non hai richieste di spostamento inviate");
            logger.info("-----Fine-----");
            return;
        }
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
        for(Richiesta richiesta : richiesteSpostamentoInviate) {
            logger.info("Docente richiedente: " + richiesta.getDocenteRichiedente().nome + " " + richiesta.getDocenteRichiedente().cognome);
            logger.info("Orario lezione da spostare: " + richiesta.getOrarioLezioneDaSpostare().getOrarioCompleto());
            logger.info("Orario lezione proposto: " + richiesta.getNuovoOrarioLezione().getOrarioCompleto());
            logger.info("Motivo della richiesta: " + richiesta.getMotivoRichiesta());
            logger.info("Stato Richiesta: "+ richiesta.getStatoRichiesta());
            if(numeroRichieste==numeroRichiesta){
                logger.info("-----Fine-----");
                return; }
            logger.info("------------------------------------------------");
            numeroRichiesta++;

        }

    }
    /**
     * Ritorna una lista che contiene le richieste inviate.
     * @return una nuova lista con le richieste di spostamento.
     */
    public List<Richiesta> getRichiesteInviate(){
        return new ArrayList<>(richiesteSpostamentoInviate);
    }
    /**
     * Carica nel docente le richieste inviate lette dal database, sostituendo quelle in memoria.
     * @param richiesteDaCaricare la lista di richieste da salvare.
     */
    public void caricaRichiesteInviate(List<Richiesta> richiesteDaCaricare){
        richiesteSpostamentoInviate = new ArrayList<>(richiesteDaCaricare);
    }

    /** Stampa l'orario del docente nel terminale.
     * @param elencoLezioni un elenco con all'interno l'orario di tutte le lezioni.
     */
    public void visualizzaOrario(OrarioLezioni elencoLezioni){
        elencoLezioni.visualizzaOrarioCompleto(this);
    }

//Gestione dei vincoli
    /** Permette di aggiungere un vincolo, massimo fino a 3 vincoli.
     * @param giorno il giorno della settimana dove e stato impostato il vincolo.
     * @param oraInzio l'ora di inizio del vincolo.
     * @param minutoInzio i minuti di inizio del vincolo.
     * @param oraFIne l'ora di fine del vincolo.
     * @param minutoFine i minuti di fine del vincolo.
     * @throws IllegalArgumentException se si è raggiunto il limite di vincoli.
     */

    public void aggiungiVincolo(String giorno, int oraInzio, int minutoInzio,int oraFIne,int minutoFine){
        if(vincoli.size()==3){
            throw new IllegalStateException("Hai già raggiunto il numero massimo di vincoli (3)");
        }
        vincoli.add(new Vincolo(giorno, oraInzio, minutoInzio, oraFIne, minutoFine));
        logger.info("Vincolo aggiunto con successo");

    }
    /**Carica una lista di vincoli all'interno di docente.
     * @param vincoliDaCaricare la lista di vincoli recuperati da dare al docente.
     */
    public void caricaVincoliInDocente(List<Vincolo> vincoliDaCaricare){
        vincoli= new ArrayList<>(vincoliDaCaricare);
    }

    /** Restituisce i vincoli del docente,
     *  utile per verificare se è disponibile in una certa fascia oraria.
     * @return una nuova lista con all'interno i vincoli.
     */

    public List<Vincolo> getVincoli(){
        return new ArrayList<>(vincoli);
    }
    /**Rimuove un vincolo in base all'indice.
     * @param indice la posizione del vincolo da rimuovere all'interno della lista.
     * @throws IllegalArgumentException se non esiste il vincolo da rimuovere.
     */
    public void rimuoviVincolo(int indice){
        if(indice<0 || indice>=vincoli.size()){
            throw new IllegalArgumentException("Non esiste il vincolo che vuoi rimuovere");
        }
        vincoli.remove(indice);
        logger.info("Vincolo rimosso con successo");
    }
    /**Mostra i vincoli nel terminale.
     */
    public void mostraVincoli(){
        if(vincoli.isEmpty()){
            logger.info("Non hai vincoli");
            return;
        }
        logger.info("Vincoli di"+this.nome+" "+this.cognome+":");
        int numeroVincolo=0;
        for(Vincolo vincolo : vincoli){
            logger.info("Numero vincolo: "+numeroVincolo);
            logger.info("Giorno: "+vincolo.getOrario().getGiorno());
            logger.info("Orario: "+vincolo.getOrario().getOrarioCompleto());
            logger.info("-----------------------------------");
            numeroVincolo++;
        }

    }
    /** Controlla se la richiesta si riferisce a una lezione esistente.
     * @param r la richiesta di spostamento.
     * @param orario l'orario delle lezioni in cui cercare.
     * @return true se la lezione della richiesta esiste, false altrimenti.
     */
    public boolean checkRichiestaLezione(Richiesta r,OrarioLezioni orario){
        boolean lezioneTrovata=false;
        for(Lezione l:getLezioni(orario)){
            lezioneTrovata=r.equalsLezione(l);
            if(lezioneTrovata) break;
        }
        return lezioneTrovata;

    }



}
