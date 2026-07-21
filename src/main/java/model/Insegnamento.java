package model;
/**Questa classe rappresenta un insegnamento universitario
 * Viene usata per assegnare insegnamenti ai docenti
 * @see Docente
 * */
public class Insegnamento {
    /** È il nome dell'insegnamento. */
   private final String nome;
    /** Sono i numeri di cfu che vengono dati se si supera l'esame di questo insegnamento. */
    private final int numeroCFU;
    /** Anno in cui l'insegnamento viene insegnato. */
   private final int annoCorso;
    /** Indica chi è il docente titolare. */
    private Docente docente;

    /**
     * Costruisce un nuovo insegnamento con nome, cfu, anno del corso e docente.
     * @param nome il nome dell'insegnamento.
     * @param numeroCFU il numero di crediti formativi.
     * @param annoCorso l'anno del corso dell'insegnamento.
     * @param docente il docente assegnato all'insegnamento.
     */
    public Insegnamento(String nome,int numeroCFU,int annoCorso, Docente docente){
       if(annoCorso>3 || annoCorso<1) {throw new IllegalArgumentException("L'anno deve essere tra 1 e 3");}

       if(numeroCFU>15 || numeroCFU<1){throw new IllegalArgumentException("Numero di cfu deve essere tra 1 e 15");}

        this.nome=nome;
        this.numeroCFU=numeroCFU;
        this.annoCorso=annoCorso;
        this.docente=docente;
    }
    /** Costruisce un nuovo insegnamento senza assegnare subito un docente.
     * @param nome il nome dell'insegnamento.
     * @param numeroCFU il numero di crediti formativi.
     * @param annoCorso l'anno del corso dell'insegnamento.
     */
    public Insegnamento(String nome,int numeroCFU,int annoCorso){
        if(annoCorso>3 || annoCorso<1) throw new IllegalArgumentException("L'anno deve essere tra 1 e 3");
        if(numeroCFU>15 || numeroCFU<1){throw new IllegalArgumentException("Numero di cfu deve essere tra 1 e 15");}
        this.nome=nome;
        this.numeroCFU=numeroCFU;
        this.annoCorso=annoCorso;
this.docente=null;
    }
    /**Costruttore che copia insegnamento clonando tutti i dati in un'altra istanza.
     * @param i l'insegnamento da cui copiare.
     */
    public Insegnamento(Insegnamento i){
        this.nome=i.nome;
        this.numeroCFU=i.numeroCFU;
        this.annoCorso=i.annoCorso;
        this.docente= new Docente(i.docente);
    }
//getter and setter

    /** Restituisce il nome dell'insegnamento.
     * @return il nome dell'insegnamento.
     */
    public String getNome() {
        return nome;
    }
    /** Restituisce il numero di crediti formativi.
     * @return i CFU dell'insegnamento.
     */
    public int getNumeroCFU() {
        return numeroCFU;
    }
    /** Restituisce l'anno del corso dell'insegnamento.
     * @return l'anno di corso.
     */
    public int getAnnoCorso() {
        return annoCorso;
    }
    /** Restituisce il docente dell'insegnamento.
     * @return il docente assegnato all'insegnamento.
     */
    public Docente getDocente() {
        if(docente==null) return null;
        return new Docente(docente);
    }

    public void setDocente(Docente doc){
        if(!doc.getInsegnamenti().contains(this)) {
        throw new IllegalArgumentException("Questo docente non può essere titolare di questa materia, perchè non la insegna!");
        }
        this.docente=doc;
    }
    /**
     * Confronta questo insegnamento con un altro oggetto per verificare se sono uguali.
     * Due insegnamenti sono considerati uguali se hanno stesso nome, stessi CFU,
     * stesso anno di corso e se i docenti assegnati sono gli stessi.
     * @param obj l'oggetto da confrontare con questo insegnamento
     * @return true se gli oggetti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Insegnamento)) return false;
        Insegnamento insegnamento = (Insegnamento) obj;
        return this.nome.equals(insegnamento.nome)
                && this.numeroCFU == insegnamento.numeroCFU
                && this.annoCorso == insegnamento.annoCorso
                && this.docente.getmail().equals(insegnamento.docente.getmail());
    }
    /**
     * Crea un hash code per questo insegnamento su determinati attributi.
     * @return l'hash code calcolato.
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.nome, this.numeroCFU, this.annoCorso, this.docente.getmail());
    }
    
}
