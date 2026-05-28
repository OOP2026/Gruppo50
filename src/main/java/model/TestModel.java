package model;
import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		OrarioLezioni orarioL=new OrarioLezioni();
	Docente carlo = new Docente("Carlo","Rossi","carlo.rossi@gmail.com","carlo.rossi","password123");
    Responsabile luca= new Responsabile("Luca","Rossi","luca.rossi@gmail.com","luca.rossi","password345");
	Orario or1= new Orario("Lunedì",10,00,12,00);
	Orario or2= new Orario("martedì",12,00,13,50);
	Insegnamento matematica= new Insegnamento("Matematica",10,2,carlo);
	Aula c3= new Aula("C3",199);
	luca.inserisciLezione(matematica,c3,or1,orarioL);
	luca.inserisciLezione(matematica,c3,or1,orarioL);
	luca.visualizzaOrarioCompleto(orarioL);
	}

}
