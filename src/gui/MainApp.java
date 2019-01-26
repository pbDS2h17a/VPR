package gui;

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
import sqlConnection.Country;
import sqlConnection.SqlHelper;

/**
 * Startet das gesamte Gesamte Spiel, indem die anderen Oberflächen-Klassen eingebunden werden
 * und die gesamte Scene administriert.
 * 
 * @author Adrian Ledwinka
 * @author Kevin Daniels
 */
public class MainApp extends Application {
	
	// Globale Variablen
	private final int APP_WIDTH = 1600;
	private final int APP_HEIGHT = 900;
	private Pane paneFrom;
	private Pane paneTo;
	private Pane app = new Pane();
    private Pane ctnApp = new Pane();
	private boolean toPane = false;
	private Scene scene = new Scene(app);
    
    // Spiel-Oberflächen
	private TitleFX titleFX = new TitleFX();
    private LobbyFX lobbyFX = new LobbyFX();
    private JoinFX joinFX = new JoinFX();
    private MatchFX matchFX = new MatchFX(lobbyFX);
    private MediaPlayerFX mpFX = new MediaPlayerFX();
    private ChatInterface chatFX;

	/**
	 * Stellt alle Spiel-Einstellungen ein damit sie in der main gestartet werden kann.
	 * 
	 * @param stage Stage
	 * @see javafx.application.Application#start(javafx.stage.Stage)
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
	    
	    // Methode um die Spiel-Schleife für die Animationen zu initialisieren
		gameLoop();
	    
	    // Methode die das Spiel immer passend zur Fenstergröße skaliert
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
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop(){
	    System.out.println("Sql verbindung beenden");
	    if(chatFX != null) {
			chatFX.getUpdateTask().cancel();
			System.out.println("Chat Updatethread beendet");
		}

	    SqlHelper.closeStatement();
	}

	/**
	 * Prozedur, die die ganze Anwendung startet sobald die Scene übergeben wurde
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Prozedur, die alle EventListener startet die für den 
	 */
    public void initializeClickEventHandlers(){
    	// Wennn der Button zum Spiel erstellen gedrückt wird
	    titleFX.getBtnCreate().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Beendet die Animation des Logos
	    	titleFX.setLogoAnimated(false);
	    	
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
	    	// Beendet die Animation des Logos
	    	titleFX.setLogoAnimated(false);
	    	
	    	// Startet die Animation für den Übergang zwischen zwei Panes
	    	paneTransition(titleFX.getBtnJoin(), titleFX.getContainer(), joinFX.getContainer());
	    	
	    	// Sound für den gedrückten Button wird abgespielt
	    	mpFX.playBtnSFX();
	    });
	    
	    // Wenn der Button zum Verlassen der Lobby gedrückt wird
	    lobbyFX.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation des Logos
	    	titleFX.setLogoAnimated(true);
	    	
	    	// Startet die Animation für den Übergang zwischen zwei Panes
			paneTransition(lobbyFX.getBtnBack(), lobbyFX.getContainer(), titleFX.getContainer());
			
			/*
			 * Sound für den gedrückten Button wird abgespielt
			 * und die Hintergrund-Musik wird gewechselt
			 */
			mpFX.playBtnSFX();
			mpFX.playBgmStart();
			mpFX.stopBgmGame();
	    });
	    
	    // Wenn der Button zum Spieler bestätigen gedrückt wird
	    lobbyFX.getBtnReady().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Wenn der Button aktiv ist...
	    	if(lobbyFX.getBtnReady().isActive()) {
		    	// ...startet die Animation für den Übergang zwischen zwei Panes
				paneTransition(lobbyFX.getBtnReady(), lobbyFX.getContainer(), matchFX.getContainer());
				
				// ...wird zur Weltkarte gewechselt positioniert sich der Chat neu
				chatFX.getPane().relocate(1650, 600);
				
				/*
				 * Sound für den gedrückten Button wird abgespielt
				 * und die Hintergrund-Musik wird gewechselt
				 */
				mpFX.playBtnSFX();
				mpFX.stopBgmStart();
				mpFX.playBgmGame();
				
				// ...das Round-Objekt wird erstellt mit den Daten der Lobby und Weltkarte
				matchFX.setRound(new Round(matchFX, matchFX.getLobby().getPlayers()));
	    	}
	    });
   
	    // Wenn der Button zum Verlassen von "Spiel beitereten" gedrückt wird
	    joinFX.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation des Logos
	    	titleFX.setLogoAnimated(true);
	    	
	    	// Startet die Animation für den Übergang zwischen zwei Panes
			paneTransition(joinFX.getBtnBack(), joinFX.getContainer(), titleFX.getContainer());
			
			/*
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
		    	// Beendet die Animation des Logos
		    	titleFX.setLogoAnimated(false);
		    	
				// Startet die Animation für den Übergang zwischen zwei Panes
				paneTransition(joinFX.getUserList()[tmp], joinFX.getContainer(), lobbyFX.getContainer());
			});
		}
		
		// Wenn der Button für die 1. Phase (setzen) gedrückt wird
	    matchFX.getPhaseBtn1().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 1
	    	matchFX.getRound().phaseAdd()
	    );
    	
	    // Wenn der Button für die 2. Phase (kämpfen) gedrückt wird
	    matchFX.getPhaseBtn2().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 2
	    	matchFX.getRound().phaseFight()
	    );
	    
	    // Wenn der Button für die 3. Phase (bewegen) gedrückt wird
	    matchFX.getPhaseBtn3().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 3
	    	matchFX.getRound().phaseMove()
	    );
	    
	    // Wenn der Button für die 4. Phase (Ende) gedrückt wird
	    matchFX.getPhaseBtn4().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 4
	    	matchFX.getRound().nextTurn()
	    );
	    
	    // Wenn der Cursor sich über den Auftrag-Button befindet
	    matchFX.getPlayerInfoAuftragGroup().addEventHandler(MouseEvent.MOUSE_MOVED, event ->
	    	// Bewegt den Auftrag-Container nach rechts
	    	matchFX.getPlayerInfoAuftragGroup().setLayoutX(200)
	    );
		
	    // Wenn der Cursor den Auftrag-Button verlässt
	    matchFX.getPlayerInfoAuftragGroup().addEventHandler(MouseEvent.MOUSE_EXITED, event ->
	    	// Bewegt den Auftrag-Container wieder nach links
	    	matchFX.getPlayerInfoAuftragGroup().setLayoutX(-200)
		);
	    
	    // Wenn im Kampfbildschirm auf den Bestätige-Button gedrückt wird
	    matchFX.getBattleReadyBtn().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	/*
	    	 * Wenn der Verteidigungs-Input deaktiviert ist befinden wir
	    	 * uns noch in der ersten Eingabe
	    	 */
	    	if(matchFX.getBattleInputB().isDisabled()) {
	    		// Die Einheiten zum Angreifen werden gesetzt
	    		matchFX.getRound().setBattleUnitsA(Integer.parseInt(matchFX.getBattleInputA().getText()));
	    		
	    		// Wenn der Wert der Angreifer im erlaubten Bereich sind...
		    	if(matchFX.getRound().getBattleUnitsA() > 0 && matchFX.getRound().getBattleUnitsA() < matchFX.getRound().getCountryA().getUnits()) {
		    		
		    		// ...werden die Eingabefelder getauscht, damit die Else-Bedingungen erfüllt wird
		    		matchFX.getBattleInputA().setDisable(true);
		    		matchFX.getBattleInputB().setDisable(false);
		    	}
	    	}
	    	
	    	/*
	    	 * Wenn der Angriffs-Input deaktiviert ist befinden wir
	    	 * uns nun in der zweiten Eingabe
	    	 */
	    	else if(matchFX.getBattleInputA().isDisabled()) {
	    		// Die Einheiten zum Verteidigen werden gesetzt
	    		matchFX.getRound().setBattleUnitsB(Integer.parseInt(matchFX.getBattleInputB().getText()));
	    		
	    		// Wenn der Wert der Verteidiger im erlaubten Bereich sind...
		    	if(matchFX.getRound().getBattleUnitsB() > 0 && matchFX.getRound().getBattleUnitsB() < 3 && matchFX.getRound().getBattleUnitsB() <= matchFX.getRound().getCountryB().getUnits()) {
		    		
		    		// Ausgabe für die Konsole zur Kontrolle
		    		System.out.println("*** Kampf beginnt ***");
		    		System.out.println("A: " + matchFX.getRound().getCountryA().getCountryName() + " | B: " + matchFX.getRound().getCountryB().getCountryName());
		    		System.out.println("A Einheiten vorher: " + matchFX.getRound().getCountryA().getUnits());
		    		System.out.println("B Einheiten vorher: " + matchFX.getRound().getCountryB().getUnits());
		    		System.out.println("A schickt in den Tod: " + matchFX.getRound().getBattleUnitsA());
		    		System.out.println("B schickt in den Tod: " + matchFX.getRound().getBattleUnitsB());
		    		
		    		// Button wird deaktiviert um weitere Eingaben zu vermeiden
		    		matchFX.getBattleReadyBtn().setActive(false);
		    		
		    		// Es werden Würfel gewürfelt anhand der eingesetzten Einheiten
		    		Integer[][] rolledDices = matchFX.getRound().rollTheDice(matchFX.getRound().getBattleUnitsA(), matchFX.getRound().getBattleUnitsB());
		    		
		    		// Auf Basis der Würfe wird der Kampf durchgeführt
		    		matchFX.getRound().updateFightResults(rolledDices, matchFX.getRound().getCountryA(), matchFX.getRound().getCountryB());
		    		
		    		// Ist der Kampf vorbei wird der Kampf beendet und die Länder aktualisiert
		    		matchFX.getRound().endFight();
		    	}
	    	}
	    });
	    
	    // Country-Array zwischenspeichern um die Variablen-Namen der Schleife abzukürzen
	    Country[] countryArray = matchFX.getCountryArray();
	    // Schleife um mit allen 42 Ländern zu kommunizieren
	    for (int i = 0; i < matchFX.getCountryArray().length; i++) {
			final int COUNT = i;
			
			// Wenn ein Land oder die Einheiten im Land angeklickt werden
			countryArray[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				matchFX.getRound().manageCountryClick(COUNT);
		    });
			
			// Wenn ein Land oder die Einheiten im Land angeklickt werden
	    	matchFX.getCountryUnitsBGArray()[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				matchFX.getRound().manageCountryClick(COUNT);
		    });
	    	
	    	// Wenn ein Land oder die Einheiten im Land angeklickt werden
	    	matchFX.getCountryUnitsLabelArray()[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				matchFX.getRound().manageCountryClick(COUNT);
		    });
			
			// Wenn der Cursor auf einem Land oder Einheit des Landes platziert wird
	    	countryArray[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		// Aktualisiert das Interface
	    		matchFX.updateCountryInfo(countryArray[COUNT]);
	    		matchFX.gameMarkNeighbourCountrys(countryArray[COUNT]);
	    	});
	    	
	    	// Wenn der Cursor auf einem Land oder Einheit des Landes platziert wird
	    	matchFX.getCountryUnitsBGArray()[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		// Aktualisiert das Interface
	    		matchFX.updateCountryInfo(countryArray[COUNT]);
	    		matchFX.gameMarkNeighbourCountrys(countryArray[COUNT]);
	    	});
	    	
	    	// Wenn der Cursor auf einem Land oder Einheit des Landes platziert wird
	    	matchFX.getCountryUnitsLabelArray()[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		// Aktualisiert das Interface
	    		matchFX.updateCountryInfo(countryArray[COUNT]);
	    		matchFX.gameMarkNeighbourCountrys(countryArray[COUNT]);
	    	});


	    	
		}
    }

	/**
	 * Steuert den Übergang zwischen zwei Panes für die Animation
	 * 
	 * @param trigger Sprite
	 * @param from Pane
	 * @param to Pane
	 */
	public void paneTransition(Sprite trigger, Pane from, Pane to) {
		// Wenn der gedrückte Button aktiv ist, kann die Animation gestartet werden
		if(trigger.isActive()) {
			
			/*
			 * Die noch aktive Pane wird mit Daten bestückt,
			 * die sich während der Animation verändern schrittweise werden
			 */
			paneFrom = from;
			paneFrom.setCache(true);
			paneFrom.setOpacity(1);
			paneFrom.setScaleX(1);
			paneFrom.setScaleY(1);
			
			/*
			 * Die noch deaktivierte Pane wird mit Daten bestückt,
			 * die sich während der Animation verändern schrittweise werden
			 */
			paneTo = to;
			paneTo.setCache(true);
			paneTo.setVisible(true);
			paneTo.setOpacity(0);
			paneTo.setScaleX(1.5);
			paneTo.setScaleY(1.5);
			
			// Startet die Animation zwischen den beiden Panes
			toPane = true;
		}
	}

	/**
	 * Prozedur, die die ganze Anwendung startet sobald die Scene übergeben wurde
	 * 
	 * @param trigger Label
	 * @param from Pane
	 * @param to Pane
	 * @see MainApp#paneTransition(Sprite trigger, Pane from, Pane to)
	 */
	public void paneTransition(Label trigger, Pane from, Pane to) {
		/*
		 * Die noch aktive Pane wird mit Daten bestückt,
		 * die sich während der Animation verändern schrittweise werden
		 */
		paneFrom = from;
		paneFrom.setCache(true);
		paneFrom.setOpacity(1);
		paneFrom.setScaleX(1);
		paneFrom.setScaleY(1);
		
		/*
		 * Die noch deaktivierte Pane wird mit Daten bestückt,
		 * die sich während der Animation verändern schrittweise werden
		 */
		paneTo = to;
		paneTo.setCache(true);
		paneTo.setVisible(true);
		paneTo.setOpacity(0);
		paneTo.setScaleX(1.5);
		paneTo.setScaleY(1.5);
		
		// Startet die Animation zwischen den beiden Panes
		toPane = true;
	}
	
	/**
	 * Wird aufgerufen wenn die Fenstergröße geändert wird und
	 * skaliert den Spiel-Container immer passend
	 * 
	 * @param stage Stage
	 * @param ctn_app Pane
	 */
	public void resizeThat(Stage stage, Pane ctn_app) {
		 // speichert das gewünschte Seitenverhältnis
		 double ratio = stage.getWidth() / stage.getHeight();
		 double scale;
		 
		 // Vergleicht beide Verhältnisse und ändert dementsprechend den Skalierungs-Faktor
		 if(ratio > ctn_app.getPrefWidth() / ctn_app.getPrefHeight())
			 scale = stage.getHeight() / ctn_app.getPrefHeight();
		 else
			 scale = stage.getWidth() / ctn_app.getPrefWidth();
		 
		 /*
		  * Sollte es größer als 1080p sein wird die Skalierung auf 1 gesetzt
		  * um verpixelung der Bilder zu verhindern
		  */ 
		 if(scale >= 1)
			 scale = 1;
		 
		 // Setzt die Skalierung und Position im Spiel-Container
		 ctn_app.setScaleX(scale);
		 ctn_app.setScaleY(scale);
		 ctn_app.relocate(stage.getWidth()/2 - ctn_app.getPrefWidth()/2, stage.getHeight()/2 - ctn_app.getPrefHeight()/2);
	}
	
	/**
	 * Eine Endlosschleife die 60 mal die Sekunde aufgerufen wird um flüssige Animationen zu ermöglichen
	 */
	public void gameLoop() {
		new AnimationTimer() {
	        public void handle(long currentNanoTime) {
	        	
	        	// Wenn das Logo animiert werden soll...
	        	if(titleFX.isLogoAnimated()) {
	        		/*
	        		 *  ... wird es nach unten verschoben bis zu einem gewissen Punkt,
	        		 *  wo es dann wieder hoch verschoben wird
	        		 */
	        		titleFX.getLogo().setTranslateY(titleFX.getLogo().getTranslateY() - titleFX.getLogo().getVy());
		        	if(titleFX.getLogo().getTranslateY() == 0) titleFX.getLogo().setVy(.25);
		        	if(titleFX.getLogo().getTranslateY() == -20) titleFX.getLogo().setVy(-.25);
	        	}
	        	
	        	// Wenn der Übergang zwischen zwei Panes aktiviert wird
	        	if(toPane) {
	        		
	        		// Die vorherige Pane wird schrittweise verkleinert und ausgeblendet
	        		if(paneFrom.getScaleY() < 1.5) {
		        		paneFrom.setScaleX(paneFrom.getScaleX() + .02);
		        		paneFrom.setScaleY(paneFrom.getScaleY() + .02);
	        		}

	        		if(paneFrom.getOpacity() > 0)
	        			paneFrom.setOpacity(paneFrom.getOpacity() - .05);
	        		
	        		// Die nächste Pane wird schrittweise vergrößert und eingeblendet
	        		if(paneTo.getScaleY() > 1) {
	        			paneTo.setScaleX(paneTo.getScaleX() - .02);
	        			paneTo.setScaleY(paneTo.getScaleY() - .02);
	        		}
	        		
	        		if(paneTo.getOpacity() < 1)
	        			paneTo.setOpacity(paneTo.getOpacity() + .05);

	        		
	        		/*
	        		 * Sind beide Übergänge fertig wird die aktuelle Pane endgültig deaktiviert,
	        		 * die nächste aktiviert und die Animation durch den boolean toPane beendet
	        		 */
	        		if(paneFrom.getScaleY() >= 1.5 && paneTo.getScaleY() <= 1) {
	        			paneFrom.setCache(true);
	        			paneFrom.setVisible(false);
	        			paneTo.setCache(false);
	        			
	        			toPane = false;
	        		}
	        	}
	        	
	        	// Aktualisiert die Lautstärke der Hintergrund-Musik
	        	mpFX.setVolumeGame();
	        	mpFX.setVolumeStart();
	        }
	    }.start();
	}

}