package view;

/*
 * TODO:
 * 		Add Subscene.
 * 		Fine tune button positioning.
 * 		Replace placeholder visuals (backgroundImg,buttonStyle) etc.
 * 		Add title image.
 * 
 * 		set menuButtonStart positions to something.getWidth() to allow for dynamic scaling.
 */

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.stage.Stage;
import model.GameButton;

/**
 * @author Naschk4tze
 * ViewManager class 
 */
public class ViewManager{

	/**
	 * Default width and height:
	 * 	@param defaultMainWidth   : Integer
	 * 	@param defaultMainHeight  : Integer
	 * 
	 * @param mainPane	          : AnchorPane
	 * @param mainScene           : Scene
	 * @param mainStage           : Stage
	 * 
	 * Initial button position.
	 * 	@param menuButtonStart_X  : Integer
	 * 	@param menuButtonStart_Y  : Integer
	 * 
	 * List storing menu buttons.
	 * 	@param menuButtons 		  : List<GameButton>
	 */
	// Maybe 1920x1080 Maybe 1024x768.
	private final int defaultMainWidth = 1024;
	private final int defaultMainHeight = 768; 
	
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private final int menuButtonStart_X = 100; 
	private final int menuButtonStart_Y = 450;

	private ImageView logo = new ImageView("view/resources/logo.png");
	
	List<GameButton> menuButtons;
		
	/**
	 * Constructor.
	 * 
	 * Setting up the mainStage.
	 * Creates all menu buttons.
	 */
	public ViewManager() {
		// AnchorPane used to organize all UI contents.
		// Allows the edges of child nodes to be anchored to an offset from the anchor pane edges and place elements using coordinates.
		menuButtons = new ArrayList<>();
		
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane,defaultMainWidth,defaultMainHeight);
		mainStage = new Stage();
		
		mainStage.setScene(mainScene);
		createLogo();
		CreateButtons();
		createBackground();
		
	}
	
	/**
	 * @return mainStage : Stage
	 * To set the primaryStage in Main.java
	 */
	public Stage getMainStage() {
		return mainStage;
	}
	
	/**
	 * Creates Buttons and adds them to the mainPane.
	 * Add code do generate buttons.
	 */
	private void CreateButtons() {
		createTutorialButton();
		createCreateGameButton();
		createJoinGameButton();
		createSettingsButton();		
	}
	
	/**
	 * Creates specific buttons and commits them  to the addMenuButtons function.
	 */
	private void createTutorialButton() {
		GameButton tutorialButton = new GameButton("Tutorial");
		addMenuButtons(tutorialButton);
	}
	
	private void createCreateGameButton() {
		GameButton createGameButton = new GameButton("Erstellen");
		addMenuButtons(createGameButton);
	}
	
	private void createJoinGameButton() {
		GameButton joinGameButton = new GameButton("Beitreten");
		addMenuButtons(joinGameButton);
	}
	
	private void createSettingsButton() {
		// Placeholder for settings image.
		GameButton settingsButton = new GameButton("SettingsImg");
		//addMenuButtons(settingsButton);
		settingsButton.setLayoutX(menuButtonStart_X + menuButtons.size() * 200);
		settingsButton.setLayoutY(menuButtonStart_Y + 100);
		menuButtons.add(settingsButton);
		mainPane.getChildren().add(settingsButton);
	}
	
	/**
	 * @param button
	 * Sets the layout of all buttons.
	 * Adds menu buttons to list.
	 */
	private void addMenuButtons(GameButton button) {
		button.setLayoutX(menuButtonStart_X + menuButtons.size() * 200);
		button.setLayoutY(menuButtonStart_Y);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}
	
	/**
	 * Creates a background object and adds it to the mainPane.
	 */
	private void createBackground() {
		Image backgroundImage = new Image("view/resources/bg.png",0,0,false,true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
		mainPane.setBackground(new Background(background));
	}
	
	/**
	 * Creates ImageView object logo.
	 * Adds logo to mainPane;
	 */
	public ImageView createLogo() {
		ImageView logo = new ImageView("view/resources/logo.png");
		// Uses placeholder values at the moment.
		logo.setLayoutX(400);
		logo.setLayoutY(50);
		
		logo.setOnMouseEntered(eventEntered->{
			// Sets a DropShadow effect.
			logo.setEffect(new DropShadow());
		});
		
		logo.setOnMouseExited(eventEntered->{
			// Sets a DropShadow effect.
			logo.setEffect(null);
		});
		
		mainPane.getChildren().add(logo);
		return logo;
	}
	
	/**
	 * Method to resize panes. 
	 */
	private void resize(){
		
	}
	
}