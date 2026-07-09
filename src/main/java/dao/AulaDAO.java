package dao;

import java.sql.SQLException;

public interface AulaDAO {
    /**
     * Permette di salvare un aula nel database
     * @param nome il nome dell'aula
     * @param capienza la capienza dell'aula
     * @throws Exception se si verifica un errore durante il salvataggio dell'aula
     */
    void salvaAulaDB(String nome,int capienza) throws SQLException;

    /**
     * Restituisce un array che contiene le aule all'interno del database
     * @return {@code Object[][]}
     */
    Object[][] caricaAulaDB();
}
