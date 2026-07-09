package model;
/**
 * Rappresenta una singola lezione collocata nell'orario settimanale.
 * <p>
 * Una lezione associa un {@link Insegnamento} (e quindi un docente),
 * un'{@link Aula} e un {@link Orario}. La classe implementa
 * {@link Comparable} per consentire l'ordinamento delle lezioni
 * prima per giorno e poi per orario di inizio.
 * </p>
 *
 * @see Insegnamento
 * @see Aula
 * @see Orario
 * @see OrarioLezioni
 */

public class Lezione implements Comparable<Lezione> {
    /** Insegnamento associato alla lezione, da cui si ricava anche il docente. */
   private Insegnamento insegnamento;

    /** Aula fisica in cui si svolge la lezione. */
   private Aula aula;

    /** Giorno e fascia oraria in cui si svolge la lezione. */
   private Orario orario;

    /**
     * Crea una nuova lezione associando insegnamento, aula e orario.
     *
     * @param i l'{@link Insegnamento} della lezione (include il docente)
     * @param a l'{@link Aula} in cui si svolge la lezione
     * @param o l'{@link Orario} (giorno e fascia oraria) della lezione
     */
    public Lezione(Insegnamento i ,Aula a , Orario o){
        this.insegnamento=i;
        this.aula=a;
        this.orario=o;

    }

    /**
     * Restituisce una descrizione testuale della lezione, comprensiva di
     * insegnamento, docente, aula e orario completo.
     *
     * @return una stringa con le informazioni principali della lezione
     */
    public String infoLezione(){
     return "Insegnamento: "+this.insegnamento.getNome()+" Docente: "+this.insegnamento.getDocente().nome+" "+this.insegnamento.getDocente().cognome+" Aula: "+this.aula.getNome()+" Orario: "+this.orario.getOrarioCompleto();
    }
    //Getter and setter
    public Insegnamento getInsegnamento () {
        return new Insegnamento(this.insegnamento);
    }
public void setInsegnamento(Insegnamento i){
        this.insegnamento=i;
}
    public Aula getAula () {
        return new Aula(this.aula);
    }
    public void setAula(Aula a){
        this.aula=a;
    }

    public Orario getOrario () {
        return new Orario(this.orario);
    }
    public void setOrario(Orario o){
        this.orario=o;}


    /**
     * Confronta questa lezione con un altro oggetto per verificarne l'uguaglianza.
     * <p>
     * Due lezioni sono considerate uguali se hanno lo stesso insegnamento,
     * lo stesso orario e si svolgono nella stessa aula (confrontata per nome).
     * Utile per individuare se una lezione è già presente nell'orario o se
     * una richiesta di spostamento si riferisce alla stessa lezione.
     * </p>
     *
     * @param obj l'oggetto da confrontare con questa lezione
     * @return {@code true} se le due lezioni sono equivalenti, {@code false} altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lezione)) return false;
        Lezione lezione = (Lezione) obj;
        return this.insegnamento.equals(lezione.insegnamento)
                && this.orario.equals(lezione.orario)
                && this.aula.getNome().equals(lezione.aula.getNome());
    }

    /**
     * Restituisce l'hash code della lezione, coerente con {@link #equals(Object)}.
     * <p>
     * È calcolato a partire da insegnamento, orario e nome dell'aula, ovvero
     * gli stessi campi usati nel confronto di uguaglianza.
     * </p>
     *
     * @Returns l'hash code della lezione
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.insegnamento, this.orario, this.aula.getNome());
    }

    /**
     * Confronta due lezioni per stabilirne l'ordine cronologico.
     * <p>
     * L'ordinamento avviene prima per giorno della settimana e, a parità
     * di giorno, per orario di inizio.
     * </p>
     *
     * @param l la lezione da confrontare con questa
     * @return un valore negativo, zero o positivo se questa lezione precede,
     *         coincide o segue {@code l} nell'ordine cronologico
     */
    @Override
    public int compareTo(Lezione l) {
        int giorno1 = this.orario.giornoToInt();
        int giorno2 = l.orario.giornoToInt();
        if (giorno1 != giorno2) {
            return Integer.compare(giorno1, giorno2);
        }
      int orarioMinuti= this.orario.getOrarioInizioInMinuti();
        int orarioMinuti2= l.orario.getOrarioInizioInMinuti();
        return Integer.compare(orarioMinuti, orarioMinuti2);
    }
    
}
