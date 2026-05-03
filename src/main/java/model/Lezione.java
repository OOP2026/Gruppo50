package model;

public class Lezione {
    public Insegnamento insegnamento;
    public Aula aula;
    public Orario orario;
    public Lezione(Insegnamento i ,Aula a , Orario o){
        this.insegnamento=i;
        this.aula=a;
        this.orario=o;

    }
   
    public boolean equals(Lezione lezione) {
      if(this.insegnamento.Nome.equals(lezione.insegnamento.Nome) && this.insegnamento.docente.email.equals(lezione.insegnamento.docente.email) && this.orario.giorno.equals(lezione.orario.giorno) && this.orario.oraInizio==lezione.orario.oraInizio && this.orario.minutoInizio==lezione.orario.minutoInizio && this.orario.oraFine==lezione.orario.oraFine && this.orario.minutoFine==lezione.orario.minutoFine){
          return true;
      }else{
          return false;
      }
    }
}
