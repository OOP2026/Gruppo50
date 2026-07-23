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
     * Crea un nuovo docente con le sue credenziali, inizializzando vuote le liste
     * di insegnamenti, vincoli e richieste inviate.
     *
     * @param nome nome di battesimo del docente.
     * @param cognome cognome del docente.
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
        String msg=insegnamento.getNome()+" è stato aggiunto a "+this.nome+" "+this.cognome;
        logger.info(msg);
    }
    /**
     * Permette di rimuovere un insegnamento al docente.
     * @param insegnamento l'insegnamento da rimuovere.
     * @throws NullPointerException se l'insegnamento passato è null.
     * @throws IllegalArgumentException se il docente è il titolare di questo
     * insegnamento e quindi non può rimuoverlo dalle proprie materie.
     */
    public void removeInsegnamento(Insegnamento insegnamento){
        if(insegnamento==null){
            throw new NullPointerException("Il valore è null non puo essere aggiunto!");
        }
        Docente tempDocente=insegnamento.getDocente();
        if(tempDocente!=null && tempDocente.getmail().equals(this.email)){
            throw new IllegalArgumentException("Non puoi rimuovere questa materia, sei il docente titolare di questo insegnamento.");
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
    /**Carica nel docente gli insegnamenti letti dal database, sostituendo
     * quelli in memoria (stesso pattern di {@link #caricaVincoliInDocente}).
     * In questo modo a ogni login la lista riflette il database senza duplicati.
     * @param insegnamentiDaCaricare la lista di insegnamenti recuperati dal database.
     */
    public void caricaInsegnamentiInDocente(List<Insegnamento> insegnamentiDaCaricare){
        insegnamenti = new ArrayList<>(insegnamentiDaCaricare);
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
     * @param motivo la motivazione della richiesta dello spostamento.
     * @param orarioProposto il nuovo orario richiesto.
     * @param orarioVecchio l'orario attuale che si vuole cambiare.
     * @throws IllegalArgumentException se la lezione della richiesta non esiste.
     */
    public void richiestaSpostamentoLezione(OrarioLezioni orario,String motivo, Orario orarioVecchio, Orario orarioProposto) {
        //creazione della richiesta
        Richiesta richiesta = new Richiesta(this, motivo, orarioVecchio, orarioProposto);
        if (!checkRichiestaLezione(richiesta, orario))
            throw new IllegalArgumentException("La lezione riferita dalla richiesta non è esistente");
        Responsabile.inviaRichiesta(richiesta);
        this.richiesteSpostamentoInviate.add(richiesta);

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



//Gestione dei vincoli
    /** Permette di aggiungere un vincolo, massimo fino a 3 vincoli.
     * @param v il vincolo (giorno e fascia oraria di indisponibilità) da aggiungere.
     * @throws IllegalStateException se si è già raggiunto il limite di 3 vincoli.
     */
    public void aggiungiVincolo(Vincolo v){
        if(vincoli.size()==3){
            throw new IllegalStateException("Hai già raggiunto il numero massimo di vincoli (3)");
        }
        vincoli.add(v);
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
