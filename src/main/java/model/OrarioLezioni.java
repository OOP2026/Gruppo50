package model;
import java.util.ArrayList;
import java.util.function.Predicate;
import model.Responsabile.Token; 
import java.util.List;

//se non funziona la funzione ler vedere le lezione è perche
// il giorno probabilmente conclude con una i differente!
public class OrarioLezioni {
    private ArrayList<Lezione> orariolezioni;
    private final String[] giorni={"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì"};



    public OrarioLezioni(){
orariolezioni=new ArrayList<>();
    }



    public boolean aggiungiLezione(Lezione l, Token token)throws IllegalArgumentException, NullPointerException {
   //Solo il responsabile puo usare questo metodo
    if(token==null){ throw new NullPointerException("Non hai il permesso");}
   
     if (l == null){
        throw new NullPointerException("Questa lezione è vuota");
    }


    if(controlloConflittoLezione(l)){ 
      throw new IllegalArgumentException("C'è un conflitto con un'altra lezione");
     }
orariolezioni.add(l);
return true;

    }


public void visualizzaOrarioCompleto(Token token){
    if(token==null){
        System.out.println("Non hai il permesso");
        return;
    }

    System.out.println("Orario completo delle lezioni:");

    giornoLezioni(giorni[0],  orariolezioni, l -> true);
    giornoLezioni(giorni[1],  orariolezioni, l -> true);
    giornoLezioni(giorni[2],  orariolezioni, l -> true);
    giornoLezioni(giorni[3],  orariolezioni, l -> true);
    giornoLezioni(giorni[4],  orariolezioni, l -> true);

}

    private void giornoLezioni(String giorno, ArrayList<Lezione> elenco, Predicate<Lezione> filtro) {
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
public void visualizzaOrarioCompleto(Studente studente){

    System.out.println("Orario completo delle lezioni Studente: "+studente.nome+" "+studente.cognome);
    giornoLezioni(giorni[0],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    giornoLezioni(giorni[1],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    giornoLezioni(giorni[2],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    giornoLezioni(giorni[3],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);
    giornoLezioni(giorni[4],    orariolezioni, l -> l.insegnamento.AnnoCorso == studente.annoCorso);

}



//Docente
public void visualizzaOrarioCompleto(Docente docente){
 
    System.out.println("Orario completo delle lezioni Docente: "+docente.nome+" "+docente.cognome);
    giornoLezioni(giorni[0], orariolezioni, l -> l.insegnamento.docente == docente);
    giornoLezioni(giorni[1], orariolezioni, l -> l.insegnamento.docente == docente);
    giornoLezioni(giorni[2], orariolezioni, l -> l.insegnamento.docente == docente);
    giornoLezioni(giorni[3], orariolezioni, l -> l.insegnamento.docente == docente);
    giornoLezioni(giorni[4], orariolezioni, l -> l.insegnamento.docente == docente);
}





public List<Lezione> getOrarioLezioni(Token token) {
    if(token==null){
        System.out.println("Non hai il permesso");
        return new ArrayList<>();
    }
    return orariolezioni;
}

private boolean  controlloConflittoOrario(Lezione l, Lezione lezioneGiaPresente){
    int inizioNuovo = l.orario.getOrarioInizioInMinuti();
    int fineNuovo = l.orario.getOrarioFineInMinuti();
           
   int inizioEsistente = lezioneGiaPresente.orario.getOrarioInizioInMinuti();
 int fineEsistente = lezioneGiaPresente.orario.getOrarioFineInMinuti();

return (inizioNuovo<fineEsistente && fineNuovo>inizioEsistente);
}

private boolean controlloConflittoLezione(Lezione l){
    for (Lezione lf : orariolezioni) {
               boolean conflittoOrario= controlloConflittoOrario(l,lf); 
                if ((lf.orario.giorno.equals(l.orario.giorno))) {
                     if(conflittoOrario){
                     if(l.aula.Nome.equals(lf.aula.Nome)) return true;
                     if(l.insegnamento.docente.equals(lf.insegnamento.docente)) return true;
                          
                }

               }
         }
         return false;
}

}