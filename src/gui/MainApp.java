package gui;
	
// import gui.ViewManager;

/*
 * TODO: ADD UpDown animation for logo to game loop.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	@Override
	public void start(Stage stage) {
		
		// Variablen
		final int APP_WIDTH = 1600;
		final int APP_HEIGHT = 900;
		
		
		// Anwendungs-Container
	    Pane app = new Pane();
	    app.setPrefSize(APP_WIDTH, APP_HEIGHT);
	    app.setStyle("-fx-background-image:url('resources/bg.png'); -fx-background-size: cover; -fx-background-position: 50% 50%;");
	    
	    
	    // Spiel-Container
	    Pane ctn_game = new Pane();
	    ctn_game.setPrefSize(1920, 1080);
	    resizeThat(stage, ctn_game);
	    ctn_game.setStyle("-fx-background-color: rgba(255,255,255, .2)");
	    
	    // Start-Container
	    Pane ctn_start = new Pane();
	    ctn_start.setPrefSize(1920, 1080);
	    ctn_start.setStyle("-fx-background-color: green");
	    
	    // Alles in den Anwendungs-Container
	    app.getChildren().addAll(ctn_game, ctn_start);
	    
		// GAME LOOP für Animationen
		new AnimationTimer()
	    {
	        public void handle(long currentNanoTime)
	        {
	        	
	        }
	    }.start();
	    
	    // Resize-Funktion
	    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
	    	resizeThat(stage, ctn_game);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener); 
	    
	    // Scene
		Scene scene = new Scene(app);
		stage.setTitle("CONQUER");
		stage.setScene(scene);
		stage.show();
	    
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void resizeThat(Stage stage, Pane game_ctn) {
		
		 double ratio = stage.getWidth() / stage.getHeight();
		 
		 double scale;
		 
		 if(ratio > game_ctn.getPrefWidth() / game_ctn.getPrefHeight())
			 scale = stage.getHeight() / game_ctn.getPrefHeight();
		 else
			 scale = stage.getWidth() / game_ctn.getPrefWidth();
		 
		 if(scale >= 1)
			 scale = 1;
		 
		 game_ctn.setScaleX(scale);
		 game_ctn.setScaleY(scale);
		 game_ctn.relocate(stage.getWidth()/2 - game_ctn.getPrefWidth()/2, stage.getHeight()/2 - game_ctn.getPrefHeight()/2);
		 
	}
	
}