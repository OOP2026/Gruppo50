package model;
import java.util.ArrayList;
import java.util.function.Predicate;
import model.Responsabile.Token;

//se non funziona la funzione ler vedere le lezione è perche
// il giorno probabilmente conclude con una i differente!
public class OrarioLezioni {
    private ArrayList<Lezione> orariolezioni=new ArrayList<Lezione>();
     
    public OrarioLezioni(){

    }

    public boolean AggiungiLezione(Lezione l, Token token)throws IllegalArgumentException, NullPointerException {
   
    if(token==null){
        throw new NullPointerException("Non hai il permesso");
    }
            if (l == null) {
                throw new NullPointerException("Questa lezione è vuota");
            }

            
            int inizioNuova = (l.orario.oraInizio * 60) + l.orario.minutoInizio;
            int fineNuova = (l.orario.oraFine * 60) + l.orario.minutoFine;
           

           
            for (Lezione lezioneGiaPresente : orariolezioni) {
                
                
                if (!lezioneGiaPresente.orario.giorno.equals(l.orario.giorno)) {
                    continue;
                }
                
                if(!lezioneGiaPresente.insegnamento.docente.equals(l.insegnamento.docente)){
                    continue;
                }
                
                 
                
               //lezione giorno orario = lezione giorno orario & lezione insegneamento docente = lezione insegnamento docente


                int inizioEsistente = (lezioneGiaPresente.orario.oraInizio * 60) + lezioneGiaPresente.orario.minutoInizio;
                int fineEsistente = (lezioneGiaPresente.orario.oraFine * 60) + lezioneGiaPresente.orario.minutoFine;


                if ((inizioNuova < fineEsistente && inizioEsistente < fineNuova) && l.insegnamento.Nome.equals(lezioneGiaPresente.insegnamento.Nome)) {
                    throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario con questo docente");
                }  

                if (inizioNuova < fineEsistente && inizioEsistente < fineNuova) {
                    throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario");
                }
                 
                 
            }
           

          
        orariolezioni.add(l);
           return true;

        

    }


public void visualizzaOrarioCompleto(Token token,OrarioLezioni o){
    if(token==null){
        System.out.println("Non hai il permesso");
        return;
    }

      ArrayList<Lezione> ElencoLezioni = o.orariolezioni;
System.out.println("Orario completo delle lezioni:");

GiornoLezioni("Lunedì",  ElencoLezioni, l -> true);
GiornoLezioni("Martedì",  ElencoLezioni, l -> true);
GiornoLezioni("Mercoledì",  ElencoLezioni, l -> true);
GiornoLezioni("Giovedì",  ElencoLezioni, l -> true);
GiornoLezioni("Venerdì",  ElencoLezioni, l -> true);

}

    private void GiornoLezioni(String giorno, ArrayList<Lezione> elenco, Predicate<Lezione> filtro) {
        System.out.println(giorno);
        boolean trovata = false;
        for (Lezione l : elenco) {
            if (!giorno.equalsIgnoreCase(l.orario.giorno)) continue;
            if (!filtro.test(l)) continue;
            // stampa campi comuni...
            trovata = true;
        }
        if (!trovata) System.out.println("Non ci sono lezioni in questo giorno");
        if (giorno.equalsIgnoreCase("Venerdi")) {
            System.out.println("---Fine Orario---");
            return;
        }
        System.out.println("------------------");
    }

//Studente
public void visualizzaOrarioCompleto(Studente studente,OrarioLezioni o){
  ArrayList<Lezione> ElencoLezioni = o.orariolezioni;
    System.out.println("Orario completo delle lezioni Studente: "+studente.nome+" "+studente.cognome);
    GiornoLezioni("Lunedì",    ElencoLezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    GiornoLezioni("Martedì",    ElencoLezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    GiornoLezioni("Mercoledì",    ElencoLezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    GiornoLezioni("Giovedì",    ElencoLezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    GiornoLezioni("Venerdì",    ElencoLezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);

}



//Docente
public void visualizzaOrarioCompleto(Docente docente, OrarioLezioni o){
  ArrayList<Lezione> ElencoLezioni = o.orariolezioni;
    System.out.println("Orario completo delle lezioni Docente: "+docente.nome+" "+docente.cognome);
    GiornoLezioni("Lunedì", ElencoLezioni, l -> l.insegnamento.docente == docente);
    GiornoLezioni("Martedì", ElencoLezioni, l -> l.insegnamento.docente == docente);
    GiornoLezioni("Mercoledì", ElencoLezioni, l -> l.insegnamento.docente == docente);
    GiornoLezioni("Giovedì", ElencoLezioni, l -> l.insegnamento.docente == docente);
    GiornoLezioni("Venerdì", ElencoLezioni, l -> l.insegnamento.docente == docente);
}





public ArrayList<Lezione> getOrarioLezioni(Token token) {
    if(token==null){
        System.out.println("Non hai il permesso");
        return null;
    }
    return orariolezioni;
}



}