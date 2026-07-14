package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
/**Questa classe rappresenta una fascia oraria
 * viene usata nelle classe{@link Lezione},{@link Richiesta} e {@link Vincolo}
 * */
public class Orario {
    /** Indica un giorno della settimana */
   private String giorno;
    /** È l'ora dell'orario di inizio  */
   private int oraInizio;
    /** Sono i minuti dell'orario di inizio */
    private int minutoInizio;
    /** È l'ora dell'orario di fine */
    private int oraFine;
    /** Sono i minuti dell'orario di fine. */
    private int minutoFine;
    /** È la lista dei giorni possibili con anche una versione senza accenti  */
    private static final List<String> giorni = new ArrayList<>(Arrays.asList(
            "lunedì", "martedì", "mercoledì", "giovedì", "venerdì",
            "lunedi", "martedi", "mercoledi", "giovedi", "venerdi"
    ));
    private static final Logger logger = Logger.getLogger(Orario.class.getName());
    /**
     * Costruisce un nuovo oggetto Orario assicurandosi che sia corretto
     * @param giorno il giorno della settimana per questo orario
     * @param oraInizio l'ora in cui inizia l'orario
     * @param minutoInizio i minuti in cui inizia l'orario
     * @param oraFine l'ora in cui termina l'orario
     * @param minutoFine i minuti in cui termina l'orario
     * @throws IllegalArgumentException se il giorno è nullo o non valido
     * @throws IllegalArgumentException se l'ora di inizio non è tra le 8 e le 17
     * @throws IllegalArgumentException se l'ora di fine non è tra le 8 e le 18
     * @throws IllegalArgumentException se i minuti non sono compresi tra 0 e 59
     * @throws IllegalArgumentException se l'orario di fine supera le 18:00
     * @throws IllegalArgumentException se l'orario di fine è precedente o uguale a quello di inizio
     */
    public Orario(String giorno, int oraInizio, int minutoInizio, int oraFine, int minutoFine) {
        checkThisGiorno(giorno);
        checkThisOrarioInizio(oraInizio, minutoInizio);
        checkThisOrarioFine(oraFine, minutoFine);
        checkConfrontoOrario(oraInizio, minutoInizio, oraFine, minutoFine);
        this.giorno = giorno.toLowerCase();
        this.oraInizio = oraInizio;
        this.minutoInizio = minutoInizio;
        this.oraFine = oraFine;
        this.minutoFine = minutoFine;
    }
    /**
     * Costruttore di copia.
     * Crea una nuova istanza di Orario copiando i dati da un altro oggetto Orario.
     * @param o l'oggetto Orario da copiare
     */
    public Orario (Orario o){
        this.giorno = o.giorno;
        this.oraInizio = o.oraInizio;
        this.minutoInizio = o.minutoInizio;
        this.oraFine = o.oraFine;
        this.minutoFine = o.minutoFine;
    }
    /**Controlla che l'orario di inizio sia compreso tra le 8 e le 17 e che i minuti siano compresi tra 0 e 59
     * @param oraInizio l'ora di inizio da controllare.
     * @param minutoInizio i minuti di inizio da controllare.
     * @throws IllegalArgumentException se l'orario di inizio non è valido.
     */
    private void checkThisOrarioInizio(int oraInizio,int minutoInizio){
        if(oraInizio<8 || oraInizio>17){throw new IllegalArgumentException("L'ora di inizio deve essere compresa tra 8 e 17");}
        if(minutoInizio<0 || minutoInizio>59){throw new IllegalArgumentException("Il minuto di inizio deve essere compreso tra 0 e 59");}
    }
    /**Controlla che l'orario di fine sia compreso tra le 8 e le 18 e che i minuti siano compresi tra 0 e 59
     * @param oraFine l'ora di fine da controllare.
     * @param minutoFine i minuti di fine da controllare.
     * @throws IllegalArgumentException se l'orario di fine non è valido.
     */
    private void checkThisOrarioFine(int oraFine,int minutoFine){
        if(oraFine<8 || oraFine>18){throw new IllegalArgumentException("L'ora di fine deve essere compresa tra 8 e 18");}
        if(minutoFine<0 || minutoFine>59){throw new IllegalArgumentException("Il minuto di fine deve essere compreso tra 0 e 59");}
        if(oraFine >= 18 && minutoFine > 0) {
            throw new IllegalArgumentException("L'ora di fine non può superare le 18:00");
        }
    }
    /**Controlla che l'orario di fine sia successivo a quello di inizio.
     * @param oraInizio l'ora di inizio da controllare.
     * @param minutoInizio i minuti di inizio da controllare.
     * @param oraFine l'ora di fine da controllare.
     * @param minutoFine i minuti di fine da controllare.
     * @throws IllegalArgumentException se l'orario di fine è precedente o uguale a quello di inizio.
     */
    private void checkConfrontoOrario(int oraInizio, int minutoInizio, int oraFine, int minutoFine){
        if(oraFine<oraInizio || (oraFine==oraInizio && minutoFine<=minutoInizio)){throw new IllegalArgumentException("L'orario di fine deve essere successivo all'orario di inizio");}
    }
    /**Controlla che l'orario di fine sia successivo a quello di inizio,
     * però usa le variabili della classe invece di riceverle come parametri
     * @throws IllegalArgumentException se l'orario di fine è precedente o uguale a quello di inizio.
     */
    private void checkConfrontoOrario(){
        if(oraFine<oraInizio || (oraFine==oraInizio && minutoFine<=minutoInizio)){throw new IllegalArgumentException("L'orario di fine deve essere successivo all'orario di inizio");}
    }
    /**Controlla che il giorno sia valido e non nullo
     * @param giorno il giorno da controllare
     * @throws IllegalArgumentException se il giorno non è valido
     * @throws NullPointerException se il giorno è nullo
     */
    private void checkThisGiorno(String giorno){
        //controlli se il giorno è scritto bene ed fa parte dei giorni della settimana
        if (giorno == null) {
            throw new NullPointerException("Il giorno non può essere nullo o vuoto");
        }
        if( !giorni.contains(giorno.toLowerCase())){
            String msg="Giorno errato: "+giorno;
            logger.info(msg);
            throw new IllegalArgumentException("Il giorno deve essere uno dei seguenti:" + giorni);}
    }


    /**
     * Ritorna l'orario di inizio in minuti
     * @return l'orario di inizio in minuti
     */
public int getOrarioInizioInMinuti() {
    return (this.oraInizio * 60) + this.minutoInizio;
}
    /**
     * Ritorna l'orario di fine in minuti
     * @return l'orario di fine in minuti
     */
public int getOrarioFineInMinuti() {
    return (this.oraFine * 60) + this.minutoFine;
}

    /**
     * Ritorna il valore in int del giorno
     * @return un numero intero da 1 (Lunedì) a 5 (Venerdì)
     * @throws IllegalArgumentException se il giorno memorizzato non è valido
     */
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
    //Setter and getters
    /** Imposta il giorno della settimana per questo orario, convertendolo in minuscolo e validandolo
     * @param giorno il giorno della settimana da impostare.
     * @throws IllegalArgumentException se il giorno non è valido.
     * @throws NullPointerException se il giorno è nullo.
     */
    public void setGiorno(String giorno){
      checkThisGiorno(giorno);
        this.giorno = giorno.toLowerCase();
    }
    /**
     * Restituisce il giorno della settimana.
     * @return il giorno come stringa
     */
    public String getGiorno() {
        return giorno;
    }
    /**
     * Imposta un nuovo orario di inizio, vedendo se il formato é valido e garantendo
     * che non sia successivo o uguale all'attuale orario di fine.
     * @param oraInizio la nuova ora di inizio.
     * @param minutoInizio i nuovi minuti di inizio.
     */
    public void setOrarioInizio(int oraInizio,int minutoInizio){
        checkThisOrarioInizio(oraInizio,minutoInizio);
        checkConfrontoOrario();
        this.oraInizio = oraInizio;
        this.minutoInizio = minutoInizio;
    }
    /**
     * Restituisce l'orario di inizio formattato.
     * @return una stringa nel formato "HH:MM".
     */
    public String getOrarioInizio(){
        return String.format("%d:%02d", this.oraInizio, this.minutoInizio);
    }
    /**
     * Imposta un nuovo orario di fine, vedendo se il formato é valido e garantendo
     * che sia strettamente successivo all'attuale orario di inizio.
     * @param oraFine la nuova ora di fine.
     * @param minutoFine i nuovi minuti di fine.
     */
    private void setOrarioFine(int oraFine, int minutoFine){
        checkThisOrarioFine(oraFine, minutoFine);
        checkConfrontoOrario();
        this.oraFine = oraFine;
        this.minutoFine = minutoFine;
    }
    /**
     * Restituisce l'orario di fine formattato.
     * @return una stringa nel formato "HH:MM".
     */
    public String getOrarioFine(){
        return String.format("%d:%02d", this.oraFine, this.minutoFine);
    }

    /**
     * Restituisce l'ora di fine (formato 24h).
     * @return l'ora di fine.
     */
    public int getOraFine() {
        return oraFine;
    }
    /**
     * Restituisce i minuti di fine.
     * @return i minuti di fine.
     */
    public int getMinutoFine () {
        return minutoFine;
    }
    /**
     * Restituisce l'ora di inizio (formato 24h).
     * @return l'ora di inizio.
     */
    public int getOraInizio() {
        return oraInizio;
    }
    /**
     * Restituisce i minuti di inizio.
     * @return i minuti di inizio.
     */
    public int getMinutoInizio() {
        return minutoInizio;
    }


    /**
     * Restituisce l'orario completo.
     * @return una stringa nel formato "HH:MM - HH:MM"
     */
    public String getOrarioCompleto() {
        return String.format("%d:%02d - %d:%02d", this.oraInizio, this.minutoInizio, this.oraFine, this.minutoFine);
    }


    /**
     * Verifica se due oggetti Orario sono uguali confrontando giorno, orario di inizio e orario di fine.
     * @param obj l'oggetto da confrontare con questo Orario
     * @return true se l'oggetto passato è uguale a questo, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Orario)) return false;
        Orario orario = (Orario) obj;
        return this.giorno.equals(orario.giorno)
                && this.getOrarioInizioInMinuti() == orario.getOrarioInizioInMinuti()
                && this.getOrarioFineInMinuti() == orario.getOrarioFineInMinuti();
    }

    /**
     * Genera un codice hash per questo oggetto Orario.
     * @return l'hashCode calcolato
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.giorno, this.getOrarioInizioInMinuti(), this.getOrarioFineInMinuti());
    }
}
