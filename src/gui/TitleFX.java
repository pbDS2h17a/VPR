package gui;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Erstellt die gesamte Oberfläche für das Hauptmenü
 * 
 * @author Kevin Daniels
 */
public class TitleFX {
	
	/**
	 *	@param title	   : Pane
	 *	@param logo        : Sprite
	 *	@param btnCreate   : Sprite
	 *	@param btnJoin	   : Sprite
	 *	@param btnTutorial : Sprite
	 */
	private Pane title = new Pane();
	private Sprite logo = new Sprite("resources/logo.png");
	private Sprite btnCreate = new Sprite("resources/btn_erstellen.png");
	private Sprite btnJoin = new Sprite("resources/btn_beitreten.png");
	private Sprite btnTutorial = new Sprite("resources/btn_tutorial.png");
	
	/**
	 * Constructor.
	 */
	public TitleFX() {
		// Start-Container (Child von Anwendungs_CTN)
	    title = new Pane();
	    title.setId("Start");
	    title.setPrefSize(1920, 1080);
	    title.setClip(new Rectangle(title.getPrefWidth(), title.getPrefHeight()));
	    
	    // Spiel-Container INHALTE
		// Logo
	    logo.relocate(title.getPrefWidth()/2 - 1073/2, title.getPrefHeight()/2 - 416/2 - 200);
	    logo.setVy(.25);
	    title.getChildren().add(logo);
	    
	    // Button - Erstellen
	    btnCreate.setButtonMode(true);
	    btnCreate.relocate(title.getPrefWidth()/2 - 303/2, title.getPrefHeight() - 303 - 100);		
	    title.getChildren().add(btnTutorial);
	    
	    // Button - Anleitung
	    btnTutorial.setButtonMode(true);
	    btnTutorial.relocate(btnCreate.getLayoutX() - 303 - 50, btnCreate.getLayoutY());	
	    title.getChildren().add(btnCreate);
	    
	    // Button - Beitreten
	    btnJoin.setButtonMode(true);
	    btnJoin.relocate(btnCreate.getLayoutX() + 303 + 50, btnCreate.getLayoutY());
		title.getChildren().add(btnJoin);
		
		// GAME LOOP Animationen
		gameLoop();
	}
	
	/**
	 * Game loop to animate the title logo.
	 */
	public void gameLoop() {
		new AnimationTimer() {
	        public void handle(long currentNanoTime) {
	        	
	        	// Animation - Logo
	        	logo.setTranslateY(logo.getTranslateY() - logo.getVy());
	        	if(logo.getTranslateY() == 0) logo.setVy(.25);
	        	if(logo.getTranslateY() == -20) logo.setVy(-.25);

	        }
	    }.start();
	}
	
	/**
	 * @return title : Pane
	 * Returns the pane for the MainApp.
	 */
	public Pane getContainer() {
		return title;
	}
	
	/**
	 * @return btnCreate : Sprite
	 * Returns the sprite for the initializeClickEventHandlers() in the MainApp.
	 */
	public Sprite getBtnCreate() {
		return btnCreate;
	}
	
	/**
	 * @return btnJoin : Sprite
	 * Returns the sprite for the initializeClickEventHandlers() in the MainApp.
	 */
	public Sprite getBtnJoin() {
		return btnJoin;
	}
	
}