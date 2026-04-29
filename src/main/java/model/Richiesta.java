package model;
import java.util.ArrayList;
public class Richiesta{
   public Docente docenteRichiedente;
   public String motivoRichiesta;
   public Orario orarioLezioneDaSpostare;
   public Orario nuovoOrarioLezione;
   public Richiesta(Docente docenteRichiedente, String motivoRichiesta, Orario orarioLezioneDaSpostare, Orario nuovoOrarioLezione) {
    this.docenteRichiedente = docenteRichiedente;
    this.motivoRichiesta = motivoRichiesta;
    this.orarioLezioneDaSpostare = orarioLezioneDaSpostare;
    this.nuovoOrarioLezione = nuovoOrarioLezione;
   }
}
