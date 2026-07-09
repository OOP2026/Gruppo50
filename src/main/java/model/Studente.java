package model;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Rappresenta uno studente all'interno del sistema.
 * Estende la classe Utente e gestisce informazioni specifiche come
 * la matricola (che é unica per studente) e l'anno di corso.
 */
public class Studente extends Utente  {
    private final String matricola;
    private final int annoCorso;

    private static final Logger logger = Logger.getLogger(Studente.class.getName());

    /**
     * Lista per tenere traccia delle matricole già assegnate
     * e garantire che ognuna sia unica in fase di creazione di un nuovo studente.
     */
    private static ArrayList<String> matricole= new ArrayList<>();


    /**
     * Crea un nuovo oggetto Studente, dandogli dei valori.
     * * @param nome il nome dello studente
     * @param cognome il cognome dello studente
     * @param email l'indirizzo email dello studente
     * @param login l'username per l'accesso al sistema
     * @param password la password per l'accesso
     * @param matricola il numero di matricola dello studente
     * @param annoCorso l'anno di corso frequentato (deve essere compreso tra 1 e 3)
     * @throws IllegalArgumentException se la matricola è vuota, nulla, o già esistente,
     * oppure se l'anno di corso non è compreso tra 1 e 3
     */


    public Studente(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) {
        super(nome,cognome,email,login,password);

        if(matricola==null || matricola.isEmpty()){
            throw new IllegalArgumentException("La matricola non può essere vuota");
        }
    if(matricole.contains(matricola)){
        throw new IllegalArgumentException("La matricola deve essere unica");
    }

        if(annoCorso<1 || annoCorso>3){
            throw new IllegalArgumentException("L'anno di corso deve essere compreso tra 1 e 3");
        }
        this.matricola = matricola;
        this.annoCorso = annoCorso;
            matricole.add(matricola);
 
    }

    /**
     * Stampa un messaggio di saluto nel terminale dicendo nome, cognome e matricola.
     */
    public void saluto() {
        logger.info("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono uno studente con matricola " + this.matricola);
    }

    /**
     * Permette allo studente di visualizzare l'orario completo delle lezioni nel terminale.
     * * @param elencoLezioni la struttura dati contenente l'orario del corso
     */
    public void visualizzaOrarioLezioni(OrarioLezioni elencoLezioni) {
        // Implementazione del metodo per visualizzare l'orario delle lezioni
elencoLezioni.visualizzaOrarioCompleto(this);

    }

    /**
     * Restituisce la matricola assegnata a questo studente.
     * @return la matricola dello studente come stringa
     */
    public String getmatricola(){
        return matricola;
    }

    /**
     * Restituisce l'anno di corso dello studente.
     * @return l'anno di corso (1-3)
     */
    public int getAnnoCorso(){
        return annoCorso;
    }



}
