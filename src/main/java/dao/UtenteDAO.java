package dao;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Interfaccia DAO per la tabella {@code utente}.
 *
 * <p>Le tabelle {@code studente}, {@code docente} e {@code responsabile} sono
 * state unificate in un'unica tabella {@code utente}, distinta dalla colonna
 * {@code ruolo}. Questo DAO è quindi l'unico punto di lettura e scrittura
 * degli utenti: i DAO di Studente, Docente e Responsabile delegano a questo
 * e filtrano i risultati in base al ruolo.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, Integer,
 * ArrayList), mai oggetti del Model. Le eccezioni del database vengono
 * rilanciate verso il Controller con {@code throws Exception}.</p>
 */
public interface UtenteDAO {

    /**Salva i dati grezzi (non gli oggetti) di un utente nella tabella utente.
     *
     * <p>Il ruolo non viene inserito: nella tabella {@code utente} è una
     * colonna {@code GENERATED}, calcolata dal database in base al prefisso
     * della matricola.</p>
     *
     * @param nome      Nome di battesimo dell'utente.
     * @param cognome   cognome di battesimo dell'utente.
     * @param email     l'email con cui si registra l'utente al sistema.
     * @param login     username con cui accede l'utente al sistema.
     * @param password  password segreta dell'utente per accedere.
     * @param matricola la matricola univoca dell'utente, con prefisso in base
     *                  al ruolo: {@code "DE"} studenti, {@code "DA"} docenti,
     *                  {@code "RE"} responsabili (per il momento docenti e
     *                  responsabili non la visualizzano a schermo).
     * @param annoCorso l'anno di corso (1-3); deve essere {@code null}
     *                  per docenti e responsabili.
     */
    void salvaUtenteDB(String nome, String cognome, String email,
                       String login, String password,
                       String matricola, Integer annoCorso) throws SQLException;

    /**Recupera i dati di tutti gli utenti registrati, di qualunque ruolo.
     * Le liste passate come parametro vengono riempite in modo parallelo
     * (stesso indice = stesso utente). Per docenti e responsabili
     * {@code matricola} e {@code annoCorso} possono contenere {@code null}.
     *
     * @param nome      lista in cui inserire i nomi.
     * @param cognome   lista in cui inserire i cognomi.
     * @param email     lista in cui inserire le email.
     * @param login     lista in cui inserire gli username.
     * @param password  lista in cui inserire le password.
     * @param matricola lista in cui inserire le matricole (o {@code null}).
     * @param annoCorso lista in cui inserire gli anni di corso (o {@code null}).
     * @param ruolo     lista in cui inserire i ruoli.
     */
    void leggiUtentiDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email,
                       ArrayList<String> login, ArrayList<String> password,
                       ArrayList<String> matricola, ArrayList<Integer> annoCorso,
                       ArrayList<String> ruolo) throws SQLException;

    /**Genera la prossima matricola univoca per il prefisso indicato,
     * basandosi sui dati presenti nella tabella utente (matricola massima
     * con quel prefisso + 1), così da evitare collisioni con utenti
     * registrati in sessioni precedenti. Il conteggio è separato per ogni
     * prefisso.
     *
     * @param prefisso il prefisso della matricola: {@code "DE1"} per gli
     *                 studenti, {@code "DA"} per i docenti, {@code "RE"}
     *                 per i responsabili.
     * @return la nuova matricola nel formato prefisso + 8 cifre numeriche
     *         (es. {@code "DA00000001"}).
     */
    String generaMatricolaDB(String prefisso) throws SQLException;
}