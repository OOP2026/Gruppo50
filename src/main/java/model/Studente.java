package model;
import java.util.ArrayList;
public class Studente extends Utente  {
    private final String matricola;
    protected int annoCorso;
    private static ArrayList<String> matricole= new ArrayList<>();


    public Studente(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) {
        super(nome,cognome,email,login,password);

        if(matricola==null || matricola.isEmpty()){
            throw new IllegalArgumentException("La matricola non può essere vuota");
        }
    if(matricole.contains(matricola)){
        throw new IllegalArgumentException("La matricola deve essere unica");
    }

        if(annoCorso<1 || annoCorso>3){
            throw new IllegalArgumentException("L'anno di corso deve essere compreso tra 1 e 3");
        }
        this.matricola = matricola;
        this.annoCorso = annoCorso;
            matricole.add(matricola);
 
    }

    public void saluto() {
        System.out.println("Ciao mi chiamo " + this.nome + " " + this.cognome + " e sono uno studente con matricola " + this.matricola);
    }
    public void visualizzaOrarioLezioni(OrarioLezioni elencoLezioni) {
        // Implementazione del metodo per visualizzare l'orario delle lezioni
elencoLezioni.visualizzaOrarioCompleto(this);

    }
    public String getmatricola(){
        return matricola;
    }



}
