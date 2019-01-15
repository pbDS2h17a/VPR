package gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.Country;
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

public class Match {

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
	private Pane ctn = new Pane();
	private Pane groupTerritoryInfo = new Pane();
	private Label territoryInfoLabel = new Label("1");
	private Label playerNameLabel = new Label();
	private Label territoryNameLabel = new Label();
	private Polygon playerName = new Polygon();
	private Polygon territoryName = new Polygon();
	private Rectangle territoryInfo = new Rectangle(80, 80);
	private Group playerInfoGroup = new Group();
	private Sprite playerInfoUnits = new Sprite("resources/game_icon_units.png");
	private Label playerInfoUnitsLabel = new Label("999");
	private Sprite playerInfoLand = new Sprite("resources/game_icon_lands.png");
	private Label playerInfoLandLabel = new Label("999");
	private Sprite playerInfoCard1 = new Sprite("resources/game_icon_card1.png");
	private Label playerInfoCard1Label = new Label("999");
	private Sprite playerInfoCard2 = new Sprite("resources/game_icon_card2.png");
	private Label playerInfoCard2Label = new Label("999");
	private Sprite playerInfoCard3 = new Sprite("resources/game_icon_card3.png");
	private Label playerInfoCard3Label = new Label("999");
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
	
	/**
	 * Constructor.
	 */
	public Match(Lobby lobby) {
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
	    
	    // Land-Infos
	    groupTerritoryInfo.setPrefSize(80,80);
	    groupTerritoryInfo.relocate(1130, 940);
	    
    	territoryInfo.setStroke(Color.WHITE);
    	territoryInfo.setStrokeWidth(5);
    	territoryInfo.setStrokeType(StrokeType.INSIDE);
    	territoryInfo.setFill(Color.GREY);
    	territoryInfo.setArcHeight(200);
    	territoryInfo.setArcWidth(200);
    	groupTerritoryInfo.getChildren().add(territoryInfo);
    	
    	territoryInfoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold;");
    	territoryInfoLabel.relocate(25, 10);
    	groupTerritoryInfo.getChildren().add(territoryInfoLabel);
	    
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
		    	countryArray[i].setStrokeWidth(2);
//		    	territorySVG[i].setNeighborIDArray(SqlHelper.getCountryNeighbor((i+1)));
//		    	for (int j : SqlHelper.getCountryNeighbor((i+1))) {
//		    		System.out.print(j + " ");
//		    	}
//		    	System.out.println();

		    	
		    	final int tmp = i;
		    	countryArray[i].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
		    			updateTerritoryInfo(tmp);
		    			gameChangeCountryStroke(countryArray[tmp], Color.RED);
		    		}
			    );
		    	
		    	groupLands.getChildren().add(countryArray[i]);
	    	}
	    
	    ctn.getChildren().add(groupLands);

	    // Spieler-Name
    	playerName.getPoints().addAll(new Double[]{
            0.0, 0.0,
            380.0, 0.0,
            380.0, 50.0,
            340.0, 80.0,
            0.0, 80.0});
    	playerName.setFill(Color.GREY);
    	playerName.setStroke(Color.WHITE);
    	playerName.setStrokeWidth(5);
    	playerName.setStrokeType(StrokeType.INSIDE);
    	playerName.relocate(-5, 50);
    	ctn.getChildren().add(playerName);
    	
    	playerNameLabel.relocate(40, 65);
    	playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 40px;");
    	ctn.getChildren().add(playerNameLabel);
    	
    	territoryName.getPoints().addAll(new Double[]{
    		0.0,   0.0,
    		0.0,   30.0,
    		30.0,  60.0,
    		430.0, 60.0,
    		430.0, 30.0,
    		400.0, 0.0});
    	territoryName.setFill(Color.GREY);
    	territoryName.setStroke(Color.WHITE);
    	territoryName.setStrokeWidth(5);
    	territoryName.setStrokeType(StrokeType.INSIDE);
    	territoryName.relocate(ctn.getPrefWidth()/2 - 215, ctn.getPrefHeight() - 130);
    	ctn.getChildren().add(territoryName);
    	
    	territoryNameLabel.setPrefWidth(430);
    	territoryNameLabel.relocate(territoryName.getLayoutX(), territoryName.getLayoutY() + 10);
    	territoryNameLabel.setAlignment(Pos.BASELINE_CENTER);
    	territoryNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	ctn.getChildren().add(territoryNameLabel);
    	
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
    	playerInfoAuftragGroup.relocate(-180, 320);
    	playerInfoGroup.getChildren().add(playerInfoAuftragGroup);
    	
    	playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_MOVED, event ->
			playerInfoAuftragGroup.setLayoutX(160)
	    );
		
    	playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
			playerInfoAuftragGroup.setLayoutX(-180)
		);
    	
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
    	phaseBtnGroup.getChildren().add(phaseBtn1);
    	
    	phaseBtn2.setButtonMode(true);
    	phaseBtn2.relocate(phaseBtn1.getLayoutX() + 115, phaseBtn1.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn2);
    	
    	phaseBtn3.setButtonMode(true);
    	phaseBtn3.relocate(phaseBtn2.getLayoutX() + 115, phaseBtn2.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn3);
    	
    	phaseBtn4.setButtonMode(true);
    	phaseBtn4.relocate(phaseBtn3.getLayoutX() + 180, phaseBtn3.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn4);
    	
		ctn.getChildren().add(phaseBtnGroup);
	
		
		startMatch(lobby);
	}
	
	/**
	 * @param min 	  : Integer
	 * @param max 	  : Integer
	 * @return random : Integer
	 * Returns a random integer between min and max. 
	 */
	static int randomInt(int min, int max) {
	    return (int)(Math.random() * (max - min + 1)) + min;
	}
	
	private void startMatch(Lobby lobby) {	
		// Verteilung der Länder auf die Spieler		
		// Länderarray wird in eine Liste konvertiert
		ArrayList<Country> countryList = new ArrayList<Country>(Arrays.asList(countryArray));
		lobby.setLobbyId(1);
		Player[] players = SqlHelper.getAllPlayersForLobby(lobby.getLobbyId()); 
		
		
		int userCount = players.length-1;
		Random rand = new Random();
		
		for (int i = 0; i < countryArray.length; i++) {	
			// zufälliges Land aus Liste
			Country randomCountry = countryList.get(rand.nextInt(countryList.size()));
			// Werte werden zugewiesen
			randomCountry.setOwnerId(players[userCount].getId());	
			randomCountry.setFill(Color.web(players[userCount].getColor()));	
			countryList.remove(randomCountry);

			if(userCount == 0) {
				userCount = players.length-1;
			} else {
				userCount--;
			}
	
		}
		
		gameChangePlayer(players[0].toString(), Color.web(players[1].getColor()));
	}
	
	/**
	 * @param color : Paint
	 * @param x 	: Double
	 * @param y 	: Double
	 * @param s 	: String
	 * Checks if the input paint color equals the current territoryInfo fill.
	 * If so: Sets the territoryInfo fill.
	 * Calls gameChangeCountry and gameChangePlayer.
	 */
	void updateTerritoryInfo(int id) {
		
		if(territoryInfo.getFill() != countryArray[id].getFill())
			territoryInfo.setFill(countryArray[id].getFill());
		
		gameChangeCountry(id);

	}
	
	/**
	 * @param neighbourCountryArray : Integer[]
	 * @param color : Paint
	 * Change the Stroke-Color of all Countrys to WHITE if it's color differs.
	 * Change the Stroke-Color of the current and neighbour-countrys
	 */
	private void gameChangeCountryStroke(Country country, Paint color) {
		for (int i = 0; i < countryArray.length; i++) {
			if(countryArray[i].getStroke() != Color.WHITE) {
				countryArray[i].setStroke(Color.WHITE);
			}
		}
		
		int[] cArr = country.getNeighborIdArray();
		
		for (int i = 0; i < cArr.length; i++) {
			System.out.print(cArr[i] + " ");
			countryArray[cArr[i]-1].setStroke(Color.RED);
			country.setStroke(Color.RED);
		}
		System.out.println();
	}
	
	/**
	 * @param s : String
	 * @param p : Paint
	 * Checks if the input string s equals the current playerName.
	 * If so: Sets the playerName.
	 * Checks if the input paint p equals the current paint of the playerName fill.
	 * If so: Sets the playerName fill.
	 */
	private void gameChangePlayer(String s, Color p) {
		if(!playerNameLabel.getText().equals(s))
			playerNameLabel.setText(s);
		
		if(playerName.getFill() != p)
			playerName.setFill(p);
	}
	
	/**
	 * @param s : String
	 * @param p : Paint
	 * Checks if the input string s equals the current name of the territory.
	 * If so: Sets the territoryName.
	 * Checks if the input paint p equals the current paint of the territoryName fill.
	 * If so: Sets the territoryName fill.
	 */
	private void gameChangeCountry(int id) {
		if(!territoryNameLabel.getText().equals(countryArray[id].getOwner())) {
			try {
				territoryNameLabel.setText(SqlHelper.getPlayerName(countryArray[id].getOwnerId()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
					

		if(territoryName.getFill() != countryArray[id].getFill()) {
			territoryName.setFill(countryArray[id].getFill());
		}
			
	}
	
	public Pane getContainer() {
		return ctn;
	}
	
}