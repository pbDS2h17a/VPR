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
	 * Konstruktor, der die Sprite durch einen Pfad erstellt und die Listener hinzugef�gt
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
	 * Methode die den Dateipfad f�r die Sprite zur�ckgibt
	 * 
	 * @return Dateipfad f�r die Sprite
	 */
	public String getImagePath() {
		return imagePath;
	}
	
	/**	
	 * Methode die zur�ckgibt ob die Sprite ein Button ist
	 * 
	 * @return gibt zur�ck, ob Sprite ein Button ist
	 */
	public boolean isButtonMode() {
		return this.isButton;
	}
	
	/**
	 * Methode um den ButtonMode f�r die Sprite zu �ndern
	 * 
	 * @param mode boolean
	 */
	public void setButtonMode(boolean mode) {
		this.isButton = mode;
	}
	
	/**
	 * Sollte der mode false sein wird das Bild zus�tzlich ausgegraut
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
	 * Methode die zur�ckgibt ob die Sprite aktiv ist
	 * 
	 * @return gibt zur�ck, ob die Sprite aktiv ist
	 */
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * Methode die den Velocity X-Wert zur�ckgibt
	 * 
	 * @return gibt den double-Wert zur�ck
	 */
	public double getVx() {
		return vx;
	}

	/**
	 * Methode die den Velocity Y-Wert zur�ckgibt
	 * 
	 * @return gibt den double-Wert zur�ck
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
	 * Methode die, beim Erstellen einer Sprite, Events einf�gt und 
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