package model;
import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		//Testiamo se funziona la funzione che mostra in modo ordinato le lezioni di un docente, se funziona vuol dire che la funzione di ordinamento funziona correttamente
		Responsabile luca= new Responsabile("Luca","Bianchi","luca.bianchi@gmail.com","luca.bianchi","password123");
		OrarioLezioni orario=new OrarioLezioni();
	Docente d1= new Docente("Mario","Rossi","mario.rossi@gmail.com","mario.rossi","password123");
	Insegnamento i1= new Insegnamento("Matematica",2,3,d1);
	Aula ai= new Aula("Aula 1",50);
	Orario o1= new Orario("Lunedì",11,13,14,16);
	Orario o2= new Orario("Lunedì",8,30,10,30);
	Orario o3= new Orario("Lunedì",15,13,17,16);;
	Lezione l1= new Lezione(i1,ai,o1);
	Lezione l2= new Lezione(i1,ai,o2);
	Lezione l3= new Lezione(i1,ai,o3);
		Orario o4= new Orario("Martedi",11,13,14,16);
		Orario o5= new Orario("Martedi",8,30,10,30);
		Orario o6= new Orario("Martedi",15,13,17,16);;
Lezione l4= new Lezione(i1,ai,o4);
Lezione l5= new Lezione(i1,ai,o5);
Lezione l6= new Lezione(i1,ai,o6);

luca.inserisciLezione(l4,orario);
luca.inserisciLezione(l5,orario);
luca.inserisciLezione(l6,orario);
		luca.inserisciLezione(l2,orario);
		luca.inserisciLezione(l1,orario);
		luca.inserisciLezione(l3,orario);
List<Lezione> lezioniDocente=d1.getLezioni(orario);
for(Lezione l : lezioniDocente){
	System.out.println(l.getInsegnamento().getNome()+"  "+l.getOrario().getGiorno()+": "+l.getOrario().getOrarioCompleto());
	}

}
}
