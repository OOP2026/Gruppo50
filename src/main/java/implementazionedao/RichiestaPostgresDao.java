package implementazionedao;

import dao.RichiestaDAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link RichiestaDAO}.
 *
 * <p>Realizza la persistenza delle richieste di spostamento sulla tabella
 * {@code richiesta}. La connessione è ottenuta dal singleton
 * {@link ConnessioneDatabase}; le query parametriche usano
 * {@link PreparedStatement} con i segnaposto {@code ?}; le {@link SQLException}
 * vengono rilanciate come {@link Exception} con un messaggio leggibile, così il
 * Controller può gestirle e mostrarle alla GUI.</p>
 *
 * <p>Sul database gli orari sono memorizzati in colonne di tipo
 * {@code time without time zone} (una colonna per l'inizio e una per la fine,
 * sia della fascia iniziale sia di quella proposta). L'interfaccia invece
 * scompone ogni orario in ora e minuto (int): la conversione tra i due formati
 * avviene tramite {@link LocalTime}, mappato dal driver JDBC PostgreSQL sul tipo
 * {@code time}. I secondi non sono significativi e valgono sempre {@code 0}.</p>
 */
public class RichiestaPostgresDao implements RichiestaDAO {

    /** Connessione JDBC verso il database PostgreSQL. */
    private final Connection connection;

    /**
     * Nel costruttore si ottiene la connessione dal singleton.
     *
     * @throws SQLException se la connessione al database fallisce
     */
    public RichiestaPostgresDao() throws SQLException {
        connection = ConnessioneDatabase.getInstance().getConnection();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Esegue un {@code INSERT} sulla tabella {@code richiesta} senza
     * specificare né {@code id} (generato dalla sequenza) né {@code stato} (che
     * assume il valore di default {@code 'IN_ATTESA'}). Ora e minuto di ciascun
     * estremo vengono ricomposti in un {@link LocalTime} prima di essere scritti
     * nelle colonne {@code time}. La clausola {@code RETURNING id} restituisce la
     * chiave primaria generata, che viene letta dal {@link ResultSet} e
     * restituita al chiamante.</p>
     */
    @Override
    public int salvaRichiestaDB(List<Object> datiRichiesta) throws SQLException {
        String sql = "INSERT INTO richiesta " +
                "(email_docente, email_responsabile, motivo, " +
                " giorno_iniziale, ora_inizio_iniziale, ora_fine_iniziale, " +
                " giorno_proposto, ora_inizio_proposto, ora_fine_proposto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, (String) datiRichiesta.get(EMAIL_DOCENTE));
            ps.setString(2, (String) datiRichiesta.get(EMAIL_RESPONSABILE));
            ps.setString(3, (String) datiRichiesta.get(MOTIVO));
            ps.setString(4, (String) datiRichiesta.get(GIORNO_INIZIALE));
            ps.setObject(5, LocalTime.of((Integer) datiRichiesta.get(ORA_INIZIO_INIZIALE),
                    (Integer) datiRichiesta.get(MINUTO_INIZIO_INIZIALE)));
            ps.setObject(6, LocalTime.of((Integer) datiRichiesta.get(ORA_FINE_INIZIALE),
                    (Integer) datiRichiesta.get(MINUTO_FINE_INIZIALE)));
            ps.setString(7, (String) datiRichiesta.get(GIORNO_PROPOSTO));
            ps.setObject(8, LocalTime.of((Integer) datiRichiesta.get(ORA_INIZIO_PROPOSTO),
                    (Integer) datiRichiesta.get(MINUTO_INIZIO_PROPOSTO)));
            ps.setObject(9, LocalTime.of((Integer) datiRichiesta.get(ORA_FINE_PROPOSTO),
                    (Integer) datiRichiesta.get(MINUTO_FINE_PROPOSTO)));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            // Es. FK inesistente (docente/responsabile) o violazione di un CHECK sugli orari.
            throw new SQLException("Impossibile salvare la richiesta sul database: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Seleziona tutte le righe della tabella {@code richiesta} il cui
     * {@code email_docente} coincide con quello passato, ordinate per {@code id}.
     * Per ogni riga i valori vengono aggiunti in coda alle liste parallele: i
     * campi testuali in un {@code String[]} ordinato secondo le costanti
     * {@code TESTO_*} dell'interfaccia, le coppie di colonne {@code time}
     * (inizio e fine) in un array {@code [oraInizio, minutoInizio, oraFine,
     * minutoFine]} tramite {@link #leggiOrario}.</p>
     */
    @Override
    public void leggiRichiesteDocenteDB(String emailDocente,
                                        ArrayList<Integer> id,
                                        ArrayList<String[]> datiTesto,
                                        ArrayList<int[]> orarioIniziale,
                                        ArrayList<int[]> orarioProposto) throws SQLException {
        String sql = "SELECT id, email_responsabile, motivo, " +
                "giorno_iniziale, ora_inizio_iniziale, ora_fine_iniziale, " +
                "giorno_proposto, ora_inizio_proposto, ora_fine_proposto, stato " +
                "FROM richiesta WHERE email_docente = ? ORDER BY id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, emailDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    id.add(rs.getInt("id"));
                    String[] testo = new String[5];
                    testo[TESTO_EMAIL_RESPONSABILE] = rs.getString("email_responsabile");
                    testo[TESTO_MOTIVO] = rs.getString("motivo");
                    testo[TESTO_GIORNO_INIZIALE] = rs.getString("giorno_iniziale");
                    testo[TESTO_GIORNO_PROPOSTO] = rs.getString("giorno_proposto");
                    testo[TESTO_STATO] = rs.getString("stato");
                    datiTesto.add(testo);
                    orarioIniziale.add(leggiOrario(rs, "ora_inizio_iniziale", "ora_fine_iniziale"));
                    orarioProposto.add(leggiOrario(rs, "ora_inizio_proposto", "ora_fine_proposto"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Impossibile leggere le richieste del docente dal database: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Seleziona tutte le righe ancora in stato {@code 'IN_ATTESA'},
     * indipendentemente dal responsabile destinatario, ordinate per {@code id}.
     * Lo stato non viene restituito perché è implicitamente {@code 'IN_ATTESA'}
     * per tutte le righe estratte.</p>
     */
    @Override
    public void leggiRichiesteInAttesaDB(ArrayList<Integer> id,
                                         ArrayList<String> emailDocente,
                                         ArrayList<String> emailResponsabile,
                                         ArrayList<String> motivo,
                                         ArrayList<String> giornoIniziale,
                                         ArrayList<Integer> oraInizioIniziale, ArrayList<Integer> minutoInizioIniziale,
                                         ArrayList<Integer> oraFineIniziale, ArrayList<Integer> minutoFineIniziale,
                                         ArrayList<String> giornoProposto,
                                         ArrayList<Integer> oraInizioProposto, ArrayList<Integer> minutoInizioProposto,
                                         ArrayList<Integer> oraFineProposto, ArrayList<Integer> minutoFineProposto) throws SQLException {
        String sql = "SELECT id, email_docente, email_responsabile, motivo, " +
                "giorno_iniziale, ora_inizio_iniziale, ora_fine_iniziale, " +
                "giorno_proposto, ora_inizio_proposto, ora_fine_proposto " +
                "FROM richiesta WHERE stato = 'IN_ATTESA' ORDER BY id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    id.add(rs.getInt("id"));
                    emailDocente.add(rs.getString("email_docente"));
                    emailResponsabile.add(rs.getString("email_responsabile"));
                    motivo.add(rs.getString("motivo"));
                    giornoIniziale.add(rs.getString("giorno_iniziale"));
                    aggiungiOra(rs, "ora_inizio_iniziale", oraInizioIniziale, minutoInizioIniziale);
                    aggiungiOra(rs, "ora_fine_iniziale", oraFineIniziale, minutoFineIniziale);
                    giornoProposto.add(rs.getString("giorno_proposto"));
                    aggiungiOra(rs, "ora_inizio_proposto", oraInizioProposto, minutoInizioProposto);
                    aggiungiOra(rs, "ora_fine_proposto", oraFineProposto, minutoFineProposto);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Impossibile leggere le richieste in attesa dal database: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Esegue un {@code UPDATE} della colonna {@code stato} sulla riga
     * identificata da {@code idRichiesta}. Il valore ammesso è controllato dal
     * vincolo {@code richiesta_stato_check} sul database.</p>
     *
     * <p>Se il nuovo stato è {@code 'APPROVATA'}, nella stessa transazione viene
     * anche spostata la lezione esistente: la riga della tabella {@code lezione}
     * che coincide con docente, giorno e orario iniziali della richiesta viene
     * aggiornata al giorno e all'orario proposti. Se la lezione non viene
     * trovata la transazione è annullata e lo stato non cambia.</p>
     */
    @Override
    public void aggiornaStatoRichiestaDB(int idRichiesta, String nuovoStato) throws SQLException {
        String sqlStato = "UPDATE richiesta SET stato = ? WHERE id = ?";
        // Sposta la lezione individuata dai dati iniziali della richiesta
        // (docente + giorno + orario) sul giorno/orario proposti. Le colonne
        // time della richiesta vengono scomposte in ora e minuto con EXTRACT,
        // perché lezione memorizza ore e minuti come interi separati.
        String sqlLezione = "UPDATE lezione l SET " +
                "giorno = r.giorno_proposto, " +
                "orainizio = EXTRACT(HOUR FROM r.ora_inizio_proposto), " +
                "minutoinizio = EXTRACT(MINUTE FROM r.ora_inizio_proposto), " +
                "orafine = EXTRACT(HOUR FROM r.ora_fine_proposto), " +
                "minutofine = EXTRACT(MINUTE FROM r.ora_fine_proposto) " +
                "FROM richiesta r WHERE r.id = ? " +
                "AND l.emaildocente = r.email_docente " +
                "AND l.giorno = r.giorno_iniziale " +
                "AND l.orainizio = EXTRACT(HOUR FROM r.ora_inizio_iniziale) " +
                "AND l.minutoinizio = EXTRACT(MINUTE FROM r.ora_inizio_iniziale) " +
                "AND l.orafine = EXTRACT(HOUR FROM r.ora_fine_iniziale) " +
                "AND l.minutofine = EXTRACT(MINUTE FROM r.ora_fine_iniziale)";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(sqlStato)) {
                ps.setString(1, nuovoStato);
                ps.setInt(2, idRichiesta);
                ps.executeUpdate();
            }

            if ("APPROVATA".equals(nuovoStato)) {
                try (PreparedStatement ps = connection.prepareStatement(sqlLezione)) {
                    ps.setInt(1, idRichiesta);
                    int righe = ps.executeUpdate();
                    if (righe == 0) {
                        throw new SQLException("nessuna lezione corrispondente all'orario iniziale della richiesta");
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            // Es. violazione del CHECK sullo stato, lezione non trovata o
            // conflitto rilevato dal trigger sul nuovo orario: si annulla tutto.
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // il rollback fallisce solo se la connessione è già compromessa
            }
            throw new SQLException("Impossibile aggiornare lo stato della richiesta sul database: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // ripristino best-effort della modalità autocommit
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Esegue un {@code UPDATE} del giorno e degli orari proposti sulla riga
     * identificata da {@code idRichiesta}. Ora e minuto vengono ricomposti in un
     * {@link LocalTime} prima di essere scritti nelle colonne {@code time}.</p>
     */
    @Override
    public void aggiornaOrarioPropostoDB(int idRichiesta, String giornoProposto,
                                         int oraInizioProposto, int minutoInizioProposto,
                                         int oraFineProposto, int minutoFineProposto) throws SQLException {
        String sql = "UPDATE richiesta SET giorno_proposto = ?, " +
                "ora_inizio_proposto = ?, ora_fine_proposto = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, giornoProposto);
            ps.setObject(2, LocalTime.of(oraInizioProposto, minutoInizioProposto));
            ps.setObject(3, LocalTime.of(oraFineProposto, minutoFineProposto));
            ps.setInt(4, idRichiesta);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Es. violazione del CHECK sugli orari proposti o del CHECK "proposto != iniziale".
            throw new SQLException("Impossibile aggiornare l'orario proposto della richiesta sul database: " + e.getMessage());
        }
    }

    /**
     * Legge una colonna di tipo {@code time} dalla riga corrente del
     * {@link ResultSet} e ne aggiunge ora e minuto, come interi separati, in
     * coda alle due liste passate. Metodo di utilità usato dai metodi di lettura
     * per evitare duplicazione nella conversione {@code time} → (ora, minuto).
     *
     * @param rs      result set posizionato sulla riga da leggere
     * @param colonna nome della colonna {@code time} da estrarre
     * @param ore     lista in cui inserire l'ora (0-23)
     * @param minuti  lista in cui inserire il minuto (0-59)
     * @throws SQLException se la lettura della colonna fallisce
     */
    private void aggiungiOra(ResultSet rs, String colonna,
                             ArrayList<Integer> ore, ArrayList<Integer> minuti) throws SQLException {
        LocalTime orario = rs.getObject(colonna, LocalTime.class);
        ore.add(orario.getHour());
        minuti.add(orario.getMinute());
    }
    /**
     * Legge due colonne di tipo {@code time} (inizio e fine) dalla riga corrente
     * del {@link ResultSet} e le converte in un array
     * {@code [oraInizio, minutoInizio, oraFine, minutoFine]}.
     *
     * @param rs         result set posizionato sulla riga da leggere
     * @param colInizio  nome della colonna {@code time} di inizio
     * @param colFine    nome della colonna {@code time} di fine
     * @return array di 4 interi: ora e minuto di inizio, ora e minuto di fine
     * @throws SQLException se la lettura delle colonne fallisce
     */
    private int[] leggiOrario(ResultSet rs, String colInizio, String colFine) throws SQLException {
        LocalTime inizio = rs.getObject(colInizio, LocalTime.class);
        LocalTime fine = rs.getObject(colFine, LocalTime.class);
        return new int[]{inizio.getHour(), inizio.getMinute(), fine.getHour(), fine.getMinute()};
    }
}