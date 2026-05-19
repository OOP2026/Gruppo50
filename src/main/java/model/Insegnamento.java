package model;

public class Insegnamento {
    public String Nome;
    public int NumeroCFU;
    public int AnnoCorso;
    public Docente docente;

    public Insegnamento(String Nome,int NumeroCFU,int AnnoCorso, Docente docente){
        this.Nome=Nome;
        this.NumeroCFU=NumeroCFU;
        this.AnnoCorso=AnnoCorso;
        this.docente=docente;
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
