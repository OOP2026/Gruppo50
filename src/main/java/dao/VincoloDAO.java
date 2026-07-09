package dao;

import java.sql.SQLException;

public interface VincoloDAO {
    /**
     * Permette di salvare un vincolo nel database
     * @param emailDocente l'email del docente a cui appartiene il vincolo
     * @param giorno il giorno del vincolo
     * @param oraInzio l'ora di inizio del vincolo
     * @param minutoInzio il minuto di inizio del vincolo
     * @param oraFine l'ora di fine del vincolo
     * @param minutoFine il minuto di fine del vincolo
     */
    void salvaVincoloDB(String emailDocente, String giorno, int oraInzio, int minutoInzio, int oraFine, int minutoFine) throws SQLException;

    /**
     * Restituisce un array che contiene i vincoli all'interno del database
     * @return {@code Object[][]}
     */
    Object[][] caricaVincoliDB(String emailDocente) throws SQLException;
    void rimuoviVincoloDB(String emailDocente, String giorno, int oraInzio, int minutoInizio, int oraFine, int minutoFine) throws SQLException;
}
