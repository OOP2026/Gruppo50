package model;

public class Lezione {
    public Insegnamento insegnamento;
    public Aula aula;
    public Orario orario;
    //se volete potete aggiungere il campo token cosi solo il responsabile può creare le lezioni
    //però penso non sia necessario visto che la funzione inserisciLezione è presente solo nella classe responsabile
    // Ed è l'unico metodo per inserire la lezione nella classe OrarioLezioni, quindi se non è presente il token non si può inserire la lezione nell'orario
    public Lezione(Insegnamento i ,Aula a , Orario o){
        this.insegnamento=i;
        this.aula=a;
        this.orario=o;
        
    }
   //permette di confrontare due lezioni e vedere se sono uguali o meno, utile per verificare se una lezione è già presente nell'orario o per verificare se una richiesta di spostamento riguarda la stessa lezione
    public boolean equals(Lezione lezione) {
      if(this.insegnamento.Nome.equals(lezione.insegnamento.Nome) && this.insegnamento.docente.email.equals(lezione.insegnamento.docente.email) && this.orario.giorno.equals(lezione.orario.giorno) && this.orario.oraInizio==lezione.orario.oraInizio && this.orario.minutoInizio==lezione.orario.minutoInizio && this.orario.oraFine==lezione.orario.oraFine && this.orario.minutoFine==lezione.orario.minutoFine){
          return true;
      }else{
          return false;
      }
    }
}
