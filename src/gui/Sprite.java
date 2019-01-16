package gui;

/**
 * @author Dang, Hoang-Ha
 */


import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
	public void initializeListeners() {
		setOnMouseEntered(eventEntered->{
			MediaPlayer.menuHover.play();
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
			MediaPlayer.menuHover.stop();
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
