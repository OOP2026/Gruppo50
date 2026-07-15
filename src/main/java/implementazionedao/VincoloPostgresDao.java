package implementazionedao;

import dao.VincoloDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VincoloPostgresDao implements VincoloDAO {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(VincoloPostgresDao.class.getName());
    public VincoloPostgresDao() throws SQLException {
        new ConnessioneDatabase();
        this.connection = ConnessioneDatabase.getInstance().getConnection();
    }
    @Override
    public void salvaVincoloDB(String emailDocente, String giorno, int oraInzio, int minutoInzio, int oraFine, int minutoFine) throws SQLException {
        // Implementazione per salvare il vincolo nel database PostgreSQL
        String sql = "INSERT INTO vincolo (docentee,orarioi,orariof,giorno) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            LocalTime orarioInizio = LocalTime.of(oraInzio, minutoInzio);
            LocalTime orarioFine = LocalTime.of(oraFine, minutoFine);
            ps.setString(1, emailDocente);
            ps.setObject(2, orarioInizio);
            ps.setObject(3, orarioFine);
            ps.setString(4, giorno);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public Object[][] caricaVincoliDB(String emailDocente) throws SQLException {
        String sql="SELECT orarioi,orariof,giorno FROM vincolo WHERE docentee = ?";
        List<Object[]> vincoli= new ArrayList<>();
        boolean haRighe=false;
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1, emailDocente);
            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                haRighe=true;
                LocalTime orarioi= rs.getTime("orarioi").toLocalTime();
                LocalTime orariof= rs.getTime("orariof").toLocalTime();
                String giorno= rs.getString("giorno");
                vincoli.add(new Object[]{giorno,orarioi.getHour(),orarioi.getMinute(),orariof.getHour(),orariof.getMinute()});
            }
            rs.close();
        }catch (SQLException e){
            throw new SQLException("Errore nel prendere i vincoli dal database: "+e.getMessage());
        }
        if(!haRighe){
            logger.info("Non ci sono vincoli per il docente");
        }
        return vincoli.toArray(new Object[0][]);

    }

    @Override
    public void rimuoviVincoloDB(String emailDocente, String giorno, int oraInzio, int minutoInzio, int oraFine, int minutoFine) throws SQLException {

        String sql = "DELETE FROM vincolo WHERE docentee = ? AND giorno ILIKE ? AND orarioi = ? AND orariof = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            LocalTime orarioInizio = LocalTime.of(oraInzio, minutoInzio);
            LocalTime orarioFine = LocalTime.of(oraFine, minutoFine);
            ps.setString(1, emailDocente);
            ps.setString(2,  giorno);
            ps.setObject(3, orarioInizio);
            ps.setObject(4, orarioFine);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessun vincolo trovato per la rimozione.");
            }
        } catch (SQLException e) {
            throw new SQLException( e.getMessage());
        }
    }

}
