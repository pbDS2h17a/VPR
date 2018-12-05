package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;

/* 
 * TODO: Add background REAL image urls.
 *		 Add REAL font path
 */

/**
 * @author Naschk4tze
 * Button class to generate customized buttons.
 */
public class GameButton extends Button {

	/**
	 * @param font_path				: String
	 * @param style_button_pressed	: String
	 * @param style_button_released : String
	 */
	private final String font_path = "src/model/resources/Roboto-Regular.ttf";
	// Background color is set to transparent due to the intended use of custom images.
	private final String style_button_pressed = "-fx-background-color:transparent; -fx-background-image: url('/model/resources/blue_button_pressed.png');";
	private final String style_button_released = "-fx-background-color:transparent; -fx-background-image: url('/model/resources/blue_button_released.png');";
	
	
	/**
	 * Constructor.
	 * 
	 * @param setButtonText : String
	 * 
	 * Generates a button with the passed text.
	 * Sets button font, default height, default width and default style.
	 * Initializes button listeners.
	 */
	public GameButton(String setButtonText) {
		setText(setButtonText);
		setButtonFont();
		setPrefHeight(49);
		setPrefWidth(190);
		setStyle(style_button_released);
		initializeButtonListeners();
	}
	
	/**
	 * Tries to set button font and size.
	 * Sets the font to a default if the font file is not found. 
	 * Prints stack trace. 
	 */
	private void setButtonFont() {
		try {
			setFont(Font.loadFont(new FileInputStream(font_path), 23));
		} catch (FileNotFoundException e) {
			setFont(Font.font("Impact", 14));
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the button style to released.
	 * Sets layout coordinates. 
	 */
	private void setButtonStyleReleased() {
		setStyle(style_button_released);
		setPrefHeight(49);
		// Use setLayout to move the object.
		// Add -x/+x if the corresponding image is x pixel smaller or greater than the other image;
		setLayoutY(getLayoutY()-4);
	}
	
	/**
	 * Sets the button style to pressed.
	 * Sets layout coordinates.
	 */
	private void setButtonStylePressed() {
		setStyle(style_button_pressed);
		setPrefHeight(45);
		// Add -x/+x if the corresponding image is x pixel smaller or greater than the other image;
		setLayoutY(getLayoutY()+4);
	}
	
	/**
	 * Initializes all button listeners.
	 * OnMousePressed.
	 * 	Sets the button style to pressed.
	 * OnMouseReleased.
	 * 	Sets the button style to released.
	 * OnMouseEntered.
	 *  Sets a DropShadow effect.
	 * OnMouseExited.
	 * 	Removes the DropShadow effect.
	 */
	private void initializeButtonListeners() {
		
		setOnMousePressed(eventPressed->{
			// Checks if the pressed button was mouse 1.
			if(eventPressed.getButton().equals(MouseButton.PRIMARY)) {
				setButtonStylePressed();
			}
		});
		
		setOnMouseReleased(eventReleased->{
			// Checks if the released button was mouse 1.
			if(eventReleased.getButton().equals(MouseButton.PRIMARY)) {
				setButtonStyleReleased();
			}
		});
		
		setOnMouseEntered(eventEntered->{
			// Sets a DropShadow effect.
			setEffect(new DropShadow());
		});
		
		setOnMouseExited(eventExited->{
			// Removes the DropShadow effect.
			setEffect(null);
		});
	}
}