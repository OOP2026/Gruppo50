package model;

import java.util.ArrayList;

public class Utente {
    protected String nome;
    protected String cognome;
    protected String email;
    protected String login;
    protected String password;

    public Utente(String nome,String cognome, String email,String login, String password) {
       this.nome=nome;
       this.cognome=cognome;
       this.email=email;
        this.login = login;
        this.password = password;
    }

    public boolean login(String login, String password) {
        return ( login.equals(this.login) && password.equals(this.password));
    }

    public void saluto(){
  System.out.println("ciao mi chiamo " + this.nome+" "+this.cognome);
    }
}
