package gui;

import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class MediaPlayerFX {
	

	/**
	 * Hintergrundmusik
	 * @param menuBackgroundMusic	 		: Sounds
	 * @param gameBackgroundMusic		 	: Sounds
	 * 
	 * Button-Sounds
	 * @param menuClick	 					: Sounds
	 * @param menuHover 					: Sounds
	 * 
	 * Volume-Slider
	 * @param volumeSlider		 			: Slider
	 * 
	 * Buttons Deklaration
	 * @param title_btn_play_mediaPlayer	: Sprite
	 * @param title_btn_stop_mediaPlayer 	: Sprite
	 * 
	 * MediaPlayer-Gruppe
	 * @param mediaPlayerGroup		 		: Group
	 */
	
	// Hintergrundmusik
	static Sounds menuBackgroundMusic = new Sounds("resources/Mechanolith.mp3");
	Sounds gameBackgroundMusic = new Sounds("resources/FinalBattleoftheDarkWizards.mp3");
	
	//Button-Sounds
	Sounds menuClick = new Sounds("resources/menuClick.wav");
	static Sounds menuHover = new Sounds("resources/menuHover.wav");
	
	// Volume-Slider.
	static Slider volumeSlider;
	
	// Buttons Deklaration.
	Sprite title_btn_play_mediaPlayer;
	Sprite title_btn_stop_mediaPlayer;
	
	// MediaPlayer-Gruppe.
	Group mediaPlayerGroup;
	
	public MediaPlayerFX() {
		// Erstellt Mediaplayer Gruppe (inkludiert Buttons und Slider für Mediaplayer).
		mediaPlayerGroup = new Group();
		// Position der Mediaplayer Gruppe.
		mediaPlayerGroup.relocate(0, 940);
		
		/**
		 * MediaPlayer - Play Button.
		 */
		// Button-Image Source für Sprite.
		title_btn_play_mediaPlayer = new Sprite("resources/play_button.png");
		// Sprite deklariert als Button.
		title_btn_play_mediaPlayer.setButtonMode(true);
		// Skaliert Buttongröße.
		title_btn_play_mediaPlayer.setScaleX(.25);
		title_btn_play_mediaPlayer.setScaleY(.25);
		// Button Position.
		title_btn_play_mediaPlayer.relocate(0, 0);
		mediaPlayerGroup.getChildren().add(title_btn_play_mediaPlayer);
		
		/**
		 * MediaPlayer - Stop Button
		 */
		// Button-Image Source für Sprite
		title_btn_stop_mediaPlayer = new Sprite("resources/stop_button.png");
		// Sprite deklariert als Button
		title_btn_stop_mediaPlayer.setButtonMode(true);
		// Skaliert Buttongröße
		title_btn_stop_mediaPlayer.setScaleX(.25);
		title_btn_stop_mediaPlayer.setScaleY(.25);
		// Button Position
		title_btn_stop_mediaPlayer.relocate(40, 0);
		mediaPlayerGroup.getChildren().addAll(title_btn_stop_mediaPlayer);
		// Volume Slider (min Wert, max Wert, aktueller Standard-Wert beim Start)
		volumeSlider = new Slider(0, 10, 1);
		mediaPlayerGroup.getChildren().add(volumeSlider);
		
	}
	
	public static void addVolumeSliderThumb(){
		
		Sprite thumb = new Sprite("resources/game_icon_units.png");
		Pane thumbPane = (Pane) MediaPlayerFX.getSlider().lookup(".thumb");
		thumbPane.setStyle("-fx-background-image:url('resources/game_icon_units.png');-fx-background-color:red");
		//thumbPane.getChildren().add(thumb);
	}
	
	/**
	 * @return mediaPlayerGroup : Group
	 * gibt den Container für Mediaplayer Elemente zurück
	 */
	public Group getContainer() {
		return mediaPlayerGroup;
	}
	
	/**
	 * @return title_btn_play_mediaPlayer : Sprite
	 * gibt die Play-Button Sprite zurück
	 */
	public Sprite getPlayBtn() {
		return title_btn_play_mediaPlayer;
	}
	
	/**
	 * @return title_btn_stop_mediaPlayer : Sprite
	 * gibt die Stop-Button Sprite zurück
	 */
	public Sprite getStopBtn() {
		return title_btn_stop_mediaPlayer;
	}
	
	/**
	 * @return volumeSlider : Slider
	 * gibt den Volume Slider zurück
	 */
	public static Slider getSlider() {
		return volumeSlider;
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