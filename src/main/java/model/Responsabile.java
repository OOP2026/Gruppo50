package model;
import java.util.ArrayList;
import java.util.List;

public class Responsabile extends Docente {
  ArrayList<Richiesta> richiesteSpostamento;
private final Token token;
    public Responsabile(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
        richiesteSpostamento= new ArrayList<>();
        this.token = new Token();
    }
    @Override
    public void saluto() {
        System.out.println("Ciao, sono il responsabile " + this.nome + " " + this.cognome);
    }
   /// Permette di visualizzare le richieste di spostamento nel terminale
    public void visualizzaRichiesteSpostamento() {
        int numeroRichiesta=0;
        int numeroRichieste=richiesteSpostamento.size();
            System.out.println("Richieste di spostamento:");
            if(numeroRichieste==0){
                System.out.println("Non ci sono richieste di spostamento");
                 System.out.println("-----Fine-----");
                return;
            }
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
  for(Richiesta richiesta : richiesteSpostamento) {
    System.out.println("Numero richiesta: " + (numeroRichiesta));
    System.out.println("Docente richiedente: " + richiesta.docenteRichiedente.nome + " " + richiesta.docenteRichiedente.cognome);
    System.out.println("Motivo della richiesta: " + richiesta.motivoRichiesta);
    System.out.println("Orario lezione da spostare: " + richiesta.orarioLezioneDaSpostare.getOrarioCompleto());
    System.out.println("Orario lezione nuovo: " + richiesta.nuovoOrarioLezione.getOrarioCompleto());
    System.out.println("Stato Richiesta: "+richiesta.statoRichiesta);
    if(numeroRichieste==numeroRichiesta+1){
        System.out.println("-----Fine-----");
   return; }
    System.out.println("------------------------------------------------");
    numeroRichiesta++;
}


    }

    ///La funzione inserisce una lezione nell'orario, controllando che non ci siano conflitti con altre lezioni e che il docente sia disponibile in quell'orario
    /// @param l è la lezione che deve essere inserita
    /// @param elencoLezioni è dove viene inserito
public void inserisciLezione(Lezione l, OrarioLezioni elencoLezioni) {
    if(!(verificaDisponibilita(l.insegnamento.docente.getVincoli(), l.orario))){
        throw new IllegalArgumentException("Il docente non è disponibile in questa fascia oraria");
    }
    // Aggiunge la lezione all'orario in memoria. Se c'è un conflitto,
    // aggiungiLezione lancia IllegalArgumentException: NON la intercettiamo qui,
    // così si propaga al Controller, che la mostra nella GUI e NON salva la
    // lezione sul database.
    elencoLezioni.aggiungiLezione(l, this.token);
    System.out.println("Lezione aggiunta con successo responsabile");
}
///Questo metodo approva una richiesta di spostamento
///Questo metodo gestisce una richiesta di spostamento: se approva è true esegue
///effettivamente lo spostamento, altrimenti rifiuta la richiesta.
public void spostamentoLezione(int numeroRichiesta, OrarioLezioni elencoLezioni, boolean approva){

    // Controllo che l'indice sia valido (get() lancerebbe un'eccezione, non restituisce mai null)
    if (numeroRichiesta < 0 || numeroRichiesta >= richiesteSpostamento.size()) {
        System.out.println("La richiesta non esiste");
        return;
    }

    Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);

    if(richiesta.statoRichiesta == StatoRichiesta.APPROVATA){
        System.out.println("La richiesta è già stata approvata");
        return;
    }
    if(richiesta.statoRichiesta == StatoRichiesta.RIFIUTATA){
        System.out.println("La richiesta è già stata rifiutata");
        return;
    }

    // Da qui in poi la richiesta è sicuramente IN_ATTESA

    // --- Caso RIFIUTO ---
    if(!approva){
        richiesta.statoRichiesta = StatoRichiesta.RIFIUTATA;
        System.out.println("La richiesta è stata rifiutata");
        return;
    }

    // --- Caso APPROVAZIONE: eseguo lo spostamento ---
    Lezione lezioneDaSpostare = cercaLezioneDaSpostare(richiesta, elencoLezioni);
    if(lezioneDaSpostare == null){
        System.out.println("La lezione da spostare non è stata trovata");
        return;
    }

    try{
        elencoLezioni.getOrarioLezioni(this.token).remove(lezioneDaSpostare);
    } catch(Exception e){
        System.out.println("Errore nello spostamento della lezione: " + e.getMessage());
        return;
    }

    Lezione nuovaLezione = new Lezione(lezioneDaSpostare.insegnamento, lezioneDaSpostare.aula, richiesta.nuovoOrarioLezione);

    try {
        elencoLezioni.aggiungiLezione(nuovaLezione, this.token);
    } catch (IllegalArgumentException e1) {
        System.out.println("Errore nello spostamento della lezione: " + e1.getMessage());
        richiesta.statoRichiesta = StatoRichiesta.RIFIUTATA;
        System.out.println("La richiesta è stata rifiutata");
        System.out.println("Tentativo di ripristinare la lezione originale...");
        try {
            elencoLezioni.aggiungiLezione(lezioneDaSpostare, this.token);
        } catch (Exception e2) {
            System.out.println("Errore nel ripristino della lezione originale: " + e2.getMessage());
        }
        return;
    }

    richiesta.statoRichiesta = StatoRichiesta.APPROVATA;
    System.out.println("La richiesta è stata approvata");
}
 ///Questo metodo permette di cambiare l'orario della richiesta
 public void cambiaOrarioRichiesta(int numeroRichiesta,Orario orarioNuovo){
Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);
if(richiesta==null){
    System.out.println("La richiesta non esiste");
    return;
}
if(richiesta.statoRichiesta==StatoRichiesta.APPROVATA){
    System.out.println("La richiesta è già stata approvata");
    return;
}
if(richiesta.statoRichiesta==StatoRichiesta.RIFIUTATA){
    System.out.println("La richiesta è già stata rifiutata");
    return;
}

richiesta.nuovoOrarioLezione= orarioNuovo;

 }
 ///Permette di visualizzare l'orario completo di utte le lezioni dle corso nel terminale
 public void visualizzaOrarioCompleto(OrarioLezioni elencoLezioni){

    elencoLezioni.visualizzaOrarioCompleto(this.token);
 }
 

///permette di verificare se l'orario viola uno dei vincoli del docente
///cioe controlla se il docente è disponibile in quella fascia oraria
private boolean verificaDisponibilita(List<Vincolo> vincoli, Orario orario){
    if(vincoli.isEmpty()) return true;
for(Vincolo vincolo:vincoli) {
    int orarioInizioVincolo = vincolo.orario.getOrarioInizioInMinuti();
    int orarioFineVincolo = vincolo.orario.getOrarioFineInMinuti();
    int orarioInizioLezione = orario.getOrarioInizioInMinuti();
    int orarioFineLezione = orario.getOrarioFineInMinuti();
    System.out.println("Sto confrontando i giorni");
    if (!vincolo.orario.getGiorno().equals(orario.getGiorno())) {
        System.out.println("Giorni diversi");
        continue;
    }
    System.out.println("Sto confrontando l'orario");

    if (orarioInizioLezione < orarioFineVincolo && orarioFineLezione > orarioInizioVincolo) return false;

}
return true;
}
///Serve per trovare la lezione che si vuole spostare
private Lezione cercaLezioneDaSpostare(Richiesta r, OrarioLezioni elencoLezioni){
          for(Lezione lezione : elencoLezioni.getOrarioLezioni(this.token)) {
        if( r.equalsLezione(lezione))
             return lezione;
    }
    return null;
 }

     /// Restituisce la lista non modificabile delle richieste di spostamento
     /// ricevute da questo responsabile.
     public java.util.List<Richiesta> getRichiesteSpostamento() {
        return java.util.Collections.unmodifiableList(richiesteSpostamento);
    }

    ///Metodo di appoggio per il controller per ottenere il valore della richiesta come stringa
    public String getStatoRichiesta(int numeroRichiesta) {
        if (numeroRichiesta < 0 || numeroRichiesta >= richiesteSpostamento.size()) {
            return null;
        }
        return richiesteSpostamento.get(numeroRichiesta).statoRichiesta.name();
    }

    ///Verifica che una richiesta sia ancora in attesa utilizzato dal controller
    public boolean isRichiestaInAttesa(int numeroRichiesta) {
        if (numeroRichiesta < 0 || numeroRichiesta >= richiesteSpostamento.size()) {
            return false;
        }
        return richiesteSpostamento.get(numeroRichiesta).statoRichiesta == StatoRichiesta.IN_ATTESA;
    }

    /// Il token serve per usare alcuni metodi che solo il responsabile puo usare
public class Token {
    private Token() {

    }

}

}