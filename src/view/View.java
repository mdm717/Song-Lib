package view;
	
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class View extends Application {
	public static final ObservableList<Song> data = FXCollections.observableArrayList();
	private static ListView<Song> listView;
	private static Stage mainStage;

	private static TextField songAlbum = new TextField(), songYear = new TextField();
	private static Button add = new Button("Add"), edit = new Button("Edit"), delete = new Button("Delete");
	
	private static int count;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		listView = new ListView<>(data);
		BorderPane root = new BorderPane();
		GridPane buttonPane = makeGridPane();
		root.setPadding(new Insets(10,10,10,10));
		root.setCenter(listView);
		root.setBottom(buttonPane);
		Scene scene = new Scene(root,400,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		if (data.isEmpty()) {
			count = 0;
		} else if (count == 1) {
			songAlbum.setText(data.get(0).getAlbum());
			songYear.setText(data.get(0).getYear());
		}
		
		songAlbum.setPromptText("Album");
		songYear.setPromptText("Year");
		
		mainStage= primaryStage;
		listView.getSelectionModel().select(0);
		if (count>=2)
			listView.getSelectionModel().selectedIndexProperty().addListener( (obs, oldVal, newVal) -> showInfo(newVal));
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void showInfo(Number newVal) {
		songAlbum.setText(data.get((int) newVal).getAlbum());
		songYear.setText(data.get((int) newVal).getYear());
	}
	
	private static GridPane makeGridPane() {

		int textLayer = 0;
		int buttonsLayer = 1;
		// all the widgets

		songAlbum.setEditable(false);
		songYear.setEditable(false);
		
		songAlbum.setPromptText("Album");
		songYear.setPromptText("Year");
		
		GridPane gridPane = new GridPane();
		gridPane.add(songAlbum, 0, textLayer, 3, 1);
		gridPane.add(songYear, 4, textLayer, 3, 1);
		gridPane.add(add, 0, buttonsLayer,2,1);
		gridPane.add(edit, 3, buttonsLayer,2,1);
		gridPane.add(delete, 6, buttonsLayer,2,1);

		gridPane.setHgap(40);
		gridPane.setVgap(40);
		gridPane.setPadding(new Insets(10,10,10,10));

		add.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Dialog d = new Dialog();
				try {
					d.start(mainStage);
					System.out.println(data.size());
					
					count++;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		edit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Dialog d = new Dialog(data.get(listView.getSelectionModel().getSelectedIndex()));
			try {
				d.start(mainStage);
				data.set(listView.getSelectionModel().getSelectedIndex(), data.get(data.size()-1));
				data.remove(data.size()-1);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		});
		delete.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				data.remove(listView.getSelectionModel().getSelectedIndex());
				if (count>=0) {
					count--;
				}
				if (count==0) {

					songAlbum.setText(null);
					songYear.setText(null);
					songAlbum.setPromptText("Album");
					songYear.setPromptText("Year");
				}
			}
		});
		return gridPane;
	}
	
	private static void add4 () { //4 dialog boxes

		Song song = new Song();
		int index = listView.getSelectionModel().getSelectedIndex(); 
		
		do {
		TextInputDialog dialog1 = new TextInputDialog();
		dialog1.initOwner(mainStage); dialog1.setTitle("List Item");
		dialog1.setContentText("Title name: ");
		Optional<String> result = dialog1.showAndWait(); 
		if (result.isPresent()) { song.setTitle(result.get()); }} while (song.getTitle()==null || song.getTitle().equals(""));
		
		do {
		TextInputDialog dialog2 = new TextInputDialog();
		dialog2.initOwner(mainStage); dialog2.setTitle("List Item");
		dialog2.setContentText("Aritst name: "); 
		Optional<String> result = dialog2.showAndWait();
		if (result.isPresent()) { song.setArtist(result.get()); }} while (song.getArtist()==null || song.getArtist().equals(""));
		
		TextInputDialog dialog3 = new TextInputDialog();
		dialog3.initOwner(mainStage); dialog3.setTitle("List Item");
		dialog3.setContentText("Album name: "); 
		Optional<String> result = dialog3.showAndWait();
		if (result.isPresent()) { song.setAlbum(result.get()); } 

		TextInputDialog dialog4 = new TextInputDialog();
		dialog4.initOwner(mainStage); dialog4.setTitle("List Item");
		dialog4.setContentText("Year: "); 
		result = dialog4.showAndWait();
		if (result.isPresent()) { song.setYear(result.get()); }
		
		data.add(song);
	}
	
}
