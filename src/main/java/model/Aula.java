package model;
/**
 * La Classe aula rappresenta un aula fisica all'interno di una universita
 * Questa classe viene usata per assegnare un aula ad una lezione
 */
public class Aula {
    public String Nome;
    public int Capienza;

    public Aula(String Nome,int Capienza) {
        this.Nome=Nome;
        this.Capienza=Capienza;
    }

    
}
