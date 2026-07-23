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

    /** Prefisso delle matricole dei responsabili. */
    private static final String PREFISSO_MATRICOLA = "RE";

    /** DAO della tabella unica utente a cui vengono delegate lettura e scrittura. */
    private final UtenteDAO utenteDao;

    /**
     * Nel costruttore si crea il DAO della tabella utente a cui delegare
     * le operazioni (che a sua volta ottiene la connessione dal singleton).
     *
     * @throws java.sql.SQLException se la connessione al database fallisce
     */
    public ResponsabilePostgresDao() throws java.sql.SQLException {
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
     * @throws java.sql.SQLException Throws exception quando la scrittura nel database non va a buon fine.
     */
    @Override
    public void salvaResponsabileDB(String nome, String cognome, String email, String login, String password) throws java.sql.SQLException {
        String matricola = utenteDao.generaMatricolaDB(PREFISSO_MATRICOLA);
        utenteDao.salvaUtenteDB(nome, cognome, email, login, password, matricola, null);
    }

}