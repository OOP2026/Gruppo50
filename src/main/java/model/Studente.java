package model;
import java.util.ArrayList;
public class Studente extends Utente {
    protected String matricola;
   

    public Studente(String nome, String cognome, String email, String login, String password, String matricola) {
        super(nome, cognome, email, login, password);
        this.matricola = matricola;
    }
 @Override
    public void saluto() {
        System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono uno studente con matricola " + this.matricola);
    }
    protected void visualizzaOrarioLezioni() {
        // Implementazione del metodo per visualizzare l'orario delle lezioni
    }   

}
