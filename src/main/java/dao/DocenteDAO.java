package dao;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Interfaccia DAO per i docenti, memorizzati nella tabella unica
 * {@code utente} con ruolo {@code "DOCENTE"}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.DocentePostgresDao}, che delega a
 * {@code UtenteDAO} e filtra i risultati in base al ruolo.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. In questo modo il package di accesso al DB
 * non dipende dal package model. Le eccezioni del database vengono rilanciate
 * verso il Controller con {@code throws Exception}.</p>
 */
public interface DocenteDAO {

    /**Salva nel i dati grezzi(non gli oggetti) del docente nel database.
     *
     * @param  nome Nome di battesimo del docente.
     * @param cognome cognome di battesimo del docente.
     * @param email l'email con cui si registra il docente al sistema.
     * @param login login con cui accede il docente al sistema.
     * @param password password segreta del docente per accedere.
     */
    void salvaDocDB(String nome, String cognome, String email,
                    String login, String password)throws SQLException;

    /**Metodo da implementare in DocentePostgreDao per recuperare i dati di un docente registrato.
     *
     * @param  nome Nome di battesimo del docente.
     * @param cognome cognome di battesimo del docente.
     * @param email l'email con cui si registra il docente al sistema.
     * @param login login con cui accede il docente al sistema.
     * @param password password segreta del docente per accedere
     */
    void leggiDocenteDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email,
                        ArrayList<String> login, ArrayList<String> password)throws SQLException;

    /**Salva nel database l'associazione tra il docente e una materia che può insegnare.
     * <p>Viene invocato sia in fase di registrazione (quando il docente appena
     * registrato seleziona subito le sue materie) sia quando le aggiunge in un
     * secondo momento dalla sua schermata insegnamenti.</p>
     *
     * @param emailDocente l'email del docente a cui associare la materia.
     * @param nomeMateria il nome dell'insegnamento (materia) da associare.
     */
    void salvaMateriaDocenteDB(String emailDocente, String nomeMateria) throws SQLException;

    /**Rimuove dal database l'associazione tra il docente e una materia.
     *
     * @param emailDocente l'email del docente da cui rimuovere la materia.
     * @param nomeMateria il nome dell'insegnamento (materia) da rimuovere.
     */
    void rimuoviMateriaDocenteDB(String emailDocente, String nomeMateria) throws SQLException;

    /**Recupera dal database i nomi delle materie associate al docente,
     * così che restino visibili anche nelle sessioni successive.
     *
     * @param emailDocente l'email del docente di cui leggere le materie.
     * @param nomiMaterie lista (di output) riempita con i nomi delle materie del docente.
     */
    void leggiMaterieDocenteDB(String emailDocente, ArrayList<String> nomiMaterie) throws SQLException;
}