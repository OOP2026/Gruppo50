package model;
import java.util.ArrayList;

public class OrarioLezioni {
    public static ArrayList<Lezione> orariolezioni=new ArrayList<Lezione>();
    
    public static void AggiungiLezione(Lezione l){
        orariolezioni.add(l);
    }

}
