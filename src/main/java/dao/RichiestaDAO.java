package dao;

import java.sql.SQLException;
import java.util.ArrayList;

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

    /**
     * Salva nel database una richiesta di spostamento inviata da un docente a un
     * responsabile. Lo stato non viene passato: sul DB assume il valore di
     * default {@code 'IN_ATTESA'}.
     *
     * @param emailDocente          email del docente richiedente (FK docente)
     * @param emailResponsabile     email del responsabile destinatario (FK responsabile)
     * @param motivo                motivo della richiesta
     * @param giornoIniziale        giorno della lezione da spostare
     * @param oraInizioIniziale     ora di inizio della lezione da spostare
     * @param minutoInizioIniziale  minuto di inizio della lezione da spostare
     * @param oraFineIniziale       ora di fine della lezione da spostare
     * @param minutoFineIniziale    minuto di fine della lezione da spostare
     * @param giornoProposto        giorno del nuovo orario proposto
     * @param oraInizioProposto     ora di inizio del nuovo orario proposto
     * @param minutoInizioProposto  minuto di inizio del nuovo orario proposto
     * @param oraFineProposto       ora di fine del nuovo orario proposto
     * @param minutoFineProposto    minuto di fine del nuovo orario proposto
     * @return l'id (chiave primaria) generato dal database per la richiesta salvata
     * @throws Exception se la scrittura sul database fallisce (ad es. FK
     *                   inesistente o violazione di un CHECK sugli orari)
     */
     int salvaRichiestaDB(String emailDocente, String emailResponsabile, String motivo,
                         String giornoIniziale, int oraInizioIniziale, int minutoInizioIniziale,
                         int oraFineIniziale, int minutoFineIniziale,
                         String giornoProposto, int oraInizioProposto, int minutoInizioProposto,
                         int oraFineProposto, int minutoFineProposto) throws SQLException;

    /**
     * Legge dal database tutte le richieste inviate da un dato docente. I
     * risultati vengono inseriti nelle liste passate come parametro: a parità di
     * indice, i valori delle varie liste appartengono alla stessa richiesta.
     *
     * @param emailDocente          email del docente di cui leggere le richieste
     * @param id                    lista in cui inserire gli id delle richieste
     * @param emailResponsabile     lista in cui inserire le email dei responsabili
     * @param motivo                lista in cui inserire i motivi
     * @param giornoIniziale        lista in cui inserire i giorni iniziali
     * @param oraInizioIniziale     lista in cui inserire le ore di inizio iniziali
     * @param minutoInizioIniziale  lista in cui inserire i minuti di inizio iniziali
     * @param oraFineIniziale       lista in cui inserire le ore di fine iniziali
     * @param minutoFineIniziale    lista in cui inserire i minuti di fine iniziali
     * @param giornoProposto        lista in cui inserire i giorni proposti
     * @param oraInizioProposto     lista in cui inserire le ore di inizio proposte
     * @param minutoInizioProposto  lista in cui inserire i minuti di inizio proposti
     * @param oraFineProposto       lista in cui inserire le ore di fine proposte
     * @param minutoFineProposto    lista in cui inserire i minuti di fine proposti
     * @param stato                 lista in cui inserire gli stati delle richieste
     * @throws Exception se la lettura dal database fallisce
     */
     void leggiRichiesteDocenteDB(String emailDocente,
                                 ArrayList<Integer> id,
                                 ArrayList<String> emailResponsabile,
                                 ArrayList<String> motivo,
                                 ArrayList<String> giornoIniziale,
                                 ArrayList<Integer> oraInizioIniziale, ArrayList<Integer> minutoInizioIniziale,
                                 ArrayList<Integer> oraFineIniziale, ArrayList<Integer> minutoFineIniziale,
                                 ArrayList<String> giornoProposto,
                                 ArrayList<Integer> oraInizioProposto, ArrayList<Integer> minutoInizioProposto,
                                  ArrayList<Integer> oraFineProposto, ArrayList<Integer> minutoFineProposto,
                                  ArrayList<String> stato) throws SQLException;



    /**
     * Legge dal database tutte le richieste ancora in stato {@code 'IN_ATTESA'},
     * indipendentemente dal responsabile destinatario. Serve a far sì che
     * qualsiasi responsabile loggato possa vedere e gestire tutte le richieste
     * pendenti. Le liste sono parallele per indice.
     *
     * @param id                    lista in cui inserire gli id delle richieste
     * @param emailDocente          lista in cui inserire le email dei docenti richiedenti
     * @param emailResponsabile     lista in cui inserire le email dei responsabili destinatari
     * @param motivo                lista in cui inserire i motivi
     * @param giornoIniziale        lista in cui inserire i giorni iniziali
     * @param oraInizioIniziale     lista in cui inserire le ore di inizio iniziali
     * @param minutoInizioIniziale  lista in cui inserire i minuti di inizio iniziali
     * @param oraFineIniziale       lista in cui inserire le ore di fine iniziali
     * @param minutoFineIniziale    lista in cui inserire i minuti di fine iniziali
     * @param giornoProposto        lista in cui inserire i giorni proposti
     * @param oraInizioProposto     lista in cui inserire le ore di inizio proposte
     * @param minutoInizioProposto  lista in cui inserire i minuti di inizio proposti
     * @param oraFineProposto       lista in cui inserire le ore di fine proposte
     * @param minutoFineProposto    lista in cui inserire i minuti di fine proposti
     * @throws Exception se la lettura dal database fallisce
     */
     void leggiRichiesteInAttesaDB(ArrayList<Integer> id,
                                  ArrayList<String> emailDocente,
                                  ArrayList<String> emailResponsabile,
                                  ArrayList<String> motivo,
                                  ArrayList<String> giornoIniziale,
                                  ArrayList<Integer> oraInizioIniziale, ArrayList<Integer> minutoInizioIniziale,
                                  ArrayList<Integer> oraFineIniziale, ArrayList<Integer> minutoFineIniziale,
                                  ArrayList<String> giornoProposto,
                                  ArrayList<Integer> oraInizioProposto, ArrayList<Integer> minutoInizioProposto,
                                   ArrayList<Integer> oraFineProposto, ArrayList<Integer> minutoFineProposto) throws SQLException;

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