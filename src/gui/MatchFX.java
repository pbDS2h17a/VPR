package gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.Country;
import sqlConnection.Lobby;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Daniels, Kevin
 * @author pbs2h17ale
 *
 */

public class MatchFX {

	/**
	 * @param ctn				 : Pane
	 * @param groupTerritoryInfo : Pane
	 * @param territoryInfoLabel : Label
	 * @param playerNameLabel	 : Label
	 * @param territoryNameLabel : Label
	 * @param playerName		 : Polygon
	 * @param territoryName		 : Polygon
	 * @param territoryInfo		 : Rectangle
	 * @param bg				 : ImageView
	 * @param groupLands		 : Group
	 * @param colorArray		 : String[]
	 * @param territoryArray	 : String[]
	 * @param territorySVG		 : SVGPath[]
	 * @param territory_group	 : Group[]
	 */
	private Round round;
	private LobbyFX lobbyFX;
	private Country currentCountry = null;
	private Player[] playerArray;
	private Pane ctn = new Pane();
	private Pane groupTerritoryInfo = new Pane();
	private Label territoryInfoLabel = new Label();
	private Label playerNameLabel = new Label();
	private Label territoryNameLabel = new Label();
	private Polygon playerName = new Polygon();
	private Polygon territoryName = new Polygon();
	private Rectangle territoryInfo = new Rectangle(80, 80);
	private Group playerInfoGroup = new Group();
	private Sprite playerInfoUnits = new Sprite("resources/game_icon_units.png");
	private Label playerInfoUnitsLabel = new Label();
	private Sprite playerInfoLand = new Sprite("resources/game_icon_lands.png");
	private Label playerInfoLandLabel = new Label();
	private Sprite playerInfoCard1 = new Sprite("resources/game_icon_card1.png");
	private Label playerInfoCard1Label = new Label("0");
	private Sprite playerInfoCard2 = new Sprite("resources/game_icon_card2.png");
	private Label playerInfoCard2Label = new Label("0");
	private Sprite playerInfoCard3 = new Sprite("resources/game_icon_card3.png");
	private Label playerInfoCard3Label = new Label("0");
	private Group playerInfoAuftragGroup = new Group();
	private Sprite playerInfoAuftrag = new Sprite("resources/btn_phase_goal.png");
	private Label playerInfoAuftragLabel = new Label("Vernichte Einheit Rot");
	private Group phaseBtnGroup = new Group();
	private Sprite phaseBtn1 = new Sprite("resources/btn_phase_add.png");
	private Sprite phaseBtn2 = new Sprite("resources/btn_phase_battle.png");
	private Sprite phaseBtn3 = new Sprite("resources/btn_phase_move.png");
	private Sprite phaseBtn4 = new Sprite("resources/btn_phase_end.png");
	private Group groupLands = new Group();
	private Country[] countryArray = new Country[42];
	private Group[] territory_group = new Group[countryArray.length];
	private ColorAdjust colorAdjust = new ColorAdjust();
	private Group battleGroup = new Group();
	private Rectangle battleA_BG = new Rectangle(960, 1080);
	private Label battleA_CountryName = new Label();
	private Rectangle battleB_BG = new Rectangle(960, 1080);
	private Label battleB_CountryName = new Label();
	private Polygon battleArrow = new Polygon();
	private Sprite battleBtnReady = new Sprite("resources/btn_bereit.png");
	private TextField battleA_Input = new TextField();
	private Label battleA_LabelUnits = new Label();
	private TextField battleB_Input = new TextField();
	private Label battleB_LabelUnits = new Label();
	private Group battleA_GroupDices = new Group();
	private Label battleA_Dice1 = new Label();
	private Label battleA_Dice2 = new Label();
	private Label battleA_Dice3 = new Label();
	private Group battleB_GroupDices = new Group();
	private Label battleB_Dice1 = new Label();
	private Label battleB_Dice2 = new Label();
	
	/**
	 * Constructor.
	 */
	public MatchFX(LobbyFX lobby) {
		
		// Partie-Container (Child von Anwendungs_CTN)
	    ctn.setCache(true);
	    ctn.setId("Partie");
	    ctn.setPrefSize(1920, 1080);
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Partie-Hintergrund
	    ImageView bg = new ImageView("resources/game_bg.png");
	    ctn.getChildren().add(bg);
	    
	    // Land
	    // Land-Gruppe
	    groupLands.setScaleX(.9);
	    groupLands.setScaleY(.9);
	    groupLands.relocate(ctn.getPrefWidth()/2 - 656, ctn.getPrefHeight()/2 - 432);

	    // Einzelnes Land
	    for(int i = 0; i < countryArray.length; i++) {	
		    	try {
					countryArray[i] = new Country(i+1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	territory_group[i] = new Group();
		    	// System.out.println(territoryArray[i].length());

		    	countryArray[i].setFill(Color.WHITE);
		    	countryArray[i].setStroke(Color.WHITE);
		    	countryArray[i].setStrokeWidth(0);
		    	countryArray[i].setScaleX(1.01);
		    	countryArray[i].setScaleY(1.01);
//		    	territorySVG[i].setNeighborIDArray(SqlHelper.getCountryNeighbor((i+1)));
//		    	for (int j : SqlHelper.getCountryNeighbor((i+1))) {
//		    		System.out.print(j + " ");
//		    	}
//		    	System.out.println();

		    	

		    	
		    	groupLands.getChildren().add(countryArray[i]);
	    	}
	    
	    ctn.getChildren().add(groupLands);

	    // Spieler-Name
    	playerName.getPoints().addAll(0.0, 0.0,
				380.0, 0.0,
				380.0, 50.0,
				340.0, 80.0,
				0.0, 80.0);
    	playerName.setFill(Color.GREY);
    	playerName.setStroke(Color.WHITE);
    	playerName.setStrokeWidth(5);
    	playerName.setStrokeType(StrokeType.INSIDE);
    	playerName.relocate(-5, 50);
    	ctn.getChildren().add(playerName);
    	
    	playerNameLabel.relocate(40, 65);
    	playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 40px;");
    	ctn.getChildren().add(playerNameLabel);
    	
    	territoryName.getPoints().addAll(0.0, 0.0,
				0.0, 30.0,
				30.0, 60.0,
				530.0, 60.0,
				530.0, 30.0,
				500.0, 0.0);
    	territoryName.setFill(Color.GREY);
    	territoryName.setStroke(Color.WHITE);
    	territoryName.setStrokeWidth(5);
    	territoryName.setStrokeType(StrokeType.INSIDE);
    	territoryName.relocate(ctn.getPrefWidth()/2 - 315, ctn.getPrefHeight() - 110);
    	ctn.getChildren().add(territoryName);
    	
    	territoryNameLabel.setPrefWidth(530);
    	territoryNameLabel.relocate(territoryName.getLayoutX(), territoryName.getLayoutY() + 13);
    	territoryNameLabel.setAlignment(Pos.BASELINE_CENTER);
    	territoryNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 27px;");
    	ctn.getChildren().add(territoryNameLabel);
    	
	    // Land-Infos
	    groupTerritoryInfo.setPrefSize(80,80);
	    groupTerritoryInfo.relocate(1130, 960);
	    
    	territoryInfo.setStroke(Color.WHITE);
    	territoryInfo.setStrokeWidth(5);
    	territoryInfo.setStrokeType(StrokeType.INSIDE);
    	territoryInfo.setFill(Color.GREY);
    	territoryInfo.setArcHeight(200);
    	territoryInfo.setArcWidth(200);
    	groupTerritoryInfo.getChildren().add(territoryInfo);
    	
    	territoryInfoLabel.setPrefSize(80, 80);
    	territoryInfoLabel.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold;");
    	territoryInfoLabel.relocate(0, 0);
    	groupTerritoryInfo.getChildren().add(territoryInfoLabel);
    	
    	ctn.getChildren().add(groupTerritoryInfo);
    	
    	// Spieler-Infos Gruppe
    	playerInfoGroup.relocate(10, 80);
    	
    	// Spieler-Infos
    	// Einheiten
    	playerInfoUnits.relocate(10, playerInfoGroup.getLayoutY());
    	playerInfoGroup.getChildren().add(playerInfoUnits);
    	
    	playerInfoUnitsLabel.relocate(90, playerInfoUnits.getLayoutY() + 18);
    	playerInfoUnitsLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoUnitsLabel);
    	
    	// Laender
    	playerInfoLand.relocate(10, playerInfoUnits.getLayoutY() + 85);
    	playerInfoGroup.getChildren().add(playerInfoLand);
    	
    	playerInfoLandLabel.relocate(90, playerInfoLand.getLayoutY() + 18);
    	playerInfoLandLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoLandLabel);
    	
    	// Karten
    	playerInfoCard1.relocate(10, playerInfoLand.getLayoutY() + 120);
    	playerInfoGroup.getChildren().add(playerInfoCard1);
    	
    	playerInfoCard1Label.relocate(80, playerInfoCard1.getLayoutY() + 18);
    	playerInfoCard1Label.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoCard1Label);
    	
    	playerInfoCard2.relocate(10, playerInfoCard1.getLayoutY() + 100);
    	playerInfoGroup.getChildren().add(playerInfoCard2);
    	
    	playerInfoCard2Label.relocate(80, playerInfoCard2.getLayoutY() + 18);
    	playerInfoCard2Label.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoCard2Label);
    	
    	playerInfoCard3.relocate(10, playerInfoCard2.getLayoutY() + 100);
    	playerInfoGroup.getChildren().add(playerInfoCard3);
    	
    	playerInfoCard3Label.relocate(80, playerInfoCard3.getLayoutY() + 18);
    	playerInfoCard3Label.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoCard3Label);
    	
    	ctn.getChildren().add(playerInfoGroup);
    	
    	// Auftrag-Gruppe
    	playerInfoAuftragGroup.relocate(-180, 360);
    	playerInfoGroup.getChildren().add(playerInfoAuftragGroup);

    	// Auftrag
    	playerInfoAuftrag.relocate(playerInfoAuftragGroup.getLayoutX(), playerInfoAuftragGroup.getLayoutY());
    	playerInfoAuftragGroup.getChildren().add(playerInfoAuftrag);
    	
    	playerInfoAuftragLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoAuftragLabel.relocate(playerInfoAuftrag.getLayoutX() + 20, playerInfoAuftrag.getLayoutY() + 60);
    	playerInfoAuftragGroup.getChildren().add(playerInfoAuftragLabel);
    	
    	ctn.getChildren().add(playerInfoAuftragGroup);
    	
    	// Phasen-Buttons
    	phaseBtnGroup.relocate(1150, 50);
    	
    	phaseBtn1.setActive(false);
    	phaseBtn1.setButtonMode(false);
    	phaseBtnGroup.getChildren().add(phaseBtn1);
    	
    	phaseBtn2.setActive(false);
    	phaseBtn2.setButtonMode(false);
    	phaseBtn2.relocate(phaseBtn1.getLayoutX() + 115, phaseBtn1.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn2);
    	
    	phaseBtn3.setActive(false);
    	phaseBtn3.setButtonMode(false);
    	phaseBtn3.relocate(phaseBtn2.getLayoutX() + 115, phaseBtn2.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn3);
    	
    	phaseBtn4.setActive(false);
    	phaseBtn4.setButtonMode(false);
    	phaseBtn4.relocate(phaseBtn3.getLayoutX() + 180, phaseBtn3.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn4);
	    
    	playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_MOVED, event ->
			playerInfoAuftragGroup.setLayoutX(160)
	    );
		
    	playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
			playerInfoAuftragGroup.setLayoutX(-180)
		);

		ctn.getChildren().add(phaseBtnGroup);

		battleGroup.setVisible(false);
		
		battleA_BG.relocate(0, 0);
		battleA_BG.setFill(Color.BLUE);
		battleA_BG.setStroke(Color.WHITE);
		battleA_BG.setStrokeWidth(10);
		battleGroup.getChildren().add(battleA_BG);
		
		battleA_CountryName.setPrefWidth(960);
		battleA_CountryName.relocate(0, 50);
		battleA_CountryName.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		battleGroup.getChildren().add(battleA_CountryName);
		
		battleB_BG.relocate(960, 0);
		battleB_BG.setFill(Color.RED);
		battleB_BG.setStroke(Color.WHITE);
		battleB_BG.setStrokeWidth(10);
		battleGroup.getChildren().add(battleB_BG);
		
		battleB_CountryName.setPrefWidth(960);
		battleB_CountryName.relocate(960, 50);
		battleB_CountryName.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		battleGroup.getChildren().add(battleB_CountryName);
		
		battleArrow.getPoints().addAll(0.0, 0.0,
				0.0, 30.0,
				-230.0, 30.0,
				-230.0, 90.0,
				0.0, 90.0,
				0.0, 120.0,
				70.0, 60.0);
		battleArrow.setFill(Color.WHITE);
		battleArrow.relocate(810, 480);
		battleGroup.getChildren().add(battleArrow);
		
		battleBtnReady.setButtonMode(true);
		battleBtnReady.relocate(960 - 167, 850);
		battleGroup.getChildren().add(battleBtnReady);
		
		battleA_Input.setPrefSize(120, 100);
		battleA_Input.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		battleA_Input.relocate(600, 430);
		battleGroup.getChildren().add(battleA_Input);
		
		battleA_LabelUnits.setPrefSize(120, 40);
		battleA_LabelUnits.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
		battleA_LabelUnits.relocate(600, 550);
		battleGroup.getChildren().add(battleA_LabelUnits);
		
		battleB_Input.setPrefSize(120, 100);
		battleB_Input.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		battleB_Input.relocate(1200, 430);
		battleB_Input.setDisable(true);
		battleGroup.getChildren().add(battleB_Input);
		
		battleB_LabelUnits.setPrefSize(120, 40);
		battleB_LabelUnits.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
		battleB_LabelUnits.relocate(1200, 550);
		battleGroup.getChildren().add(battleB_LabelUnits);
		
		battleA_GroupDices.relocate(0, 0);
		
		battleA_Dice1.setPrefSize(150, 150);
		battleA_Dice1.relocate(100, 265);
		battleA_Dice1.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleA_GroupDices.getChildren().add(battleA_Dice1);
		
		battleA_Dice2.setPrefSize(150, 150);
		battleA_Dice2.relocate(100, 465);
		battleA_Dice2.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleA_GroupDices.getChildren().add(battleA_Dice2);
		
		battleA_Dice3.setPrefSize(150, 150);
		battleA_Dice3.relocate(100, 665);
		battleA_Dice3.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleA_GroupDices.getChildren().add(battleA_Dice3);
		
		battleGroup.getChildren().add(battleA_GroupDices);

		battleB_GroupDices.relocate(0, 0);
		
		battleB_Dice1.setPrefSize(150, 150);
		battleB_Dice1.relocate(1670, 365);
		battleB_Dice1.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleB_GroupDices.getChildren().add(battleB_Dice1);
		
		battleB_Dice2.setPrefSize(150, 150);
		battleB_Dice2.relocate(1670, 565);
		battleB_Dice2.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleB_GroupDices.getChildren().add(battleB_Dice2);
		
		battleGroup.getChildren().add(battleB_GroupDices);
		
		ctn.getChildren().add(battleGroup);
		
		lobbyFX = lobby;
		startMatch(lobbyFX);
		
		initializeClickEventHandlers();
	}
	
	/**
	 * @param min 	  : Integer
	 * @param max 	  : Integer
	 * @return random : Integer
	 * Returns a random integer between min and max (inclusive). 
	 */
	static int randomInt(int min, int max) {
	    return (int)(Math.random() * (max - min + 1)) + min;
	}
	
	private void startMatch(LobbyFX lobbyFX) {	
		// Verteilung der Länder auf die Spieler		
		// Länderarray wird in eine Liste konvertiert
		Lobby lobby = lobbyFX.getLobby();
		final Player[] playersInLobby;
		int lobbyId = lobby.getLobbyId();
		int userCount;
		
		ArrayList<Country> countryList = new ArrayList<Country>(Arrays.asList(countryArray));
		
		// Erstellen der Testspieler
		Player p1 = new Player("Bob1", lobbyId);
        Player p2 = new Player("Bob2", lobbyId);
        Player p3 = new Player("Bob3", lobbyId);
        Player p4 = new Player("Bob4", lobbyId);
        Player p5 = new Player("Bob5", lobbyId);
        Player p6 = new Player("Bob6", lobbyId);
        
        p1.setColor("FFD800");
        p2.setColor("C42B2B");
        p3.setColor("26BF00");
        p4.setColor("0066ED");
        p5.setColor("000000");
        p6.setColor("EF4CE7");
        
        // hinzufügen von Testspielern
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        lobby.addPlayer(p4);
        lobby.addPlayer(p5);
        lobby.addPlayer(p6);
        
        
        
        //lobbyleader setzen
        lobby.setLobbyLeader(p1.getPlayerId());
		playersInLobby = lobby.getPlayers();
		
		for (Player p : playersInLobby) {
			System.out.println(p.toString());
		}
		
		userCount = lobby.getPlayers().length;
		Random rand = new Random();
		
		for (int i = 0; i < countryArray.length; i++) {	
			// zufälliges Land aus Liste
			Player currentPlayer = playersInLobby[userCount-1];
			Country randomCountry = countryList.get(rand.nextInt(countryList.size()));
			// Werte werden zugewiesen
			randomCountry.setOwnerId(currentPlayer.getPlayerId());
			randomCountry.setOwner(currentPlayer.getName());
			randomCountry.setFill(Color.web(currentPlayer.getColor()));
			SqlHelper.insertCountryOwner(lobbyId, currentPlayer.getPlayerId(),randomCountry.getCountryId());
			// Land aus der Liste entfernen
			countryList.remove(randomCountry);

			if(userCount == 1) {
				userCount = playersInLobby.length;
			} else {
				userCount--;
			}
		}

		gameChangePlayer(playersInLobby[0].getName(), Color.web(playersInLobby[0].getColor()));
		
	    lobbyFX.getBtnReady().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	round = new Round(this, playersInLobby, countryArray);
	    });
	    
	}
	
	/**
	 * Checks if the input paint color equals the current territoryInfo fill.
	 * If so: Sets the territoryInfo fill.
	 * Calls gameChangeCountry and gameChangePlayer.
	 */
	void updateTerritoryInfo(Country country) {
		
		if(territoryInfo.getFill() != country.getFill()) {
			territoryInfo.setFill(country.getFill());
		}
		
		if(!territoryInfoLabel.getText().equals(String.valueOf(country.getUnits()))) {
			territoryInfoLabel.setText(String.valueOf(country.getUnits()));
		}
		
		if(territoryName.getFill() != country.getFill()) {
			territoryName.setFill(country.getFill());
		}
		
		if(!territoryNameLabel.getText().equals(country.getCountryName())) {
			territoryNameLabel.setText(country.getCountryName());
		}

	}

	public void gameMarkNeighbourCountrys(Country country) {
		if(currentCountry != null && currentCountry.getCountryId() == country.getCountryId()) {
			return;
		}
		currentCountry = country;
		// Reset all previous changes
		colorAdjust.setBrightness(0.2);
		colorAdjust.setContrast(0.5);
		colorAdjust.setHue(0.05);
		for (int i = 0; i < countryArray.length; i++) {
			countryArray[i].setStrokeWidth(0);
			countryArray[i].setEffect(null);
		}
		
		//System.out.println(country.getCountryName());

		// Add the new changes
		int[] cArr = country.getNeighborIdArray();

		for (int i = 0; i < cArr.length; i++) {
			countryArray[cArr[i]-1].setStrokeWidth(5);
			countryArray[cArr[i]-1].setEffect(colorAdjust);
			country.setStrokeWidth(5);
			country.setEffect(colorAdjust);
		}
	}
	
	void gameChangePlayerUnits(int i) {
		playerInfoUnitsLabel.setText(Integer.toString(i));
	}
	
	void gameChangePlayerTerritories(int i) {
		playerInfoLandLabel.setText(Integer.toString(i));
	}
	
	void gameChangePlayerCard1(int i) {
		playerInfoCard1Label.setText(Integer.toString(i));
	}
	
	void gameChangePlayerCard2(int i) {
		playerInfoCard2Label.setText(Integer.toString(i));
	}
	
	void gameChangePlayerCard3(int i) {
		playerInfoCard3Label.setText(Integer.toString(i));
	}
	
	/**
	 * @param s : String
	 * @param p : Paint
	 * Checks if the input string s equals the current playerName.
	 * If so: Sets the playerName.
	 * Checks if the input paint p equals the current paint of the playerName fill.
	 * If so: Sets the playerName fill.
	 */
	void gameChangePlayer(String s, Color p) {
		if(!playerNameLabel.getText().equals(s)) {
			playerNameLabel.setText(s);
		}
		
		if(playerName.getFill() != p) {
			playerName.setFill(p);
		}
	}
	
	public Pane getContainer() {
		return ctn;
	}
	
	public Group getBattleInterface() {
		return battleGroup;
	}
	
	public Rectangle getBattleBackgroundA() {
		return battleA_BG;
	}
	
	public Rectangle getBattleBackgroundB() {
		return battleB_BG;
	}
	
	public Group getDicesA() {
		return battleA_GroupDices;
	}
	
	public Group getDicesB() {
		return battleB_GroupDices;
	}
	
	public Label getCountryNameA() {
		return battleA_CountryName;
	}
	
	public Label getCountryNameB() {
		return battleB_CountryName;
	}
	
	public Label getCountryUnitsA() {
		return battleA_LabelUnits;
	}
	
	public Label getCountryUnitsB() {
		return battleB_LabelUnits;
	}
	
	public Sprite getBattleReadyBtn() {
		return battleBtnReady;
	}
	
	public TextField getBattleInputA() {
		return battleA_Input;
	}
	
	public TextField getBattleInputB() {
		return battleB_Input;
	}
	
	public void activateWorldMap(boolean isActive) {
		for (int i = 0; i < countryArray.length; i++) {
			countryArray[i].setVisible(isActive);
		}
	}
	
	public Group getPhaseBtnGroup() {
		return phaseBtnGroup;
	}
	
	public void editPhaseButtons(boolean add, boolean fight, boolean move, boolean end) {
		phaseBtn1.setActive(add);
		phaseBtn1.setButtonMode(add);
		phaseBtn2.setActive(fight);
		phaseBtn2.setButtonMode(fight);
		phaseBtn3.setActive(move);
		phaseBtn3.setButtonMode(move);
		phaseBtn4.setActive(end);
		phaseBtn4.setButtonMode(end);
	}
	
	public Round getRound() {
		return round;
	}

	public Country[] getCountryArray() {
		return countryArray;
	}

	public Sprite getPhaseBtn1() {
		return phaseBtn1;
	}

	public Sprite getPhaseBtn2() {
		return phaseBtn2;
	}

	public Sprite getPhaseBtn3() {
		return phaseBtn3;
	}

	public Sprite getPhaseBtn4() {
		return phaseBtn4;
	}

	public Group getPlayerInfoAuftragGroup() {
		return playerInfoAuftragGroup;
	}
	
	private void initializeClickEventHandlers() {

	    phaseBtn1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	round.phaseAdd();
	    });
    	
	    phaseBtn2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	round.phaseFight();
	    });
	    
	    phaseBtn3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	round.phaseMove();
	    });
	    
	    phaseBtn4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	round.nextTurn();
	    });
	    
	    playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_MOVED, event ->
	    	playerInfoAuftragGroup.setLayoutX(160)
	    );
		
	    playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
	    	playerInfoAuftragGroup.setLayoutX(-180)
		);

		// Wenn im Kampfbildschirm auf den Bestätige-Button gedrückt wird
	    battleBtnReady.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	    	/**
	    	 * Wenn der Verteidigungs-Input deaktiviert ist befinden wir
	    	 * uns noch in der ersten Eingabe
	    	 */
	    	if(battleB_Input.isDisabled()) {
	    		// Die Einheiten zum Angreifen werden gesetzt
	    		round.setBattleUnitsA(Integer.parseInt(battleA_Input.getText()));
	    		
	    		// Wenn der Wert der Angreifer im erlaubten Bereich sind...
		    	if(round.getBattleUnitsA() > 0 && round.getBattleUnitsA() < round.getCountryA().getUnits()) {
		    		
		    		// ...werden die Eingabefelder getauscht, damit die Else-Bedingungen erfüllt wird
		    		battleA_Input.setDisable(true);
		    		battleB_Input.setDisable(false);
		    	}
	    	}
	    	
	    	/*
	    	 * Wenn der Angriffs-Input deaktiviert ist befinden wir
	    	 * uns nun in der zweiten Eingabe
	    	 */
	    	else if(battleA_Input.isDisabled()) {
	    		// Die Einheiten zum Verteidigen werden gesetzt
	    		round.setBattleUnitsB(Integer.parseInt(battleB_Input.getText()));
	    		
	    		// Wenn der Wert der Verteidiger im erlaubten Bereich sind...
		    	if(round.getBattleUnitsB() > 0 && round.getBattleUnitsB() < 3 && round.getBattleUnitsB() <= round.getCountryB().getUnits()) {
		    		
		    		// Ausgabe für die Konsole zur Kontrolle
		    		System.out.println("*** Kampf beginnt ***");
		    		System.out.println("A: " + round.getCountryA().getCountryName() + " | B: " + round.getCountryB().getCountryName());
		    		System.out.println("A Einheiten vorher: " + round.getCountryA().getUnits());
		    		System.out.println("B Einheiten vorher: " + round.getCountryB().getUnits());
		    		System.out.println("A schickt in den Tod: " + round.getBattleUnitsA());
		    		System.out.println("B schickt in den Tod: " + round.getBattleUnitsB());
		    		
		    		// Button wird deaktiviert um weitere Eingaben zu vermeiden
		    		battleBtnReady.setActive(false);
		    		
		    		// Es werden Würfel gewürfelt anhand der eingesetzten Einheiten
		    		Integer[][] rolledDices = round.rollTheDice(round.getBattleUnitsA(), round.getBattleUnitsB());
		    		
		    		// Auf Basis der Würfe wird der Kampf durchgeführt
		    		round.updateFightResults(rolledDices, round.getCountryA(), round.getCountryB());
		    		
		    		// Ist der Kampf vorbei wird der Kampf beendet und die Länder aktualisiert
		    		round.endFight();
		    	}
	    	}
	    });
	    

	    
		for (int i = 0; i < countryArray.length; i++) {
			final int COUNT = i;

			countryArray[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

				if(round.isAssign()) {
					if(round.isOwnLand(countryArray[COUNT])) {
						round.getActivePlayer().setUnassignedUnits(round.getActivePlayer().getUnassignedUnits() - 1);
						countryArray[COUNT].setUnits(countryArray[COUNT].getUnits() + 1);
						
						if(round.getActivePlayerIndex() == round.getPlayerArray().length-1) {
							round.setActivePlayerIndex(0);
						} else {
							round.setActivePlayerIndex(round.getActivePlayerIndex() + 1);
						}
					}
				}
				
				else if(round.isAdd()) {
					if(round.isOwnLand(countryArray[COUNT])) {
						if(round.getActivePlayer().getUnassignedUnits() > 0) {
							round.getActivePlayer().setUnassignedUnits(round.getActivePlayer().getUnassignedUnits() - 1);
							countryArray[COUNT].setUnits(countryArray[COUNT].getUnits() + 1);
							updateTerritoryInfo(countryArray[COUNT]);
						}
					}
				}
				
				else if(round.isFight()) {
					
					if(round.getCountryA() == null) {
						if(round.isOwnLand(countryArray[COUNT]) && countryArray[COUNT].getUnits() > 1) {
							round.setCountryA(countryArray[COUNT]);
							round.getCountryA().setStrokeWidth(10);
						}
					}
					
					else {
						if(!round.isOwnLand(countryArray[COUNT]) && round.isNeighbour(round.getCountryA(), countryArray[COUNT])) {
							round.setCountryB(countryArray[COUNT]);
							round.startFight();
						}
					}
				}
				
				else if(round.isMove()) {
					if(round.isOwnLand(countryArray[COUNT])) {
						if(round.getCountryA() == null) {
							round.setCountryA(countryArray[COUNT]);
							round.getCountryA().setStrokeWidth(10);
						}
						
						else {
							round.setCountryB(countryArray[COUNT]);
							
							if(round.isNeighbour(round.getCountryA(), round.getCountryB()) && round.getCountryA().getUnits() > 1) {
								round.getCountryA().setUnits(round.getCountryA().getUnits() - 1);
								round.getCountryB().setUnits(round.getCountryB().getUnits() + 1);
							}
							
							round.getCountryA().setStrokeWidth(0);
							round.setCountryA(null);
							round.setCountryB(null);
						}	
					}
				}
				
				/*
				 * Falls wir etwas mit Rechtsklick brauchen ->
				 * if(((MouseEvent) event).getButton().equals(MouseButton.SECONDARY)) WENN RECHTSKLICK
				 */
				
				if(round.isAssign() && round.isFinishedAssigning()) {
					round.getActivePlayer().setUnassignedUnits(round.getActivePlayer().getUnassignedUnits() + round.getActivePlayer().getUnitsPerRound());
					round.setAssign(false);
					round.setAdd(true);
					round.setActivePlayerIndex(0);
					round.phaseAdd();
				}
				
				round.updatePlayerInterface(round.getActivePlayer());
				updateTerritoryInfo(countryArray[COUNT]);
		    });

	    	countryArray[COUNT].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
	    		updateTerritoryInfo(countryArray[COUNT]);
	    			gameMarkNeighbourCountrys(countryArray[COUNT]);
	    		}
		    );
		}
	}
}