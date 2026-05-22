package model;

enum StatoRichiesta {
    IN_ATTESA,
    APPROVATA,
    RIFIUTATA
}
public class Richiesta{
   protected Docente docenteRichiedente;
   protected String motivoRichiesta;
   protected Orario orarioLezioneDaSpostare;
   protected Orario nuovoOrarioLezione;
   protected StatoRichiesta statoRichiesta= StatoRichiesta.IN_ATTESA;
   
   public Richiesta(Docente docenteRichiedente, String motivoRichiesta, Orario orarioLezioneDaSpostare, Orario nuovoOrarioLezione) {
    this.docenteRichiedente = docenteRichiedente;
    this.motivoRichiesta = motivoRichiesta;
    this.orarioLezioneDaSpostare = orarioLezioneDaSpostare;
    this.nuovoOrarioLezione = nuovoOrarioLezione;
   }
}
