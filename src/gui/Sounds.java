package gui;

import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Sounds
{
	MediaPlayer mediaPlayer;
	Media musicFile;
	
	public Sounds(String mediaPath){
		musicFile = new Media(getClass().getClassLoader().getResource(mediaPath).toString());
		mediaPlayer = new MediaPlayer(musicFile);
	}

	public void play(){
		this.mediaPlayer.play();
		this.mediaPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				mediaPlayer.stop();
			}
		});
	}
	
	public void playBackground() {
		this.mediaPlayer.rateProperty().set(1);
		this.mediaPlayer.play();
		this.mediaPlayer.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   mediaPlayer.seek(Duration.ZERO);
		       }
		});
	}
	
	public void pause(){
		this.mediaPlayer.pause();
	}
	
	public void stop(){
		this.mediaPlayer.stop();
	}
	
	public void setVolume(Slider volumeSlider){
		mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
	}
	
}