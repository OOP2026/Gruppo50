package model;

public class Studente extends Utente {
    private String matricola;
   

    public Studente(String nome, String cognome, String email, String login, String password, String matricola) {
        super(nome, cognome, email, login, password);
        this.matricola = matricola;
    }

    public void saluto() {
        System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono uno studente con matricola " + this.matricola);
    }

    
}
