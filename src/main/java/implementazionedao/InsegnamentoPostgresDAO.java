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

public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(InsegnamentoPostgresDAO.class.getName());


    public InsegnamentoPostgresDAO() throws SQLException {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }




    @Override
    public Object[][] caricaInsegnamentiDB(){
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
            logger.info("Errore nel prendere gli insegnamenti dal database: "+e.getMessage());
        }
        if(!haRighe){
            logger.info("Non ci sono insegnamenti nel database");
            return new Object[0][0];
        }
        return insegnamenti.toArray(new Object[0][]);
    }

    @Override
    public void salvaInsegnamento(String nome, int annoCorso, int cfu, String emailDocente) throws SQLException {
        // Implementazione per salvare il vincolo nel database PostgreSQL
        String sql = "INSERT INTO insegnamento (nomecorso,cfu,annocorso,emaildoc) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,nome);
            ps.setInt(2,cfu);
            ps.setInt(3,annoCorso);
            ps.setString(4,emailDocente);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile caricare l'insegnamento nel DataBase: " + e.getMessage());
        }
    }


}
