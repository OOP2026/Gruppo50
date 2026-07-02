package model;
import java.util.ArrayList;
import java.util.List;
/**
 * La classe Docente rappresenta un professore universitario.
 * <p>
 * Il ddocente viene salvato come un Utente nel sistema ed egli può
 * visualizzare le proprie lezioni, richiedere di spostarle a un responsabile inoltre
 * può indicare anche dei vincoli(max 3) dove non è reperibile.
 * </p>
 *
 * @see Utente
 * @see Insegnamento
 * @see Vincolo
 * @see Richiesta
 * @see Lezione
 */
public class Docente extends Utente {
    /**l'elenco degli insegnamenti abilitati al docente.*/
    List<Insegnamento> insegnamenti;
    /**Registro delle richieste IN_ATTESA inoltrate dal docente.*/
    private ArrayList<Richiesta> richiesteSpostamentoInviate;
    /** Elenco dei vincoli indicati dal docente indicati come fascia oraria. */
    private ArrayList<Vincolo> vincoli;


    /**
     * Crea una nuova lezione associando insegnamento, aula e orario.
     *
     * @param nome primo nome di battesimo del docente.
     * @param cognome secondo nome di battesimo del docente.
     * @param email   email ad uso universitario del docente tipicamente(nome.cognome@dominio.estensione).
     * @param login   username utilizzato dal docente per accedere.
     */
    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome,cognome,email,login,password);
        richiesteSpostamentoInviate= new ArrayList<>();
        vincoli= new ArrayList<>();
        insegnamenti=new ArrayList<>();
    }
    /**Permette di aggiungere un insegnamento al docente
     * @param insegnamento è proprio l'insegnamento che vogliamo aggiugere al docente es(se il professore è abilitato
     * a insegnare 'Matematica, statistica ecc..'.
     */
    public void addInsegnamento(Insegnamento insegnamento){
        if(insegnamento==null){
            throw new NullPointerException("Il valore è null non puo essere aggiunto!");
        }
        this.insegnamenti.add(insegnamento);
        System.out.println(insegnamento.Nome+" è stato aggiunto a "+this.nome+" "+this.cognome);
    }
    ///Rimuove un materia che insegna o che puo insegnare il docente
    public void removeInsegnamento(Insegnamento insegnamento){
        if(insegnamento==null){
            throw new NullPointerException("Il valore è null non puo essere aggiunto!");
        }
        this.insegnamenti.remove(insegnamento);
    }
    ///Ritorna gli insegnamenti del docente
    public List<Insegnamento> getInsegnamenti(){
        return new ArrayList<>(insegnamenti);
    }
    public void saluto() {
        System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono un docente");

    }
    ///Ritorna le lezioni del docente
    public List<Lezione> getLezioni(OrarioLezioni o){
        return o.getDocenteLezioni(this);
    }
    ///questa funzione invia una richiesta di spostamento al responsabile
    public void richiestaSpostamentoLezione(OrarioLezioni orario,Responsabile responsabile,String motivo, Orario orarioVecchio, Orario orarioProposto) {
        //creazione della richiesta
        Richiesta richiesta = new Richiesta(this, motivo, orarioVecchio, orarioProposto);
        if(!checkRichiestaLezione(richiesta,orario))throw new IllegalArgumentException("La lezione riferita dalla richiesta non è esistente");
        responsabile.richiesteSpostamento.add(richiesta);
        this.richiesteSpostamentoInviate.add(richiesta);

    }
    ///Permette di vedere le richieste inviate del docente nel terminale
    protected void visualizzaRichiesteInviate(){
        int numeroRichiesta=1;
        int numeroRichieste=richiesteSpostamentoInviate.size();
        System.out.println("Richieste di spostamento inviate:");
        if(numeroRichieste==0){
            System.out.println("Non hai richieste di spostamento inviate");
            System.out.println("-----Fine-----");
            return;
        }
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
        for(Richiesta richiesta : richiesteSpostamentoInviate) {
            System.out.println("Docente richiedente: " + richiesta.docenteRichiedente.nome + " " + richiesta.docenteRichiedente.cognome);
            System.out.println("Orario lezione da spostare: " + richiesta.orarioLezioneDaSpostare.getOrarioCompleto());
            System.out.println("Orario lezione proposto: " + richiesta.nuovoOrarioLezione.getOrarioCompleto());
            System.out.println("Motivo della richiesta: " + richiesta.motivoRichiesta);
            System.out.println("Stato Richiesta: "+richiesta.statoRichiesta);
            if(numeroRichieste==numeroRichiesta){
                System.out.println("-----Fine-----");
                return; }
            System.out.println("------------------------------------------------");
            numeroRichiesta++;

        }

    }
    ///Ritorna una lista che contiene le richieste inviate
    public List<Richiesta> getRichiesteInviate(){
        return new ArrayList<>(richiesteSpostamentoInviate);
    }
    ///Carica nel docente le richieste inviate lette dal database, sostituendo quelle in memoria
    public void caricaRichiesteInviate(List<Richiesta> richiesteDaCaricare){
        richiesteSpostamentoInviate = new ArrayList<>(richiesteDaCaricare);
    }

    ///Permette di visualizzare l'orario del docente nel terminale
    public void visualizzaOrario(OrarioLezioni elencoLezioni){
        elencoLezioni.visualizzaOrarioCompleto(this);
    }

//Gestione dei vincoli
    /// Permette di aggiungere un vincolo, massimo fino a 3 vincoli
    public void aggiungiVincolo(String giorno, int oraInzio, int minutoInzio,int oraFIne,int minutoFine){
        if(vincoli.size()==3){
            throw new IllegalStateException("Hai già raggiunto il numero massimo di vincoli (3)");
        }
        vincoli.add(new Vincolo(giorno, oraInzio, minutoInzio, oraFIne, minutoFine));
        System.out.println("Vincolo aggiunto con successo");

    }
    public void caricaVincoliInDocente(List<Vincolo> vincoliDaCaricare){
        vincoli= new ArrayList<>(vincoliDaCaricare);
    }
    ///Metodo che restituisce i vincoli del docente, utile per verificare se il docente è disponibile in un certo orario
    public List<Vincolo> getVincoli(){
        return new ArrayList<>(vincoli);
    }
    ///rimuove un vincolo in base all'indice.
    public void rimuoviVincolo(int indice){
        if(indice<0 || indice>=vincoli.size()){
            throw new IllegalArgumentException("Non esiste il vincolo che vuoi rimuovere");
        }
        vincoli.remove(indice);
        System.out.println("Vincolo rimosso con successo");
    }
    ///Mostra i vincoli nel terminale
    public void mostraVincoli(){
        if(vincoli.isEmpty()){
            System.out.println("Non hai vincoli");
            return;
        }
        System.out.println("Vincoli di"+this.nome+" "+this.cognome+":");
        int numeroVincolo=0;
        for(Vincolo vincolo : vincoli){
            System.out.println("Numero vincolo: "+numeroVincolo);
            System.out.println("Giorno: "+vincolo.orario.getGiorno());
            System.out.println("Orario: "+vincolo.orario.getOrarioCompleto());
            System.out.println("-----------------------------------");
            numeroVincolo++;
        }

    }
    ///Controlla se la richiesta si riferisce a una lezione esistente
    public boolean checkRichiestaLezione(Richiesta r,OrarioLezioni orario){
        boolean lezioneTrovata=false;
        for(Lezione l:getLezioni(orario)){
            if(lezioneTrovata=r.equalsLezione(l)) break;
        }
        return lezioneTrovata;

    };



}
