package model;
/**
 * La Classe aula rappresenta un aula fisica all'interno dell' universita
 * Questa classe viene usata per assegnare un aula a una lezione
 */
public class Aula {
    /** Il nome associato all'aula espresso in lettere es(A1,B1,C1...) */
    final private String Nome;
    /** Capienza fisica associata all'aula espressa con un valore intero. */
  final private int Capienza;

//getter and setter
    public String getNome(){
        return Nome;
    }

 public int getCapienza(){
     return Capienza;
 }

    /**
     * Crea una nuova aula, aula e orario.
     *
     * @param Nome dell'aula in cui si svolge la lezione
     * @param Capienza  dell'aula in cui si svolge la lezione
     */
    public Aula(String Nome,int Capienza) {
        this.Nome=Nome;
        this.Capienza=Capienza;
    }
public Aula(Aula a){
        this.Nome= a.Nome;
        this.Capienza= a.Capienza;
}
    
}
