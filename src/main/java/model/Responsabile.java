package model;
import java.util.ArrayList;

public class Responsabile extends Docente {
  ArrayList<Richiesta> richiesteSpostamento;
private final Token token;
    public Responsabile(String nome, String cognome, String email, String login, String password, String matematica) {
        super(nome, cognome, email, login, password);
        richiesteSpostamento= new ArrayList<Richiesta>();
        this.token = new Token();
    }
    @Override
    public void saluto() {
        System.out.println("Ciao, sono il responsabile " + this.nome + " " + this.cognome);
    }
   
    public void visualizzaRichiesteSpostamento() {
        int numeroRichiesta=0;
        int numeroRichieste=richiesteSpostamento.size();
            System.out.println("Richieste di spostamento:");
            if(numeroRichieste==0){
                System.out.println("Non ci sono richieste di spostamento");
                 System.out.println("-----Fine-----");
                return;
            }
        //Implementazione del metodo per visualizzare le richieste di spostamento delle lezioni
  for(Richiesta richiesta : richiesteSpostamento) {
    System.out.println("Numero richiesta: " + (numeroRichiesta));
    System.out.println("Docente richiedente: " + richiesta.docenteRichiedente.nome + " " + richiesta.docenteRichiedente.cognome);
    System.out.println("Motivo della richiesta: " + richiesta.motivoRichiesta);
    System.out.println("Orario lezione da spostare: " + richiesta.orarioLezioneDaSpostare.getOrarioCompleto());
    System.out.println("Orario lezione nuovo: " + richiesta.nuovoOrarioLezione.getOrarioCompleto());
    System.out.println("Stato Richiesta: "+richiesta.statoRichiesta);
    if(numeroRichieste==numeroRichiesta+1){
        System.out.println("-----Fine-----");
   return; }
    System.out.println("------------------------------------------------");
    numeroRichiesta++;
}
// System.out.println("----------------------Fine Richieste di Spostamento--------------------------");

    }
//La funzione inserisce una lezione nell'orario, controllando che non ci siano conflitti con altre lezioni e che il docente sia disponibile in quell'orario
    protected void inserisciLezione(Insegnamento insegnamento, Aula aula, Orario orario, OrarioLezioni ElencoLezioni) {
        //Implementazione del metodo per creare una nuova lezione
        if(!(VerificaDisponibilita(insegnamento.docente.getVincoli(), orario))){
            System.out.println("Il docente non è disponibile in questo orario");
            return;
        }
Lezione nuovaLezione = new Lezione(insegnamento, aula, orario);
try{
    ElencoLezioni.AggiungiLezione(nuovaLezione,this.token);
}catch(Exception e){
    System.out.println("Errore nella creazione della lezione: " + e.getMessage());

}
System.out.println("Lezione aggiunta con successo responsabile"); 
    }
//La funzione inserisce una lezione nell'orario, controllando che non ci siano conflitti con altre lezioni e che il docente sia disponibile in quell'orario
public void inserisciLezione(Lezione l, OrarioLezioni ElencoLezioni) {
    if(!(VerificaDisponibilita(l.insegnamento.docente.getVincoli(), l.orario))){
            System.out.println("Il docente non è disponibile in questo orario");
            return;
        }
        //Implementazione del metodo per creare una nuova lezione
Lezione nuovaLezione = l;
try{
    ElencoLezioni.AggiungiLezione(nuovaLezione,this.token);
}catch(Exception e){
    System.out.println("Errore nella creazione della lezione: " + e.getMessage());

}

System.out.println("Lezione aggiunta con successo responsabile"); 
}

public void SpostamentoLezione(int numeroRichiesta, OrarioLezioni ElencoLezioni){

    Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);
    Lezione lezioneDaSpostare = null;
if(richiesta==null){
    System.out.println("La richiesta non esiste");
    return;
}
if(richiesta.statoRichiesta==StatoRichiesta.APPROVATA){
    System.out.println("La richiesta è già stata approvata");
    return;
}
if(richiesta.statoRichiesta==StatoRichiesta.RIFIUTATA){
    System.out.println("La richiesta è già stata rifiutata");
    return;
}
     if(richiesta.statoRichiesta==StatoRichiesta.IN_ATTESA){

         lezioneDaSpostare = cercaLezioneDaSpostare(richiesta, ElencoLezioni);
         if(lezioneDaSpostare == null){
             System.out.println("La lezione da spostare non è stata trovata");
             return;
         }

          try{
                ElencoLezioni.getOrarioLezioni(this.token).remove(lezioneDaSpostare);
            }
            
            catch(Exception e){
                System.out.println("Errore nello spostamento della lezione: " + e.getMessage());
                return;
            }
               Lezione nuovaLezione = new Lezione(lezioneDaSpostare.insegnamento, lezioneDaSpostare.aula, richiesta.nuovoOrarioLezione);
               
                try {
                     ElencoLezioni.AggiungiLezione(nuovaLezione,this.token);
                    
                } catch (IllegalArgumentException e1) {
                    System.out.println("Errore nello spostamento della lezione: " + e1.getMessage());
                    richiesta.statoRichiesta=StatoRichiesta.RIFIUTATA;
                    System.out.println("La richiesta è stata rifiutata");
                    System.out.println("Tentativo di ripristinare la lezione originale...");
                    try {
                        ElencoLezioni.AggiungiLezione(lezioneDaSpostare,this.token);
                    } catch (Exception e2) {
                        System.out.println("Errore nel ripristino della lezione originale: " + e2.getMessage());
                    }
                    return;
                }

         richiesta.statoRichiesta=StatoRichiesta.APPROVATA;
                System.out.println("La richiesta è stata approvata");
                

        }

  
 } 

 public void rifiutaRichiesta(int numeroRichiesta){
//get(); ottiene la richiesta usando numeroRichiesta. cosi trova la posizione della richiesta
    Richiesta richiesta = richiesteSpostamento.get(numeroRichiesta);
    if(richiesta==null){
        System.out.println("La richiesta non esiste");
        return;
    }
    if(richiesta.statoRichiesta==StatoRichiesta.APPROVATA){
        System.out.println("La richiesta è stata gia approvata non puo essere rifiutata");
        return;
    }
     if(richiesta.statoRichiesta==StatoRichiesta.RIFIUTATA){
        System.out.println("La richiesta è stata gia rifiutata");
        return;
    }
    richiesta.statoRichiesta=StatoRichiesta.RIFIUTATA;
    System.out.println("La richiesta è stata rifiutata");
 }
 public void cambiaOrarioRichiesta(int numeroRichiesta){

 };
 public void VisualizzaOrarioCompleto(OrarioLezioni ElencoLezioni){

    ElencoLezioni.visualizzaOrarioCompleto(this.token,ElencoLezioni);
 }
 

//permette di verificare se l'orario viola uno dei vincoli del docente
//cioe controlla se il docente è disponibile in quella fascia oraria
private boolean VerificaDisponibilita(ArrayList<Vincolo> vincoli, Orario orario){
    if(vincoli.size()==0) return true;
for(Vincolo vincolo:vincoli){
int orarioInizioVincolo= (vincolo.orario.oraInizio*60)+vincolo.orario.minutoInizio;
int orarioFineVincolo= (vincolo.orario.oraFine*60)+vincolo.orario.minutoFine;
int orarioInizioLezione= (orario.oraInizio*60)+orario.minutoInizio;
int orarioFineLezione= (orario.oraFine*60)+orario.minutoFine;

if(!vincolo.orario.giorno.equals(orario.giorno))continue;

if(orarioInizioLezione>orarioFineVincolo || orarioFineLezione<orarioInizioVincolo){
    continue;}
    else{return false;} 
 
}
return true;
}
private Lezione cercaLezioneDaSpostare(Richiesta R, OrarioLezioni O){
          for(Lezione lezione : O.getOrarioLezioni(this.token)) {
        if(lezione.insegnamento.docente.email.equals(R.docenteRichiedente.email)==false){
            continue;
        }
        if(lezione.orario.giorno.equals(R.orarioLezioneDaSpostare.giorno)==false){
            continue;
        }
        if(lezione.orario.oraInizio==R.orarioLezioneDaSpostare.oraInizio && lezione.orario.minutoInizio==R.orarioLezioneDaSpostare.minutoInizio){
            if(lezione.orario.oraFine==R.orarioLezioneDaSpostare.oraFine && lezione.orario.minutoFine==R.orarioLezioneDaSpostare.minutoFine){
               
              return lezione;
        }
     
    }

     }
return null;
    }
// Il token serve per usare alcuni metodi che solo il responsabile puo usare
public class Token {

    private Token() {

    }

}

}