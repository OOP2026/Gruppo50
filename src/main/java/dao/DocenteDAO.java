package dao;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Interfaccia DAO per i docenti, memorizzati nella tabella unica
 * {@code utente} con ruolo {@code "DOCENTE"}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.DocentePostgresDao}, che delega a
 * {@code UtenteDAO} e filtra i risultati in base al ruolo.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. In questo modo il package di accesso al DB
 * non dipende dal package model. Le eccezioni del database vengono rilanciate
 * verso il Controller con {@code throws Exception}.</p>
 */
public interface DocenteDAO {

    /**Salva nel i dati grezzi(non gli oggetti) del docente nel database.
     *
     * @param  nome Nome di battesimo del docente.
     * @param cognome cognome di battesimo del docente.
     * @param email l'email con cui si registra il docente al sistema.
     * @param login login con cui accede il docente al sistema.
     * @param password password segreta del docente per accedere.
     */
    void salvaDocDB(String nome, String cognome, String email,
                    String login, String password)throws SQLException;

    /**Metodo da implementare in DocentePostgreDao per recuperare i dati di un docente registrato.
     *
     * @param  nome Nome di battesimo del docente.
     * @param cognome cognome di battesimo del docente.
     * @param email l'email con cui si registra il docente al sistema.
     * @param login login con cui accede il docente al sistema.
     * @param password password segreta del docente per accedere
     */
    void leggiDocenteDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email,
                        ArrayList<String> login, ArrayList<String> password)throws SQLException;
}