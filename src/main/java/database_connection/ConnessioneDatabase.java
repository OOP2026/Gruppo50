package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Classe di utilità per la gestione della connessione al database PostgreSQL.
 * Implementa il pattern Singleton: esiste una sola istanza della connessione
 * per tutta la durata dell'applicazione.
 *
 * Usata dalle classi del package implementazionePostgresDAO per ottenere
 * la connessione al database tramite il metodo getConnection().
 *
 * Schema: orariolezione
 * Tabelle: studente, docente, aula, insegnamento, responsabile
 */
public class ConnessioneDatabase {

    // --- ATTRIBUTI ---
    /**Se status è {@code false} significa che il programma continuera offline
     * senza mandare query al database
     * Se status è {@code true} il programma mandare query al database
     * Serve per evitare inutili problemi con gli errori delle query quando la connessione con il db non c'è
     * Lo status viene impostato appena si entra nel programma, se la connessione al db va a buon fine allora status è {@code true}, altrimenti è {@code false}
     * */
private static boolean status;
    /** Unica istanza della classe (pattern Singleton) */
    private static ConnessioneDatabase instance;

    /** Oggetto Connection JDBC verso il database PostgreSQL */
    public Connection connection = null;

    /** Nome utente del database */
    private String nome = "postgres";

    /** Password del database */
    private String password = "Basidati2026";

    /**
     * URL di connessione JDBC.
     * Formato: jdbc:postgresql://<host>:<porta>/<nome_database>
     * Il database si chiama "orariolezione" come da schema SQL fornito.
     */
    private String url = "jdbc:postgresql://localhost:5432/orariolezionez";

    /** Nome del driver JDBC per PostgreSQL */
    private String driver = "org.postgresql.Driver";
    private static final Logger logger = Logger.getLogger(ConnessioneDatabase.class.getName());


    // --- COSTRUTTORE PRIVATO ---

    /**
     * Costruttore privato: impedisce l'istanziazione diretta dall'esterno.
     * Carica il driver JDBC e apre la connessione al database.
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
            logger.info("Connessione al database 'orariolezione' avvenuta con successo.");

        } catch (ClassNotFoundException ex) {
            logger.info("Driver PostgreSQL non trovato: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, "Driver non trovato", ex);
        }
    }


    // --- METODO SINGLETON ---

    /**
     * Restituisce l'unica istanza di ConnessioneDatabase .
     * Se l'istanza non esiste ancora, la crea.
     * Se la connessione è stata chiusa, la ricrea.
     *
     * Esempio di utilizzo nelle classi ImplementazionePostgresDAO:
     *   connection = ConnessioneDatabase.getInstance().getConnection();
     *
     * @return l'istanza singleton di ConnessioneDatabase
     * @throws SQLException se la creazione della connessione fallisce
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }


    // --- GETTER ---

    /**
     * Restituisce l'oggetto Connection JDBC.
     * Da chiamare nelle classi DAO per eseguire query sul database.
     *
     * @return la connessione JDBC attiva
     */
    public Connection getConnection() {
        return connection;
    }
    
    public static boolean getStatus(){return ConnessioneDatabase.status;}
    public static void setStatus(boolean status){
        ConnessioneDatabase.status =status;}
}

