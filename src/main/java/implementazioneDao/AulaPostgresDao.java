package implementazioneDao;


import dao.AulaDAO;
import database_connection.ConnessioneDatabase;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AulaPostgresDao implements AulaDAO {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(AulaPostgresDao.class.getName());

    public AulaPostgresDao() throws Exception {
        this.connection = ConnessioneDatabase.getInstance().getConnection();
    }

    @Override
    public void salvaAulaDB(String nome, int capienza) throws Exception {
        String sql = "INSERT INTO aula (nome,capienza) " +
                "VALUES (?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, capienza);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Impossibile salvare salvare l'aula nel DataBase: " + e.getMessage());
        }
    }

    @Override
    public Object[][] caricaAulaDB() {
        String sql="SELECT * FROM aula";
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
            logger.info("Errore nel prendere le aule dal database: "+e.getMessage());
        }
if(!haRighe){
    return new Object[0][0];
}
        return aule.toArray(new Object[0][]);
    }
}
