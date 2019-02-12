
package gui;

import javafx.beans.binding.DoubleExpression;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

 
/**
 * Beinhaltet Methoden f�r die Funktionen, die f�r die einzelnen
 * MediaPlayer Komponenten ben�tigt werden.
 * 
 * @author Nam-Max Liebner
 */

class Sounds {
	
	// Globale Variablen
	private final MediaPlayer mediaPlayer;

	/**
	 * Konstruktor.
	 * @param mediaPath
	 */
	public Sounds(String mediaPath){
		// Path Converter f�r die Media Source.
		Media musicFile = new Media(getClass().getClassLoader().getResource(mediaPath).toString());
		// Erzeugt MediaPlayer mit entsprechendem Source.
		mediaPlayer = new MediaPlayer(musicFile);
	}

	/**
	 * Startet die Medie.
	 */
	public void play(){
		// Startet MediaPlayer.
		this.mediaPlayer.play();
		// Stoppt den MediaPlayer, sobald Medie endet.
		this.mediaPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				mediaPlayer.stop();
			}
		});
	}
	
	/**
	 * Hintergrundmusik-Schleife.
	 */
	public void playBackground() {
		// Startet MediaPlayer.
		this.mediaPlayer.play();
		// Setzt MediaPlayer wieder auf Anfang, sobald Medie endet.
		this.mediaPlayer.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mediaPlayer.seek(Duration.ZERO);
		       }
		});
	}

	/**
	 * Stoppt den MediaPlayer.
	 */
	public void stop(){
		this.mediaPlayer.stop();
	}
	
	/**
	 * Regelt die Lautst�rke
	 * @param volumeSlider
	 */
	public void setVolume(Slider volumeSlider){
		/* Setzt einen Wert zwischen 0 und 1 (wobei 0: Stumm, 1: Maximale Lautst�rke),
		 * f�r die Lautst�rkeeigenschaft des MediaPlayers.
		 * Die DoubleExpression wandelt die Int-Werte in Double-Werte als VolumeProperty Wert um
		 * und dividiert diese mit 10.
		 */
		mediaPlayer.volumeProperty().bind(DoubleExpression.doubleExpression(volumeSlider.valueProperty()).divide(10));
		// Tick Marks Anzeige
		volumeSlider.setShowTickMarks(true);
		// TickView-Controller
		// Tick Anzahl f�r Hauptwerte
		volumeSlider.setMajorTickUnit(5);
		// Tick Anzahl zwischen den Hauptwerten
		volumeSlider.setMinorTickCount(4);
		
		// Volume Listener f�r den Slider
		volumeSlider.valueProperty().addListener((obs, oldval, newVal) -> 
		// F�r entsprechende Tick-Werte wurden Int-Werte zwischen 0 und 10 ausgegeben.
	    volumeSlider.setValue(newVal.intValue())
	    );
		
	}
	
}