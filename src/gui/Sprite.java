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
	private boolean isButton;
	private boolean isActive = true;
	private double vx;
	private double vy;
	
	private final ColorAdjust colorAdjust = new ColorAdjust();
/**
 * Sprite Constructor<br>
 * Sprite wird durch einen Pfad erstellt und Listener werden hinzugef�gt
 * @param path
 * 			Dateipfad f�r die Sprite
 */
	public Sprite(String path) {
        Image image = new Image(path);
		this.setImage(image);
		String imagePath = path;
		initializeListeners();
	}

    /**
 * Methode die zur�ckgibt ob die Sprite ein Button ist
 * @return isButton
 * 			true = Sprite ist ein Button
 */
private boolean isButtonMode() {
		return this.isButton;
	}
/**
 * Methode um den ButtonMode f�r die Sprite zu�ndern
 * @param mode
 * 			true = Sprite ist ein Button
 */
	public void setButtonMode(boolean mode) {
		this.isButton = mode;
	}
/**
 * Sollte der mode false sein wird das Bild zus�tzlich ausgegraut
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
 * Mthode die zur�ckgibt ob die Sprite aktiv ist
 * @return isActive
 * 			ture = aktiv
 */
	public boolean isActive() {
		return this.isActive;
	}

    public double getVy() {
		return vy;
	}

    public void setVy(double vy) {
		this.vy = vy;
	}
/**
 * Methode die, beim Erstellen einer Sprite, Events einf�gt<br>
 * beim Hovern wird ein Soundeffekt abgespielt
 */
private void initializeListeners() {
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