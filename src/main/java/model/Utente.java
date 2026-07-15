package model;
/**
 * La classe base che rappresenta un utente generico all'interno del sistema.
 * Contiene le informazioni e le credenziali di access a
 * tutti i tipi di utenti (come Studente, Docente e Responsabile).
 */

public class Utente {
    /** Il nome dell'utente. */
    protected String nome;
    /** Il cognome dell'utente. */
    protected String cognome;
    /** L'indirizzo email dell'utente. */
    protected String email;
    /** L'username utilizzato per l'accesso al sistema. */
    protected String username;
    /** La password per l'autenticazione. */
    protected String password;

    /**
     * Costruisce un nuovo Utente con i dati e le credenziali specificate.
     * @param nome il nome dell'utente.
     * @param cognome il cognome dell'utente.
     * @param email l'indirizzo email.
     * @param login l'username per l'accesso.
     * @param password la password per l'autenticazione.
     */
    public Utente(String nome,String cognome, String email,String login, String password) {
       this.nome=nome;
       this.cognome=cognome;

       this.email=email;
        this.username = login;
        this.password = password;
    }
    /**
     * Costruttore di copia.
     * Crea una nuova istanza di Utente copiando i dati da un altro oggetto Utente.
     * @param u l'utente da cui copiare i dati.
     */
    public Utente (Utente u){
        this.nome=u.nome;
        this.cognome=u.cognome;
        this.email=u.email;
        this.username = u.username;
        this.password = u.password;
    }

    /**
     * Verifica le credenziali di accesso dell'utente.
     * @param username l'username inserito in fase di login.
     * @param password la password inserita in fase di login.
     * @return true se le credenziali corrispondono a quelle dell'utente, false altrimenti.
     */
    public boolean login(String username, String password) {
        return ( username.equals(this.username) && password.equals(this.password));
    }



    /**
     * Restituisce l'indirizzo email dell'utente.
     * @return l'email dell'utente come stringa.
     */
    //getter
    public String getmail(){
        return this.email;
    }

    public String getUsername(){
        return this.username;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }
}
