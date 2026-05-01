package model;
import java.util.ArrayList;
public class Docente extends Utente {
ArrayList<String> insegnamenti = new ArrayList<String>();
 ArrayList<Richiesta> richiesteSpostamentoInviate = new ArrayList<Richiesta>();
    public Docente(String nome, String cognome, String email, String login, String password,String insegnamento) {
        super(nome, cognome, email, login, password);
        this.insegnamenti.add(insegnamento);
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


    
}
