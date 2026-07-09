package model;
/**Questa classe rappresenta un insegnamento universitario
 * Viene usata per assegnare insegnamenti ai docenti
 * @see Docente
 * */
public class Insegnamento {
    ///È il nome dell'insegnamento
   private final String nome;
    ///Sono i numeri di cfu che vengono dati se si supera l'esame di questo insegnamento
    private final int numeroCFU;
    ///Anno in cui l'insegnamento viene insegnato
   private final int annoCorso;
    ///Indica chi è il docente titolare
    private Docente docente;

    public Insegnamento(String nome,int numeroCFU,int annoCorso, Docente docente){
        this.nome=nome;
        this.numeroCFU=numeroCFU;
        this.annoCorso=annoCorso;
        this.docente=docente;
    }
    public Insegnamento(String nome,int numeroCFU,int annoCorso){
        this.nome=nome;
        this.numeroCFU=numeroCFU;
        this.annoCorso=annoCorso;

    }
    public Insegnamento(Insegnamento i){
        this.nome=i.nome;
        this.numeroCFU=i.numeroCFU;
        this.annoCorso=i.annoCorso;
        this.docente= new Docente(i.docente);
    }
//getter and setter
    public String getNome() {
        return nome;
    }
    public int getNumeroCFU() {
        return numeroCFU;
    }
    public int getAnnoCorso() {
        return annoCorso;
    }
    public Docente getDocente() {
        return new Docente(docente);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Insegnamento)) return false;
        Insegnamento insegnamento = (Insegnamento) obj;
        return this.nome.equals(insegnamento.nome)
                && this.numeroCFU == insegnamento.numeroCFU
                && this.annoCorso == insegnamento.annoCorso
                && this.docente.email.equals(insegnamento.docente.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.nome, this.numeroCFU, this.annoCorso, this.docente.email);
    }
    
}
