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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lezione)) return false;
        Lezione lezione = (Lezione) obj;
        return this.insegnamento.equals(lezione.insegnamento)
                && this.orario.equals(lezione.orario)
                && this.aula.Nome.equals(lezione.aula.Nome);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.insegnamento, this.orario, this.aula.Nome);
    }

    
}
