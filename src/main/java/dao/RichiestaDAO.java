package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la tabella {@code richiesta}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.RichiestaPostgresDao}.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. In questo modo il package di accesso al DB
 * non dipende dal package model. Gli orari, memorizzati sul DB come colonne di
 * tipo {@code time}, vengono qui scomposti in ora e minuto (int), coerentemente
 * con la classe {@code model.Orario}. Le eccezioni del database vengono
 * rilanciate verso il Controller con {@code throws Exception}.</p>
 */
public interface RichiestaDAO {

    // Indici dei valori nella lista passata a salvaRichiestaDB.
    int EMAIL_DOCENTE = 0;
    int EMAIL_RESPONSABILE = 1;
    int MOTIVO = 2;
    int GIORNO_INIZIALE = 3;
    int ORA_INIZIO_INIZIALE = 4;
    int MINUTO_INIZIO_INIZIALE = 5;
    int ORA_FINE_INIZIALE = 6;
    int MINUTO_FINE_INIZIALE = 7;
    int GIORNO_PROPOSTO = 8;
    int ORA_INIZIO_PROPOSTO = 9;
    int MINUTO_INIZIO_PROPOSTO = 10;
    int ORA_FINE_PROPOSTO = 11;
    int MINUTO_FINE_PROPOSTO = 12;

    // Indici dei valori negli array String[] riempiti da leggiRichiesteDocenteDB.
    int TESTO_EMAIL_RESPONSABILE = 0;
    int TESTO_MOTIVO = 1;
    int TESTO_GIORNO_INIZIALE = 2;
    int TESTO_GIORNO_PROPOSTO = 3;
    int TESTO_STATO = 4;


    // Indici dei valori negli array String[] riempiti da leggiRichiesteInAttesaDB.
    int ATTESA_EMAIL_DOCENTE = 0;
    int ATTESA_EMAIL_RESPONSABILE = 1;
    int ATTESA_MOTIVO = 2;
    int ATTESA_GIORNO_INIZIALE = 3;
    int ATTESA_GIORNO_PROPOSTO = 4;
    /**
     * Salva nel database una richiesta di spostamento inviata da un docente a un
     * responsabile. Lo stato non viene passato: sul DB assume il valore di
     * default {@code 'IN_ATTESA'}.
     *
     * <p>I dati della richiesta viaggiano in un'unica lista di {@link Object},
     * i cui elementi devono rispettare l'ordine definito dalle costanti di
     * questa interfaccia ({@link #EMAIL_DOCENTE} ... {@link #MINUTO_FINE_PROPOSTO}):
     * email del docente (String), email del responsabile (String), motivo (String),
     * giorno iniziale (String), ora/minuto di inizio e fine iniziali (Integer),
     * giorno proposto (String), ora/minuto di inizio e fine proposti (Integer).</p>
     *
     * @param datiRichiesta lista dei 13 valori della richiesta, nell'ordine
     *                      indicato dalle costanti di indice
     * @return l'id (chiave primaria) generato dal database per la richiesta salvata
     * @throws SQLException se la scrittura sul database fallisce (ad es. FK
     * inesistente o violazione di un CHECK sugli orari)
     */
    int salvaRichiestaDB(List<Object> datiRichiesta) throws SQLException;

    /**
     * Legge dal database tutte le richieste inviate da un dato docente. I
     * risultati vengono inseriti nelle liste passate come parametro: a parità di
     * indice, i valori delle varie liste appartengono alla stessa richiesta.
     *
     * @param emailDocente          email del docente di cui leggere le richieste
     * @param id                    lista in cui inserire gli id delle richieste
     * @param datiTesto             lista in cui inserire i campi testuali; ogni
     *                              elemento è un array il cui ordine è definito
     *                              dalle costanti {@link #TESTO_EMAIL_RESPONSABILE},
     *                              {@link #TESTO_MOTIVO}, {@link #TESTO_GIORNO_INIZIALE},
     *                              {@link #TESTO_GIORNO_PROPOSTO}, {@link #TESTO_STATO}
     * @param orarioIniziale        lista in cui inserire gli orari iniziali; ogni
     *                              elemento è un array {@code [oraInizio,
     *                              minutoInizio, oraFine, minutoFine]}
     * @param orarioProposto        lista in cui inserire gli orari proposti, nello
     *                              stesso formato di {@code orarioIniziale}
     * @throws Exception se la lettura dal database fallisce
     */
    void leggiRichiesteDocenteDB(String emailDocente,
                                 List<Integer> id,
                                 List<String[]> datiTesto,
                                 List<int[]> orarioIniziale,
                                 List<int[]> orarioProposto) throws SQLException;



    /**
     * Legge dal database tutte le richieste ancora in stato {@code 'IN_ATTESA'},
     * indipendentemente dal responsabile destinatario. Serve a far sì che
     * qualsiasi responsabile loggato possa vedere e gestire tutte le richieste
     * pendenti. Le liste sono parallele per indice.
     * @param id                    lista in cui inserire gli id delle richieste
     * @throws Exception se la lettura dal database fallisce
     */
    public void leggiRichiesteInAttesaDB(List<Integer> id,
                                         List<String[]> datiTesto,
                                         List<int[]> orarioIniziale,
                                         List<int[]> orarioProposto) throws SQLException;

    /**
     * Aggiorna lo stato di una richiesta (tipicamente quando il responsabile la
     * approva o la rifiuta).
     *
     * @param idRichiesta id della richiesta da aggiornare
     * @param nuovoStato  nuovo stato: {@code 'IN_ATTESA'}, {@code 'APPROVATA'} o {@code 'RIFIUTATA'}
     * @throws Exception se l'aggiornamento fallisce (ad es. stato non ammesso dal CHECK)
     */
    void aggiornaStatoRichiestaDB(int idRichiesta, String nuovoStato) throws SQLException;

    /**
     * Aggiorna l'orario proposto di una richiesta ancora in attesa (usato quando
     * il responsabile propone una fascia diversa da quella richiesta dal docente).
     *
     * @param idRichiesta          id della richiesta da aggiornare
     * @param giornoProposto       nuovo giorno proposto
     * @param oraInizioProposto    nuova ora di inizio proposta
     * @param minutoInizioProposto nuovo minuto di inizio proposto
     * @param oraFineProposto      nuova ora di fine proposta
     * @param minutoFineProposto   nuovo minuto di fine proposto
     * @throws Exception se l'aggiornamento fallisce
     */
    void aggiornaOrarioPropostoDB(int idRichiesta, String giornoProposto,
                                  int oraInizioProposto, int minutoInizioProposto,
                                  int oraFineProposto, int minutoFineProposto) throws SQLException;
}