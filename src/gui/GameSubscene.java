package gui;

/*
 * TODO:
 * 		Comment and finish this.
 */

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

public class GameSubscene extends SubScene{

	//private final String font_path = "src/resources/Roboto-Regular.ttf";
	private final static String background_image_path = "resources/bg.png";
	
	/**
	 * Constructor. 
	 */
	public GameSubscene() {
		super(new AnchorPane(),1024,768);
		prefWidth(1024);
		prefHeight(768);
		
		BackgroundImage background = new BackgroundImage(new Image(background_image_path,256,256,false,true),
														BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		
		AnchorPane rootPane = (AnchorPane) this.getRoot();
		
		rootPane.setBackground(new Background(background));
		
		setLayoutX(1024);
		setLayoutY(180);
	}
	
	public void moveSubScene() {
		TranslateTransition transition = new TranslateTransition();
		transition.setDuration(Duration.seconds(0.4));
		transition.setNode(this);
		
		transition.setToX(-676);
		
		transition.play();
	}

}
