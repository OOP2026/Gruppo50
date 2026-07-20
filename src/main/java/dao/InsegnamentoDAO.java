package dao;

import java.sql.SQLException;

/**
 * Interfaccia DAO per la tabella {@code insegnamento}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazionedao.InsegnamentoPostgresDAO}.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int, array),
 * mai oggetti del Model. In questo modo il package di accesso al DB non dipende
 * dal package model. Le eccezioni del database vengono rilanciate verso il
 * Controller come {@link SQLException} con un messaggio leggibile.</p>
 */
public interface InsegnamentoDAO {

    /**
     * Salva nel database un nuovo insegnamento, inizialmente senza docente
     * titolare assegnato.
     *
     * @param nome      nome dell'insegnamento (chiave primaria)
     * @param annoCorso anno di corso in cui è erogato l'insegnamento
     * @param cfu       numero di crediti formativi dell'insegnamento
     * @throws SQLException se la scrittura sul database fallisce
     *                      (ad es. insegnamento già esistente con lo stesso nome)
     */
    void salvaInsegnamento(String nome, int annoCorso, int cfu) throws SQLException;

    /**
     * Legge dal database tutti gli insegnamenti presenti.
     *
     * @return una matrice in cui ogni riga rappresenta un insegnamento: in
     *         posizione {@code [i][0]} il nome (String), in {@code [i][1]} i
     *         cfu (Integer), in {@code [i][2]} l'anno di corso (Integer) e in
     *         {@code [i][3]} l'email del docente titolare (String, {@code null}
     *         se non assegnato); matrice vuota se non ci sono insegnamenti
     * @throws SQLException se la lettura dal database fallisce
     */
    Object[][] caricaInsegnamentiDB() throws SQLException;

    /**
     * Rimuove dal database l'insegnamento con il nome indicato.
     *
     * @param nome nome dell'insegnamento da rimuovere
     * @throws SQLException se la rimozione fallisce o se non esiste alcun
     *                      insegnamento con quel nome
     */
    void rimuoviInsegnamentoDB(String nome) throws SQLException;

    /**
     * Assegna un docente come titolare di un insegnamento.
     *
     * @param email email del docente da assegnare come titolare
     * @param ins   nome dell'insegnamento a cui assegnare il docente
     * @throws SQLException se l'aggiornamento fallisce o se non esiste alcun
     *                      insegnamento con quel nome
     */
    void assegnaDocenteTitolare(String email,String ins) throws SQLException;
}