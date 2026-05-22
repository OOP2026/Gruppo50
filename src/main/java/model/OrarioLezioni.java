package model;
import java.util.ArrayList;
import java.util.function.Predicate;
import model.Responsabile.Token;

//se non funziona la funzione ler vedere le lezione è perche
// il giorno probabilmente conclude con una i differente!
public class OrarioLezioni {
    private ArrayList<Lezione> orariolezioni;
     
    public OrarioLezioni(){
orariolezioni=new ArrayList<Lezione>();
    }

    public boolean AggiungiLezione(Lezione l, Token token)throws IllegalArgumentException, NullPointerException {
   //Solo il responsabile puo usare questo metodo
    if(token==null){
        throw new NullPointerException("Non hai il permesso");
    }
            if (l == null) {
                throw new NullPointerException("Questa lezione è vuota");
            }

        
            for (Lezione lf : orariolezioni) {
               Boolean conflittoOrario= controlloConflittoOrario(l,lf); 
                if ((lf.orario.giorno.equals(l.orario.giorno))) {
                    System.out.println(l.orario.giorno+" "+l.orario.getOrarioCompleto()+" "+lf.orario.getOrarioCompleto());
                     if(conflittoOrario){
                        System.out.println("l'orario e conflittato");
                         if(l.aula.Nome.equals(lf.aula.Nome)) return false;
                         System.out.println("Aula diversa"+" "+l.aula.Nome+" "+lf.aula.Nome+" "+l.aula.Nome.equals(lf.aula.Nome));
                         if(l.insegnamento.docente.equals(lf.insegnamento.docente)) return false;
                          System.out.println("Docente diverso!");
            
                     }

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
            if (!giorno.equalsIgnoreCase(l.orario.giorno) || !filtro.test(l)) continue;
        
            System.out.println("Docente: "+l.insegnamento.docente.nome+" "+l.insegnamento.docente.cognome);
            System.out.println("Insegamento: "+l.insegnamento.Nome);
            System.out.println("Orario: "+l.orario.getOrarioCompleto());
            System.out.println("Aula: "+l.aula.Nome);
            
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

private boolean  controlloConflittoOrario(Lezione l, Lezione lezioneGiaPresente){
    int inizioNuovo = l.orario.getOrarioInizioInMinuti();
    int fineNuovo = l.orario.getOrarioFineInMinuti();
           
   int inizioEsistente = lezioneGiaPresente.orario.getOrarioInizioInMinuti();
 int fineEsistente = lezioneGiaPresente.orario.getOrarioFineInMinuti();
   
 if(inizioNuovo<fineEsistente && fineNuovo>inizioEsistente) return true;
    
             
        
return false;
}

}