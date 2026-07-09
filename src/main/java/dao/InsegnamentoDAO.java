package dao;

import java.sql.SQLException;

public interface InsegnamentoDAO {
    void salvaInsegnamento(String nome, int annoCorso, int cfu) throws SQLException;

    Object[][] caricaInsegnamentiDB();

}