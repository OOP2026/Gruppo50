package model;


public class Utente {
    protected String nome;
    protected String cognome;
    protected String email;
    protected String username;
    protected String password;

    public Utente(String nome,String cognome, String email,String login, String password) {
       this.nome=nome;
       this.cognome=cognome;

       this.email=email;
        this.username = login;
        this.password = password;
    }

    public boolean login(String login, String password) {
        return ( login.equals(this.username) && password.equals(this.password));
    }
    
    public void saluto(){
  System.out.println("ciao mi chiamo " + this.nome+" "+this.cognome);
    }

    public String getmail(){
        return this.email;
    }
}
