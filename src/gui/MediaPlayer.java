package gui;

import javafx.scene.Group;
import javafx.scene.control.Slider;

public class MediaPlayer {
	
	static Sounds menuBackgroundMusic = new Sounds("resources/Mechanolith.mp3");
	static Sounds menuHover = new Sounds("resources/menuHover.wav");
	Sounds gameBackgroundMusic = new Sounds("resources/FinalBattleoftheDarkWizards.mp3");
	Sounds menuClick = new Sounds("resources/menuClick.wav");
	Sprite title_btn_play_mediaPlayer;
	Sprite title_btn_stop_mediaPlayer;
	Slider volumeSlider;
	Group mediaPlayerGroup;
	
	public MediaPlayer() {
		
		mediaPlayerGroup = new Group();
		mediaPlayerGroup.relocate(0, 940);
		
		//Button - Mediaplayer starten
		title_btn_play_mediaPlayer = new Sprite("resources/play_button.png");
		title_btn_play_mediaPlayer.setButtonMode(true);
		title_btn_play_mediaPlayer.setScaleX(.25);
		title_btn_play_mediaPlayer.setScaleY(.25);
		title_btn_play_mediaPlayer.relocate(0, 0);
		mediaPlayerGroup.getChildren().add(title_btn_play_mediaPlayer);
		
		//Button - Mediaplayer stoppen
		title_btn_stop_mediaPlayer = new Sprite("resources/stop_button.png");
		title_btn_stop_mediaPlayer.setButtonMode(true);
		title_btn_stop_mediaPlayer.setScaleX(.25);
		title_btn_stop_mediaPlayer.setScaleY(.25);
		title_btn_stop_mediaPlayer.relocate(40, 0);
		mediaPlayerGroup.getChildren().add(title_btn_stop_mediaPlayer);
		
		//Slider - Lautstärke anpassen
		volumeSlider = new Slider(0, 1, 1);
		volumeSlider.relocate(150, 80);
		volumeSlider.setValue(0.1);
		mediaPlayerGroup.getChildren().add(volumeSlider);

	}
	
	public Group getContainer() {
		return mediaPlayerGroup;
	}
	
	public Sprite getPlayBtn() {
		return title_btn_play_mediaPlayer;
	}
	
	public Sprite getStopBtn() {
		return title_btn_stop_mediaPlayer;
	}
	
	public Slider getSlider() {
		return volumeSlider;
	}
	
	public void playBtnSFX() {
		menuClick.play();
	}
	
	public void playBgmStart() {
		menuBackgroundMusic.playBackground();
	}
	
	public void stopBgmStart() {
		menuBackgroundMusic.stop();
	}
	
	public void playBgmGame() {
		gameBackgroundMusic.playBackground();
	}
	
	public void stopBgmGame() {
		gameBackgroundMusic.stop();
	}
	
	public void setVolumeStart() {
		menuBackgroundMusic.setVolume(volumeSlider);
	}
	
	public void setVolumeGame() {
		gameBackgroundMusic.setVolume(volumeSlider);
	}
	
}
