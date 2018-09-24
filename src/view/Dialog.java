package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog extends Application {

	private static TextField title = new TextField(), album = new TextField(),
			artist = new TextField(), year = new TextField();
	private static Button ok = new Button("OK"), cancel = new Button("Cancel");
	private static Stage mainStage;
	private static boolean edit;
	private static Song s1;
	
	public Dialog() {
		edit = false;
	}
	
	public Dialog (Song s) {
		edit = true;
		s1=new Song();
		s1.setAlbum(s.getAlbum());
		s1.setArtist(s.getArtist());
		s1.setTitle(s.getTitle());
		s1.setYear(s.getYear());
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

		ok.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Song song = new Song(title.getText(),artist.getText(),album.getText(),year.getText());
				if (song.getTitle().equals(null) || song.getTitle().equals("") || song.getArtist().equals(null) || song.getArtist().equals("")) {}
				else {
					if (edit) {
						View.data.add(song);
						//---------Edit The Info On The List
						new View().start(mainStage);
					} else {
						View.data.add(song);
						new View().start(mainStage);
					}
				}
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				new View().start(mainStage);
			}
		});
		return gridPane;
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
