package implementazioneDao;

import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InsegnamentoPostgresDAO implements InsegnamentoDAO {
    private final Connection connection;


    public InsegnamentoPostgresDAO() throws Exception {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }




    @Override
    public Object[][] caricaInsegnamentiDB(){
        String sql="SELECT * FROM insegnamento";
        List<Object[]> insegnamenti= new ArrayList<>();
        boolean haRighe=false;
        try(PreparedStatement ps=connection.prepareStatement(sql)){

            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                haRighe=true;
                String nome= rs.getString("nomecorso");
                int cfu= rs.getInt("cfu");
                int annoCorso= rs.getInt("annocorso");
                insegnamenti.add(new Object[]{nome,cfu,annoCorso});
            }
            rs.close();
        }catch (SQLException e){
            System.out.println("Errore nel prendere gli insegnamenti dal database: "+e.getMessage());
        }
        if(!haRighe){
            System.out.println("Non ci sono insegnamenti nel database");
            return new Object[0][0];
        }
        return insegnamenti.toArray(new Object[0][]);
    }

    @Override
    public void salvaInsegnamento(String nome, int annoCorso, int cfu) throws Exception {
        // Implementazione per salvare il vincolo nel database PostgreSQL
        String sql = "INSERT INTO insegnamento (nomecorso,cfu,annocorso) " +
                "VALUES (?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,nome);
            ps.setInt(2,cfu);
            ps.setInt(3,annoCorso);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Impossibile caricare l'insegnamento nel DataBase: " + e.getMessage());
        }
    }


}
