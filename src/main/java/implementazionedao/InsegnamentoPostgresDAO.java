package implementazionedao;

import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link InsegnamentoDAO}.
 *
 * <p>Realizza la persistenza degli insegnamenti sulla tabella
 * {@code insegnamento}. La connessione è ottenuta dal singleton
 * {@link ConnessioneDatabase}; le query parametriche usano
 * {@link PreparedStatement} con i segnaposto {@code ?}; le
 * {@link SQLException} vengono rilanciate con un messaggio leggibile, così il
 * Controller può gestirle e mostrarle alla GUI.</p>
 */
public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    /** Connessione JDBC verso il database PostgreSQL. */
    private final Connection connection;
    /** Logger della classe, usato per segnalare l'assenza di insegnamenti. */
    private static final Logger logger = Logger.getLogger(InsegnamentoPostgresDAO.class.getName());


    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public InsegnamentoPostgresDAO() throws SQLException {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }




    /**
     * {@inheritDoc}
     *
     * <p>Esegue una {@code SELECT} di nome, cfu, anno di corso ed email del
     * docente titolare di tutti gli insegnamenti e costruisce la matrice
     * risultato riga per riga dal {@link ResultSet}; se la tabella è vuota
     * lo segnala sul logger e restituisce una matrice {@code 0×0}.</p>
     */
    @Override
    public Object[][] caricaInsegnamentiDB() throws SQLException {
        String sql="SELECT nomecorso,cfu,annocorso,emaildoc FROM insegnamento";
        List<Object[]> insegnamenti= new ArrayList<>();
        boolean haRighe=false;
        try(PreparedStatement ps=connection.prepareStatement(sql)){

            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                haRighe=true;
                String nome= rs.getString("nomecorso");
                int cfu= rs.getInt("cfu");
                int annoCorso= rs.getInt("annocorso");
                String emaildoc= rs.getString("emaildoc");
                insegnamenti.add(new Object[]{nome,cfu,annoCorso,emaildoc});
            }
            rs.close();
        }catch (SQLException e){
            throw new SQLException (e.getMessage());
        }
        if(!haRighe){
            logger.info("Non ci sono insegnamenti nel database");
            return new Object[0][0];
        }
        return insegnamenti.toArray(new Object[0][]);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Esegue una {@code DELETE} sulla tabella {@code insegnamento} filtrando
     * per nome; se nessuna riga viene eliminata solleva una
     * {@link SQLException}.</p>
     */
    @Override
    public void rimuoviInsegnamentoDB(String nome) throws SQLException {
        String sql = "DELETE FROM Insegnamento WHERE nomecorso =?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessun insegnamento trovato per la rimozione.");
            }
        } catch (SQLException e) {
            throw new SQLException( e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     *
     * <p>Esegue un {@code UPDATE} della colonna {@code emaildoc} della tabella
     * {@code insegnamento} filtrando per nome; se nessuna riga viene aggiornata
     * solleva una {@link SQLException}.</p>
     */
    @Override
    public void assegnaDocenteTitolare(String email, String ins) throws SQLException {

        String sql = "UPDATE insegnamento SET emaildoc=? WHERE nomecorso =?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, ins);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessun insegnamento trovato");
            }
        } catch (SQLException e) {
            throw new SQLException( e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Esegue un {@code INSERT} sulla tabella {@code insegnamento} con nome,
     * cfu e anno di corso passati come parametri; la colonna {@code emaildoc}
     * resta {@code null} finché non viene assegnato un docente titolare.</p>
     */
    @Override
    public void salvaInsegnamento(String nome, int annoCorso, int cfu) throws SQLException {
        // Implementazione per salvare il vincolo nel database PostgreSQL
        String sql = "INSERT INTO insegnamento (nomecorso,cfu,annocorso) " +
                "VALUES (?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,nome);
            ps.setInt(2,cfu);
            ps.setInt(3,annoCorso);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile caricare l'insegnamento nel DataBase: " + e.getMessage());
        }
    }


}
