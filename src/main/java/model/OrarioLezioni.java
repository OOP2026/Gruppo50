package model;
import java.util.ArrayList;

public class OrarioLezioni {
    public static ArrayList<Lezione> orariolezioni=new ArrayList<Lezione>();
    
    public static void AggiungiLezione(Lezione l){
        try {
            if(l==null){throw new NullPointerException("Questa lezione e vuota");}
            if(orariolezioni.size()==0){
                orariolezioni.add(l);
                return;
            }
            for (Lezione lezione : orariolezioni) {
                if(lezione.orario.giorno.equals(l.orario.giorno)==false){
                   continue;
                }
                if(lezione.aula.Nome.equals(l.aula.Nome)==false){
                    continue;
                }   
                if(lezione.orario.oraInizio==l.orario.oraInizio && lezione.orario.minutoInizio==l.orario.minutoInizio){
                   throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario");
                }
                if(lezione.orario.oraFine==l.orario.oraFine && lezione.orario.minutoFine==l.orario.minutoFine){
                    throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario");
                }
                if(lezione.orario.oraInizio>=l.orario.oraInizio && lezione.orario.oraFine<=l.orario.oraFine){
                    throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario");
                }
                orariolezioni.add(l);
            }

            ;       
            System.out.println("GG");} catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
