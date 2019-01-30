package gui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import network.ChatInterface;
import sqlConnection.Country;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

/**
 * Startet das gesamte Gesamte Spiel, indem die anderen Oberfl�chen-Klassen eingebunden werden
 * und die gesamte Scene administriert.
 * 
 * @author Adrian Ledwinka
 * @author Hoang Ha Dang
 * @author Kevin Daniels
 * @author Nam Max Liebner
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
	private Player player;
    
    // Spiel-Oberfl�chen
	private TitleFX titleFX = new TitleFX();
    private LobbyFX lobbyFX = new LobbyFX();
    private JoinFX joinFX = new JoinFX();
    private MatchFX matchFX = new MatchFX();
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

		// Anwendungs-Container der das Spiel darstellt mit der eingestellten Aufl�sung (1600x900px)
	    app.setPrefSize(APP_WIDTH, APP_HEIGHT);
	    app.setStyle("-fx-background-image:url('resources/bg.png'); -fx-background-size: cover; -fx-background-position: 50% 50%;");
	    
	    // Favicon f�r die Taskleiste
	    stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("../resources/btn_lobby_host.png")));
	    
	    // Spiel-Container der alle Oberfl�chen sammelt und ausgibt
	    ctnApp.setPrefSize(1920, 1080);
	    resizeThat(stage, ctnApp);

	    // Alle Oberfl�chen werden in den Spiel-Container geladen
		ctnApp.getChildren().addAll(
				titleFX.getContainer(),
				lobbyFX.getContainer(),
				joinFX.getContainer(),
				matchFX.getContainer(),
				mpFX.getContainer()
		);
		
		app.getChildren().add(ctnApp);
		
		// Spielt die Hintergrund-Musik f�r das Hauptmen� ab
		mpFX.playBgmStart();
		
		// Methode um die ClickEvents zu initialisieren
	    initializeClickEventHandlers();
	    
	    // Methode um die Spiel-Schleife f�r die Animationen zu initialisieren
		gameLoop();
	    
	    // Methode die das Spiel immer passend zur Fenstergr��e skaliert
	    ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
	    	resizeThat(stage, ctnApp);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener);
		// Setzt den Titel f�r die Anwendung in die Scene und startet sie in der Main
		stage.setTitle("CONQUER | All risk all fun");
		scene.getStylesheets().add("resources/style.css");
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
	 * Prozedur, die die ganze Anwendung startet sobald die Scene �bergeben wurde
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Prozedur, die alle EventListener startet die f�r den 
	 */
    public void initializeClickEventHandlers(){
    	// Wennn der Button zum Spiel erstellen gedr�ckt wird
	    titleFX.getBtnCreate().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Beendet die Animation des Logos
	    	titleFX.setLogoAnimated(false);

	    	// Startet die Animation f�r den �bergang zwischen zwei Panes
	    	paneTransition(titleFX.getBtnCreate(), titleFX.getContainer(), lobbyFX.getContainer());
	    	
	    	// Sound f�r den gedr�ckten Button wird abgespielt
			mpFX.playBtnSFX();
			
			//Lobby leeren
			lobbyFX.getLobby().clearPlayers();
			
			//Spieler-Objekt und Chat-Objekt werden erstellt
	    	createPlayer();
//	    	lobbyFX.getLobby().setLobbyLeader(player);
//	    	lobbyFX.guiAddPlayer(lobbyFX.getNextSlotId());
	    });
	   
	    // Wenn der Button zum Spiel beitreten gedr�ckt wird
	    titleFX.getBtnJoin().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Beendet die Animation des Logos
	    	titleFX.setLogoAnimated(false);
	    	
	    	// Startet die Animation f�r den �bergang zwischen zwei Panes
	    	paneTransition(titleFX.getBtnJoin(), titleFX.getContainer(), joinFX.getContainer());
	    	
	    	// Sound f�r den gedr�ckten Button wird abgespielt
	    	mpFX.playBtnSFX();
	    	
	    	//Spieler-Objekt und Chat-Objekt werden erstellt
	    	createPlayer();


	    });
	    
	    // Wenn der Button zum Verlassen der Lobby gedr�ckt wird
	    lobbyFX.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation des Logos
	    	titleFX.setLogoAnimated(true);
	    	
	    	// Startet die Animation f�r den �bergang zwischen zwei Panes
			paneTransition(lobbyFX.getBtnBack(), lobbyFX.getContainer(), titleFX.getContainer());
			
			/*
			 * Sound f�r den gedr�ckten Button wird abgespielt
			 * und die Hintergrund-Musik wird gewechselt
			 */
			mpFX.playBtnSFX();
			mpFX.playBgmStart();
			mpFX.stopBgmGame();
	    });
	    
	    // Wenn der Button zum Spieler best�tigen gedr�ckt wird
	    lobbyFX.getBtnReady().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Wenn der Button aktiv ist...
	    	if(lobbyFX.getBtnReady().isActive()) {
		    	// ...startet die Animation f�r den �bergang zwischen zwei Panes
				paneTransition(lobbyFX.getBtnReady(), lobbyFX.getContainer(), matchFX.getContainer());
				
				// ...wird zur Weltkarte gewechselt positioniert sich der Chat neu
				chatFX.getPane().relocate(1580, 460);
				chatFX.getPane().setPrefWidth(300);
				
				/*
				 * Sound f�r den gedr�ckten Button wird abgespielt
				 * und die Hintergrund-Musik wird gewechselt
				 */
				mpFX.playBtnSFX();
				mpFX.stopBgmStart();
				mpFX.playBgmGame();
				
				// ...das Round-Objekt wird erstellt mit den Daten der Lobby und Weltkarte
				matchFX.initializeMatch(lobbyFX);
				matchFX.setGameMechanics(new GameMechanics(matchFX,lobbyFX.getLobby().getPlayers()));
	    	}
	    });
   
	    // Wenn auf ein Farben-Quadrat in der Lobby gedr�ckt wird
	    for(int i = 0; i < lobbyFX.getColors().length; i++)  {
	    	final int COUNT = i;

	    	lobbyFX.getColorRectArray()[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    		// Farbe des Slots der Person die gedr�ckt hat wird aktualisiert
				player.setColor(lobbyFX.getColorRectArray()[COUNT].getFill().toString());
	    		lobbyFX.guiChangeColor(player.getSlotId(), lobbyFX.getColorRectArray()[COUNT].getFill());
	    	});
	    }
	    
	    // Wenn auf den Best�tigen-Button neben dem Namens-Eingefeld gedr�ckt wird
	    lobbyFX.getBtnCheck().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// �ndert den Namen des Spielers in seinem Slot
	    	lobbyFX.changePlayerName(player.getSlotId(), lobbyFX.getInputName().getText());
	    });
	    
	    // Wenn im Namens-Eingabefeld eine Taste gedr�ckt wird
	    lobbyFX.getInputName().setOnKeyReleased(event -> {
	    	// Wenn diese Taste "Enter" ist...
	    	if (event.getCode() == KeyCode.ENTER) {
	    		// ...wird der Name des Spielers in seinem Slot ge�ndert
	    		lobbyFX.changePlayerName(lobbyFX.getNextSlotId(), lobbyFX.getInputName().getText());
	    	}

	    });
	    // Limitiert die Zeichen im Eingabefeld auf 15 Zeichen
	    addTextLimiter(lobbyFX.getInputName(), 15);
	    
	    // Wenn auf das rote Kreuz eines Spielers gedr�ckt wird
	    for(int i = 1; i < lobbyFX.getSlotRolesArray().length; i++) {
	    	final int COUNT = i;

	    	lobbyFX.getSlotRolesArray()[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    		lobbyFX.guiRemovePlayer(COUNT);
	    	});
	    }
	    
	    // Wenn der Button zum Verlassen von "Spiel beitereten" gedr�ckt wird
	    joinFX.getBtnBack().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	// Startet die Animation des Logos
	    	titleFX.setLogoAnimated(true);
	    	
	    	// Startet die Animation f�r den �bergang zwischen zwei Panes
			paneTransition(joinFX.getBtnBack(), joinFX.getContainer(), titleFX.getContainer());
			
			/*
			 * Sound f�r den gedr�ckten Button wird abgespielt
			 * und die Hintergrund-Musik wird gewechselt
			 */
			mpFX.playBtnSFX();
			mpFX.playBgmStart();
			mpFX.stopBgmGame();
	    });
	    
	    // Wenn der Play-Button des MediaPlayers gedr�ckt wird
		mpFX.getPlayBtn().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// Startet die Hintergrundmusik
			mpFX.playBgmStart();		
		});
		
		// Wenn der Stop-Button des MediaPlayers gedr�ckt wird
		mpFX.title_btn_stop_mediaPlayer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			// Stoppt die Hintergrundmusik
			mpFX.stopBgmStart();			
		});
		
		// Schleife um mit allen potentiellen neun Lobbys zu kommunizieren
		for (int i = 0; i < joinFX.getUserList().length; i++) {
			final int tmp = i;
			
			// Wenn auf eine Lobby-Verbindung gedr�ckt wird
			joinFX.getUserList()[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
		    	// Beendet die Animation des Logos
		    	titleFX.setLogoAnimated(false);
		    	
				// Startet die Animation f�r den �bergang zwischen zwei Panes
				paneTransition(joinFX.getUserList()[tmp], joinFX.getContainer(), lobbyFX.getContainer());
			});
		}
		
		// Wenn der Button f�r die 1. Phase (setzen) gedr�ckt wird
	    matchFX.getPhaseBtn1().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 1
	    	matchFX.getGameMechanics().phaseAdd()
	    );
    	
	    // Wenn der Button f�r die 2. Phase (k�mpfen) gedr�ckt wird
	    matchFX.getPhaseBtn2().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 2
	    	matchFX.getGameMechanics().phaseFight()
	    );
	    
	    // Wenn der Button f�r die 3. Phase (bewegen) gedr�ckt wird
	    matchFX.getPhaseBtn3().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 3
	    	matchFX.getGameMechanics().phaseMove()
	    );
	    
	    // Wenn der Button f�r die 4. Phase (Ende) gedr�ckt wird
	    matchFX.getPhaseBtn4().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    	// initiiert Phase 4
	    	matchFX.getGameMechanics().nextTurn()
	    );
	    
	    // Wenn der Cursor sich �ber den Auftrag-Button befindet
	    matchFX.getPlayerInfoAuftragGroup().addEventHandler(MouseEvent.MOUSE_MOVED, event ->
	    	// Bewegt den Auftrag-Container nach rechts
	    	matchFX.getPlayerInfoAuftragGroup().setLayoutX(200)
	    );
		
	    // Wenn der Cursor den Auftrag-Button verl�sst
	    matchFX.getPlayerInfoAuftragGroup().addEventHandler(MouseEvent.MOUSE_EXITED, event ->
	    	// Bewegt den Auftrag-Container wieder nach links
	    	matchFX.getPlayerInfoAuftragGroup().setLayoutX(-200)
		);
	    
	    // Wenn im Kampfbildschirm auf den Best�tige-Button gedr�ckt wird
	    matchFX.getBattleReadyBtn().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	/*
	    	 * Wenn der Verteidigungs-Input deaktiviert ist befinden wir
	    	 * uns noch in der ersten Eingabe
	    	 */
	    	if(matchFX.getBattleInputB().isDisabled()) {
	    		// Die Einheiten zum Angreifen werden gesetzt
	    		matchFX.getGameMechanics().setBattleUnitsA(Integer.parseInt(matchFX.getBattleInputA().getText()));
	    		
	    		// Wenn der Wert der Angreifer im erlaubten Bereich sind...
		    	if(matchFX.getGameMechanics().getBattleUnitsA() > 0 && matchFX.getGameMechanics().getBattleUnitsA() < matchFX.getGameMechanics().getCountryA().getUnits()) {
		    		
		    		// ...werden die Eingabefelder getauscht, damit die Else-Bedingungen erf�llt wird
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
	    		matchFX.getGameMechanics().setBattleUnitsB(Integer.parseInt(matchFX.getBattleInputB().getText()));
	    		
	    		// Wenn der Wert der Verteidiger im erlaubten Bereich sind...
		    	if(matchFX.getGameMechanics().getBattleUnitsB() > 0 && matchFX.getGameMechanics().getBattleUnitsB() < 3 && matchFX.getGameMechanics().getBattleUnitsB() <= matchFX.getGameMechanics().getCountryB().getUnits()) {
		    		
		    		// Ausgabe f�r die Konsole zur Kontrolle
		    		System.out.println("*** Kampf beginnt ***");
		    		System.out.println("A: " + matchFX.getGameMechanics().getCountryA().getCountryName() + " | B: " + matchFX.getGameMechanics().getCountryB().getCountryName());
		    		System.out.println("A Einheiten vorher: " + matchFX.getGameMechanics().getCountryA().getUnits());
		    		System.out.println("B Einheiten vorher: " + matchFX.getGameMechanics().getCountryB().getUnits());
		    		System.out.println("A schickt in den Tod: " + matchFX.getGameMechanics().getBattleUnitsA());
		    		System.out.println("B schickt in den Tod: " + matchFX.getGameMechanics().getBattleUnitsB());
		    		
		    		// Button wird deaktiviert um weitere Eingaben zu vermeiden
		    		matchFX.getBattleReadyBtn().setActive(false);
		    		
		    		// Es werden W�rfel gew�rfelt anhand der eingesetzten Einheiten
		    		Integer[][] rolledDices = matchFX.getGameMechanics().rollTheDice(matchFX.getGameMechanics().getBattleUnitsA(), matchFX.getGameMechanics().getBattleUnitsB());
		    		
		    		// Auf Basis der W�rfe wird der Kampf durchgef�hrt
		    		matchFX.getGameMechanics().updateFightResults(rolledDices, matchFX.getGameMechanics().getCountryA(), matchFX.getGameMechanics().getCountryB());
		    		
		    		// Ist der Kampf vorbei wird der Kampf beendet und die L�nder aktualisiert
		    		matchFX.getGameMechanics().endFight();
		    	}
	    	}
	    });
	    
	    // Country-Array zwischenspeichern um die Variablen-Namen der Schleife abzuk�rzen
	    Country[] countryArray = matchFX.getCountryArray();
	    // Schleife um mit allen 42 L�ndern zu kommunizieren
	    for (int i = 0; i < matchFX.getCountryArray().length; i++) {
			final int COUNT = i;
			
			// Wenn ein Land oder die Einheiten im Land angeklickt werden
			countryArray[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				matchFX.getGameMechanics().manageCountryClick(COUNT);
		    });
			
			// Wenn ein Land oder die Einheiten im Land angeklickt werden
	    	matchFX.getCountryUnitsBGArray()[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				matchFX.getGameMechanics().manageCountryClick(COUNT);
		    });
	    	
	    	// Wenn ein Land oder die Einheiten im Land angeklickt werden
	    	matchFX.getCountryUnitsLabelArray()[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				matchFX.getGameMechanics().manageCountryClick(COUNT);
		    });
			
			// Wenn der Cursor auf einem Land oder Einheit des Landes platziert wird
	    	countryArray[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		// Aktualisiert das Interface
	    		matchFX.updateCountryInfo(countryArray[COUNT]);
	    		if(matchFX.getGameMechanics().getCountryA() == null) {
	    			matchFX.markNeighbourCountrys(countryArray[COUNT]);
	    		}
	    	});
	    	
	    	// Wenn der Cursor auf einem Land oder Einheit des Landes platziert wird
	    	matchFX.getCountryUnitsBGArray()[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		// Aktualisiert das Interface
	    		matchFX.updateCountryInfo(countryArray[COUNT]);
	    		if(matchFX.getGameMechanics().getCountryA() == null) {
	    			matchFX.markNeighbourCountrys(countryArray[COUNT]);
	    		}
	    	});
	    	
	    	// Wenn der Cursor auf einem Land oder Einheit des Landes platziert wird
	    	matchFX.getCountryUnitsLabelArray()[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		// Aktualisiert das Interface
	    		matchFX.updateCountryInfo(countryArray[COUNT]);
	    		if(matchFX.getGameMechanics().getCountryA() == null) {
	    			matchFX.markNeighbourCountrys(countryArray[COUNT]);
	    		}
	    	});
	
		}
    }

	/**
	 * Steuert den �bergang zwischen zwei Panes f�r die Animation
	 * 
	 * @param trigger Sprite
	 * @param from Pane
	 * @param to Pane
	 */
	public void paneTransition(Sprite trigger, Pane from, Pane to) {
		// Wenn der gedr�ckte Button aktiv ist, kann die Animation gestartet werden
		if(trigger.isActive()) {
			
			/*
			 * Die noch aktive Pane wird mit Daten best�ckt,
			 * die sich w�hrend der Animation ver�ndern schrittweise werden
			 */
			paneFrom = from;
			paneFrom.setCache(true);
			paneFrom.setOpacity(1);
			paneFrom.setScaleX(1);
			paneFrom.setScaleY(1);
			
			/*
			 * Die noch deaktivierte Pane wird mit Daten best�ckt,
			 * die sich w�hrend der Animation ver�ndern schrittweise werden
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
	 * Prozedur, die die ganze Anwendung startet sobald die Scene �bergeben wurde
	 * 
	 * @param trigger Label
	 * @param from Pane
	 * @param to Pane
	 * @see MainApp#paneTransition(Sprite trigger, Pane from, Pane to)
	 */
	public void paneTransition(Label trigger, Pane from, Pane to) {
		/*
		 * Die noch aktive Pane wird mit Daten best�ckt,
		 * die sich w�hrend der Animation ver�ndern schrittweise werden
		 */
		paneFrom = from;
		paneFrom.setCache(true);
		paneFrom.setOpacity(1);
		paneFrom.setScaleX(1);
		paneFrom.setScaleY(1);
		
		/*
		 * Die noch deaktivierte Pane wird mit Daten best�ckt,
		 * die sich w�hrend der Animation ver�ndern schrittweise werden
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
	 * Wird aufgerufen wenn die Fenstergr��e ge�ndert wird und
	 * skaliert den Spiel-Container immer passend
	 * 
	 * @param stage Stage
	 * @param ctn_app Pane
	 */
	public void resizeThat(Stage stage, Pane ctn_app) {
		 // speichert das gew�nschte Seitenverh�ltnis
		 double ratio = stage.getWidth() / stage.getHeight();
		 double scale;
		 
		 // Vergleicht beide Verh�ltnisse und �ndert dementsprechend den Skalierungs-Faktor
		 if(ratio > ctn_app.getPrefWidth() / ctn_app.getPrefHeight())
			 scale = stage.getHeight() / ctn_app.getPrefHeight();
		 else
			 scale = stage.getWidth() / ctn_app.getPrefWidth();
		 
		 /*
		  * Sollte es gr��er als 1080p sein wird die Skalierung auf 1 gesetzt
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
	 * Eine Endlosschleife die 60 mal die Sekunde aufgerufen wird um fl�ssige Animationen zu erm�glichen
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
	        	
	        	// Wenn der �bergang zwischen zwei Panes aktiviert wird
	        	if(toPane) {
	        		
	        		// Die vorherige Pane wird schrittweise verkleinert und ausgeblendet
	        		if(paneFrom.getScaleY() < 1.5) {
		        		paneFrom.setScaleX(paneFrom.getScaleX() + .02);
		        		paneFrom.setScaleY(paneFrom.getScaleY() + .02);
	        		}

	        		if(paneFrom.getOpacity() > 0)
	        			paneFrom.setOpacity(paneFrom.getOpacity() - .05);
	        		
	        		// Die n�chste Pane wird schrittweise vergr��ert und eingeblendet
	        		if(paneTo.getScaleY() > 1) {
	        			paneTo.setScaleX(paneTo.getScaleX() - .02);
	        			paneTo.setScaleY(paneTo.getScaleY() - .02);
	        		}
	        		
	        		if(paneTo.getOpacity() < 1)
	        			paneTo.setOpacity(paneTo.getOpacity() + .05);

	        		
	        		/*
	        		 * Sind beide �berg�nge fertig wird die aktuelle Pane endg�ltig deaktiviert,
	        		 * die n�chste aktiviert und die Animation durch den boolean toPane beendet
	        		 */
	        		if(paneFrom.getScaleY() >= 1.5 && paneTo.getScaleY() <= 1) {
	        			paneFrom.setCache(true);
	        			paneFrom.setVisible(false);
	        			paneTo.setCache(false);
	        			
	        			toPane = false;
	        		}
	        	}
	        	
	        	// Aktualisiert die Lautst�rke der Hintergrund-Musik
	        	mpFX.setVolumeGame();
	        	mpFX.setVolumeStart();
	        }
	    }.start();
	}
	
	
	public static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}
	
	
	/**
	 * Erstellt ein Player-Objekt und ein zugeh�riges Chat-Interface
	 * @author pbs2h17asc
	 */
	private void createPlayer() {
		player = new Player(lobbyFX.getLobby(),lobbyFX.getNextSlotId());
		lobbyFX.guiAddPlayer(player.getSlotId());
		lobbyFX.getLobby().addPlayer(player);
		player.setColor("FFD800");
		
		// Erstellt das ChatInterface und positioniert es in der Lobby
		chatFX = new ChatInterface(player);
		ctnApp.getChildren().add(chatFX.getPane());
		chatFX.getPane().relocate(42, 420);
	}

}