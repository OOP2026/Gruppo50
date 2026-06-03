package gui;

import controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class Home {
	private Controller controller;
	public static void main(String[] args) {


		Controller controller = new Controller(new ArrayList<>());
        PrimoFrame primoFrame = new PrimoFrame(controller);
		primoFrame.frame.setVisible(true);

	}

}


