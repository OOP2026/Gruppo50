package dao;

import java.sql.SQLException;

/**
 * Interfaccia DAO per la tabella {@code aula}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.AulaPostgresDao}.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int, array),
 * mai oggetti del Model. In questo modo il package di accesso al DB non dipende
 * dal package model. Le eccezioni del database vengono rilanciate verso il
 * Controller come {@link SQLException} con un messaggio leggibile.</p>
 */
public interface AulaDAO {

    /**
     * Salva nel database una nuova aula.
     *
     * @param nome     nome dell'aula (chiave primaria)
     * @param capienza capienza massima dell'aula
     * @throws SQLException se la scrittura sul database fallisce
     *                      (ad es. aula già esistente con lo stesso nome)
     */
    void salvaAulaDB(String nome,int capienza) throws SQLException;

    /**
     * Legge dal database tutte le aule presenti.
     *
     * @return una matrice in cui ogni riga rappresenta un'aula: in posizione
     *         {@code [i][0]} il nome (String) e in posizione {@code [i][1]} la
     *         capienza (Integer); matrice vuota se non ci sono aule
     * @throws SQLException se la lettura dal database fallisce
     */
    Object[][] caricaAulaDB() throws SQLException;

    /**
     * Rimuove dal database l'aula con il nome indicato.
     *
     * @param nome nome dell'aula da rimuovere
     * @throws SQLException se la rimozione fallisce o se non esiste alcuna
     *                      aula con quel nome
     */
    void rimuoviAulaDB(String nome) throws SQLException;
}
