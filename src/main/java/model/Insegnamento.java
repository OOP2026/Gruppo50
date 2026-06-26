package model;
/**Questa classe rappresenta un insegnamento universitario
 * Viene usata per assegnare insegnamenti ai docenti
 * @see Docente
 * */
public class Insegnamento {
    ///E' il nome dell'insegnamento
    public String Nome;
    ///Sono i numeri di cfu che vengono dati se si supera l'esame di questo insegnamento
    public int NumeroCFU;
    ///Anno in cui l'insegnamento viene insegnato
    public int AnnoCorso;
    ///Indica chi è il docente titolare
    public Docente docente;

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
