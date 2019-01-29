package gui;

import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Dang, Hoang-Ha
 */

/**
 * Sprite Klasse
 */
public class Sprite extends ImageView
{
	private String imagePath;
	private Image image;
	private boolean isButton;
	private boolean isActive = true;
	private double vx;
	private double vy;
	private double scale;
	private double vscale;
	
	ColorAdjust colorAdjust = new ColorAdjust();
/**
 * Sprite Constructor<br>
 * Sprite wird durch einen Pfad erstellt und Listener werden hinzugefügt
 * @param path
 * 			Dateipfad für die Sprite
 */
	public Sprite(String path) {
		image = new Image(path);
		this.setImage(image);
		imagePath = path;
		initializeListeners();
	}
/**
 * Methode die den Dateipfad für die Sprite zurückgibt
 * @return imagePath
 * 			Dateipfad für die Sprite
 */
	public String getImagePath() {
		return imagePath;
	}
/**	
 * Methode die zurückgibt ob die Sprite ein Button ist
 * @return isButton
 * 			true = Sprite ist ein Button
 */
	public boolean isButtonMode() {
		return this.isButton;
	}
/**
 * Methode um den ButtonMode für die Sprite zuändern
 * @param mode
 * 			true = Sprite ist ein Button
 */
	public void setButtonMode(boolean mode) {
		this.isButton = mode;
	}
/**
 * Sollte der mode false sein wird das Bild zusätzlich ausgegraut
 * @param mode
 * 			true = aktiv
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
 * Mthode die zurückgibt ob die Sprite aktiv ist
 * @return isActive
 * 			ture = aktiv
 */
	public boolean isActive() {
		return this.isActive;
	}
	
	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}

	public double getScale() {
		return scale;
	}

	public double getVscale() {
		return vscale;
	}

	public void setVx(double vx){
		this.vx = vx;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public void setVscale(double vscale) {
		this.vscale = vscale;
	}
/**
 * Methode die, beim Erstellen einer Sprite, Events einfügt<br>
 * beim Hovern wird ein Soundeffekt abgespielt
 */
	public void initializeListeners() {
		setOnMouseEntered(eventEntered->{
			MediaPlayerFX.menuHover.play();
			if(isButtonMode() && isActive()) {
				colorAdjust.setBrightness(0.1);
				colorAdjust.setContrast(0.3);
				colorAdjust.setHue(0.025);
				this.setEffect(colorAdjust);
			}
			
			if(isButtonMode())
				getScene().setCursor(Cursor.HAND);
		});
		setOnMouseExited(eventExited->{
			MediaPlayerFX.menuHover.stop();
			if(isButtonMode() && isActive()) {
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