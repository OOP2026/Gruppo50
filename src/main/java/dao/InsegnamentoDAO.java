package dao;

import java.sql.SQLException;

public interface InsegnamentoDAO {
    void salvaInsegnamento(String nome, int annoCorso, int cfu, String emailDocente) throws SQLException;

    Object[][] caricaInsegnamentiDB() throws SQLException;

    void rimuoviInsegnamentoDB(String nome) throws SQLException;
}