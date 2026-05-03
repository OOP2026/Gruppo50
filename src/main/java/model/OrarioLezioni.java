package model;
import java.util.ArrayList;

import model.Responsabile.Token;

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
                
                
                if (!lezioneGiaPresente.orario.giorno.equals(l.orario.giorno) || 
                    !lezioneGiaPresente.aula.Nome.equals(l.aula.Nome)) {
                    continue;
                }

               
                int inizioEsistente = (lezioneGiaPresente.orario.oraInizio * 60) + lezioneGiaPresente.orario.minutoInizio;
                int fineEsistente = (lezioneGiaPresente.orario.oraFine * 60) + lezioneGiaPresente.orario.minutoFine;

                if (inizioNuova < fineEsistente && inizioEsistente < fineNuova) {
                    throw new IllegalArgumentException("C'è già una lezione in questa aula in questo orario");
                }
            }

          
        orariolezioni.add(l);
           return true;

        

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

public ArrayList<Lezione> getOrarioLezioni(Token token) {
    if(token==null){
        System.out.println("Non hai il permesso");
        return null;
    }
    return orariolezioni;
}
}