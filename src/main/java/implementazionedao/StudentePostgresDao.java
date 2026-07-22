package implementazionedao;

import dao.StudenteDAO;
import dao.UtenteDAO;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link StudenteDAO}.
 *
 * <p>Dopo l'unificazione delle tabelle in {@code utente}, questa classe
 * delega la lettura e la scrittura a {@link UtentePostgresDao} e filtra
 * i risultati con ruolo {@code "STUDENTE"}.</p>
 */
public class StudentePostgresDao implements StudenteDAO {

    
    /** DAO della tabella unica utente a cui vengono delegate lettura e scrittura. */
    private final UtenteDAO utenteDao;

    /**
     * Nel costruttore si crea il DAO della tabella utente a cui delegare
     * le operazioni (che a sua volta ottiene la connessione dal singleton).
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public StudentePostgresDao() throws SQLException {
        utenteDao = new UtentePostgresDao();
    }

    /**
     * Salva lo studente nella tabella {@code utente} delegando a
     * {@link UtenteDAO#salvaUtenteDB}: dal prefisso {@code "DE"} della
     * matricola il database ricava il ruolo {@code "STUDENTE"}.
     *
     * @param nome      Nome di battesimo dello studente.
     * @param cognome   cognome di battesimo dello studente.
     * @param email     l'email con cui si registra lo studente al sistema.
     * @param login     username con cui accede lo studente al sistema.
     * @param password  password segreta dello studente per accedere.
     * @param matricola la matricola univoca dello studente.
     * @param annoCorso l'anno di corso (1-3).
     * @throws SQLException Sql Exception se la scrittura al DB fallisce.
     */
    @Override
    public void salvaStudenteDB(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) throws SQLException {
        utenteDao.salvaUtenteDB(nome, cognome, email, login, password, matricola, annoCorso);
    }

}