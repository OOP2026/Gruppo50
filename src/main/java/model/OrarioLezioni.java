package model;
import java.util.ArrayList;

public class OrarioLezioni {
    protected ArrayList<Lezione> orariolezioni=new ArrayList<Lezione>();
     
    public OrarioLezioni(){

    }

    public  void AggiungiLezione(Lezione l){
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
                if(lezione.orario.oraInizio<=l.orario.oraInizio && lezione.orario.oraFine<=l.orario.oraFine){
                    throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario");
                }
                orariolezioni.add(l);
            }

            ;       
            System.out.println("GG");} catch (Exception e) {
            System.out.println(e.getMessage());
              System.exit(1);
        }
    }

public void visualizzaOrarioCompleto(){
System.out.println("Orario completo delle lezioni:");
GiornoLezioni("Lunedì");
GiornoLezioni("Martedì");
GiornoLezioni("Mercoledì");
GiornoLezioni("Giovedì");
GiornoLezioni("Venerdi");

}
private void GiornoLezioni(String giorno){
    System.out.println(giorno);
    boolean trovata=false;
    for(Lezione lezione : orariolezioni){
 if(giorno.equalsIgnoreCase(lezione.orario.giorno) == false){continue;}
        System.out.println("Docente: "+lezione.insegnamento.docente.nome+" "+lezione.insegnamento.docente.cognome);
    System.out.println("Insegnamento: "+lezione.insegnamento.Nome);
    System.out.println("Anno corso: "+lezione.insegnamento.AnnoCorso);
    System.out.println("Orario: "+lezione.orario.oraInizio+":"+lezione.orario.minutoInizio+"-"+lezione.orario.oraFine+":"+lezione.orario.minutoFine);
    System.out.println("Aula: "+lezione.aula.Nome);
    trovata=true;
}
if(!trovata){System.out.println("Non ci sono lezioni in questo giorno");}
if(giorno.equalsIgnoreCase("Venerdi")){
System.out.println("------------------------Fine Orario--------------------------"); 
return;}
System.out.println("--------------------------------------------------");
}


public void visualizzaOrarioCompleto(Studente studente){
System.out.println("Orario completo delle lezioni:");
GiornoLezioni("Lunedì", studente.annoCorso);
GiornoLezioni("Martedì", studente.annoCorso);
GiornoLezioni("Mercoledì", studente.annoCorso);
GiornoLezioni("Giovedì", studente.annoCorso);
GiornoLezioni("Venerdi", studente.annoCorso);

}

private void GiornoLezioni(String giorno,int annoCorso){
    System.out.println(giorno);
    boolean trovata=false;
    for(Lezione lezione : orariolezioni){
 if(giorno.equalsIgnoreCase(lezione.orario.giorno) == false || lezione.insegnamento.AnnoCorso != annoCorso){continue;}
        System.out.println("Docente: "+lezione.insegnamento.docente.nome+" "+lezione.insegnamento.docente.cognome);
    System.out.println("Insegnamento: "+lezione.insegnamento.Nome);
    System.out.println("Orario: "+lezione.orario.oraInizio+":"+lezione.orario.minutoInizio+"-"+lezione.orario.oraFine+":"+lezione.orario.minutoFine);
    System.out.println("Aula: "+lezione.aula.Nome);
    trovata=true;
}
if(!trovata){System.out.println("Non ci sono lezioni in questo giorno");}
if(giorno.equalsIgnoreCase("Venerdi")){
System.out.println("------------------------Fine Orario--------------------------"); 
return;}
System.out.println("--------------------------------------------------");
}
}