package implementazionedao;


import dao.AulaDAO;
import database_connection.ConnessioneDatabase;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Implementazione PostgreSQL dell'interfaccia {@link AulaDAO}.
 *
 * <p>Realizza la persistenza delle aule sulla tabella {@code aula}. La
 * connessione è ottenuta dal singleton {@link ConnessioneDatabase}; le query
 * parametriche usano {@link PreparedStatement} con i segnaposto {@code ?}; le
 * {@link SQLException} vengono rilanciate con un messaggio leggibile, così il
 * Controller può gestirle e mostrarle alla GUI.</p>
 */
public class AulaPostgresDao implements AulaDAO {
    /** Connessione JDBC verso il database PostgreSQL. */
    private final Connection connection;
    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public AulaPostgresDao() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().getConnection();
    }
    /**
     * {@inheritDoc}
     *
     * <p>Esegue un {@code INSERT} sulla tabella {@code aula} con nome e
     * capienza passati come parametri.</p>
     */
    @Override
    public void salvaAulaDB(String nome, int capienza) throws SQLException {
 String sql="INSERT INTO aula (nome, capienza) VALUES (?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, capienza);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>Esegue una {@code SELECT} di nome e capienza di tutte le aule e
     * costruisce la matrice risultato riga per riga dal {@link ResultSet};
     * se la tabella è vuota restituisce una matrice {@code 0×0}.</p>
     */
    @Override
    public Object[][] caricaAulaDB() throws SQLException {
        String sql="SELECT nome, capienza FROM aula";
        List<Object[]> aule= new ArrayList<>();
        boolean haRighe=false;
        try(PreparedStatement ps=connection.prepareStatement(sql)){

            ResultSet rs= ps.executeQuery();

          while(rs.next()){
              haRighe=true;
              String nome= rs.getString("nome");
              int capienza= rs.getInt("capienza");
              aule.add(new Object[]{nome,capienza});
          }
rs.close();
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
if(!haRighe){
    return new Object[0][0];
}
        return aule.toArray(new Object[0][]);
    }
    /**
     * {@inheritDoc}
     *
     * <p>Esegue una {@code DELETE} sulla riga con il nome indicato; se nessuna
     * riga viene eliminata (aula inesistente) lancia una {@link SQLException}.</p>
     */
    @Override
    public void rimuoviAulaDB(String nome) throws SQLException {
        String sql="DELETE FROM aula WHERE nome=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,nome);
            int righeEliminate=ps.executeUpdate();
            if(righeEliminate==0){
                throw new SQLException("Nessuna aula trovata con il nome: "+nome);
            }
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }

    }
}
