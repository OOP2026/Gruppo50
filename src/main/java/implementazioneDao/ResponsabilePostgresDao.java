package implementazioneDao;

import dao.ResponsabileDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResponsabilePostgresDao implements ResponsabileDAO {
    private final Connection connection;
    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws Exception se la connessione al database fallisce
     */
    public ResponsabilePostgresDao() throws Exception {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }

    /**
     * @param nome     Nome di battesimo del Responsabile.
     * @param cognome  cognome di battesimo del Responsabile.
     * @param email    l'email del Responsabile.
     * @param login    username con cui accede il Responsabile al sistema.
     * @param password password segreta del Responsabile per accedere.
     * @throws Exception Throws exception quando la scrittura nel database non va a buon fine.
     */
    @Override
    public void salvaResponsabileDB(String nome, String cognome, String email, String login, String password) throws Exception {
        String sql = "INSERT INTO responsabile (nome, cognome, email, username, password) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, email);
            ps.setString(4, login);
            ps.setString(5, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Es. violazione della primary key: docente con questa email già presente.
            throw new Exception("Impossibile salvare il docente sul database: " + e.getMessage());
        }
    }

    /**
     * @param nome la lista dei nomi dei docenti presenti nel db.
     * @param cognome la lista dei  cognomi dei docenti presenti nel db.
     * @param email la lista dei nomi dei docenti presenti nel db.
     * @param login la lista dei nomi dei docenti presenti nel db.
     * @param password la lista dei nomi dei docenti presenti nel db.
     * @throws Exception Throws exception quando la lettura nel database non va a buon fine.
     */
    @Override
    public void leggiResponsabileDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> login, ArrayList<String> password) throws Exception {
        String sql = "SELECT nome, cognome, email, username, password FROM responsabile";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                nome.add(rs.getString("nome"));
                cognome.add(rs.getString("cognome"));
                email.add(rs.getString("email"));
                login.add(rs.getString("username"));
                password.add(rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new Exception("Impossibile leggere i docenti dal database: " + e.getMessage());
        }
    }
}
