package dao;

import java.util.ArrayList;

/**
 * Interfaccia DAO per la tabella {@code lezione}.
 *
 * <p>Pattern BCE + DAO: questo package contiene solo la descrizione dei metodi
 * di accesso ai dati persistenti. L'implementazione concreta per PostgreSQL si
 * trova in {@code implementazioneDao.LezionePostgresDao}.</p>
 *
 * <p>Nota: i metodi ricevono e restituiscono solo dati (String, int,
 * ArrayList), mai oggetti del Model. In questo modo il package di accesso al DB
 * non dipende dal package model. Le eccezioni del database vengono rilanciate
 * verso il Controller con {@code throws Exception}.</p>
 */

public interface LezioneDAO {

    /**
     * Salva nel database una lezione creata dal responsabile.
     * I parametri sono i dati grezzi della lezione (non oggetti del Model).
     *
     * @param nomeInsegnamento nome del corso/insegnamento
     * @param annoCorso        anno di corso
     * @param emailDocente     email del docente
     * @param nomeAula         nome dell'aula
     * @param giorno           giorno della settimana
     * @param oraInizio        ora di inizio
     * @param minutoInizio     minuto di inizio
     * @param oraFine          ora di fine
     * @param minutoFine       minuto di fine
     * @throws Exception se la scrittura sul database fallisce
     *                   (ad es. lezione già presente in quello slot/aula)
     */
    void salvaLezioneDB(String nomeInsegnamento, int annoCorso,
                        String emailDocente,
                        String nomeAula,
                        String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) throws Exception;

    /**
     * Legge dal database tutte le lezioni di un dato anno di corso (quelle che
     * compongono l'orario dello studente di quell'anno). I risultati vengono
     * inseriti nelle liste passate come parametro: a parità di indice, i valori
     * delle varie liste appartengono alla stessa lezione. Il Controller potrà
     * poi usarli per costruire gli oggetti Lezione in memoria (nel Model).
     *
     * @param annoCorso        anno di corso da filtrare
     * @param nomiInsegnamento lista in cui inserire i nomi degli insegnamenti
     * @param emailDocente     lista in cui inserire le email dei docenti
     * @param nomeAula         lista in cui inserire i nomi delle aule
     * @param giorno           lista in cui inserire i giorni
     * @param oraInizio        lista in cui inserire le ore di inizio
     * @param minutoInizio     lista in cui inserire i minuti di inizio
     * @param oraFine          lista in cui inserire le ore di fine
     * @param minutoFine       lista in cui inserire i minuti di fine
     * @throws Exception se la lettura dal database fallisce
     */
    void leggiLezioniDB(int annoCorso,
                        ArrayList<String> nomiInsegnamento,
                        ArrayList<String> emailDocente,
                        ArrayList<String> nomeAula,
                        ArrayList<String> giorno, ArrayList<Integer> oraInizio, ArrayList<Integer> minutoInizio,
                        ArrayList<Integer> oraFine, ArrayList<Integer> minutoFine) throws Exception;
    }