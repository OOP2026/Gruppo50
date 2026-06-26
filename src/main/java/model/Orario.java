package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**Questa classe rappresenta una fascia oraria
 * viene usata nelle classe{@link Lezione},{@link Richiesta} e {@link Vincolo}
 * */
public class Orario {
    ///Indica un giorno della settimana
    public String giorno;
    ///È l'ora dell'orario di inizio
    public int oraInizio;
    /// Sono i minuti dell'orario di inizio
    public int minutoInizio;
    ///È l'ora dell'orario di fine
    public int oraFine;
    ///Sono i minuti dell'orario fine
    public int minutoFine;
    ///È la lista dei giorni possibili
    private static final List<String> giorni = new ArrayList<>(Arrays.asList(
            "lunedì", "martedì", "mercoledì", "giovedì", "venerdì",
            "lunedi", "martedi", "mercoledi", "giovedi", "venerdi"
    ));
    public Orario(String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {

        //controlli se il giorno è scritto bene ed fa parte dei giorni della settimana
if( giorno==null ||!giorni.contains(giorno.toLowerCase())){throw new IllegalArgumentException("Il giorno deve essere uno dei seguenti: " + giorni);}
//Controlla se le ore sono compresi negli intervalli validi e se l'orario di fine è successivo a quello di inizio
if(oraInizio<8 || oraInizio>17){throw new IllegalArgumentException("L'ora di inizio deve essere compresa tra 8 e 17");}
if(oraFine<8 || oraFine>18){throw new IllegalArgumentException("L'ora di fine deve essere compresa tra 8 e 18");}
if(minutoInizio<0 || minutoInizio>59){throw new IllegalArgumentException("Il minuto di inizio deve essere compreso tra 0 e 59");}

if(oraFine == 18 && minutoFine > 0) {
    throw new IllegalArgumentException("L'ora di fine non può superare le 18:00");
}


if(oraFine<oraInizio || (oraFine==oraInizio && minutoFine<=minutoInizio)){throw new IllegalArgumentException("L'orario di fine deve essere successivo all'orario di inizio");}      


        this.giorno = giorno.toLowerCase();
        this.oraInizio = oraInizio;
        this.minutoInizio = minutoInizio;
        this.oraFine = oraFine;
        this.minutoFine = minutoFine;
    }
    ///Ritorna l'orario di inizio in minuti
public int getOrarioInizioInMinuti() {
    return (this.oraInizio * 60) + this.minutoInizio;
}
///Ritorna l'orario di fine in minuti
public int getOrarioFineInMinuti() {
    return (this.oraFine * 60) + this.minutoFine;
}
///Restituisce l'orario completo
public String getOrarioCompleto() {
        return String.format("%d:%02d - %d:%02d", this.oraInizio, this.minutoInizio, this.oraFine, this.minutoFine);
    }
    ///Ritorna il valore in int del giorno
    protected int giornoToInt() {
        switch (this.giorno.toLowerCase()) {
            case "lunedì":
            case "lunedi":
                return 1;
            case "martedì":
            case "martedi":
                return 2;
            case "mercoledì":
            case "mercoledi":
                return 3;
            case "giovedì":
            case "giovedi":
                return 4;
            case "venerdì":
            case "venerdi":
                return 5;
            default:
                throw new IllegalArgumentException("Giorno non valido: " + this.giorno);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Orario)) return false;
        Orario orario = (Orario) obj;
        return this.giorno.equals(orario.giorno)
                && this.getOrarioInizioInMinuti() == orario.getOrarioInizioInMinuti()
                && this.getOrarioFineInMinuti() == orario.getOrarioFineInMinuti();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.giorno, this.getOrarioInizioInMinuti(), this.getOrarioFineInMinuti());
    }
}
