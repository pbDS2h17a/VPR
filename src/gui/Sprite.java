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
 * @param imagePath		: String
 * @param image			: Image
 * @param isButton		: boolean
 * @param isActive		: boolean
 * @param vx			: double
 * @param vy			: double
 * @param scale			: double
 * @param vscale		: double
 * @param colorAdjust	: Coloradjust
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
 * Sprite Constructor
 * @param path : String
 * Sprite wird durch einen Pfad erstellt und Listener werden hinzugefügt
 */
	public Sprite(String path) {
		image = new Image(path);
		this.setImage(image);
		imagePath = path;
		initializeListeners();
	}
	
	public Sprite() {
		initializeListeners();
	}
	public String getImagePath() {
		return imagePath;
	}
	
	public boolean isButtonMode() {
		return this.isButton;
	}
	public void setButtonMode(boolean mode) {
		this.isButton = mode;
	}
/**
 * setActive(boolean mode)
 * @param mode
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

	public void setImagePath(String imagePath) {
		this.setImage(new Image(imagePath));
		this.imagePath = imagePath;
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

	public void click() {
		
	}
/**
 * initializeListeners()
 * fügt beim Erstellen einer Sprite Events ein
 * beim hovern wird ein Soundeffekt abgespielt
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
