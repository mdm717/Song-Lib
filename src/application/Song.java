/*
 * Craig Sirota and Matthew Marrazzo
 * 
 * Song Class
 * 
 * 		This class is designed to allow the application to create song objects
 * 		that contain at least a song title and artist, but also can include an
 * 		album name and release year.
 * 
 */

package application;

public class Song {

	private String title, artist, album, year;
	
	public Song() {}		//No-Arg Constructor -- creates a song when the parameters for unknown parameters
	
	public Song(String title, String artist, String album, String year) {  //constructor designed to create a song with known parameters
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	//Get and Set methods for parameter fields
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}

	public String getArtist() {return artist;}
	public void setArtist(String artist) {this.artist = artist;}

	public String getAlbum() {return album;}
	public void setAlbum(String album) {this.album = album;}

	public String getYear() {return year;}
	public void setYear(String year) {this.year = year;}
	
	//Returns a song's title and artist in a formatted string
	public String toString() {
		return title + "   ||   "+artist;
	}
	
	public boolean equals(Object o) {
		if (o==null || !(o instanceof Song)) {return false;}
		Song s = (Song) o;
		if (s.getTitle().equals(title) && s.getArtist().equals(artist)) {return true;}
		return false;
	}
}
