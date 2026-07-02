package model;
enum StatoRichiesta {
    IN_ATTESA,
    APPROVATA,
    RIFIUTATA
}
public class Richiesta{
    ///Id della richiesta sul database, vale -1 se la richiesta non è (ancora) salvata sul DB
    public int id = -1;
    public Docente docenteRichiedente;
    public String motivoRichiesta;
    public Orario orarioLezioneDaSpostare;
    public Orario nuovoOrarioLezione;
    public StatoRichiesta statoRichiesta= StatoRichiesta.IN_ATTESA;

    public Richiesta(Docente docenteRichiedente, String motivoRichiesta, Orario orarioLezioneDaSpostare, Orario nuovoOrarioLezione) {
        this.docenteRichiedente = docenteRichiedente;
        this.motivoRichiesta = motivoRichiesta;
        this.orarioLezioneDaSpostare = orarioLezioneDaSpostare;
        this.nuovoOrarioLezione = nuovoOrarioLezione;
        if (orarioLezioneDaSpostare.equals(nuovoOrarioLezione)){
            throw new IllegalArgumentException("La richiesta non può avere il nuovo orario uguale a quello vecchio");
        }
    }
    ///Imposta lo stato della richiesta a partire dalla stringa letta dal database
    ///(es. "IN_ATTESA", "APPROVATA", "RIFIUTATA"). L'enum StatoRichiesta non è
    ///visibile fuori dal package model, quindi il Controller usa questo metodo.
    public void caricaStatoDaDB(String stato){
        this.statoRichiesta = StatoRichiesta.valueOf(stato);
    }
    ///Questo metodo a come scopo quello di individuare la lezione a cui si riferisce la richiesta
    public boolean equalsLezione(Lezione l){
        return this.docenteRichiedente.email.equals(l.insegnamento.docente.email) &&
                this.orarioLezioneDaSpostare.equals(l.orario);
    }

}