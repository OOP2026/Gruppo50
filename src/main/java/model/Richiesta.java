package model;
/** Questa classe rappresenta una richiesta di spostamento per una lezione.
 * Viene generata da un {@link Docente} e gestita da un {@link Responsabile}.
 */
public class Richiesta{
    /** Id della richiesta sul database.
     * Vale -1 se la richiesta non è (ancora) salvata sul DB.
     */
   private int id = -1;
    /** Il docente che ha mandato la richiesta di spostamento. */
    private final Docente docenteRichiedente;
    /** Il motivo dato dal docente per giustificare lo spostamento. */
    private final String motivoRichiesta;
    /** L'orario attuale della lezione che si vuole spostare. */
    private final Orario orarioLezioneDaSpostare;
    /** Il nuovo orario creato per la lezione. */
    private Orario nuovoOrarioLezione;
    /** Lo stato attuale della richiesta che di base è IN_ATTESA. */
    private StatoRichiesta statoRichiesta= StatoRichiesta.IN_ATTESA;

    /** Crea una nuova richiesta di spostamento.
     * @param docenteRichiedente il docente che richiede lo spostamento.
     * @param motivoRichiesta il testo con la motivazione dello spostamento.
     * @param orarioLezioneDaSpostare l'orario della lezione da spostare.
     * @param nuovoOrarioLezione il nuovo orario della lezione proposta.
     * @throws IllegalArgumentException se il nuovo orario è identico a quello vecchio.
     */
    public Richiesta(Docente docenteRichiedente, String motivoRichiesta, Orario orarioLezioneDaSpostare, Orario nuovoOrarioLezione) {
        this.docenteRichiedente = docenteRichiedente;
        this.motivoRichiesta = motivoRichiesta;
        this.orarioLezioneDaSpostare = orarioLezioneDaSpostare;
        this.nuovoOrarioLezione = nuovoOrarioLezione;
        if (orarioLezioneDaSpostare.equals(nuovoOrarioLezione)){
            throw new IllegalArgumentException("La richiesta non può avere il nuovo orario uguale a quello vecchio");
        }
    }
    //getter and setter

    /** Crea una copia del docente richiedente.
     * @return una nuova istanza di docente con i dati del docente richiedente.
     */
    public Docente getDocenteRichiedente() {
        return new Docente(docenteRichiedente);
    }
    /** Crea una copia dell'orario della lezione da spostare.
     * @return una nuova istanza dell'orario con i dati dell'orario originale.
     */
    public Orario getOrarioLezioneDaSpostare(){
        return new Orario(orarioLezioneDaSpostare);
    }
    /** Crea una copia del nuovo orario proposto.
     * @return una nuova istanza di Orario con l'orario nuovo.
     */
    public Orario getNuovoOrarioLezione(){
        return new Orario(nuovoOrarioLezione);
    }
    /** Modifica l'orario della lezione per lo spostamento.
     * @param nuovoOrarioLezione la stringa contiene la motivazione.
     */
    public void setNuovoOrarioLezione(Orario nuovoOrarioLezione) {
        this.nuovoOrarioLezione = nuovoOrarioLezione;
    }
    /** Restituisce il motivo della richiesta.
     * @return la stringa con il motivo della richiesta.
     */
    public String getMotivoRichiesta() {
        return motivoRichiesta;
    }
    /** Restituisce lo stato della richiesta.
     * @return lo stato della richiesta che puo essere IN_ATTESA, APPROVATA o RIFIUTATA.
     */
    public StatoRichiesta getStatoRichiesta(){
        return statoRichiesta;
    }
    /** Imposta un nuovo stato della richiesta.
     * @param statoRichiesta il nuovo stato da assegnare.
     */
    public void setStatoRichiesta(StatoRichiesta statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }
    /** Restituisce l'identificativo della richiesta sul database.
     * @return l'id della richiesta, oppure -1 se non ancora salvata.
     */
    public int getId() {
        return id;
    }
    /** Imposta l'identificativo della richiesta.
     * @param id l'id univoco del database.
     */

    public void setId(int id) {
        this.id = id;
    }

    /**Imposta lo stato della richiesta a partire dalla stringa letta dal database
     *(es. "IN_ATTESA", "APPROVATA", "RIFIUTATA"). L'enum StatoRichiesta non è
     *visibile fuori dal package model, quindi il Controller usa questo metodo.
     * @param stato la stringa che rappresenta lo stato della richeista.
     */
    public void caricaStatoDaDB(String stato){
        this.statoRichiesta = StatoRichiesta.valueOf(stato);
    }

    /**Questo metodo a come scopo quello di individuare
     * la lezione a cui si riferisce la richiesta.
     * @param l la lezione da confrontare.
     * @return true se la richiesta si riferisce a questa lezione, false altrimenti.
     */

    public boolean equalsLezione(Lezione l){
        return this.docenteRichiedente.email.equals(l.getInsegnamento().getDocente().email) &&
                this.orarioLezioneDaSpostare.equals(l.getOrario());
    }

}