package model;
import java.util.ArrayList;

public class Responsabile extends Docente {
 public ArrayList<Richiesta> richiesteSpostamento= new ArrayList<Richiesta>();

    public Responsabile(String nome, String cognome, String email, String login, String password, String insegnamento) {
        super(nome, cognome, email, login, password,insegnamento);
    
    }
    @Override
    public void saluto() {
        System.out.println("Ciao, sono il responsabile " + this.nome + " " + this.cognome);
    }
   
    protected void VizualizzaRichiesteSpostamento() {
        int numeroRichiesta=0;
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
  for(Richiesta richiesta : richiesteSpostamento) {
    System.out.println("Richieste di spostamento:");
    System.out.println("Numero richiesta: " + (numeroRichiesta));
    System.out.println("Docente richiedente: " + richiesta.docenteRichiedente.nome + " " + richiesta.docenteRichiedente.cognome);
    System.out.println("Motivo della richiesta: " + richiesta.motivoRichiesta);
    System.out.println("Orario lezione da spostare: " + richiesta.orarioLezioneDaSpostare.giorno + " " + richiesta.orarioLezioneDaSpostare.oraInizio + ":" + richiesta.orarioLezioneDaSpostare.minutoInizio + " - " + richiesta.orarioLezioneDaSpostare.oraFine + ":" + richiesta.orarioLezioneDaSpostare.minutoFine);
    System.out.println("Orario lezione nuovo: " + richiesta.nuovoOrarioLezione.giorno + " " + richiesta.nuovoOrarioLezione.oraInizio + ":" + richiesta.nuovoOrarioLezione.minutoInizio + " - " + richiesta.nuovoOrarioLezione.oraFine + ":" + richiesta.nuovoOrarioLezione.minutoFine);
    System.out.println("Stato Richiesta: "+richiesta.statoRichiesta);
    System.out.println("------------------------------------------------");
    numeroRichiesta++;
}
// System.out.println("----------------------Fine Richieste di Spostamento--------------------------");

    }

    protected void creaLezione(Insegnamento insegnamento, Aula aula, Orario orario, OrarioLezioni ElencoLezioni) {
        //Implementazione del metodo per creare una nuova lezione
Lezione nuovaLezione = new Lezione(insegnamento, aula, orario);
ElencoLezioni.AggiungiLezione(nuovaLezione);
    }


protected void SpostamentoLezione(int numeroRichiesta, OrarioLezioni ElencoLezioni){ {
    Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);
    Lezione lezioneDaSpostare = null;
    boolean trovato = false;
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
    return;}
     if(richiesta.statoRichiesta==StatoRichiesta.IN_ATTESA){
    for(Lezione lezione : ElencoLezioni.orariolezioni){
        if(lezione.insegnamento.docente.email.equals(richiesta.docenteRichiedente.email)==false){
            continue;
        }
        if(lezione.orario.giorno.equals(richiesta.orarioLezioneDaSpostare.giorno)==false){
            continue;
        }
        if(lezione.orario.oraInizio==richiesta.orarioLezioneDaSpostare.oraInizio && lezione.orario.minutoInizio==richiesta.orarioLezioneDaSpostare.minutoInizio){
            if(lezione.orario.oraFine==richiesta.orarioLezioneDaSpostare.oraFine && lezione.orario.minutoFine==richiesta.orarioLezioneDaSpostare.minutoFine){
               lezioneDaSpostare=lezione;
               trovato=true;
               break;
        }
     
    }

     }

     if(trovato==false){
        System.out.println("La lezione da spostare non è stata trovata");
        return;
     }
          try{
                ElencoLezioni.orariolezioni.remove(lezioneDaSpostare);
            }
            
            catch(Exception e){
                System.out.println("Errore nello spostamento della lezione: " + e.getMessage());
                System.exit(1);
            }
               Lezione nuovaLezione = new Lezione(lezioneDaSpostare.insegnamento, lezioneDaSpostare.aula, richiesta.nuovoOrarioLezione);
                ElencoLezioni.AggiungiLezione(nuovaLezione);
                lezioneDaSpostare=null;
                richiesta.statoRichiesta=StatoRichiesta.APPROVATA;
                System.out.println("La richiesta è stata approvata");
                return;  

  }

 }
} }