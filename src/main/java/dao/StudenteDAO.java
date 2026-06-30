package dao;

import java.util.ArrayList;

public interface StudenteDAO {
    /**Salva nel i dati grezzi(non gli oggetti) del Responsabile nel database.
     *
     * @param  nome Nome di battesimo del Responsabile.
     * @param cognome cognome di battesimo del Responsabile.
     * @param email l'email del Responsabile.
     * @param login username con cui accede il Responsabile al sistema.
     * @param password password segreta del Responsabile per accedere.
     */
    void salvaResponsabileDB(String nome, String cognome, String email,
                             String login, String password,)throws Exception;

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


