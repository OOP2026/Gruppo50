package model;
import java.util.ArrayList;
import java.util.List;
public class Docente extends Utente {
   List<Insegnamento> insegnamenti;
private ArrayList<Richiesta> richiesteSpostamentoInviate;
 private ArrayList<Vincolo> vincoli;

    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
        richiesteSpostamentoInviate= new ArrayList<Richiesta>();
        vincoli= new ArrayList<Vincolo>();
        insegnamenti=new ArrayList<Insegnamento>();
    }

    public void Inserisci_insegnamenti(List<Insegnamento> insegnamenti){
        this.insegnamenti=new ArrayList<>(insegnamenti);
    }
    @Override
    public void saluto() {
System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono un docente");

    }
    //questa funzione invia una richiesta di spostamento al responsabile
    public void RichiestaSpostamentoLezione(Responsabile responsabile, String motivo, String giornoVecchio, int oraInizioVecchio, int minutoInizioVecchio, int oraFineVecchio, int minutoFineVecchio, String giornoNuovo, int oraInizioNuovo, int minutoInizioNuovo, int oraFineNuovo, int minutoFineNuovo) {  
      //creazione della richiesta
    Orario orarioDaSpostare = new Orario(giornoVecchio, oraInizioVecchio, minutoInizioVecchio, oraFineVecchio, minutoFineVecchio);
    Orario nuovoOrario = new Orario(giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo);
    Richiesta richiesta = new Richiesta(this, motivo, orarioDaSpostare, nuovoOrario);
    responsabile.richiesteSpostamento.add(richiesta);
    this.richiesteSpostamentoInviate.add(richiesta);
    
}
//Vede le proprie richieste che ha inviato
protected void VisualizzaRichiesteInviate(){
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

public void VisualizzaOrario(OrarioLezioni ElencoLezioni){
ElencoLezioni.visualizzaOrarioCompleto(this,ElencoLezioni);
}

//Gestione dei vincoli
public void aggiungiVincolo(String giorno, int OraInzio, int MinutoInzio,int OraFIne,int MinutoFine){
    if(vincoli.size()==3){
        System.out.println("Hai già raggiunto il numero massimo di vincoli");
        return;
    }
vincoli.add(new Vincolo(giorno, OraInzio, MinutoInzio, OraFIne, MinutoFine));
System.out.println("Vincolo aggiunto con successo");

}
//funzione che restituisce i vincoli del docente, utile per verificare se il docente è disponibile in un certo orario
public ArrayList<Vincolo> getVincoli(){
return new ArrayList<>(vincoli);
}
//rimuove un vincolo in base all'indice.
public void rimuoviVincolo(int indice){
    if(indice<0 || indice>=vincoli.size()){
        System.out.println("Indice non valido");
        return;
    }
    vincoli.remove(indice);
    System.out.println("Vincolo rimosso con successo");
}
//Mostra i vincoli
public void mostraVincoli(){
    if(vincoli.size()==0){
        System.out.println("Non hai vincoli");
        return;
    }
    System.out.println("Vincoli di"+this.nome+" "+this.cognome+":");
    int numeroVincolo=0;
    for(Vincolo vincolo : vincoli){
        System.out.println("Numero vincolo: "+numeroVincolo);
        System.out.println("Giorno: "+vincolo.orario.giorno);
        System.out.println("Orario: "+vincolo.orario.oraInizio+":"+vincolo.orario.minutoInizio+" - "+vincolo.orario.oraFine+":"+vincolo.orario.minutoFine);
        System.out.println("-----------------------------------");
        numeroVincolo++;
    }
}
}
