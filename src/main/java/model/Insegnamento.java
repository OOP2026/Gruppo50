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

    
    
}
