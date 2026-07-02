package model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import model.Responsabile.Token;
import java.util.List;
///Rappresenta l'orario delle lezioni, all'interno ci sono tutti le lezioni create
/**
 * Rappresenta l'orario delle lezioni; all'interno contiene e gestisce
 * tutte le lezioni create all'interno del sistema.
 */
public class OrarioLezioni {

    /** Una lista che contiene tutte le {@link Lezione Lezioni} programmate  */
    private ArrayList<Lezione> orariolezioni;
    /** Un elenco con i giorni validi          */
    private final String[] giorni={"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì"};


    /**
     * Costruisce un nuovo oggetto OrarioLezioni, controllando l'arraylist */

    public OrarioLezioni(){
        orariolezioni=new ArrayList<>();
    }

///Restituisce le lezioni del docente
    /**
     * Restituisce una lista di tutte le lezioni assegnate a un docente
     * @param docente il docente di cui si vogliono cercare le lezioni
     * @return una lista di {@link Lezione} associate al docente, vuota se non ha lezioni assegnate
     */
    public List<Lezione> getDocenteLezioni(Docente docente){
        List<Lezione> lista=new ArrayList<Lezione>();
        for(Lezione l : orariolezioni){
            if(l.insegnamento.docente.email.equals(docente.email)){
                lista.add(l);
            }
        }
        if(lista.isEmpty()){
            System.out.println("Non hai lezioni assegnate");
            return new ArrayList<>();
        }
        List<Lezione> lista2 = new ArrayList<>(lista);
        Collections.sort(lista2);
        return lista2;

    };
    ///Il metodo permette di aggiungere una lezione nell'orario
    ///@param l È un oggetto di tipo {@link Lezione}
    ///@param token è un oggetto che ha solo un responsabile
    /**
     * Permette di aggiungere una nuova lezione all'orario
     * Solo un Responsabile in possesso del Token valido può eseguire questa operazione.
     * @param l la {@link Lezione} da aggiungere all'orario
     * @param token l'oggetto Token che da i permessi al responsabile
     * @return true se la lezione viene aggiunta con successo
     * @throws IllegalArgumentException se c'è un conflitto di orario, aula o docente con un'altra lezione
     * @throws NullPointerException se la lezione passata è nulla o se il token è nullo (permesso negato)
     */
    public boolean aggiungiLezione(Lezione l, Token token)throws IllegalArgumentException, NullPointerException {
        //Solo il responsabile puo usare questo metodo
        if(token==null){ throw new NullPointerException("Non hai il permesso");}

        if (l == null){
            throw new NullPointerException("Questa lezione è vuota");
        }

        if(controlloConflittoLezione(l)){
            throw new IllegalArgumentException("C'è un conflitto con un'altra lezione");
        }
        orariolezioni.add(l);
        return true;
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

/// Permette di vedere l'orario completo del corso dentro il terminale
/// solamente il responsabile può farlo
    /**
     * Stampa nel terminale l'orario completo di tutti i corsi.
     * Operazione che solo il Responsabile puó fare.
     * @param token l'oggetto Token che da i permessi al responsabile
     */
    public void visualizzaOrarioCompleto(Token token){
        if(token==null){
            System.out.println("Non hai il permesso");
            return;
        }

        System.out.println("Orario completo delle lezioni:");

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
        System.out.println(giorno);
        boolean trovata = false;
        for (Lezione l : elenco) {
            if (!giorno.equalsIgnoreCase(l.orario.giorno) || !filtro.test(l)) continue;

            System.out.println("Docente: "+l.insegnamento.docente.nome+" "+l.insegnamento.docente.cognome);
            System.out.println("Insegamento: "+l.insegnamento.Nome);
            System.out.println("Orario: "+l.orario.getOrarioCompleto());
            System.out.println("Aula: "+l.aula.Nome);

            // stampa campi comuni...
            trovata = true;
        }
        if (!trovata) System.out.println("Non ci sono lezioni in questo giorno");
        if (giorno.equalsIgnoreCase("Venerdi")) {
            System.out.println("---Fine Orario---");
            return;
        }
        System.out.println("------------------");
    }

//Studente
    /**
     * Stampa l'orario delle lezioni filtrate per l'anno di corso dello studente
     * @param studente lo studente che richiede di visualizzare il proprio orario
     */
    public void visualizzaOrarioCompleto(Studente studente){

        System.out.println("Orario completo delle lezioni Studente: "+studente.nome+" "+studente.cognome);
        giornoLezioni(giorni[0],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
        giornoLezioni(giorni[1],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
        giornoLezioni(giorni[2],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
        giornoLezioni(giorni[3],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
        giornoLezioni(giorni[4],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);

    }



//Docente
    /**
     * Stampa l'orario delle lezioni assegnate a un docente specifico
     * @param docente il docente che richiede di visualizzare il proprio orario
     */
    public void visualizzaOrarioCompleto(Docente docente){

        System.out.println("Orario completo delle lezioni Docente: "+docente.nome+" "+docente.cognome);
        giornoLezioni(giorni[0], orariolezioni, l -> l.insegnamento.docente == docente);
        giornoLezioni(giorni[1], orariolezioni, l -> l.insegnamento.docente == docente);
        giornoLezioni(giorni[2], orariolezioni, l -> l.insegnamento.docente == docente);
        giornoLezioni(giorni[3], orariolezioni, l -> l.insegnamento.docente == docente);
        giornoLezioni(giorni[4], orariolezioni, l -> l.insegnamento.docente == docente);
    }



    /**
     * Restituisce la lista completa di tutte le lezioni presenti nel sistema
     * @param token l'oggetto Token che da i permessi al responsabile
     * @return la lista completa delle lezioni, oppure una lista vuota se il permesso è negato
     */

    public List<Lezione> getOrarioLezioni(Token token) {
        if(token==null){
            System.out.println("Non hai il permesso");
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
        int inizioNuovo = l.orario.getOrarioInizioInMinuti();
        int fineNuovo = l.orario.getOrarioFineInMinuti();

        int inizioEsistente = lezioneGiaPresente.orario.getOrarioInizioInMinuti();
        int fineEsistente = lezioneGiaPresente.orario.getOrarioFineInMinuti();

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
            if ((lf.orario.giorno.equals(l.orario.giorno))) {
                if(conflittoOrario){
                    if(l.aula.Nome.equals(lf.aula.Nome)) return true;
                    if(l.insegnamento.docente.equals(lf.insegnamento.docente)) return true;

                }

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
            if (l.insegnamento.AnnoCorso == studente.annoCorso) {
                risultato.add(l);
            }
        }
        return risultato;
    }

}