package model;
import java.util.ArrayList;

import model.Responsabile.Token;

//se non funziona la funzione ler vedere le lezione è perche
// il giorno probabilemnte conclude con una i differente!
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
GiornoLezioni("Lunedì",ElencoLezioni);
GiornoLezioni("Martedì",ElencoLezioni);
GiornoLezioni("Mercoledì",ElencoLezioni);
GiornoLezioni("Giovedì",ElencoLezioni);
GiornoLezioni("Venerdi",ElencoLezioni);

}

private void GiornoLezioni(String giorno, ArrayList<Lezione> ElencoLezioni){
    System.out.println(giorno);
    boolean trovata=false;
   
    for(Lezione lezione : ElencoLezioni){
        
 if(giorno.equalsIgnoreCase(lezione.orario.giorno) == false){continue;}
        System.out.println("Numero lezione: "+ElencoLezioni.indexOf(lezione));
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

//Studente
public void visualizzaOrarioCompleto(Studente studente,OrarioLezioni o){
  ArrayList<Lezione> ElencoLezioni = o.orariolezioni;
    System.out.println("Orario completo delle lezioni:");
GiornoLezioni("Lunedì", studente.annoCorso,ElencoLezioni);
GiornoLezioni("Martedì", studente.annoCorso,ElencoLezioni);
GiornoLezioni("Mercoledì", studente.annoCorso,ElencoLezioni);
GiornoLezioni("Giovedì", studente.annoCorso,ElencoLezioni);
GiornoLezioni("Venerdi", studente.annoCorso,ElencoLezioni);

}

private void GiornoLezioni(String giorno,int annoCorso, ArrayList<Lezione> ElencoLezioni){
    System.out.println(giorno);
    boolean trovata=false;
    for(Lezione lezione : ElencoLezioni){
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

//Docente
public void visualizzaOrarioCompleto(Docente docente, OrarioLezioni o){
  ArrayList<Lezione> ElencoLezioni = o.orariolezioni;
    System.out.println("Orario completo delle lezioni:");
GiornoLezioni("Lunedì", docente, ElencoLezioni);
GiornoLezioni("Martedì", docente, ElencoLezioni);
GiornoLezioni("Mercoledì", docente, ElencoLezioni);
GiornoLezioni("Giovedì", docente, ElencoLezioni);
GiornoLezioni("Venerdi", docente, ElencoLezioni);
}

private void GiornoLezioni(String giorno, Docente docente, ArrayList<Lezione> ElencoLezioni){
    System.out.println(giorno);
    boolean trovata=false;
    for(Lezione lezione : ElencoLezioni){
 if(giorno.equalsIgnoreCase(lezione.orario.giorno) == false || lezione.insegnamento.docente != docente){continue;}
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