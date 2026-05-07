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
        add("lunedi");
        add("martedi");
        add("mercoledi");
        add("giovedi");
        add("venerdi"); 
    }};
    public Orario(String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {
      try {
        //controlli se il giorno è scritto bene ed fa parte dei giorni della settimana
if( giorno==null ||!giorni.contains(giorno.toLowerCase())){throw new IllegalArgumentException("Il giorno deve essere uno dei seguenti: " + giorni);}
//Controlla se le ore sono compresi negli intervalli validi e se l'orario di fine è successivo a quello di inizio
if(oraInizio<8 || oraInizio>17){throw new IllegalArgumentException("L'ora di inizio deve essere compresa tra 8 e 17");}
if(minutoInizio<0 || minutoInizio>59){throw new IllegalArgumentException("Il minuto di inizio deve essere compreso tra 0 e 59");}
if(oraFine<8 || oraFine>18){throw new IllegalArgumentException("L'ora di fine deve essere compresa tra 8 e 18");}
if(minutoFine<0 || minutoFine>59){throw new IllegalArgumentException("Il minuto di fine deve essere compreso tra 0 e 59");}
if(oraFine<oraInizio || (oraFine==oraInizio && minutoFine<=minutoInizio)){throw new IllegalArgumentException("L'orario di fine deve essere successivo all'orario di inizio");}      
      } catch (IllegalArgumentException e) {
        //Finisce il programma
        System.out.println("errore nell'orario: "+e.getMessage());
        System.exit(1);
    }

        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.minutoInizio = minutoInizio;
        this.oraFine = oraFine;
        this.minutoFine = minutoFine;
    }
    

}
