/*
 * Craig Sirota and Matthew Marrazzo
 * 
 * ListController Class
 * 
 * 		This class creates the main UI for the user to add, edit, and
 * 		delete songs from a list
 * 
 */
package view;
	
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;

import com.sun.glass.events.WindowEvent;

import application.Song;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class ListController extends Application {
	public static final ObservableList<Song> data = FXCollections.observableArrayList();
	private static ListView<Song> listView;
	private static Stage mainStage;
	private static Scene scene;

	private static TextField songAlbum, songYear, titleIn, albumIn, artistIn, yearIn;

	private static int count; //counts items in list
	private static int action = -1;
	private static int index = -1;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		try {
			Parent root= FXMLLoader.load(getClass().getResource("List.fxml"));
			scene = new Scene(root);
			listView = (ListView<Song>) scene.lookup("#listView");
			listView.setItems(data);
			titleIn = (TextField) scene.lookup("#titleIn");
			artistIn = (TextField) scene.lookup("#artistIn");
			albumIn = (TextField) scene.lookup("#albumIn");
			yearIn = (TextField) scene.lookup("#yearIn");

			fileRead();
			
			sort();
			
			//Selects first song on startup if List is not empty
			if(data.size() > 0)
			{
				//showInfo(0);
				listView.getSelectionModel().select(0);
				showInfo(0);
			}
			
			primaryStage.setOnCloseRequest(e -> {
				try {
					fileWrite();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        Platform.exit();
		        System.exit(0);
		    });
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainStage= primaryStage;
		
		if (data.isEmpty()) {
			count = 0;
		} else if (count == 1) {
		}
		
		
		handler(action);
		
		listView.getSelectionModel().selectedIndexProperty().addListener( (obs, oldVal, newVal) -> showInfo(newVal));
	}
	
	public void handler(int a) {
		switch (a) {
			case -1:
				break;
			case 0:
				listView.getSelectionModel().select(data.size()-1);
				showInfo(data.size()-1);
				sort();
				action=-1;
				break;
			case 1:
				if (index==-1) {index++;}
				showInfo(data.size()-1);
				sort();
				action=-1;
				break;	
			case 2:
				listView.getSelectionModel().select(index-1);
				showInfo(data.size()-1);
				sort();
				action=-1;
				break;
			default:
				break;
		}
	}
	
	@FXML
	private void add(ActionEvent e) {
		action=0;
		Text text = (Text) scene.lookup("#op");
		text.setText("Enter Information For New Song");
		
		clear();
	}
	
	@FXML
	private void edit(ActionEvent e) {
		if (!(data.isEmpty())) {
			action=1;
			Text text = (Text) scene.lookup("#op");
			text.setText("Change Information For Editted Song");
			clear();
			index = listView.getSelectionModel().getSelectedIndex();
			titleIn.setText(data.get(index).getTitle());
			artistIn.setText(data.get(index).getArtist());
			albumIn.setText(data.get(index).getAlbum());
			yearIn.setText(data.get(index).getYear());
		}
	}
	
	@FXML
	private void delete(ActionEvent e) {
		if (!(data.isEmpty())) {
			action=2;
			int NextIndex = listView.getSelectionModel().getSelectedIndex();
			data.remove(listView.getSelectionModel().getSelectedIndex());
			listView.getSelectionModel().select(NextIndex);
			if(data.size() == 0)
				{ clear(); }
			
		}
	}
	
	@FXML
	private void confrim(ActionEvent e) {
		Song s =new Song(titleIn.getText(),artistIn.getText(),albumIn.getText(),yearIn.getText());
		switch (action) {
		case -1:
			break;
		case 0:
			boolean failed=false;
			boolean newSong=true;
			
			for (int i = 0; i < data.size(); i++) {
				if (s.equals(data.get(i))) {		
					//If either the song title or artist is missing, do nothing
					newSong=false;
				}
			}
			if (s.getTitle().equals(null) || s.getTitle().equals("") ||
				s.getArtist().equals(null) || s.getArtist().equals("")) {
				missingInfo(mainStage);
			}
			else if (newSong) {//if at least title and artist are provided add the song to the end of the list
				data.add(s);
				index++;
			}
			else {
				showAlert(mainStage);
			}
			listView.getSelectionModel().select(data.size()-1);
			showInfo(data.size()-1);
			sort();
			if (!failed) {
				action=-1;

				Text text = (Text) scene.lookup("#op");
				text.setText("Please Select An Operation");
			}
			break;
		case 1:
			newSong=true;
			failed=false;
			
			if (index==-1) {index++;}
			else {
				index = listView.getSelectionModel().getSelectedIndex();
			}
			for (int i = 0; i < data.size(); i++) {
				if (s.equals(data.get(i)) && i != index) {		
					//If either the song title or artist is missing, do nothing
					newSong=false;
				}
			}
			if (s.getTitle().equals(null) || s.getTitle().equals("") ||
				s.getArtist().equals(null) || s.getArtist().equals("")) {
				missingInfo(mainStage);
				failed=true;
				
			}
			else if (newSong) {//if at least title and artist are provided add the song to the end of the list
				data.remove(index);
				data.add(new Song(titleIn.getText(), artistIn.getText(), albumIn.getText(), yearIn.getText()));
				listView.getSelectionModel().select(data.size()-1);
				
			}
			else {
				showAlert(mainStage);
				failed=true;
			}
			
			sort();
						
			if (!failed) {
				action=-1;
				
				Text text = (Text) scene.lookup("#op");
				text.setText("Please Select An Operation");
			}
			break;
		default:
			break;
		}
	}
	
	public static void showInfo(Number newVal) {
		if (!(data.isEmpty())) {
			titleIn.setText(data.get((int) newVal).getTitle());
			artistIn.setText(data.get((int) newVal).getArtist());
			albumIn.setText(data.get((int) newVal).getAlbum());
			yearIn.setText(data.get((int) newVal).getYear());
		}
	}
	
	private static void sort() {
		
		 for (int i = 0; i < data.size(); i++) 
	        {
	            for (int j = i + 1; j < data.size(); j++) 
	            {
	                if (data.get(i).toString().compareTo(data.get(j).toString())>0) 
	                {
	                    Song s = new Song(data.get(i).getTitle(),data.get(i).getArtist(),data.get(i).getAlbum(),data.get(i).getYear());
	                    data.set(i, data.get(j));
	                    data.set(j, s);
	                }
	            }
	        }
	}
	
	private static void clear() {
		titleIn.setText("");
		artistIn.setText("");
		albumIn.setText("");
		yearIn.setText("");
	}
	
	private static void fileWrite() throws IOException {
		FileWriter fw = new FileWriter("currSongs.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0; i<data.size(); i++)
		{
			bw.write(data.get(i).getTitle() + " " + data.get(i).getArtist());
			if(data.get(i).getAlbum().equals("") && data.get(i).getYear().equals(""))
			{
				bw.write(" null null");
			}
			else if(!data.get(i).getAlbum().equals("") && data.get(i).getYear().equals(""))
			{
				bw.write(" " + data.get(i).getAlbum() + " null");
			}
			else if(data.get(i).getAlbum().equals("") && !data.get(i).getYear().equals(""))
			{
				bw.write(" null " + data.get(i).getYear());
			}
			else if(!data.get(i).getAlbum().equals("") && !data.get(i).getYear().equals(""))
			{
				bw.write(" " + data.get(i).getAlbum() + " " + data.get(i).getYear());
			}
			bw.newLine();
		}
		bw.close();
	}
	
	private static void fileRead() throws IOException{
		InputStream fis = new FileInputStream("currSongs.txt");
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		String line;
		
		while((line = br.readLine()) != null) 
		{	
			String[] words = line.split(" ");
			if(words[2].equals("null") && words[3].equals("null")) 
			{ Song s = new Song(words[0], words[1], "", ""); data.add(s); }
			else if(!words[2].equals("null") && words[3].equals("null"))
			{ Song s = new Song(words[0], words[1], words[2], ""); data.add(s); }
			else if(words[2].equals("null") && !words[3].equals("null"))
			{ Song s = new Song(words[0], words[1], "", words[3]); data.add(s); }
			else
			{ Song s = new Song(words[0], words[1], words[2], words[3]); data.add(s); }
		}
		br.close();
	}

	private static void missingInfo(Stage mainStage) {
		 Alert message = new Alert(AlertType.INFORMATION);
		 message.initOwner(mainStage);
		 message.setTitle("List Item");
		 message.setHeaderText("Missing Info");
		 String content = "Please add a title and an artist."; 
		 message.setContentText(content);
		 message.showAndWait();
	} 
	private static void showAlert(Stage mainStage) {
		 Alert message = new Alert(AlertType.INFORMATION);
		 message.initOwner(mainStage);
		 message.setTitle("List Item");
		 message.setHeaderText("Song Already Exists");
		 String content = "A new song with the same name and artist can't be added to the list."; 
		 message.setContentText(content);
		 message.showAndWait();
	} 
}
