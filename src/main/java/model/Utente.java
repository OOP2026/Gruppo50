package model;


public class Utente {
    public String nome;
    public String cognome;
    public String email;
    protected String username;
    protected String password;

    public Utente(String nome,String cognome, String email,String login, String password) {
       this.nome=nome;
       this.cognome=cognome;

       this.email=email;
        this.username = login;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return ( username.equals(this.username) && password.equals(this.password));
    }
    
    public void saluto(){
  System.out.println("ciao mi chiamo " + this.nome+" "+this.cognome);
    }

    public String getmail(){
        return this.email;
    }
}
