package implementazionedao;

import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link LezioneDAO}.
 *
 * <p>Realizza la persistenza delle lezioni sulla tabella {@code lezione}.
 * La connessione è ottenuta dal singleton {@link ConnessioneDatabase}; le query
 * parametriche usano {@link PreparedStatement} con i segnaposto {@code ?}; le
 * {@link SQLException} vengono rilanciate come {@link Exception} con un messaggio
 * leggibile, così il Controller può gestirle e mostrarle alla GUI.</p>
 */
public class LezionePostgresDao implements LezioneDAO {

    /** Connessione JDBC verso il database PostgreSQL. */
    private final Connection connection;

    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public LezionePostgresDao() throws SQLException {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }

    @Override
    public void salvaLezioneDB(String nomeInsegnamento, int annoCorso,
                               String emailDocente,
                               String nomeAula,
                               String giorno, int [] orarioIn) throws SQLException {
        String sql = "INSERT INTO lezione " +
                "(nomecorso, annocorso, emaildocente, nomeaula, " +
                " giorno, orainizio, minutoinizio, orafine, minutofine) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeInsegnamento);
            ps.setInt(2, annoCorso);
            ps.setString(3, emailDocente);
            ps.setString(4, nomeAula);
            ps.setString(5, giorno);
            ps.setInt(6, orarioIn[0]);
            ps.setInt(7, orarioIn[1]);
            ps.setInt(8, orarioIn[2]);
            ps.setInt(9, orarioIn[3]);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Es. violazione del vincolo UNIQUE: lezione già presente in quello slot/aula.
            throw new SQLException("Impossibile salvare la lezione sul database: " + e.getMessage());
        }
    }


    @Override
    public void leggiTutteLezioniDB(ArrayList<String> nomiInsegnamento,
                                    ArrayList<Integer> annoCorso,
                                    ArrayList<String> emailDocente,
                                    ArrayList<String> nomeAula,
                                    ArrayList<String> giorno, ArrayList<Integer> oraInizio, ArrayList<Integer> minutoInizio,
                                    ArrayList<Integer> oraFine, ArrayList<Integer> minutoFine) throws SQLException {
        String sql = "SELECT nomecorso, annocorso, emaildocente, nomeaula, " +
                "giorno, orainizio, minutoinizio, orafine, minutofine " +
                "FROM lezione";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                nomiInsegnamento.add(rs.getString("nomecorso"));
                annoCorso.add(rs.getInt("annocorso"));
                emailDocente.add(rs.getString("emaildocente"));
                nomeAula.add(rs.getString("nomeaula"));
                giorno.add(rs.getString("giorno"));
                oraInizio.add(rs.getInt("orainizio"));
                minutoInizio.add(rs.getInt("minutoinizio"));
                oraFine.add(rs.getInt("orafine"));
                minutoFine.add(rs.getInt("minutofine"));
            }
        } catch (SQLException e) {
            throw new SQLException("Impossibile leggere tutte le lezioni dal database: " + e.getMessage());
        }
    }
}