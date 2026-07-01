package model;
/**
 * La Classe aula rappresenta un aula fisica all'interno dell' universita
 * Questa classe viene usata per assegnare un aula ad una lezione
 */
public class Aula {
    /** Il nome associato all'aula espresso in lettere es(A1,B1,C1...) */
    public String Nome;
    /** Capienza fisica associata all'aula espressa con un valore intero. */
    public int Capienza;



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

    
}
