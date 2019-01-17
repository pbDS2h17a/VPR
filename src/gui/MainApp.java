package gui;

import java.sql.SQLException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sqlConnection.SqlHelper;

/**
 * @author Daniels, Kevin
 * @author pbs2h17ale
 */

public class MainApp extends Application {
	
	/**
	 * @param APP_WIDTH	 : Integer
	 * @param APP_HEIGHT : Integer
	 * @param von		 : Pane
	 * @param zu		 : Pane
	 * @param toPane	 : boolean
	 * 
	 * Anwendungs-Container
	 * @param app		 : Pane
	 * 
	 * Spiel-Container
	 * @param ctn_app 	 : Pane
	 * 
	 * Spiel-Oberflaechen
	 * @param title		 : Title
	 * @param lobby		 : Lobby
	 * @param join		 : Join
	 * @param match		 : Match
	 * 
	 * Scene
	 * @param scene 		 : Scene
	 */
	private final int APP_WIDTH = 1600;
	private final int APP_HEIGHT = 900;
	private Pane von = null;
	private Pane zu  = null;
	private boolean toPane = false;
	
	private Pane app = new Pane();
    private Pane ctn_app = new Pane();
    
    // Spiel-Oberflächen
	private Title title = new Title();
    private Lobby lobby = new Lobby();
    private Join join = new Join();
    private Match match = new Match(lobby);
    private MediaPlayer mp = new MediaPlayer();
    
    // Scene
	private Scene scene = new Scene(app);
	
	@Override
	public void start(Stage stage) {

		// Anwendungs-Container;
	    app.setPrefSize(APP_WIDTH, APP_HEIGHT);
	    app.setStyle("-fx-background-image:url('resources/bg.png'); -fx-background-size: cover; -fx-background-position: 50% 50%;");
	    
	    // Favicon
	    stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("../resources/btn_lobby_host.png")));
	    
	    // Spiel-Container
	    ctn_app.setPrefSize(1920, 1080);
	    resizeThat(stage, ctn_app);

	    // Alles in die richtigen Container schieben
		ctn_app.getChildren().addAll(
				title.getContainer(),
				lobby.getContainer(),
				join.getContainer(),
				match.getContainer(),
				mp.getContainer()
		);

		// Click Events
	    initializeClickEventHandlers();
	    
	    // Game-Loop Method
		gameLoop();
	    
	    // Resize-Methode
	    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
	    	resizeThat(stage, ctn_app);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener); 
		
	    app.getChildren().add(ctn_app);
	    
		mp.playBgmStart();

		for (int i = 0; i < join.getUserList().length; i++) {
			final int tmp = i;
			join.getUserList()[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
				paneTransition(join.getUserList()[tmp], join.getContainer(), lobby.getContainer())
			);
		}

		stage.setTitle("CONQUER | All risk all fun");
		stage.setScene(scene);
		stage.show();
	    
	}
	
	@Override
	public void stop(){
	    System.out.println("Sql verbindung beenden");
	    SqlHelper.closeStatement();
	}
	
	public static void main(String[] args) throws SQLException {
		launch(args);
	}
	
	/**
	 * Initializes all clickEventHandlers.
	 * Initializes transitionStart.
	 */
    public void initializeClickEventHandlers(){
	    title.getBtnCreate().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	paneTransition(title.getBtnCreate(), title.getContainer(), lobby.getContainer());
			mp.playBtnSFX();
	    });
	    	    
	    title.getBtnJoin().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	paneTransition(title.getBtnJoin(), title.getContainer(), join.getContainer());
	    	mp.playBtnSFX();
	    });
	    
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
    }
	    
	
	/**
	 * @param trigger : Sprite
	 * @param v		  : Pane
	 * @param z		  : Pane
	 * Checks if the trigger sprite is set to active.
	 * Set toPane to true for the game loop.
	 */
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
	
	/**
	 * @param trigger : Label
	 * @param v		  : Pane
	 * @param z		  : Pane
	 * Checks if the trigger label is set to active.
	 * Set toPane to true for the game loop.
	 */
	public void paneTransition(Label trigger, Pane v, Pane z) {
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
	
	/**
	 * @param stage	  : Stage
	 * @param ctn_app : Pane
	 * Checks if the ratio is greater than the preferred ratio.
	 * If so : Scales the height.
	 * else : Scales width.
	 */
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
	
	/**
	 * Game loop.
	 */
	public void gameLoop() {
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
	        	
	        	mp.setVolumeGame();
	        	mp.setVolumeStart();
	        }
	    }.start();
	}
}