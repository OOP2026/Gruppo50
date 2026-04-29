package model;
import java.util.ArrayList;

public class Responsabile extends Docente {
 public ArrayList<Richiesta> richiesteSpostamento= new ArrayList<Richiesta>();

    public Responsabile(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
       
    }
    @Override
    public void saluto() {
        System.out.println("Ciao, sono il responsabile " + this.nome + " " + this.cognome);
    }
   
    private void VizualizzaRichiesteSpostamento() {
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
richiesteSpostamento.forEach(richiesta -> {
    System.out.println("Docente richiedente: " + richiesta.docenteRichiedente.nome + " " + richiesta.docenteRichiedente.cognome);
    System.out.println("Motivo della richiesta: " + richiesta.motivoRichiesta);
    System.out.println("Orario lezione da spostare: " + richiesta.orarioLezioneDaSpostare.giorno + " " + richiesta.orarioLezioneDaSpostare.oraInizio + ":" + richiesta.orarioLezioneDaSpostare.minutoInizio + " - " + richiesta.orarioLezioneDaSpostare.oraFine + ":" + richiesta.orarioLezioneDaSpostare.minutoFine);
    System.out.println("Orario lezione nuovo: " + richiesta.nuovoOrarioLezione.giorno + " " + richiesta.nuovoOrarioLezione.oraInizio + ":" + richiesta.nuovoOrarioLezione.minutoInizio + " - " + richiesta.nuovoOrarioLezione.oraFine + ":" + richiesta.nuovoOrarioLezione.minutoFine);
    });  
    }

    private void creaLezione() {
        //Implementazione del metodo per creare una nuova lezione
    }
    
}