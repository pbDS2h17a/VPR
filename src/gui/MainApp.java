package gui;

import java.sql.SQLException;
import java.sql.Statement;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sqlConnection.SqlHelper;
import sqlCreation.SqlQuery;

public class MainApp extends Application {
	
	// Globale Variablen
	private final int APP_WIDTH = 1600;
	private final int APP_HEIGHT = 900;
	private Pane von = null;
	private Pane zu  = null;
	private boolean toPane = false;
	
	@Override
	public void start(Stage stage) {

		// Anwendungs-Container
	    Pane app = new Pane();
	    app.setPrefSize(APP_WIDTH, APP_HEIGHT);
	    app.setStyle("-fx-background-image:url('resources/bg.png'); -fx-background-size: cover; -fx-background-position: 50% 50%;");
	    
	    // Favicon
	    stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("../resources/btn_lobby_host.png")));
	    
	    // Spiel-Container
	    Pane ctn_app = new Pane();
	    ctn_app.setPrefSize(1920, 1080);
	    resizeThat(stage, ctn_app);
	    
	    // Spiel-Oberflächen
	    Title title = new Title();
	    Lobby lobby = new Lobby();
	    Join join = new Join();
	    Match match = new Match(lobby);
	    MediaPlayer mp = new MediaPlayer();

	    // Alles in die richtigen Container schieben
		ctn_app.getChildren().addAll(
				title.getContainer(),
				lobby.getContainer(),
				join.getContainer(),
				match.getContainer(),
				mp.getContainer()
		);

	    app.getChildren().add(ctn_app);

		// Click Events
	    title.getBtnCreate().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			paneTransition(title.getBtnCreate(), title.getContainer(), lobby.getContainer());
			mp.playBtnSFX();
	    });
	    	    
//	    title.getBtnJoin().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//	    	paneTransition(title.getBtnJoin(), title.getContainer(), join.getContainer());
//	    	mp.playBtnSFX();
//	    });
	    
	    lobby.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			paneTransition(lobby.getBtnBack(), lobby.getContainer(), title.getContainer());
			mp.playBtnSFX();
			mp.playBgmStart();
			mp.stopBgmGame();
	    });
	    
	    lobby.getBtnReady().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			paneTransition(lobby.getBtnReady(), lobby.getContainer(), match.getContainer());
			mp.playBtnSFX();
			mp.stopBgmStart();
			mp.playBgmGame();
	    });
   
	    join.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			paneTransition(join.getBtnBack(), join.getContainer(), title.getContainer());
			mp.playBtnSFX();
			mp.playBgmStart();
			mp.stopBgmGame();
	    });
	    
		mp.getPlayBtn().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			mp.playBgmStart();		
		});
		
		mp.title_btn_stop_mediaPlayer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			mp.stopBgmStart();			
		});
	    
		mp.playBgmStart();
		
	    /*
	    join.getBtnCheck().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> 
			paneTransitionStart(join.getBtnCheck(), join.getContainer(), lobby.getContainer())
        );*/

		// GAME LOOP Animationen
		new AnimationTimer() {
	        public void handle(long currentNanoTime) {
	        	if(toPane) {
	        		if(von.getScaleY() < 1.5) {
		        		von.setScaleX(von.getScaleX() + .02);
		        		von.setScaleY(von.getScaleY() + .02);
	        		}

	        		if(von.getOpacity() > 0)
	        			von.setOpacity(von.getOpacity() - .05);
	        		
	        		if(zu.getScaleY() > 1) {
	        			zu.setScaleX(zu.getScaleX() - .02);
	        			zu.setScaleY(zu.getScaleY() - .02);
	        		}
	        		
	        		if(zu.getOpacity() < 1)
	        			zu.setOpacity(zu.getOpacity() + .05);

	        		if(von.getScaleY() >= 1.5 && zu.getScaleY() <= 1) {
	        			von.setCache(true);
	        			von.setVisible(false);
	        			zu.setCache(false);
	        			
	        			toPane = false;
	        		}
	        	}
	        	
//	        	mp.setVolumeGame();
//	        	mp.setVolumeStart();
	        }
	    }.start();
	    
	    // Resize-Funktion
	    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
	    	resizeThat(stage, ctn_app);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener); 
		
	    // Scene
		Scene scene = new Scene(app);
		stage.setTitle("CONQUER | All risk all fun");
		stage.setScene(scene);
		stage.show();
	    
	}
	
	@Override
	public void stop(){
		SqlQuery.disableForeignKeyConstraints();
		
		System.out.println("Lobby table clearen");
		SqlHelper.clearTable("lobby");
		
		System.out.println("Player table clearen");
		SqlHelper.clearTable("player");
		
		SqlQuery.enableForeignKeyConstraints();
	    System.out.println("Sql verbindung beenden");
	    SqlHelper.closeStatement();
	    // Save file
	}
	
	public static void main(String[] args) throws SQLException {
      Statement stmt = SqlHelper.getStatement();
      
      // testspieler in Lobby eintragen
      SqlQuery.disableForeignKeyConstraints();

      stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser1','127.0.0.1', 1 ,1)");
      stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser2','127.0.0.1', 1 ,2)");
      stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser3','127.0.0.1', 1 ,3)");
      stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser4','127.0.0.1', 1 ,4)");
      stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser5','127.0.0.1', 1 ,5)");
      stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser6','127.0.0.1', 1 ,6)");
      stmt.executeUpdate("INSERT INTO lobby VALUES(NULL,DEFAULT,NULL,1,1,1)");
      SqlQuery.enableForeignKeyConstraints();
		launch(args);
	}
	
	public void paneTransition(Sprite trigger, Pane v, Pane z) {
		if(trigger.isActive()) {
			von = v;
			von.setCache(true);
			von.setOpacity(1);
			von.setScaleX(1);
			von.setScaleY(1);
			
			zu = z;
			zu.setCache(true);
			zu.setVisible(true);
			zu.setOpacity(0);
			zu.setScaleX(1.5);
			zu.setScaleY(1.5);
			
			toPane = true;
		}
	}

	public void resizeThat(Stage stage, Pane ctn_app) {
		 double ratio = stage.getWidth() / stage.getHeight();
		 double scale;
		 
		 if(ratio > ctn_app.getPrefWidth() / ctn_app.getPrefHeight())
			 scale = stage.getHeight() / ctn_app.getPrefHeight();
		 else
			 scale = stage.getWidth() / ctn_app.getPrefWidth();
		 
		 if(scale >= 1)
			 scale = 1;
		 
		 ctn_app.setScaleX(scale);
		 ctn_app.setScaleY(scale);
		 ctn_app.relocate(stage.getWidth()/2 - ctn_app.getPrefWidth()/2, stage.getHeight()/2 - ctn_app.getPrefHeight()/2);
	}
	
}