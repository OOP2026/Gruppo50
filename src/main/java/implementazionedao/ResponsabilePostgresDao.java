package implementazionedao;

import dao.ResponsabileDAO;
import dao.UtenteDAO;

import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link ResponsabileDAO}.
 *
 * <p>Dopo l'unificazione delle tabelle in {@code utente}, questa classe
 * delega la lettura e la scrittura a {@link UtentePostgresDao} e filtra
 * i risultati con ruolo {@code "RESPONSABILE"}. Alla registrazione viene
 * generata e salvata una matricola con prefisso {@code "RE"}, ma per il
 * momento il responsabile non la visualizza a schermo.</p>
 */
public class ResponsabilePostgresDao implements ResponsabileDAO {

    /** Ruolo con cui i responsabili sono salvati nella tabella utente. */
    private static final String RUOLO = "RESPONSABILE";
    /** Prefisso delle matricole dei responsabili. */
    private static final String PREFISSO_MATRICOLA = "RE";

    /** DAO della tabella unica utente a cui vengono delegate lettura e scrittura. */
    private final UtenteDAO utenteDao;

    /**
     * Nel costruttore si crea il DAO della tabella utente a cui delegare
     * le operazioni (che a sua volta ottiene la connessione dal singleton).
     *
     * @throws Exception se la connessione al database fallisce
     */
    public ResponsabilePostgresDao() throws Exception {
        utenteDao = new UtentePostgresDao();
    }

    /**
     * Salva il responsabile nella tabella {@code utente}. La matricola viene
     * generata automaticamente con prefisso {@code "RE"}, da cui il database
     * ricava il ruolo {@code "RESPONSABILE"} (per il momento la matricola non
     * è visualizzata a schermo); l'anno di corso viene salvato a {@code NULL}.
     *
     * @param nome     Nome di battesimo del Responsabile.
     * @param cognome  cognome di battesimo del Responsabile.
     * @param email    l'email del Responsabile.
     * @param login    username con cui accede il Responsabile al sistema.
     * @param password password segreta del Responsabile per accedere.
     * @throws Exception Throws exception quando la scrittura nel database non va a buon fine.
     */
    @Override
    public void salvaResponsabileDB(String nome, String cognome, String email, String login, String password) throws Exception {
        String matricola = utenteDao.generaMatricolaDB(PREFISSO_MATRICOLA);
        utenteDao.salvaUtenteDB(nome, cognome, email, login, password, matricola, null);
    }

    /**
     * Recupera tutti i responsabili: legge tutti gli utenti tramite
     * {@link UtenteDAO#leggiUtentiDB} e filtra le righe con ruolo {@code "RESPONSABILE"}.
     *
     * @param nome la lista dei nomi dei responsabili presenti nel db.
     * @param cognome la lista dei  cognomi dei responsabili presenti nel db.
     * @param email la lista delle email dei responsabili presenti nel db.
     * @param login la lista degli username dei responsabili presenti nel db.
     * @param password la lista delle password dei responsabili presenti nel db.
     * @throws Exception Throws exception quando la lettura nel database non va a buon fine.
     */
    @Override
    public void leggiResponsabileDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> login, ArrayList<String> password) throws Exception {
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<String> logins = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();
        ArrayList<String> matricole = new ArrayList<>();
        ArrayList<Integer> anniCorso = new ArrayList<>();
        ArrayList<String> ruoli = new ArrayList<>();
        utenteDao.leggiUtentiDB(nomi, cognomi, emails, logins, passwords, matricole, anniCorso, ruoli);

        for (int i = 0; i < ruoli.size(); i++) {
            if (RUOLO.equalsIgnoreCase(ruoli.get(i))) {
                nome.add(nomi.get(i));
                cognome.add(cognomi.get(i));
                email.add(emails.get(i));
                login.add(logins.get(i));
                password.add(passwords.get(i));
                // matricola e annoCorso vengono ignorati: per ora il responsabile non li visualizza.
            }
        }
    }
}