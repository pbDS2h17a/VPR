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
import network.ChatInterface;
import sqlConnection.SqlHelper;

/**
 * Startet das gesamte Gesamte Spiel, indem die anderen Oberflächen-Klassen eingebunden werden
 * und die gesamte Scene administriert.
 * 
 * @author Adrian Ledwinka
 * @author Kevin Daniels
 */

public class MainApp extends Application {
	/**
	 * @param APP_WIDTH	 : Integer
	 * @param APP_HEIGHT : Integer
	 * @param von		 : Pane
	 * @param zu		 : Pane
	 * @param app		 : Pane
	 * @param ctnApp 	 : Pane
	 * @param toPane	 : boolean
	 * 
	 * Spiel-Oberflaechen
	 * @param titleFX	 : TitleFX
	 * @param lobbyFX	 : LobbyFX
	 * @param joinFX	 : JoinFx
	 * @param matchFX	 : MatchFX
	 * @param mpFX		 : MediaPlayerFX
	 * @param chatFX	 : ChatInterface
	 * @param scene		 : Scene
	 */
	
	// Globale Variablen
	private final int APP_WIDTH = 1600;
	private final int APP_HEIGHT = 900;
	private Pane von;
	private Pane zu;
	private static Pane app = new Pane();
    private Pane ctnApp = new Pane();
	private boolean toPane = false;
	private static Scene scene = new Scene(app);
    
    // Spiel-Oberflächen
	private TitleFX titleFX = new TitleFX();
    private LobbyFX lobbyFX = new LobbyFX();
    private JoinFX joinFX = new JoinFX();
    private MatchFX matchFX = new MatchFX(lobbyFX);
    private MediaPlayerFX mpFX = new MediaPlayerFX();
    private ChatInterface chatFX;

	/**
	 * @param stage : Stage
	 * 
	 * Stellt alle Spiel-Einstellungen ein damit sie in der main gestartet werden kann.
	 * 
	 */
	@Override
	public void start(Stage stage) {

		// Anwendungs-Container der das Spiel darstellt mit der eingestellten Auflösung (1600x900px)
	    app.setPrefSize(APP_WIDTH, APP_HEIGHT);
	    app.setStyle("-fx-background-image:url('resources/bg.png'); -fx-background-size: cover; -fx-background-position: 50% 50%;");
	    
	    // Favicon für die Taskleiste
	    stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("../resources/btn_lobby_host.png")));
	    
	    // Spiel-Container der alle Oberflächen sammelt und ausgibt
	    ctnApp.setPrefSize(1920, 1080);
	    resizeThat(stage, ctnApp);

	    // Alle Oberflächen werden in den Spiel-Container geladen
		ctnApp.getChildren().addAll(
				titleFX.getContainer(),
				lobbyFX.getContainer(),
				joinFX.getContainer(),
				matchFX.getContainer(),
				mpFX.getContainer()
		);
		
		app.getChildren().add(ctnApp);
		
		// Spielt die Hintergrund-Musik für das Hauptmenü ab
		mpFX.playBgmStart();
		
		// Methode um die ClickEvents zu initialisieren
	    initializeClickEventHandlers();
	    
	    // Methode um die ClickEvents zu initialisieren
		gameLoop();
	    
	    // Resize-Methode die das Spiel immer passend zur Fenstergröße skaliert
	    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
	    	resizeThat(stage, ctnApp);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener); 
		
		// Setzt den Titel für die Anwendung in die Scene und startet sie in der Main
		stage.setTitle("CONQUER | All risk all fun");
		stage.setScene(scene);
		stage.show();
	    
	}
	
	/**
	 * Prozedur, die die Datenbankverbindung beendet wenn das Spiel beendet wird
	 * 
	 */
	@Override
	public void stop(){
	    System.out.println("Sql verbindung beenden");
	    SqlHelper.closeStatement();
	}
	
	/**
	 * @param args	:	String[]
	 * 
	 * Prozedur, die die ganze Anwendung startet sobald die Scene übergeben wurde
	 * 
	 */
	public static void main(String[] args) throws SQLException {
		launch(args);
	}
	
	/**
	 * Prozedur, die alle EventListener startet die für den 
	 */
    public void initializeClickEventHandlers(){
    	
    	// Wennn der Button zum Spiel erstellen gedrückt wird
	    titleFX.getBtnCreate().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation für den Übergang zwischen zwei Panes
	    	paneTransition(titleFX.getBtnCreate(), titleFX.getContainer(), lobbyFX.getContainer());
	    	
	    	// Sound für den gedrückten Button wird abgespielt
			mpFX.playBtnSFX();
			
			// Erstellt das ChatInterface und positioniert es in der Lobby
			chatFX = new ChatInterface(1,1);
			chatFX.getPane().relocate(63, 550);
			ctnApp.getChildren().add(chatFX.getPane());
	    });
	   
	    // Wenn der Button zum Spiel beitreten gedrückt wird
	    titleFX.getBtnJoin().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation für den Übergang zwischen zwei Panes
	    	paneTransition(titleFX.getBtnJoin(), titleFX.getContainer(), joinFX.getContainer());
	    	
	    	// Sound für den gedrückten Button wird abgespielt
	    	mpFX.playBtnSFX();
	    });
	    
	    // Wenn der Button zum Verlassen der Lobby gedrückt wird
	    lobbyFX.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation für den Übergang zwischen zwei Panes
			paneTransition(lobbyFX.getBtnBack(), lobbyFX.getContainer(), titleFX.getContainer());
			
			/**
			 * Sound für den gedrückten Button wird abgespielt
			 * und die Hintergrund-Musik wird gewechselt
			 */
			mpFX.playBtnSFX();
			mpFX.playBgmStart();
			mpFX.stopBgmGame();
	    });
	    
	    // Wenn der Button zum Spieler bestätigen gedrückt wird
	    lobbyFX.getBtnReady().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation für den Übergang zwischen zwei Panes
			paneTransition(lobbyFX.getBtnReady(), lobbyFX.getContainer(), matchFX.getContainer());
			
			// Wird zur Weltkarte gewechselt positioniert sich der Chat um
			if(lobbyFX.getBtnReady().isActive()) {
				chatFX.getPane().relocate(1650, 600);
			}
			
			/**
			 * Sound für den gedrückten Button wird abgespielt
			 * und die Hintergrund-Musik wird gewechselt
			 */
			mpFX.playBtnSFX();
			mpFX.stopBgmStart();
			mpFX.playBgmGame();
	    });
   
	    // Wenn der Button zum Verlassen von "Spiel beitereten" gedrückt wird
	    joinFX.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation für den Übergang zwischen zwei Panes
			paneTransition(joinFX.getBtnBack(), joinFX.getContainer(), titleFX.getContainer());
			
			/**
			 * Sound für den gedrückten Button wird abgespielt
			 * und die Hintergrund-Musik wird gewechselt
			 */
			mpFX.playBtnSFX();
			mpFX.playBgmStart();
			mpFX.stopBgmGame();
	    });
	    
	    // Wenn der Play-Button des MediaPlayers gedrückt wird
		mpFX.getPlayBtn().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// Startet die Hintergrundmusik
			mpFX.playBgmStart();		
		});
		
		
		// Wenn der Stop-Button des MediaPlayers gedrückt wird
		mpFX.title_btn_stop_mediaPlayer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// Stoppt die Hintergrundmusik
			mpFX.stopBgmStart();			
		});
		
		// Schleife um mit allen potentiellen neun Lobbys zu kommunizieren
		for (int i = 0; i < joinFX.getUserList().length; i++) {
			final int tmp = i;
			
			// Wenn auf eine Lobby-Verbindung gedrückt wird
			joinFX.getUserList()[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				// Startet die Animation für den Übergang zwischen zwei Panes
				paneTransition(joinFX.getUserList()[tmp], joinFX.getContainer(), lobbyFX.getContainer());
			});
		}
		
	    matchFX.getBattleReadyBtn().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	if(matchFX.getBattleInputB().isDisabled()) {
	    		matchFX.getRound().setBattleUnitsA(Integer.parseInt(matchFX.getBattleInputA().getText()));
		    	if(matchFX.getRound().getBattleUnitsA() > 0 && matchFX.getRound().getBattleUnitsA() < matchFX.getRound().getCountryA().getUnits()) {
		    		matchFX.getBattleInputA().setDisable(true);
		    		matchFX.getBattleInputB().setDisable(false);
		    	}
	    	}
	    	
	    	else if(matchFX.getBattleInputA().isDisabled()) {
	    		matchFX.getRound().setBattleUnitsB(Integer.parseInt(matchFX.getBattleInputB().getText()));
		    	if(matchFX.getRound().getBattleUnitsB() > 0 && matchFX.getRound().getBattleUnitsB() < 3 && matchFX.getRound().getBattleUnitsB() <= matchFX.getRound().getCountryB().getUnits()) {
		    		System.out.println("*** Kampf beginnt ***");
		    		System.out.println("A: " + matchFX.getRound().getCountryA().getCountryName() + " | B: " + matchFX.getRound().getCountryB().getCountryName());
		    		System.out.println("A Einheiten vorher: " + matchFX.getRound().getCountryA().getUnits());
		    		System.out.println("B Einheiten vorher: " + matchFX.getRound().getCountryB().getUnits());
		    		System.out.println("A schickt in den Tod: " + matchFX.getRound().getBattleUnitsA());
		    		System.out.println("B schickt in den Tod: " + matchFX.getRound().getBattleUnitsB());
		    		
		    		matchFX.getBattleReadyBtn().setActive(false);
		    		Integer[][] rolledDices = matchFX.getRound().rollTheDice(matchFX.getRound().getBattleUnitsA(), matchFX.getRound().getBattleUnitsB());
		    		matchFX.getRound().updateFightResults(rolledDices, matchFX.getRound().getCountryA(), matchFX.getRound().getCountryB());
		    		matchFX.getRound().endFight();
		    	}
	    	}
	    });
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
	        	
	        	mpFX.setVolumeGame();
	        	mpFX.setVolumeStart();
	        }
	    }.start();
	}

	public static Scene getScene() {
		return scene;
	}

}