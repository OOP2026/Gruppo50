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
    public Utente (Utente u){
        this.nome=u.nome;
        this.cognome=u.cognome;
        this.email=u.email;
        this.username = u.username;
        this.password = u.password;
    }

    public boolean login(String username, String password) {
        return ( username.equals(this.username) && password.equals(this.password));
    }
    


    public String getmail(){
        return this.email;
    }
}
