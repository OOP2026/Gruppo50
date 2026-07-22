package implementazionedao;
import dao.DocenteDAO;
import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link DocenteDAO}.
 *
 * <p>Dopo l'unificazione delle tabelle in {@code utente}, questa classe
 * delega la lettura e la scrittura a {@link UtentePostgresDao} e filtra
 * i risultati con ruolo {@code "DOCENTE"}. Alla registrazione viene
 * generata e salvata una matricola con prefisso {@code "DA"}, ma per il
 * momento il docente non la visualizza a schermo.</p>
 */
public class DocentePostgresDao implements DocenteDAO {

    /** Prefisso delle matricole dei docenti. */
    private static final String PREFISSO_MATRICOLA = "DA";

    /** DAO della tabella unica utente a cui vengono delegate lettura e scrittura. */
    private final UtenteDAO utenteDao;
    /** Connessione diretta al database usata per la tabella materiadocente. */
    private final Connection connection;

    /**
     * Nel costruttore si crea il DAO della tabella utente a cui delegare
     * le operazioni (che a sua volta ottiene la connessione dal singleton).
     * Inoltre si ottiene la connessione diretta per la tabella di associazione
     * {@code materiadocente}, che memorizza le materie che ogni docente può insegnare.
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public DocentePostgresDao() throws SQLException {
        utenteDao = new UtentePostgresDao();
        connection = ConnessioneDatabase.getInstance().getConnection();
    }

    /**
     * Salva il docente nella tabella {@code utente}. La matricola viene
     * generata automaticamente con prefisso {@code "DA"}, da cui il database
     * ricava il ruolo {@code "DOCENTE"} (per il momento la matricola non è
     * visualizzata a schermo); l'anno di corso viene salvato a {@code NULL}.
     *
     * @param nome     Nome di battesimo del docente.
     * @param cognome  cognome di battesimo del docente.
     * @param email    l'email con cui si registra il docente al sistema.
     * @param login    login con cui accede il docente al sistema.
     * @param password password segreta del docente per accedere.
     * @throws SQLException quando si inseriscono dati sbagliati.
     */
    @Override
    public void salvaDocDB(String nome, String cognome, String email, String login, String password) throws SQLException {
        String matricola = utenteDao.generaMatricolaDB(PREFISSO_MATRICOLA);
        utenteDao.salvaUtenteDB(nome, cognome, email, login, password, matricola, null);
    }


    /**
     * Salva nella tabella {@code materiadocente} l'associazione tra il
     * docente e una materia. La clausola {@code WHERE NOT EXISTS} evita di
     * inserire due volte la stessa coppia senza generare errore.
     *
     * @param emailDocente l'email del docente a cui associare la materia.
     * @param nomeMateria il nome dell'insegnamento (materia) da associare.
     * @throws SQLException se l'inserimento nel database fallisce.
     */
    @Override
    public void salvaMateriaDocenteDB(String emailDocente, String nomeMateria) throws SQLException {
        String sql = "INSERT INTO materiadocente (emaildocente, nomecorso) " +
                "SELECT ?, ? WHERE NOT EXISTS " +
                "(SELECT 1 FROM materiadocente WHERE emaildocente = ? AND nomecorso = ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, emailDocente);
            ps.setString(2, nomeMateria);
            ps.setString(3, emailDocente);
            ps.setString(4, nomeMateria);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile salvare la materia del docente nel DataBase: " + e.getMessage());
        }
    }

    /**
     * Rimuove dalla tabella {@code materiadocente} l'associazione tra il
     * docente e una materia. Se l'associazione non esiste non viene sollevato
     * alcun errore (la materia poteva essere stata aggiunta solo in memoria).
     *
     * @param emailDocente l'email del docente da cui rimuovere la materia.
     * @param nomeMateria il nome dell'insegnamento (materia) da rimuovere.
     * @throws SQLException se la cancellazione nel database fallisce.
     */
    @Override
    public void rimuoviMateriaDocenteDB(String emailDocente, String nomeMateria) throws SQLException {
        String sql = "DELETE FROM materiadocente WHERE emaildocente = ? AND nomecorso = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, emailDocente);
            ps.setString(2, nomeMateria);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile rimuovere la materia del docente dal DataBase: " + e.getMessage());
        }
    }

    /**
     * Legge dalla tabella {@code materiadocente} i nomi delle materie
     * associate al docente indicato e li aggiunge alla lista di output.
     *
     * @param emailDocente l'email del docente di cui leggere le materie.
     * @param nomiMaterie lista (di output) riempita con i nomi delle materie del docente.
     * @throws SQLException se la lettura dal database fallisce.
     */
    @Override
    public void leggiMaterieDocenteDB(String emailDocente, ArrayList<String> nomiMaterie) throws SQLException {
        String sql = "SELECT nomecorso FROM materiadocente WHERE emaildocente = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, emailDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nomiMaterie.add(rs.getString("nomecorso"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Impossibile leggere le materie del docente dal DataBase: " + e.getMessage());
        }
    }
}