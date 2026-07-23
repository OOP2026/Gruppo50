package dao;
import java.sql.SQLException;
/**
 * Interfaccia DAO per gli studenti, memorizzati nella tabella unica
 * {@code utente} con ruolo {@code "STUDENTE"}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.StudentePostgresDao}, che delega a
 * {@code UtenteDAO} e filtra i risultati in base al ruolo.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. Rispetto a Docente e Responsabile lo
 * studente ha due campi in più: {@code matricola} e {@code annoCorso}.</p>
 */
public interface StudenteDAO {
    /**Salva i dati grezzi (non gli oggetti) dello studente nel database.
     *
     * @param nome      Nome di battesimo dello studente.
     * @param cognome   cognome di battesimo dello studente.
     * @param email     l'email con cui si registra lo studente al sistema.
     * @param login     username con cui accede lo studente al sistema.
     * @param password  password segreta dello studente per accedere.
     * @param matricola la matricola univoca dello studente.
     * @param annoCorso l'anno di corso (1-3).
     */
     void salvaStudenteDB(String nome, String cognome, String email,
                          String login, String password,String matricola, int annoCorso) throws SQLException;





}


