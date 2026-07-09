package model;
/**
 * La Classe aula rappresenta un aula fisica all'interno dell' universita
 * Questa classe viene usata per assegnare un aula a una lezione
 */
public class Aula {
    /** Il nome associato all'aula espresso in lettere es(A1,B1,C1...) */
    private final String nome;
    /** Capienza fisica associata all'aula espressa con un valore intero. */
  private final int capienza;

//getter and setter
    public String getNome(){
        return nome;
    }

 public int getCapienza(){
     return capienza;
 }

    /**
     * Crea una nuova aula, aula e orario.
     *
     * @param nome dell'aula in cui si svolge la lezione
     * @param capienza  dell'aula in cui si svolge la lezione
     */
    public Aula(String nome,int capienza) {
        this.nome=nome;
        this.capienza=capienza;
    }
public Aula(Aula a){
        this.nome= a.nome;
        this.capienza= a.capienza;
}
    
}
