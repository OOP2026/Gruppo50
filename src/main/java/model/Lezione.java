package model;


public class Lezione implements Comparable<Lezione> {
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
///Ritorna le informazione della lezione come l'orario completo, la materia, l'aula e il docente che la fa.
   public String infoLezione(){
     return "Insegnamento: "+this.insegnamento.Nome+" Docente: "+this.insegnamento.docente.nome+" "+this.insegnamento.docente.cognome+" Aula: "+this.aula.Nome+" Orario: "+this.orario.getOrarioCompleto();
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

    //Ordina le lezioni per giorno e orario!
    @Override
    public int compareTo(Lezione l) {
        int giorno1 = this.orario.giornoToInt();
        int giorno2 = l.orario.giornoToInt();
        if (giorno1 != giorno2) {
            return Integer.compare(giorno1, giorno2);
        }
      int orarioMinuti= this.orario.getOrarioInizioInMinuti();
        int orarioMinuti2= l.orario.getOrarioInizioInMinuti();
        return Integer.compare(orarioMinuti, orarioMinuti2);
    }
    
}
