package dao;
import java.sql.SQLException;
/**
 * Interfaccia DAO per i responsabili, memorizzati nella tabella unica
 * {@code utente} con ruolo {@code "RESPONSABILE"}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.ResponsabilePostgresDao}, che delega a
 * {@code UtenteDAO} e filtra i risultati in base al ruolo.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. In questo modo il package di accesso al DB
 * non dipende dal package model. Le eccezioni del database vengono rilanciate
 * verso il Controller con {@code throws Exception}.</p>
 */
public interface ResponsabileDAO {
    /**Salva nel i dati grezzi(non gli oggetti) del Responsabile nel database.
     *
     * @param  nome Nome di battesimo del Responsabile.
     * @param cognome cognome di battesimo del Responsabile.
     * @param email l'email del Responsabile.
     * @param login username con cui accede il Responsabile al sistema.
     * @param password password segreta del Responsabile per accedere.
     */
    void salvaResponsabileDB(String nome, String cognome, String email,
                             String login, String password) throws SQLException;



}