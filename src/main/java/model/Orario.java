package model;

import java.util.ArrayList;

public class Orario {
    public String giorno;
    public int oraInizio;
    public int minutoInizio;
    public int oraFine;
    public int minutoFine;
    private ArrayList<String> giorni= new ArrayList<String>(){{
        add("lunedì");
        add("martedì");
        add("mercoledì");
        add("giovedì");
        add("venerdì");
    }};
    public Orario(String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {
      try {
if(!giorni.contains(giorno.toLowerCase())){throw new IllegalArgumentException("Il giorno deve essere uno dei seguenti: " + giorni);}
if(oraInizio<0 || oraInizio>23){throw new IllegalArgumentException("L'ora di inizio deve essere compresa tra 0 e 23");}
if(minutoInizio<0 || minutoInizio>59){throw new IllegalArgumentException("Il minuto di inizio deve essere compreso tra 0 e 59");}
if(oraFine<0 || oraFine>23){throw new IllegalArgumentException("L'ora di fine deve essere compresa tra 0 e 23");}
if(minutoFine<0 || minutoFine>59){throw new IllegalArgumentException("Il minuto di fine deve essere compreso tra 0 e 59");}
if(oraFine<oraInizio || (oraFine==oraInizio && minutoFine<=minutoInizio)){throw new IllegalArgumentException("L'orario di fine deve essere successivo all'orario di inizio");}      
      } catch (IllegalArgumentException e) {
        System.out.println("errore nell'orario: "+e.getMessage());
    }

        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.minutoInizio = minutoInizio;
        this.oraFine = oraFine;
        this.minutoFine = minutoFine;
    }

}
