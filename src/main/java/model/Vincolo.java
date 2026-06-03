package model;

public class Vincolo{
public Orario orario;
public Vincolo(String giorno,int oraInzio, int MinutoInzio, int OraFine, int MinutoFine ){
this.orario= new Orario(giorno, oraInzio, MinutoInzio, OraFine, MinutoFine);

}
}