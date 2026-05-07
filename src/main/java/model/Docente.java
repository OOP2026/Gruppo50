package model;
import java.util.ArrayList;
import java.util.List;
public class Docente extends Utente {
   List<Insegnamento> insegnamenti = new ArrayList<Insegnamento>();
 ArrayList<Richiesta> richiesteSpostamentoInviate = new ArrayList<Richiesta>();
    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);

    }

    public void Inserisci_insegnamenti(List<Insegnamento> insegnamenti){
        this.insegnamenti=new ArrayList<>(insegnamenti);
    }
    @Override
    public void saluto() {
System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono un docente");

    }
    public void RichiestaSpostamentoLezione(Responsabile responsabile, String motivo, String giornoVecchio, int oraInizioVecchio, int minutoInizioVecchio, int oraFineVecchio, int minutoFineVecchio, String giornoNuovo, int oraInizioNuovo, int minutoInizioNuovo, int oraFineNuovo, int minutoFineNuovo) {  
        //Incompleto manca l'orario della lezione da spostare e l'orario nuovo
    Orario orarioDaSpostare = new Orario(giornoVecchio, oraInizioVecchio, minutoInizioVecchio, oraFineVecchio, minutoFineVecchio);
    Orario nuovoOrario = new Orario(giornoNuovo, oraInizioNuovo, minutoInizioNuovo, oraFineNuovo, minutoFineNuovo);
    Richiesta richiesta = new Richiesta(this, motivo, orarioDaSpostare, nuovoOrario);
    responsabile.richiesteSpostamento.add(richiesta);
    this.richiesteSpostamentoInviate.add(richiesta);
    
}

public void VisualizzaRichiesteInviate(){
     int numeroRichiesta=0;
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
  for(Richiesta richiesta : richiesteSpostamentoInviate) {
    System.out.println("Richieste di spostamento inviate:");
    //System.out.println("Numero richiesta: " + (numeroRichiesta));
    System.out.println("Docente richiedente: " + richiesta.docenteRichiedente.nome + " " + richiesta.docenteRichiedente.cognome);
    System.out.println("Motivo della richiesta: " + richiesta.motivoRichiesta);
    System.out.println("Orario lezione da spostare: " + richiesta.orarioLezioneDaSpostare.giorno + " " + richiesta.orarioLezioneDaSpostare.oraInizio + ":" + richiesta.orarioLezioneDaSpostare.minutoInizio + " - " + richiesta.orarioLezioneDaSpostare.oraFine + ":" + richiesta.orarioLezioneDaSpostare.minutoFine);
    System.out.println("Orario lezione nuovo: " + richiesta.nuovoOrarioLezione.giorno + " " + richiesta.nuovoOrarioLezione.oraInizio + ":" + richiesta.nuovoOrarioLezione.minutoInizio + " - " + richiesta.nuovoOrarioLezione.oraFine + ":" + richiesta.nuovoOrarioLezione.minutoFine);
    System.out.println("Stato Richiesta: "+richiesta.statoRichiesta);
    System.out.println("------------------------------------------------");
    

    }
}

public void VisualizzaOrario(OrarioLezioni ElencoLezioni){
ElencoLezioni.visualizzaOrarioCompleto(this,ElencoLezioni);
}
}
