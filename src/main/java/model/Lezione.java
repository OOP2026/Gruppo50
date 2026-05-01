package model;

public class Lezione {
    public Insegnamento insegnamento;
    public Aula aula;
    public Orario orario;
    public Lezione(Insegnamento i ,Aula a , Orario o){
        this.insegnamento=i;
        this.aula=a;
        this.orario=o;

    }
    
}
