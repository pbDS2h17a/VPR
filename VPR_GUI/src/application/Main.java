package application;
	
/*
 * TODO: ADD UpDown animation for logo to game loop.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			ViewManager manager = new ViewManager();
			primaryStage = manager.getMainStage();
			
			//<GAME LOOP>
			new AnimationTimer()
		    {
		        public void handle(long currentNanoTime)
		        {
		        	
		        }
		    }.start();
		  //</GAME LOOP>
		    
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}