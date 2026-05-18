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
      if(this.insegnamento.equals(lezione.insegnamento) && this.orario.equals(lezione.orario) && this.aula.Nome.equals(lezione.aula.Nome))
          return true;
      
      return false;
    }

    
}
