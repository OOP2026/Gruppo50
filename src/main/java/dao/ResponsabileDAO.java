package dao;
import java.util.ArrayList;
/**
 * Interfaccia DAO per la tabella {@code Responsabile}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.ResponsabilePostgresDao}.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. In questo modo il package di accesso al DB
 * non dipende dal package model. Le eccezioni del database vengono rilanciate
 * verso il Controller con {@code throws Exception}.</p>
 */
public interface ResponsabileDAO {
    /**Salva nel i dati grezzi(non gli oggetti) del Responsabile nel database.
     *
     * @param  nome Nome di battesimo del Responsabile.
     * @param cognome cognome di battesimo del Responsabile.
     * @param email l'email del Responsabile.
     * @param login username con cui accede il Responsabile al sistema.
     * @param password password segreta del Responsabile per accedere.
     */
    void salvaResponsabileDB(String nome, String cognome, String email,
                    String login, String password)throws Exception;

    /**Metodo da implementare in ResponsabilePostgreDao per recuperare i dati di un Responsabile registrato.
     *
     * @param  nome Nome di battesimo del Responsabile.
     * @param cognome cognome di battesimo del Responsabile.
     * @param email l'email del Responsabile.
     * @param login lusername con cui accede il Responsabile al sistema.
     * @param password password segreta del Responsabile per accedere.
     */
    void leggiResponsabileDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email,
                        ArrayList<String> login, ArrayList<String> password)throws Exception;

}
