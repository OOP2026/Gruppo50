package model;

/**
 * Enumerazione degli stati possibili di una {@link Richiesta} di modifica
 * dell'orario inviata da un docente al responsabile.
 *
 * <p>Una richiesta nasce nello stato {@link #IN_ATTESA} e passa a
 * {@link #APPROVATA} o {@link #RIFIUTATA} quando il responsabile la
 * valuta.</p>
 */
public enum StatoRichiesta {
    /** La richiesta è stata inviata e non è ancora stata valutata dal responsabile. */
    IN_ATTESA,
    /** La richiesta è stata accettata dal responsabile. */
    APPROVATA,
    /** La richiesta è stata respinta dal responsabile. */
    RIFIUTATA
}