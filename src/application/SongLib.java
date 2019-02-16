/*
 * Craig Sirota and Matthew Marrazzo
 * 
 * SongLib Class
 * 
 * 		This class contains the main() method. All it does it launch the program.
 * 
 */package application;

import javafx.application.Application;
import view.ListController;

public class SongLib {

	public static void main(String[] args) {
		Application.launch(ListController.class, args);
	}
}
