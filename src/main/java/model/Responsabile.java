package model;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * La classe Responsabile rappresenta un utente con privilegi e ruoli diversi.
 * Estende {@link Docente} e ha il compito di gestire gli orari,
 * approvare o rifiutare richieste di spostamenti delle lezioni e risolvere eventuali conflitti.
 */
public class Responsabile extends Utente {
    /** Lista delle richieste di spostamento ricevute.
     */
 private static ArrayList<Richiesta> richiesteSpostamento= new ArrayList<>();
  /** Token di sicurezza utilizzato per autorizzare modifiche sull'orario.
   */
private final Token token;
    private static final Logger logger = Logger.getLogger(Responsabile.class.getName());

    /** Costruisce un nuovo responsabile definendo nome, cognome, email, login e password.
     * @param nome nome del responsabile.
     * @param cognome cognome del responsabile.
     * @param email indirizzo email del responsabile.
     * @param login username per l'accesso al sistema.
     * @param password password per l'accesso al sistema.
     */
    public Responsabile(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
        this.token = new Token();
    }

   /** Permette di visualizzare le richieste di spostamento nel terminale.
    */
    public void visualizzaRichiesteSpostamento() {
        int numeroRichiesta=0;
        int numeroRichieste=richiesteSpostamento.size();
            logger.info("Richieste di spostamento:");
            if(numeroRichieste==0){
                logger.info("Non ci sono richieste di spostamento");
                 logger.info("-----Fine-----");
                return;
            }
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
  for(Richiesta richiesta : richiesteSpostamento) {
   String msg="Numero richiesta: " + (numeroRichiesta);
    logger.info(msg);
    msg="Docente richiedente: " + richiesta.getDocenteRichiedente().nome + " " + richiesta.getDocenteRichiedente().cognome;
    logger.info(msg);
    logger.info("Motivo della richiesta: " + richiesta.getMotivoRichiesta());
    logger.info("Orario lezione da spostare: " + richiesta.getOrarioLezioneDaSpostare().getOrarioCompleto());
    logger.info("Orario lezione nuovo: " + richiesta.getNuovoOrarioLezione().getOrarioCompleto());
    logger.info("Stato Richiesta: "+ richiesta.getStatoRichiesta());
    if(numeroRichieste==numeroRichiesta+1){
        logger.info("-----Fine-----");
   return; }
    logger.info("------------------------------------------------");
    numeroRichiesta++;
  }


    }

    /**La funzione inserisce una lezione nell'orario,
     * controllando che non ci siano conflitti con altre lezioni e che il docente sia disponibile in quell'orario
     * @param l è la lezione che deve essere inserita.
     * @param elencoLezioni è dove viene inserito.
     * @throws IllegalArgumentException se il docente ha impostato un vincolo in quell'orario.
     */
public void inserisciLezione(Lezione l, OrarioLezioni elencoLezioni) {
    if(!(verificaDisponibilita(l.getInsegnamento().getDocente().getVincoli(), l.getOrario()))){
        throw new IllegalArgumentException("Il docente non è disponibile in questa fascia oraria");
    }
    // Aggiunge la lezione all'orario in memoria. Se c'è un conflitto,
    // aggiungiLezione lancia IllegalArgumentException: NON la intercettiamo qui,
    // così si propaga al Controller, che la mostra nella GUI e NON salva la
    // lezione sul database.
    elencoLezioni.aggiungiLezione(l, this.token);
    logger.info("Lezione aggiunta con successo responsabile");
}
    /**
     * Gestisce una richiesta di spostamento che se viene approvata, esegue lo
     * spostamento nell'orario generale, altrimenti la rifiuta aggiornandone lo stato.
     * @param numeroRichiesta l'indice della richiesta nella lista
     * @param elencoLezioni l'orario generale da aggiornare
     * @param approva true per approvare ed eseguire lo spostamento, false per rifiutare
     */
public void spostamentoLezione(int numeroRichiesta, OrarioLezioni elencoLezioni, boolean approva){

    // Controllo che l'indice sia valido (get() lancerebbe un'eccezione, non restituisce mai null)
        if (numeroRichiesta < 0 || numeroRichiesta >= richiesteSpostamento.size()) {
        logger.info("La richiesta non esiste");
        return;
    }

    Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);

        if(richiesta.getStatoRichiesta() == StatoRichiesta.APPROVATA){
        logger.info("La richiesta è già stata approvata");
        return;
    }
    if(richiesta.getStatoRichiesta() == StatoRichiesta.RIFIUTATA){
        logger.info("La richiesta è già stata rifiutata");
        return;
    }

    // Da qui in poi la richiesta è sicuramente IN_ATTESA

    // --- Caso RIFIUTO ---
        if(!approva){
        richiesta.setStatoRichiesta(StatoRichiesta.RIFIUTATA);
        logger.info("La richiesta è stata rifiutata");
        return;
    }

    // --- Caso APPROVAZIONE: eseguo lo spostamento ---
    Lezione lezioneDaSpostare = cercaLezioneDaSpostare(richiesta, elencoLezioni);
    if(lezioneDaSpostare == null){
        logger.info("La lezione da spostare non è stata trovata");
        return;
    }

    try{
        elencoLezioni.getOrarioLezioni(this.token).remove(lezioneDaSpostare);
    } catch(Exception e){
        logger.info("Errore nello spostamento della lezione: " + e.getMessage());
        return;
    }

    Lezione nuovaLezione = new Lezione(lezioneDaSpostare.getInsegnamento(), lezioneDaSpostare.getAula(), richiesta.getNuovoOrarioLezione());

    try {
        elencoLezioni.aggiungiLezione(nuovaLezione, this.token);
    } catch (IllegalArgumentException e1) {
        logger.severe("Errore nello spostamento della lezione: " + e1.getMessage());
        richiesta.setStatoRichiesta(StatoRichiesta.RIFIUTATA);
        logger.info("La richiesta è stata rifiutata");
        logger.info("Tentativo di ripristinare la lezione originale...");
        try {
            elencoLezioni.aggiungiLezione(lezioneDaSpostare, this.token);
        } catch (Exception e2) {
            logger.info("Errore nel ripristino della lezione originale: " + e2.getMessage());
        }
        return;
    }

    richiesta.setStatoRichiesta(StatoRichiesta.APPROVATA);
    logger.info("La richiesta è stata approvata");
}
 /** Questo metodo permette di cambiare l'orario della richiesta.
  * @param numeroRichiesta l'indice della richiesta.
  * @param orarioNuovo il nuovo orario da associare alla richiesta.
  */
 public void cambiaOrarioRichiesta(int numeroRichiesta,Orario orarioNuovo){
Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);
 if(richiesta==null){
     logger.info("La richiesta non esiste");
     return;
 }
 if(richiesta.getStatoRichiesta() ==StatoRichiesta.APPROVATA){
     logger.info("La richiesta è già stata approvata");
     return;
 }
 if(richiesta.getStatoRichiesta() ==StatoRichiesta.RIFIUTATA){
     logger.info("La richiesta è già stata rifiutata");
     return;
 }

richiesta.setNuovoOrarioLezione(orarioNuovo);

 }
 /** Permette di visualizzare l'orario completo
  * di tutte le lezioni del corso nel terminale.
  * @param elencoLezioni l'elenco generale delle lezioni*/
 public void visualizzaOrarioCompleto(OrarioLezioni elencoLezioni){

    elencoLezioni.visualizzaOrarioCompleto(this.token);
 }
 

/** Permette di verificare se l'orario viola uno dei vincoli del docente
 * cioe controlla se il docente è disponibile in quella fascia oraria.
 *@param vincoli la lista dei vincoli del docente.
 * @param orario la fascia oraria da verificare.
 * @return true se il docente è disponibile, false altrimenti.
 */
public boolean verificaDisponibilita(List<Vincolo> vincoli, Orario orario){
    if(vincoli.isEmpty()) return true;
for(Vincolo vincolo:vincoli) {
    int orarioInizioVincolo = vincolo.getOrario().getOrarioInizioInMinuti();
    int orarioFineVincolo = vincolo.getOrario().getOrarioFineInMinuti();
    int orarioInizioLezione = orario.getOrarioInizioInMinuti();
    int orarioFineLezione = orario.getOrarioFineInMinuti();
    if (!vincolo.getOrario().getGiorno().equals(orario.getGiorno())) {

        continue;
    }

    if (orarioInizioLezione < orarioFineVincolo && orarioFineLezione > orarioInizioVincolo) return false;

}
return true;
}
/** Serve per trovare la lezione che si vuole spostare.
 * @param r la richiesta di spostamento con i dettagli della lezione.
 * @param elencoLezioni l'elenco delle lezioni in cui cercare.
 */
private Lezione cercaLezioneDaSpostare(Richiesta r, OrarioLezioni elencoLezioni){
          for(Lezione lezione : elencoLezioni.getOrarioLezioni(this.token)) {
        if( r.equalsLezione(lezione))
             return lezione;
    }
    return null;
 }
    /**Carica nel responsabile le richieste di spostamento lette dal database,
     * sostituendo quelle in memoria.
     * @param richiesteDaCaricare la lista di richieste recuperate dal database.
     */
    public static void  caricaRichiesteSpostamento(List<Richiesta> richiesteDaCaricare){
        richiesteSpostamento = new ArrayList<>(richiesteDaCaricare);
    }


    /** Restituisce la lista non modificabile delle richieste di spostamento
     * ricevute da questo responsabile.
     * @return la lista delle richieste solamente leggibili.
     */
     public java.util.List<Richiesta> getRichiesteSpostamento() {
        return java.util.Collections.unmodifiableList(richiesteSpostamento);
    }

    /** Metodo di appoggio per il controller
     *  per ottenere il valore della richiesta come stringa.
     *  @param numeroRichiesta l'indice della richiesta.
     *  @return lo stato della richiesta come stringa , o null se l'indice non è valido.
     */
    public String getStatoRichiesta(int numeroRichiesta) {
        if (numeroRichiesta < 0 || numeroRichiesta >= richiesteSpostamento.size()) {
            return null;
        }
        return richiesteSpostamento.get(numeroRichiesta).getStatoRichiesta().name();
    }

    /** Verifica che una richiesta sia ancora in attesa utilizzato dal controller.
     * @param numeroRichiesta l'indice della richiesta
     * @return true se la richiesta è IN_ATTESA, false altrimenti.*/
    public boolean isRichiestaInAttesa(int numeroRichiesta) {
        if (numeroRichiesta < 0 || numeroRichiesta >= richiesteSpostamento.size()) {
            return false;
        }
        return richiesteSpostamento.get(numeroRichiesta).getStatoRichiesta() == StatoRichiesta.IN_ATTESA;
    }



    /** Il token serve per usare alcuni metodi che solo il responsabile puo usare. */
public class Token {
    private Token() {

    }

}

    /**
     * Rimuove una lezione dall'orario generale.
     * <p>
     * Operazione riservata al Responsabile. Il metodo fa da delegato, passando
     * in automatico il token di sicurezza interno per autorizzare la cancellazione.
     * </p>
     * @param l la lezione da eliminare
     * @param o il registro orario da cui rimuovere la lezione
     */
public void rimuoviLezione(Lezione l,OrarioLezioni o){
    o.rimuoviLezione(l,this.token);
}

    /**
     * Aggiunge una richiesta di spostamento alla coda del responsabile.
     * Metodo usato dal Controller/DAO per segnalare nuove richieste.
     * @param r la richiesta da inserire
     */
    public static void inviaRichiesta(Richiesta r){
        richiesteSpostamento.add(r);
    }

}