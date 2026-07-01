package implementazioneDao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link UtenteDAO}.
 *
 * <p>Opera sull'unica tabella {@code utente}, che contiene studenti, docenti
 * e responsabili distinti dalla colonna {@code ruolo}. I DAO specifici
 * ({@code StudentePostgresDao}, {@code DocentePostgresDao},
 * {@code ResponsabilePostgresDao}) delegano a questa classe e filtrano
 * i risultati in base al ruolo.</p>
 */
public class UtentePostgresDao implements UtenteDAO {

    /** Connessione JDBC verso il database PostgreSQL. */
    private final Connection connection;

    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws Exception se la connessione al database fallisce
     */
    public UtentePostgresDao() throws Exception {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }

    /**
     * Salva un utente nella tabella {@code utente}.
     * Per docenti e responsabili {@code annoCorso} viene passato
     * {@code null} e salvato come {@code NULL}.
     */
    @Override
    public void salvaUtenteDB(String nome, String cognome, String email,
                              String login, String password,
                              String matricola, Integer annoCorso, String ruolo) throws Exception {
        String sql = "INSERT INTO utente (nome, cognome, email, username, password, matricola, annocorso, ruolo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, email);
            ps.setString(4, login);
            ps.setString(5, password);
            ps.setString(6, matricola);
            if (annoCorso != null) {
                ps.setInt(7, annoCorso);
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, ruolo);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Es. violazione della primary key: utente con questa email già presente.
            throw new Exception("Impossibile salvare l'utente sul database: " + e.getMessage());
        }
    }

    /**
     * Legge tutti gli utenti presenti nella tabella {@code utente} e popola
     * in modo posizionale le liste fornite (stesso indice = stesso utente).
     */
    @Override
    public void leggiUtentiDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email,
                              ArrayList<String> login, ArrayList<String> password,
                              ArrayList<String> matricola, ArrayList<Integer> annoCorso,
                              ArrayList<String> ruolo) throws Exception {
        String sql = "SELECT nome, cognome, email, username, password, matricola, annocorso, ruolo FROM utente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                nome.add(rs.getString("nome"));
                cognome.add(rs.getString("cognome"));
                email.add(rs.getString("email"));
                login.add(rs.getString("username"));
                password.add(rs.getString("password"));
                matricola.add(rs.getString("matricola"));
                int anno = rs.getInt("annocorso");
                annoCorso.add(rs.wasNull() ? null : anno);
                ruolo.add(rs.getString("ruolo"));
            }
        } catch (SQLException e) {
            throw new Exception("Impossibile leggere gli utenti dal database: " + e.getMessage());
        }
    }

    /**
     * Genera una nuova matricola univoca per il prefisso indicato.
     * <p>
     * Il metodo cerca il valore massimo della colonna "matricola" tra le sole
     * matricole che iniziano con il prefisso ({@code "DE"} studenti,
     * {@code "DA"} docenti, {@code "RE"} responsabili) tramite {@code MAX()}
     * e {@code LIKE}. Se trova un risultato, estrae la parte numerica
     * successiva al prefisso, la incrementa di uno e la formatta nuovamente.
     * Se non ci sono matricole con quel prefisso, il conteggio parte da 1.
     * </p>
     *
     * @param prefisso il prefisso della matricola in base al ruolo.
     * @return la nuova matricola nel formato prefisso + 8 cifre numeriche (es. {@code "DA00000001"}).
     * @throws Exception se si verifica un errore SQL durante la lettura del valore massimo.
     */
    @Override
    public String generaMatricolaDB(String prefisso) throws Exception {
        String sql = "SELECT MAX(matricola) AS maxmat FROM utente WHERE matricola LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prefisso + "%");
            try (ResultSet rs = ps.executeQuery()) {
                long prossimo = 1;
                if (rs.next()) {
                    String maxMat = rs.getString("maxmat");
                    if (maxMat != null && maxMat.length() > prefisso.length()) {
                        // Estrae la parte numerica dopo il prefisso e incrementa.
                        prossimo = Long.parseLong(maxMat.substring(prefisso.length())) + 1;
                    }
                }
                return prefisso + String.format("%08d", prossimo);
            }
        } catch (SQLException e) {
            throw new Exception("Impossibile generare la matricola dal database: " + e.getMessage());
        }
    }
}