package model;
/**
 * Rappresenta un vincolo di indisponibilità per un docente.
 * Indica una fascia oraria in un determinato giorno della settimana in cui
 * il docente non è disponibile per tenere lezioni o accettare spostamenti.
 * @see Docente
 * @see Orario
 */
public class Vincolo{
    /** L'orario e il giorno dati a questo vincolo di indisponibilità. */
private final Orario orario;
    /**
     * Crea un nuovo vincolo di indisponibilità dicendo il giorno e l'ora.
     * L'oggetto {@link Orario} viene instanziato direttamente all'interno del costruttore.
     * @param giorno il giorno della settimana.
     * @param oraInizio l'ora in cui inizia il vincolo.
     * @param minutoInizio i minuti in cui inizia il vincolo.
     * @param oraFine l'ora in cui termina il vincolo.
     * @param minutoFine i minuti in cui termina il vincolo.
     */
public Vincolo(String giorno,int oraInizio, int minutoInizio, int oraFine, int minutoFine ){
this.orario= new Orario(giorno, oraInizio, minutoInizio, oraFine, minutoFine);
  }
    /**
     * Restituisce una copia dell'orario associato al vincolo.
     * Viene utilizzata una copia per proteggere lo stato interno dell'oggetto
     * da modifiche accidentali esterne.
     * @return una nuova istanza di {@link Orario} contenente i dati di questo vincolo.
     */
  public Orario getOrario(){
      return new Orario(this.orario);
  }
}