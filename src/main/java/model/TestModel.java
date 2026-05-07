package model;
import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		/* Utente u = new Studente("topolino","minni","topolino@gmail.com","morfeo","macazo","De20034");
		* Docente Marco= new Docente("Marco","Rossi","marco.rossi@gmail.com","marco.rossi","password123");
		* Responsabile Giovanni = new Responsabile("Giovanni","Verdi","giovanni.verdi@gmail.com","giovanni.verdi","password123");
		* Utente Luca= new Utente("Luca","Bianchi","luca.bianchi@gmail.com","luca.bianchi","password123");
		* u.saluto();
		* Marco.saluto( );
		* Giovanni.saluto();
		* Luca.saluto();
		*/
		//Docente e insegnamenti
		Docente Marco= new Docente("Marco","Rossi","marco.rossi@gmail.com","marco.rossi","password123");
		Marco.saluto();
		Insegnamento m1=new Insegnamento("Matematicà",6,1,Marco);
		List<Insegnamento> ls1=new ArrayList<>();
		ls1.add(m1);
		Marco.Inserisci_insegnamenti(ls1);
		//Responsabile
		Responsabile Giovanni = new Responsabile("Giovanni","Verdi","giovanni.verdi@gmail.com","giovanni.verdi","password123","matematica");
		Giovanni.saluto();
		//Utente
		Utente Luca= new Utente("Luca","Bianchi","luca.bianchi@gmail.com","luca.bianchi","password123");
		Luca.saluto();
		//Insegnamento
		Insegnamento Matematica = new Insegnamento ("Matematica",6,1,Marco);
		Aula Aula1 = new Aula("Aula 1",30);
		Aula Aula2 = new Aula("Aula 2",20);
		Orario Orario1 = new Orario("Lunedì",9,0,10,30);
		Orario Orario2 = new Orario("Lunedì",8,00	,9,5);
		Lezione l1 = new Lezione(Matematica,Aula1,Orario1);
		Lezione l2 = new Lezione(Matematica,Aula2,Orario1);	

		OrarioLezioni orarioLezioni = new OrarioLezioni();
		Giovanni.inserisciLezione(l1, orarioLezioni);	
		Giovanni.inserisciLezione(l2, orarioLezioni);
		Giovanni.VisualizzaOrarioCompleto(orarioLezioni);
		Richiesta richiestaSpostamento = new Richiesta(Marco,"Motivo personale",Orario1,Orario2);
		Giovanni.richiesteSpostamento.add(richiestaSpostamento);
		Giovanni.VizualizzaRichiesteSpostamento();
		Giovanni.SpostamentoLezione(0, orarioLezioni);
		Giovanni.VizualizzaRichiesteSpostamento();

	}

}
