package implementazioneDao;

import dao.StudenteDAO;
import database_connection.ConnessioneDatabase;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class StudentePostgresDao implements StudenteDAO {
    private final Connection connection;

    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws Exception se la connessione al database fallisce
     */
    public StudentePostgresDao() throws Exception {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }

    /**
     * @param nome      Nome di battesimo dello studente.
     * @param cognome   cognome di battesimo dello studente.
     * @param email     l'email con cui si registra lo studente al sistema.
     * @param login     username con cui accede lo studente al sistema.
     * @param password  password segreta dello studente per accedere.
     * @param matricola la matricola univoca dello studente.
     * @param annoCorso l'anno di corso (1-3).
     * @throws Exception Sql Exception se la scrittura al DB fallisce.
     */
    @Override
    public void salvaStudenteDB(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) throws Exception {
        String sql = "INSERT INTO studente(nome,cognome,email,username,password,matricola,annoCorso)" +
                "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, email);
            ps.setString(4, login);
            ps.setString(5, password);
            ps.setString(6, matricola);
            ps.setInt(7, annoCorso);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Es. violazione della primary key: studente con questa email/matricola già presente.
            throw new Exception("Impossibile salvare lo studente sul database: " + e.getMessage());
        }
    }

    /**
     * Recupera tutti i dati degli studenti presenti nel database e li inserisce
     * all'interno delle rispettive liste passate come parametro.
     * <p>
     * Il metodo esegue una query di selezione sulla tabella "studente" e popola
     * in modo posizionale le liste fornite.
     * </p>
     *
     * @param nome      la lista in cui verranno aggiunti i nomi degli studenti recuperati.
     * @param cognome   la lista in cui verranno aggiunti i cognomi degli studenti recuperati.
     * @param email     la lista in cui verranno aggiunte le email degli studenti recuperati.
     * @param login     la lista in cui verranno aggiunti gli username degli studenti recuperati.
     * @param password  la lista in cui verranno aggiunte le password degli studenti recuperati.
     * @param matricola la lista in cui verranno aggiunte le matricole degli studenti recuperati.
     * @param annoCorso la lista in cui verranno aggiunti gli anni di corso degli studenti recuperati.
     * @throws Exception se si verifica un errore SQL durante l'accesso al database o durante la lettura dei dati.
     */
    @Override
    public void leggiStudenteDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> login, ArrayList<String> password, ArrayList<String> matricola, ArrayList<Integer> annoCorso) throws Exception {
        String sql = "SELECT nome ,cognome,email,username,password," +
                "matricola,annoCorso FROM studente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                nome.add(rs.getString("nome"));
                cognome.add(rs.getString("cognome"));
                email.add(rs.getString("email"));
                login.add(rs.getString("username"));
                password.add(rs.getString("password"));
                matricola.add(rs.getString("matricola"));
                annoCorso.add(rs.getInt("annoCorso"));
            }
        } catch (SQLException e) {
            throw new Exception("Impossibile leggi studente sul database: " + e.getMessage());
        }
    }

    /**
     * Genera una nuova matricola univoca interrogando il database.
     * <p>
     * Il metodo cerca il valore massimo attualmente presente nella colonna "matricola"
     * della tabella "studente" tramite l'operatore SQL {@code MAX()}. Se trova un risultato,
     * estrae la parte numerica successiva al prefisso "DE", la incrementa di uno e la
     * formatta nuovamente. Se la tabella è vuota o non ci sono matricole valide,
     * il conteggio parte dal valore predefinito 1.
     * </p>
     *
     * @return la nuova matricola generata nel formato con prefisso "DE" e 8 cifre numeriche (es. {@code "DE00000001"}).
     * @throws Exception se si verifica un errore SQL durante la lettura del valore massimo o l'accesso al database.
     */
    @Override
    public String generaMatricolaDB() throws Exception {
        String sql = "SELECT MAX(matricola) AS maxmat FROM studente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            long prossimo = 1;
            if (rs.next()) {
                String maxMat = rs.getString("maxmat");
                if (maxMat != null && maxMat.length() > 2) {
                    // Estrae la parte numerica dopo il prefisso "DE" e incrementa.
                    prossimo = Long.parseLong(maxMat.substring(2)) + 1;
                }
            }
            return "DE" + String.format("%08d", prossimo);
        } catch (SQLException e) {
            throw new Exception("Impossibile generare la matricola dal database: " + e.getMessage());
        }
    }
}