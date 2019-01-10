package gui;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Title {
	
	// Globale Variablen
	private Pane title;
	private Sprite btnCreate;
	private Sprite btnJoin;
	private Sprite btnTutorial;
	
	public Title() {
		// Start-Container (Child von Anwendungs_CTN)
	    title = new Pane();
	    title.setId("Start");
	    title.setPrefSize(1920, 1080);
	    title.setClip(new Rectangle(title.getPrefWidth(), title.getPrefHeight()));
	    
	    // Spiel-Container INHALTE
		// Logo
	    Sprite logo = new Sprite("resources/logo.png");
	    logo.relocate(title.getPrefWidth()/2 - 1073/2, title.getPrefHeight()/2 - 416/2 - 200);
	    logo.setVy(.25);
	    
	    // Button - Erstellen
	    btnCreate = new Sprite("resources/btn_erstellen.png");
	    btnCreate.setButtonMode(true);
	    btnCreate.relocate(title.getPrefWidth()/2 - 303/2, title.getPrefHeight() - 303 - 100);		
		
	    // Button - Anleitung
	    btnTutorial = new Sprite("resources/btn_tutorial.png");
	    btnTutorial.setButtonMode(true);
	    btnTutorial.relocate(btnCreate.getLayoutX() - 303 - 50, btnCreate.getLayoutY());	
		
	    // Button - Beitreten
	    btnJoin = new Sprite("resources/btn_beitreten.png");
	    btnJoin.setButtonMode(true);
	    btnJoin.relocate(btnCreate.getLayoutX() + 303 + 50, btnCreate.getLayoutY());
	    
		title.getChildren().addAll(logo, btnTutorial, btnCreate, btnJoin);
		
		// GAME LOOP Animationen
		new AnimationTimer() {
	        public void handle(long currentNanoTime) {
	        	
	        	// Animation - Logo
	        	logo.setTranslateY(logo.getTranslateY() - logo.getVy());
	        	if(logo.getTranslateY() == 0) logo.setVy(.25);
	        	if(logo.getTranslateY() == -20) logo.setVy(-.25);

	        }
	    }.start();
		
	}
	
	public Pane getContainer() {
		return title;
	}
	
	public Sprite getBtnCreate() {
		return btnCreate;
	}

	public Sprite getBtnJoin() {
		return btnJoin;
	}
	
}