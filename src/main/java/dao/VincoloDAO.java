package dao;

import java.sql.SQLException;

/**
 * Interfaccia DAO per la gestione dei vincoli di disponibilità dei docenti.
 * Definisce le operazioni di salvataggio, caricamento e rimozione dei vincoli dal database.
 */
public interface VincoloDAO {
    /**
     * Permette di salvare un vincolo nel database
     * @param emailDocente l'email del docente a cui appartiene il vincolo
     * @param giorno il giorno del vincolo
     * @param oraInzio l'ora di inizio del vincolo
     * @param minutoInzio il minuto di inizio del vincolo
     * @param oraFine l'ora di fine del vincolo
     * @param minutoFine il minuto di fine del vincolo
     * @throws SQLException se si verifica un errore durante il salvataggio nel database
     */
    void salvaVincoloDB(String emailDocente, String giorno, int oraInzio, int minutoInzio, int oraFine, int minutoFine) throws SQLException;

    /**
     * Restituisce un array che contiene i vincoli del docente presenti nel database
     * @param emailDocente l'email del docente di cui caricare i vincoli
     * @return {@code Object[][]} in cui ogni riga rappresenta un vincolo nel formato
     *         {giorno, oraInizio, minutoInizio, oraFine, minutoFine}
     * @throws SQLException se si verifica un errore durante il caricamento dal database
     */
    Object[][] caricaVincoliDB(String emailDocente) throws SQLException;

    /**
     * Rimuove dal database il vincolo corrispondente ai parametri specificati
     * @param emailDocente l'email del docente a cui appartiene il vincolo
     * @param giorno il giorno del vincolo
     * @param oraInzio l'ora di inizio del vincolo
     * @param minutoInizio il minuto di inizio del vincolo
     * @param oraFine l'ora di fine del vincolo
     * @param minutoFine il minuto di fine del vincolo
     * @throws SQLException se si verifica un errore durante la rimozione o se il vincolo non esiste
     */
    void rimuoviVincoloDB(String emailDocente, String giorno, int oraInzio, int minutoInizio, int oraFine, int minutoFine) throws SQLException;
}