package implementazioneDao;

import dao.RichiestaDAO;

import java.util.ArrayList;

public class RichiestaPostgresDao implements RichiestaDAO {
    /**
     * @param emailDocente         email del docente richiedente (FK docente)
     * @param emailResponsabile    email del responsabile destinatario (FK responsabile)
     * @param motivo               motivo della richiesta
     * @param giornoIniziale       giorno della lezione da spostare
     * @param oraInizioIniziale    ora di inizio della lezione da spostare
     * @param minutoInizioIniziale minuto di inizio della lezione da spostare
     * @param oraFineIniziale      ora di fine della lezione da spostare
     * @param minutoFineIniziale   minuto di fine della lezione da spostare
     * @param giornoProposto       giorno del nuovo orario proposto
     * @param oraInizioProposto    ora di inizio del nuovo orario proposto
     * @param minutoInizioProposto minuto di inizio del nuovo orario proposto
     * @param oraFineProposto      ora di fine del nuovo orario proposto
     * @param minutoFineProposto   minuto di fine del nuovo orario proposto
     * @return
     * @throws Exception
     */
    @Override
    public int salvaRichiestaDB(String emailDocente, String emailResponsabile, String motivo, String giornoIniziale, int oraInizioIniziale, int minutoInizioIniziale, int oraFineIniziale, int minutoFineIniziale, String giornoProposto, int oraInizioProposto, int minutoInizioProposto, int oraFineProposto, int minutoFineProposto) throws Exception {
        return 0;
    }

    /**
     * @param emailDocente         email del docente di cui leggere le richieste
     * @param id                   lista in cui inserire gli id delle richieste
     * @param emailResponsabile    lista in cui inserire le email dei responsabili
     * @param motivo               lista in cui inserire i motivi
     * @param giornoIniziale       lista in cui inserire i giorni iniziali
     * @param oraInizioIniziale    lista in cui inserire le ore di inizio iniziali
     * @param minutoInizioIniziale lista in cui inserire i minuti di inizio iniziali
     * @param oraFineIniziale      lista in cui inserire le ore di fine iniziali
     * @param minutoFineIniziale   lista in cui inserire i minuti di fine iniziali
     * @param giornoProposto       lista in cui inserire i giorni proposti
     * @param oraInizioProposto    lista in cui inserire le ore di inizio proposte
     * @param minutoInizioProposto lista in cui inserire i minuti di inizio proposti
     * @param oraFineProposto      lista in cui inserire le ore di fine proposte
     * @param minutoFineProposto   lista in cui inserire i minuti di fine proposti
     * @param stato                lista in cui inserire gli stati delle richieste
     * @throws Exception
     */
    @Override
    public void leggiRichiesteDocenteDB(String emailDocente, ArrayList<Integer> id, ArrayList<String> emailResponsabile, ArrayList<String> motivo, ArrayList<String> giornoIniziale, ArrayList<Integer> oraInizioIniziale, ArrayList<Integer> minutoInizioIniziale, ArrayList<Integer> oraFineIniziale, ArrayList<Integer> minutoFineIniziale, ArrayList<String> giornoProposto, ArrayList<Integer> oraInizioProposto, ArrayList<Integer> minutoInizioProposto, ArrayList<Integer> oraFineProposto, ArrayList<Integer> minutoFineProposto, ArrayList<String> stato) throws Exception {

    }

    /**
     * @param emailResponsabile    email del responsabile di cui leggere le richieste
     * @param id                   lista in cui inserire gli id delle richieste
     * @param emailDocente         lista in cui inserire le email dei docenti richiedenti
     * @param motivo               lista in cui inserire i motivi
     * @param giornoIniziale       lista in cui inserire i giorni iniziali
     * @param oraInizioIniziale    lista in cui inserire le ore di inizio iniziali
     * @param minutoInizioIniziale lista in cui inserire i minuti di inizio iniziali
     * @param oraFineIniziale      lista in cui inserire le ore di fine iniziali
     * @param minutoFineIniziale   lista in cui inserire i minuti di fine iniziali
     * @param giornoProposto       lista in cui inserire i giorni proposti
     * @param oraInizioProposto    lista in cui inserire le ore di inizio proposte
     * @param minutoInizioProposto lista in cui inserire i minuti di inizio proposti
     * @param oraFineProposto      lista in cui inserire le ore di fine proposte
     * @param minutoFineProposto   lista in cui inserire i minuti di fine proposti
     * @param stato                lista in cui inserire gli stati delle richieste
     * @throws Exception
     */


    /**
     * @param id                   lista in cui inserire gli id delle richieste
     * @param emailDocente         lista in cui inserire le email dei docenti richiedenti
     * @param emailResponsabile    lista in cui inserire le email dei responsabili destinatari
     * @param motivo               lista in cui inserire i motivi
     * @param giornoIniziale       lista in cui inserire i giorni iniziali
     * @param oraInizioIniziale    lista in cui inserire le ore di inizio iniziali
     * @param minutoInizioIniziale lista in cui inserire i minuti di inizio iniziali
     * @param oraFineIniziale      lista in cui inserire le ore di fine iniziali
     * @param minutoFineIniziale   lista in cui inserire i minuti di fine iniziali
     * @param giornoProposto       lista in cui inserire i giorni proposti
     * @param oraInizioProposto    lista in cui inserire le ore di inizio proposte
     * @param minutoInizioProposto lista in cui inserire i minuti di inizio proposti
     * @param oraFineProposto      lista in cui inserire le ore di fine proposte
     * @param minutoFineProposto   lista in cui inserire i minuti di fine proposti
     * @throws Exception
     */
    @Override
    public void leggiRichiesteInAttesaDB(ArrayList<Integer> id, ArrayList<String> emailDocente, ArrayList<String> emailResponsabile, ArrayList<String> motivo, ArrayList<String> giornoIniziale, ArrayList<Integer> oraInizioIniziale, ArrayList<Integer> minutoInizioIniziale, ArrayList<Integer> oraFineIniziale, ArrayList<Integer> minutoFineIniziale, ArrayList<String> giornoProposto, ArrayList<Integer> oraInizioProposto, ArrayList<Integer> minutoInizioProposto, ArrayList<Integer> oraFineProposto, ArrayList<Integer> minutoFineProposto) throws Exception {

    }

    /**
     * @param idRichiesta id della richiesta da aggiornare
     * @param nuovoStato  nuovo stato: {@code 'IN_ATTESA'}, {@code 'APPROVATA'} o {@code 'RIFIUTATA'}
     * @throws Exception
     */
    @Override
    public void aggiornaStatoRichiestaDB(int idRichiesta, String nuovoStato) throws Exception {

    }

    /**
     * @param idRichiesta          id della richiesta da aggiornare
     * @param giornoProposto       nuovo giorno proposto
     * @param oraInizioProposto    nuova ora di inizio proposta
     * @param minutoInizioProposto nuovo minuto di inizio proposto
     * @param oraFineProposto      nuova ora di fine proposta
     * @param minutoFineProposto   nuovo minuto di fine proposto
     * @throws Exception
     */
    @Override
    public void aggiornaOrarioPropostoDB(int idRichiesta, String giornoProposto, int oraInizioProposto, int minutoInizioProposto, int oraFineProposto, int minutoFineProposto) throws Exception {

    }
}
