package model;
import java.util.ArrayList;
public class Studente extends Utente {
    private final String matricola;
   public int annoCorso;
   private static ArrayList<String> Matricole= new ArrayList<String>();

    public Studente(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) {
        super(nome, cognome, email, login, password);
        if(matricola==null || matricola.isEmpty()){
            throw new IllegalArgumentException("La matricola non può essere vuota");
        }
    if(Matricole.contains(matricola)){
        throw new IllegalArgumentException("La matricola deve essere unica");
    }

       
        if(annoCorso<1 || annoCorso>3){
            throw new IllegalArgumentException("L'anno di corso deve essere compreso tra 1 e 3");
        }
        this.matricola = matricola;
        this.annoCorso = annoCorso;
            Matricole.add(matricola);
 
    }
 @Override
    public void saluto() {
        System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono uno studente con matricola " + this.matricola);
    }
    protected void visualizzaOrarioLezioni(OrarioLezioni ElencoLezioni) {
        // Implementazione del metodo per visualizzare l'orario delle lezioni
ElencoLezioni.visualizzaOrarioCompleto(this,ElencoLezioni);

    }

    public String getmatricola(){
        return matricola;
    }
}
