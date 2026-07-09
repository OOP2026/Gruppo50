package model;

public class Vincolo{
private final Orario orario;
public Vincolo(String giorno,int oraInizio, int minutoInizio, int oraFine, int minutoFine ){
this.orario= new Orario(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
  }
  public Orario getOrario(){
      return new Orario(this.orario);
  }
}