package implementazionedao;

import dao.StudenteDAO;
import dao.UtenteDAO;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link StudenteDAO}.
 *
 * <p>Dopo l'unificazione delle tabelle in {@code utente}, questa classe
 * delega la lettura e la scrittura a {@link UtentePostgresDao} e filtra
 * i risultati con ruolo {@code "STUDENTE"}.</p>
 */
public class StudentePostgresDao implements StudenteDAO {
    /** Ruolo con cui gli studenti sono salvati nella tabella utente. */
    private static final String RUOLO = "STUDENTE";
    /** Prefisso delle matricole degli studenti. */
    private static final String PREFISSO_MATRICOLA = "DE";

    /** DAO della tabella unica utente a cui vengono delegate lettura e scrittura. */
    private final UtenteDAO utenteDao;

    /**
     * Nel costruttore si crea il DAO della tabella utente a cui delegare
     * le operazioni (che a sua volta ottiene la connessione dal singleton).
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public StudentePostgresDao() throws SQLException {
        utenteDao = new UtentePostgresDao();
    }

    /**
     * Salva lo studente nella tabella {@code utente} delegando a
     * {@link UtenteDAO#salvaUtenteDB}: dal prefisso {@code "DE"} della
     * matricola il database ricava il ruolo {@code "STUDENTE"}.
     *
     * @param nome      Nome di battesimo dello studente.
     * @param cognome   cognome di battesimo dello studente.
     * @param email     l'email con cui si registra lo studente al sistema.
     * @param login     username con cui accede lo studente al sistema.
     * @param password  password segreta dello studente per accedere.
     * @param matricola la matricola univoca dello studente.
     * @param annoCorso l'anno di corso (1-3).
     * @throws SQLException Sql Exception se la scrittura al DB fallisce.
     */
    @Override
    public void salvaStudenteDB(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) throws SQLException {
        utenteDao.salvaUtenteDB(nome, cognome, email, login, password, matricola, annoCorso);
    }

    /**
     * Recupera tutti gli studenti: legge tutti gli utenti tramite
     * {@link UtenteDAO#leggiUtentiDB} e filtra le righe con ruolo
     * {@code "STUDENTE"}, popolando in modo posizionale le liste fornite.
     *
     * @param nome      la lista in cui verranno aggiunti i nomi degli studenti recuperati.
     * @param cognome   la lista in cui verranno aggiunti i cognomi degli studenti recuperati.
     * @param email     la lista in cui verranno aggiunte le email degli studenti recuperati.
     * @param login     la lista in cui verranno aggiunti gli username degli studenti recuperati.
     * @param password  la lista in cui verranno aggiunte le password degli studenti recuperati.
     * @param matricola la lista in cui verranno aggiunte le matricole degli studenti recuperati.
     * @param annoCorso la lista in cui verranno aggiunti gli anni di corso degli studenti recuperati.
     * @throws SQLException se si verifica un errore SQL durante l'accesso al database o durante la lettura dei dati.
     */
    @Override
    public void leggiStudenteDB(ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> login, ArrayList<String> password, ArrayList<String> matricola, ArrayList<Integer> annoCorso) throws SQLException {
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
                matricola.add(matricole.get(i));
                Integer anno = anniCorso.get(i);
                annoCorso.add(anno != null ? anno : 1);
            }
        }
    }


}