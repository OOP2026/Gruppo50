package dao;

public interface InsegnamentoDAO {
    void salvaInsegnamento(String nome, int annoCorso, int cfu) throws Exception;

    Object[][] caricaInsegnamentiDB();

}