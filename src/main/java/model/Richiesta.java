package model;
enum StatoRichiesta {
    IN_ATTESA,
    APPROVATA,
    RIFIUTATA
}
public class Richiesta{
   public Docente docenteRichiedente;
   public String motivoRichiesta;
   public Orario orarioLezioneDaSpostare;
   public Orario nuovoOrarioLezione;
   public StatoRichiesta statoRichiesta= StatoRichiesta.IN_ATTESA;
   
   public Richiesta(Docente docenteRichiedente, String motivoRichiesta, Orario orarioLezioneDaSpostare, Orario nuovoOrarioLezione) {
    this.docenteRichiedente = docenteRichiedente;
    this.motivoRichiesta = motivoRichiesta;
    this.orarioLezioneDaSpostare = orarioLezioneDaSpostare;
    this.nuovoOrarioLezione = nuovoOrarioLezione;
    if (orarioLezioneDaSpostare.equals(nuovoOrarioLezione)){
        throw new IllegalArgumentException("La richiesta non può avere il nuovo orario uguale a quello vecchio");
    }
   }
   ///Questo metodo a come scopo quello di individuare la lezione a cui si riferisce la richiesta
   public boolean equalsLezione(Lezione l){
       return this.docenteRichiedente.email.equals(l.insegnamento.docente.email) &&
        this.orarioLezioneDaSpostare.equals(l.orario);
   }

}
