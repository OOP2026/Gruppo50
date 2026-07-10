package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import model.Responsabile.Token;
import java.util.List;
import java.util.logging.Logger;
/**
 * Rappresenta l'orario delle lezioni; all'interno contiene e gestisce
 * tutte le lezioni create all'interno del sistema.
 */
public class OrarioLezioni {

    /** Una lista che contiene tutte le {@link Lezione Lezioni} programmate  */
    private ArrayList<Lezione> orariolezioni;
    /** Un elenco con i giorni validi          */
    private final String[] giorni={"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì"};
    private static final Logger logger = Logger.getLogger(OrarioLezioni.class.getName());


    /**
     * Costruisce un nuovo oggetto OrarioLezioni, controllando l'arraylist */

    public OrarioLezioni(){
        orariolezioni=new ArrayList<>();
    }

    /**
     * Restituisce una lista di tutte le lezioni assegnate a un docente
     * @param docente il docente di cui si vogliono cercare le lezioni
     * @return una lista di {@link Lezione} associate al docente, vuota se non ha lezioni assegnate
     */
    public List<Lezione> getDocenteLezioni(Docente docente){
        List<Lezione> lista=new ArrayList<>();
        for(Lezione l : orariolezioni){
            if(l.getInsegnamento().getDocente().email.equals(docente.email)){
                lista.add(l);
            }
        }
        if(lista.isEmpty()){
            logger.info("Non hai lezioni assegnate");
            return new ArrayList<>();
        }
        List<Lezione> lista2 = new ArrayList<>(lista);
        Collections.sort(lista2);
        return lista2;

    }
    /**
     * Permette di aggiungere una nuova lezione all'orario
     * Solo un Responsabile in possesso del Token valido può eseguire questa operazione.
     * @param l la {@link Lezione} da aggiungere all'orario
     * @param token l'oggetto Token che da i permessi al responsabile
     * @return true se la lezione viene aggiunta con successo
     * @throws IllegalArgumentException se c'è un conflitto di orario, aula o docente con un'altra lezione
     * @throws NullPointerException se la lezione passata è nulla o se il token è nullo (permesso negato)
     */
    public void aggiungiLezione(Lezione l, Token token)throws IllegalArgumentException, NullPointerException {
        //Solo il responsabile puo usare questo metodo
        if(token==null){ throw new NullPointerException("Non hai il permesso");}

        if (l == null){
            throw new NullPointerException("Questa lezione è vuota");
        }

        if(controlloConflittoLezione(l)){
            throw new IllegalArgumentException("C'è un conflitto con un'altra lezione");
        }
        orariolezioni.add(l);
    }

    /**
     * Carica in memoria una lezione letta dal database.
     * <p>
     * A differenza di {@link #aggiungiLezione(Lezione, Token)}, questo metodo non
     * richiede il Token del Responsabile né esegue il controllo dei conflitti:
     * si tratta infatti di un'operazione di sistema che ricostruisce l'orario a
     * partire da dati già validati e salvati in precedenza sul database (usata
     * dal Controller al login). Serve a rendere visibili le lezioni persistite,
     * ad esempio ai docenti che accedono in una nuova sessione.
     * </p>
     *
     * @param l la {@link Lezione} ricostruita dai dati letti dal database
     */
    public void caricaLezioneDaDB(Lezione l) {
        if (l == null) {
            return;
        }
        orariolezioni.add(l);
    }

    /**
     * Stampa nel terminale l'orario completo di tutti i corsi.
     * Operazione che solo il Responsabile puó fare.
     * @param token l'oggetto Token che da i permessi al responsabile
     */
    public void visualizzaOrarioCompleto(Token token){
        if(token==null){
            logger.info("Non hai il permesso");
            return;
        }

        logger.info("Orario completo delle lezioni:");

        giornoLezioni(giorni[0],  orariolezioni, l -> true);
        giornoLezioni(giorni[1],  orariolezioni, l -> true);
        giornoLezioni(giorni[2],  orariolezioni, l -> true);
        giornoLezioni(giorni[3],  orariolezioni, l -> true);
        giornoLezioni(giorni[4],  orariolezioni, l -> true);

    }


    /**
     * Metodo per stampare le lezioni in base a dei parametri specifici utilizzando un filtro
     * @param giorno il giorno della settimana da stampare
     * @param elenco la lista completa delle lezioni
     * @param filtro un oggetto usato per filtrare le lezioni da visualizzare
     */
    private void giornoLezioni(String giorno, ArrayList<Lezione> elenco, Predicate<Lezione> filtro) {
        logger.info(giorno);
        boolean trovata = false;
        for (Lezione l : elenco) {
            if (!giorno.equalsIgnoreCase(l.getOrario().getGiorno()) || !filtro.test(l)) continue;

            logger.info("Docente: "+l.getInsegnamento().getDocente().nome+" "+l.getInsegnamento().getDocente().cognome);
            logger.info("Insegamento: "+l.getInsegnamento().getNome());
            logger.info("Orario: "+l.getOrario().getOrarioCompleto());
            logger.info("Aula: "+l.getAula().getNome());

            // stampa campi comuni...
            trovata = true;
        }
        if (!trovata) logger.info("Non ci sono lezioni in questo giorno");
        if (giorno.equalsIgnoreCase("Venerdi")) {
            logger.info("---Fine Orario---");
            return;
        }
        logger.info("------------------");
    }

//Studente
    /**
     * Stampa l'orario delle lezioni filtrate per l'anno di corso dello studente
     * @param studente lo studente che richiede di visualizzare il proprio orario
     */
    public void visualizzaOrarioCompleto(Studente studente){

        logger.info("Orario completo delle lezioni Studente: "+studente.nome+" "+studente.cognome);
        giornoLezioni(giorni[0],    orariolezioni, l -> l.getInsegnamento().getAnnoCorso() == studente.getAnnoCorso());
        giornoLezioni(giorni[1],    orariolezioni, l -> l.getInsegnamento().getAnnoCorso() == studente.getAnnoCorso());
        giornoLezioni(giorni[2],    orariolezioni, l -> l.getInsegnamento().getAnnoCorso() == studente.getAnnoCorso());
        giornoLezioni(giorni[3],    orariolezioni, l -> l.getInsegnamento().getAnnoCorso() == studente.getAnnoCorso());
        giornoLezioni(giorni[4],    orariolezioni, l -> l.getInsegnamento().getAnnoCorso() == studente.getAnnoCorso());

    }



//Docente
    /**
     * Stampa l'orario delle lezioni assegnate a un docente specifico
     * @param docente il docente che richiede di visualizzare il proprio orario
     */
    public void visualizzaOrarioCompleto(Docente docente){

        logger.info("Orario completo delle lezioni Docente: "+docente.nome+" "+docente.cognome);
        giornoLezioni(giorni[0], orariolezioni, l -> l.getInsegnamento().getDocente().equals(docente));
        giornoLezioni(giorni[1], orariolezioni, l -> l.getInsegnamento().getDocente()== docente);
        giornoLezioni(giorni[2], orariolezioni, l -> l.getInsegnamento().getDocente() == docente);
        giornoLezioni(giorni[3], orariolezioni, l -> l.getInsegnamento().getDocente() == docente);
        giornoLezioni(giorni[4], orariolezioni, l -> l.getInsegnamento().getDocente() == docente);
    }



    /**
     * Restituisce la lista completa di tutte le lezioni presenti nel sistema
     * @param token l'oggetto Token che da i permessi al responsabile
     * @return la lista completa delle lezioni, oppure una lista vuota se il permesso è negato
     */

    public List<Lezione> getOrarioLezioni(Token token) {
        if(token==null){
            logger.info("Non hai il permesso");
            return new ArrayList<>();
        }
        return orariolezioni;
    }

    /**
     * Controlla se le fasce orarie di due lezioni si scontrino
     * @param l la nuova lezione da inserire
     * @param lezioneGiaPresente una lezione già presente nell'orario
     * @return true se c'è uno scontro di orario, false altrimenti
     */
    private boolean  controlloConflittoOrario(Lezione l, Lezione lezioneGiaPresente){
       if(!l.getOrario().getGiorno().equalsIgnoreCase(lezioneGiaPresente.getOrario().getGiorno())){
           return false;
           }
        int inizioNuovo = l.getOrario().getOrarioInizioInMinuti();
        int fineNuovo = l.getOrario().getOrarioFineInMinuti();

        int inizioEsistente = lezioneGiaPresente.getOrario().getOrarioInizioInMinuti();
        int fineEsistente = lezioneGiaPresente.getOrario().getOrarioFineInMinuti();

        return (inizioNuovo<fineEsistente && fineNuovo>inizioEsistente);
    }

    /**
     * Controlla se la nuova lezione entra in conflitto con lezioni già esistenti
     * @param l la lezione da controllare
     * @return true se viene rilevato un conflitto di risorse, false se può essere inserita
     */
    private boolean controlloConflittoLezione(Lezione l){
        for (Lezione lf : orariolezioni) {
            boolean conflittoOrario= controlloConflittoOrario(l,lf);

                if(conflittoOrario){
                    if(l.getAula().getNome().equals(lf.getAula().getNome())) return true;
                    if(l.getInsegnamento().getDocente().equals(lf.getInsegnamento().getDocente())) return true;



            }
        }
        return false;
    }

    /**
     * Restituisce le lezioni filtrate per anno di corso dello studente.
     * Non richiede il Token perché lo studente ha diritto di vedere il proprio orario.
     * @param studente lo studente di cui si vuole ottenere le lezioni per il proprio anno
     * @return una lista di lezioni appartenenti all'anno di corso dello studente
     */
    public List<Lezione> getLezioniStudente(Studente studente) {
        List<Lezione> risultato = new ArrayList<>();
        for (Lezione l : orariolezioni) {
            if (l.getInsegnamento().getAnnoCorso() == studente.getAnnoCorso()) {
                risultato.add(l);
            }
        }
        return risultato;
    }

}