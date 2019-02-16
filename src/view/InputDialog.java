/*
 * Craig Sirota and Matthew Marrazzo
 * 
 * Dialog Class
 * 
 * 		This class creates a UI for the user to input information to either add
 * 		or edit a song in the main list.
 * 
 */



package view;

import java.util.Optional;

import application.Song;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class InputDialog extends Application {

		//TextFields allow the user to input the information for each song
	private static TextField title = new TextField(), album = new TextField(),
			artist = new TextField(), year = new TextField();
	
	private static Button ok = new Button("OK"), cancel = new Button("Cancel");
	
	private static Stage mainStage;
	private static boolean edit;	//allows the 
	private static Song s1;			//Song object used when editing an existing song
	private static int index;
	
	public InputDialog() {		//No-arg constructor used for creating a new song
		edit = false;
	}
	
	public InputDialog (Song s, int i) {//sets the info of the selected song on the list to the song on the dialog window
		edit = true;
		s1=new Song();
		s1.setAlbum(s.getAlbum());
		s1.setArtist(s.getArtist());
		s1.setTitle(s.getTitle());
		s1.setYear(s.getYear());
		index=i;
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stubs
		primaryStage.setTitle("Set Song Info");
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		GridPane root = makeGridPane();
		root.setPadding(new Insets(10,10,10,10));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		mainStage=primaryStage;
	}
	

	private static GridPane makeGridPane() {
		// all the widgets

		TextField title = new TextField(), album = new TextField(),
				artist = new TextField(), year = new TextField();
		Button ok = new Button("OK"), cancel = new Button("Cancel");
		
		title.setPromptText("Title");
		artist.setPromptText("Artist");
		album.setPromptText("Album");
		year.setPromptText("Year");
		
		if (edit) {
			String Title= s1.getTitle();
			String Album=s1.getAlbum();
			String Artist=s1.getArtist();
			String Year=s1.getYear();
			
			title.setText(Title);
			artist.setText(Artist);
			
			if (Album.equals(null) || Album.equals(""))
				album.setPromptText("Album");
			else
				album.setText(Album);
	
			if (Year.equals(null) || Year.equals(""))
				year.setPromptText("Year");
			else
				year.setText(Year);
		}
		
		GridPane gridPane = new GridPane();
		gridPane.add(title, 0, 0);
		gridPane.add(artist, 1, 0);
		gridPane.add(album, 0, 1);
		gridPane.add(year, 1, 1);
		gridPane.add(cancel,0,2);
		gridPane.add(ok,1,2);

		gridPane.setHgap(40);
		gridPane.setVgap(40);
		gridPane.setPadding(new Insets(10,10,10,10));

		ok.setOnAction(new EventHandler<ActionEvent>() {	//if Ok button is pressed
			public void handle(ActionEvent e) {
				boolean newSong = true;
				Song song = new Song(title.getText(),artist.getText(),album.getText(),year.getText());

				for (int i = 0; i < ListController.data.size(); i++) {
					if (edit && index!=i) {
						if (song.equals(ListController.data.get(i))) {		
								//If either the song title or artist is missing, do nothing
							newSong=false;
						}
					} else if (!edit) {
						if (song.equals(ListController.data.get(i))) {		
							//If either the song title or artist is missing, do nothing
							newSong=false;
						}
					}
				}
				if (song.getTitle().equals(null) || song.getTitle().equals("") ||
					song.getArtist().equals(null) || song.getArtist().equals("")) {}
				else if (newSong) {//if at least title and artist are provided add the song to the end of the list
					ListController.data.add(song);
					new ListController().start(mainStage);
				}
				else {
					showAlert(mainStage);
				}
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>() {	//if cancel button is pressed return to the main list
			public void handle(ActionEvent e) {
				new ListController().start(mainStage);
			}
		});
		return gridPane;
	}
	
	 private static void showAlert(Stage mainStage) {
		 Alert message = new Alert(AlertType.INFORMATION);
		 message.initOwner(mainStage);
		 message.setTitle("List Item");
		 message.setHeaderText(
		 "Song Already Exists");
		 String content = "A new song with the same name and artist can't be added to the list."; 
		 message.setContentText(content);
		 message.showAndWait();
		 } 

	public String getTitle() {
		return title.getText();
	}
	public String getArtist() {
		return artist.getText();
	}
	public String getAlbum() {
		return album.getText();
	}
	public String getYear() {
		return year.getText();
	}
}
