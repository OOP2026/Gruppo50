package dao;

import java.sql.SQLException;
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
     * @param nomeInsegnamento nome del corso/insegnamento
     * @param annoCorso anno di corso della lezione.
     * @param emailDocente email del docente.
     * @param nomeAula nome dell'aula.
     * @param giorno giorno della settimana.
     * @param orarioIn  arraylist che contiene l'orario di inizio e di fine della lezione{@code [oraInizio, minutoInizio, oraFine, minutoFine]}.
     * @throws SQLException se la scrittura sul database fallisce.
     * (ad es. lezione già presente in quello slot/aula)
     */
    void salvaLezioneDB(String nomeInsegnamento, int annoCorso,
                        String emailDocente,
                        String nomeAula,
                        String giorno, int [] orarioIn) throws SQLException;



    /**
     * Legge dal database tutte le lezioni presenti, senza filtrare per anno di
     * corso. Serve al Controller per ricostruire in memoria l'intero orario
     * (pattern BCE + DAO) al momento del login, così che ogni docente possa
     * vedere le lezioni salvate in sessioni precedenti.Restituisce anche l'anno di corso di ciascuna
     * lezione, necessario per ricostruire l'insegnamento. I risultati vengono
     * inseriti nelle liste passate come parametro: a parità di indice, i valori
     * delle varie liste appartengono alla stessa lezione.
     *
     * @param nomiInsegnamento lista in cui inserire i nomi degli insegnamenti
     * @param annoCorso        lista in cui inserire gli anni di corso
     * @param emailDocente     lista in cui inserire le email dei docenti
     * @param nomeAula         lista in cui inserire i nomi delle aule
     * @param giorno           lista in cui inserire i giorni
     * @param orario           lista in cui inserire gli orari; ogni elemento è un
     *                         array {@code [oraInizio, minutoInizio, oraFine, minutoFine]},
     *                         nello stesso formato di {@code orarioIn} in
     *                         {@link #salvaLezioneDB}.
     * @throws SQLException se la lettura dal database fallisce.
     */
    void leggiTutteLezioniDB(ArrayList<String> nomiInsegnamento,
                             ArrayList<Integer> annoCorso,
                             ArrayList<String> emailDocente,
                             ArrayList<String> nomeAula,
                             ArrayList<String> giorno,
                             ArrayList<int[]> orario) throws SQLException;
}