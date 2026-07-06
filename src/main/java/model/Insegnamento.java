package model;
/**Questa classe rappresenta un insegnamento universitario
 * Viene usata per assegnare insegnamenti ai docenti
 * @see Docente
 * */
public class Insegnamento {
    ///È il nome dell'insegnamento
   final private String Nome;
    ///Sono i numeri di cfu che vengono dati se si supera l'esame di questo insegnamento
    final private int NumeroCFU;
    ///Anno in cui l'insegnamento viene insegnato
   final private int AnnoCorso;
    ///Indica chi è il docente titolare
    private Docente docente;

    public Insegnamento(String Nome,int NumeroCFU,int AnnoCorso, Docente docente){
        this.Nome=Nome;
        this.NumeroCFU=NumeroCFU;
        this.AnnoCorso=AnnoCorso;
        this.docente=docente;
    }
    public Insegnamento(String Nome,int NumeroCFU,int AnnoCorso){
        this.Nome=Nome;
        this.NumeroCFU=NumeroCFU;
        this.AnnoCorso=AnnoCorso;

    }
    public Insegnamento(Insegnamento i){
        this.Nome=i.Nome;
        this.NumeroCFU=i.NumeroCFU;
        this.AnnoCorso=i.AnnoCorso;
        this.docente= new Docente(i.docente);
    }
//getter and setter
    public String getNome() {
        return Nome;
    }
    public int getNumeroCFU() {
        return NumeroCFU;
    }
    public int getAnnoCorso() {
        return AnnoCorso;
    }
    public Docente getDocente() {
        return new Docente(docente);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Insegnamento)) return false;
        Insegnamento insegnamento = (Insegnamento) obj;
        return this.Nome.equals(insegnamento.Nome)
                && this.NumeroCFU == insegnamento.NumeroCFU
                && this.AnnoCorso == insegnamento.AnnoCorso
                && this.docente.email.equals(insegnamento.docente.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.Nome, this.NumeroCFU, this.AnnoCorso, this.docente.email);
    }
    
}
