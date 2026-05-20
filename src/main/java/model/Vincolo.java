package model;

public class Vincolo{
protected Orario orario;
public Vincolo(String giorno,int oraInzio, int MinutoInzio, int OraFine, int MinutoFine ){
this.orario= new Orario(giorno, oraInzio, MinutoInzio, OraFine, MinutoFine);

}
}