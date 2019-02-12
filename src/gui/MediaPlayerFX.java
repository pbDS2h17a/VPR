package gui;

import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;


/**
 * Beinhaltet alle visuellen Komponenten für den MediaPlayer
 * 
 * @author Nam-Max Liebner
 */
public class MediaPlayerFX {
	
	// Globale Variablen
	// Hintergrundmusik
	Sounds menuBackgroundMusic = new Sounds("resources/menuBackgroundMusic.mp3");
	Sounds gameBackgroundMusic = new Sounds("resources/gameBackgroundMusic.mp3");
	
	// Button-Sounds
	Sounds menuClick = new Sounds("resources/menuClick.wav");
	static Sounds menuHover = new Sounds("resources/menuHover.wav");
	
	// Volume-Slider
	Slider volumeSlider;
	
	// Panethumb für den Sliderthumb
	Pane paneThumb;
	
	// Sprite als Sliderthumb (liegt in der Panethumb)
	Sprite thumb = new Sprite("resources/btn_lobby_host.png");
	
	// Button-Play
	Sprite title_btn_play_mediaPlayer;
	// Button-Stop
	Sprite title_btn_stop_mediaPlayer;
	
	// MediaPlayer-Komponentengruppe
	Group mediaPlayerGroup;
	
	public MediaPlayerFX() {
		// Erstellt Mediaplayer Gruppe (inkludiert alle Komponenten für den Mediaplayer).
		mediaPlayerGroup = new Group();
		// Position der Mediaplayer Gruppe.
		mediaPlayerGroup.relocate(0, 940);
		
		// PlayButton-View
		//  Button-Image Source für Sprite.
		title_btn_play_mediaPlayer = new Sprite("resources/play_button.png");
		//  Aktiviert die Spriteeigenschaft für Button
		title_btn_play_mediaPlayer.setButtonMode(true);
		//  Skaliert Buttongröße.
		title_btn_play_mediaPlayer.setScaleX(.25);
		title_btn_play_mediaPlayer.setScaleY(.25);
		//  Button Position.
		title_btn_play_mediaPlayer.relocate(0, 0);
		mediaPlayerGroup.getChildren().add(title_btn_play_mediaPlayer);
		
		/**
		 * MediaPlayer - Stop Button
		 */
		// Button-Image Source für Sprite
		title_btn_stop_mediaPlayer = new Sprite("resources/stop_button.png");
		// Aktiviert die Spriteeigenschaft für Button
		title_btn_stop_mediaPlayer.setButtonMode(true);
		// Skaliert Buttongröße
		title_btn_stop_mediaPlayer.setScaleX(.25);
		title_btn_stop_mediaPlayer.setScaleY(.25);
		// Button Position
		title_btn_stop_mediaPlayer.relocate(40, 0);
		mediaPlayerGroup.getChildren().addAll(title_btn_stop_mediaPlayer);
		/**
		 * MediaPlayer - Volume Slider
		 */
		// Aktiviert die Spriteeigenschaft für Button
		thumb.setButtonMode(true);
		// Erzeugt Lautstärke-Slider
		volumeSlider = new Slider(0, 10, 1);
		// Position für den Slider in der MediaPlayerGruppe.
		volumeSlider.relocate(150, 50);
		mediaPlayerGroup.getChildren().add(volumeSlider);
		
	}
	
	/**
	 * gibt den Container für Mediaplayer Elemente zurück
	 * 
	 * @return mediaPlayerGroup : Group
	 */
	public Group getContainer() {
		return mediaPlayerGroup;
	}
	
	
	/**
	 * übergibt den Sliderthumb
	 */
	public void sliderThumbChange() {
		// Sprite wird der Pane übergeben.
  		paneThumb.getChildren().addAll(thumb);
  		// Pane wird skaliert (kleiner, damit die Klickfläche innerhalb der Sprite ist).
  		paneThumb.setScaleX(.15);
  		paneThumb.setScaleY(.15);
  		// Sprite wird skaliert (größer, damit die Visualisierung außerhalb der Pane ist).
  		thumb.setScaleX(5);
  		thumb.setScaleY(5);
	}
	
	/**
	 * gibt die Play-Button Sprite zurück
	 * 
	 * @return title_btn_play_mediaPlayer : Sprite
	 */
	public Sprite getPlayBtn() {
		return title_btn_play_mediaPlayer;
	}
	
	/**
	 * gibt die Stop-Button Sprite zurück
	 * 
	 * @return title_btn_stop_mediaPlayer : Sprite
	 */
	public Sprite getStopBtn() {
		return title_btn_stop_mediaPlayer;
	}
	

	/**
	 * gibt den Volume Slider zurück
	 * 
	 * @return volumeSlider : Slider
	 */
	public Slider getSlider() {
		return volumeSlider;
	}
	
	/**
	 * Spielt Sound für den Play-Button ab
	 */
	public void playBtnSFX() {
		menuClick.play();
	}
	
	/**
	 * Spielt Hintergrundmusik ab
	 */
	public void playBgmStart() {
		menuBackgroundMusic.playBackground();
	}
	
	/**
	 * Stoppt Hintergrundmusik
	 */
	public void stopBgmStart() {
		menuBackgroundMusic.stop();
	}
	
	/**
	 * Spielt In-Game-Musik ab
	 */
	public void playBgmGame() {
		gameBackgroundMusic.playBackground();
	}
	
	/**
	 * Stoppt In-Game-Musik
	 */
	public void stopBgmGame() {
		gameBackgroundMusic.stop();
	}
	
	/**
	 * Setzt Lautstärkevolumen bei Spielstart
	 */
	public void setVolumeStart() {
		menuBackgroundMusic.setVolume(volumeSlider);
	}
	
	/**
	 * Setzt Lautstärkevolumen bei In-Game-Start
	 */
	public void setVolumeGame() {
		gameBackgroundMusic.setVolume(volumeSlider);
	}
	
}