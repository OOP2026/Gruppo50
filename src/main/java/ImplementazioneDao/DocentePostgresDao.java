package ImplementazioneDao;
import dao.DocenteDAO;
import dao.UtenteDAO;

import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link DocenteDAO}.
 *
 * <p>Dopo l'unificazione delle tabelle in {@code utente}, questa classe
 * delega la lettura e la scrittura a {@link UtentePostgresDao} e filtra
 * i risultati con ruolo {@code "DOCENTE"}. Alla registrazione viene
 * generata e salvata una matricola con prefisso {@code "DA"}, ma per il
 * momento il docente non la visualizza a schermo.</p>
 */
public class DocentePostgresDao implements DocenteDAO {

    /** Ruolo con cui i docenti sono salvati nella tabella utente. */
    private static final String RUOLO = "DOCENTE";
    /** Prefisso delle matricole dei docenti. */
    private static final String PREFISSO_MATRICOLA = "DA";

    /** DAO della tabella unica utente a cui vengono delegate lettura e scrittura. */
    private final UtenteDAO utenteDao;

    /**
     * Nel costruttore si crea il DAO della tabella utente a cui delegare
     * le operazioni (che a sua volta ottiene la connessione dal singleton).
     *
     * @throws Exception se la connessione al database fallisce
     */
    public DocentePostgresDao() throws Exception {
        utenteDao = new UtentePostgresDao();
    }

    /**
     * Salva il docente nella tabella {@code utente}. La matricola viene
     * generata automaticamente con prefisso {@code "DA"}, da cui il database
     * ricava il ruolo {@code "DOCENTE"} (per il momento la matricola non è
     * visualizzata a schermo); l'anno di corso viene salvato a {@code NULL}.
     *
     * @param nome     Nome di battesimo del docente.
     * @param cognome  cognome di battesimo del docente.
     * @param email    l'email con cui si registra il docente al sistema.
     * @param login    login con cui accede il docente al sistema.
     * @param password password segreta del docente per accedere.
     * @throws Exception quando si inseriscono dati sbagliati.
     */
    @Override
    public void salvaDocDB(String nome, String cognome, String email, String login, String password) throws Exception {
        String matricola = utenteDao.generaMatricolaDB(PREFISSO_MATRICOLA);
        utenteDao.salvaUtenteDB(nome, cognome, email, login, password, matricola, null);
    }

    /**
     * Recupera tutti i docenti: legge tutti gli utenti tramite
     * {@link UtenteDAO#leggiUtentiDB} e filtra le righe con ruolo {@code "DOCENTE"}.
     *
     * @param nome la lista dei nomi dei docenti presenti nel db.
     * @param cognome la lista dei  cognomi dei docenti presenti nel db.
     * @param email la lista delle email dei docenti presenti nel db.
     * @param login la lista degli username dei docenti presenti nel db.
     * @param password la lista delle password dei docenti presenti nel db.
     * @throws Exception quando la lettura nel database fallisce.
     */
    @Override
    public void leggiDocenteDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> login, ArrayList<String> password) throws Exception {
        ArrayList<String> nomi = new ArrayList<>();
        ArrayList<String> cognomi = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<String> logins = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();
        ArrayList<String> matricole = new ArrayList<>();
        ArrayList<Integer> anniCorso = new ArrayList<>();
        ArrayList<String> ruoli = new ArrayList<>();
        utenteDao.leggiUtentiDB(nomi, cognomi, emails, logins, passwords, matricole, anniCorso, ruoli);

        for (int i = 0; i < ruoli.size(); i++) {
            if (RUOLO.equalsIgnoreCase(ruoli.get(i))) {
                nome.add(nomi.get(i));
                cognome.add(cognomi.get(i));
                email.add(emails.get(i));
                login.add(logins.get(i));
                password.add(passwords.get(i));
                // matricola e annoCorso vengono ignorati: per ora il docente non li visualizza.
            }
        }
    }
}
