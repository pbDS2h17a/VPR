package gui;

import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * Beinhaltet die Sprite-Klasse, die von ImageView erbt und sie mit interaktiven
 * Elementen und Listenern ausstattet, um die restlichen Objekte entlasten.
 * 
 * @author Hoang-Ha Dang
 */
public class Sprite extends ImageView {
	
	// Globale Variablen
	private String imagePath;
	private Image image;
	private boolean isButton;
	private boolean isActive = true;
	private double vx;
	private double vy;
	private ColorAdjust colorAdjust = new ColorAdjust();

	/**
	 * Konstruktor, der die Sprite durch einen Pfad erstellt und die Listener hinzugefügt
	 * 
	 * @param path String
	 */
	public Sprite(String path) {
		image = new Image(path);
		this.setImage(image);
		imagePath = path;
		initializeListeners();
	}
	
	/**
	 * Methode die den Dateipfad für die Sprite zurückgibt
	 * 
	 * @return Dateipfad für die Sprite
	 */
	public String getImagePath() {
		return imagePath;
	}
	
	/**	
	 * Methode die zurückgibt ob die Sprite ein Button ist
	 * 
	 * @return gibt zurück, ob Sprite ein Button ist
	 */
	public boolean isButtonMode() {
		return this.isButton;
	}
	
	/**
	 * Methode um den ButtonMode für die Sprite zu ändern
	 * 
	 * @param mode boolean
	 */
	public void setButtonMode(boolean mode) {
		this.isButton = mode;
	}
	
	/**
	 * Sollte der mode false sein wird das Bild zusätzlich ausgegraut
	 */
	public void setActive(boolean mode) {
		if (mode) {
			colorAdjust.setSaturation(0);
			this.setEffect(colorAdjust);			
		} else {
			colorAdjust.setSaturation(-1);
			this.setEffect(colorAdjust);
		}
		this.isActive = mode;
	}
	
	/**
	 * Methode die zurückgibt ob die Sprite aktiv ist
	 * 
	 * @return gibt zurück, ob die Sprite aktiv ist
	 */
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * Methode die den Velocity X-Wert zurückgibt
	 * 
	 * @return gibt den double-Wert zurück
	 */
	public double getVx() {
		return vx;
	}

	/**
	 * Methode die den Velocity Y-Wert zurückgibt
	 * 
	 * @return gibt den double-Wert zurück
	 */
	public double getVy() {
		return vy;
	}

	
	/**
	 * Methode, die den Velocity X-Wert setzt
	 * 
	 * @param vx double
	 */
	public void setVx(double vx){
		this.vx = vx;
	}

	/**
	 * Methode, die den Velocity Y-Wert setzt
	 * 
	 * @param vy double
	 */
	public void setVy(double vy) {
		this.vy = vy;
	}
	
	/**
	 * Methode die, beim Erstellen einer Sprite, Events einfügt und 
	 * beim Hovern ein Soundeffekt abspielt
	 */
	public void initializeListeners() {
		setOnMouseEntered(eventEntered->{
			if(isButtonMode() && isActive()) {
				MediaPlayerFX.menuHover.play();
				colorAdjust.setBrightness(0.1);
				colorAdjust.setContrast(0.3);
				colorAdjust.setHue(0.025);
				this.setEffect(colorAdjust);
			}
			
			if(isButtonMode())
				getScene().setCursor(Cursor.HAND);
		});
		
		setOnMouseExited(eventExited->{
			if(isButtonMode() && isActive()) {
				MediaPlayerFX.menuHover.stop();
				colorAdjust.setBrightness(0);
				colorAdjust.setContrast(0);
				colorAdjust.setHue(0);
				this.setEffect(colorAdjust);
			}
			
			if(isButtonMode())
				getScene().setCursor(Cursor.DEFAULT);
		});
	}
	
}