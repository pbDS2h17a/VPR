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
import java.util.List;
import java.util.Random;

public class Match {

	// Globale Variablen
	private Pane ctn;
	private Pane groupTerritoryInfo;
	private Label territoryInfoLabel;
	private Label playerNameLabel;
	private Label territoryNameLabel;
	private Polygon playerName;
	private Polygon territoryName;
	private Rectangle territoryInfo;
	
	private Country[] countryArray = new Country[42];
	
	private Group[] territory_group = new Group[countryArray.length];
	
	public Match(Lobby lobby) {
		// Partie-Container (Child von Anwendungs_CTN)
	    ctn = new Pane();
	    ctn.setCache(true);
	    ctn.setId("Partie");
	    ctn.setPrefSize(1920, 1080);
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Partie-Hintergrund
	    ImageView bg = new ImageView("resources/game_bg.png");
	    
	    // Land
	    // Land-Gruppe
	    Group groupLands = new Group();
	    groupLands.setScaleX(.9);
	    groupLands.setScaleY(.9);
	    groupLands.relocate(ctn.getPrefWidth()/2 - 656, ctn.getPrefHeight()/2 - 432);
	    
	    // Land-Infos
	    groupTerritoryInfo = new Pane();
	    groupTerritoryInfo.setPrefSize(80,80);
	    groupTerritoryInfo.relocate(1130, 940);
	    
    	territoryInfo = new Rectangle(80, 80);
    	territoryInfo.setStroke(Color.WHITE);
    	territoryInfo.setStrokeWidth(5);
    	territoryInfo.setStrokeType(StrokeType.INSIDE);
    	territoryInfo.setFill(Color.GREY);
    	territoryInfo.setArcHeight(200);
    	territoryInfo.setArcWidth(200);
   
    	territoryInfoLabel = new Label("");
    	territoryInfoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold;");
    	territoryInfoLabel.relocate(25, 10);
    	
    	groupTerritoryInfo.getChildren().addAll(territoryInfo, territoryInfoLabel);
	    
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
		    	for (int j : SqlHelper.getCountryNeighbor((i+1))) {
		    		System.out.print(j + " ");
		    	}
		    	System.out.println();

		    	
		    	final int tmp = i;
		    	
		    	countryArray[i].addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
		    			updateTerritoryInfo(tmp);
		    		}
			    );
		    	groupLands.getChildren().add(countryArray[i]);
	    	}
	    

	    // Spieler-Name
    	playerName = new Polygon();
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
    	
    	playerNameLabel = new Label("");
    	playerNameLabel.relocate(40, 65);
    	playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 40px;");
	    
    	territoryName = new Polygon();
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
    	
    	territoryNameLabel = new Label("");
    	territoryNameLabel.setPrefWidth(430);
    	territoryNameLabel.relocate(territoryName.getLayoutX(), territoryName.getLayoutY() + 10);
    	territoryNameLabel.setAlignment(Pos.BASELINE_CENTER);
    	territoryNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");

    	// Spieler-Infos Gruppe
    	Group playerInfoGroup = new Group();
    	playerInfoGroup.relocate(10, 80);
    	
    	// Spieler-Infos
    	// Einheiten
    	Sprite playerInfoUnits = new Sprite("resources/game_icon_units.png");
    	playerInfoUnits.relocate(10, playerInfoGroup.getLayoutY());
    	playerInfoGroup.getChildren().add(playerInfoUnits);
    	
    	Label playerInfoUnitsLabel = new Label("999");
    	playerInfoUnitsLabel.relocate(90, playerInfoUnits.getLayoutY() + 18);
    	playerInfoUnitsLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoUnitsLabel);
    	
    	// Laender
    	Sprite playerInfoLand = new Sprite("resources/game_icon_lands.png");
    	playerInfoLand.relocate(10, playerInfoUnits.getLayoutY() + 85);
    	playerInfoGroup.getChildren().add(playerInfoLand);
    	
    	Label playerInfoLandLabel = new Label("999");
    	playerInfoLandLabel.relocate(90, playerInfoLand.getLayoutY() + 18);
    	playerInfoLandLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoLandLabel);
    	
    	// Karten
    	Sprite playerInfoCard1 = new Sprite("resources/game_icon_card1.png");
    	playerInfoCard1.relocate(10, playerInfoLand.getLayoutY() + 120);
    	playerInfoGroup.getChildren().add(playerInfoCard1);
    	
    	Label playerInfoCard1Label = new Label("999");
    	playerInfoCard1Label.relocate(80, playerInfoCard1.getLayoutY() + 18);
    	playerInfoCard1Label.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoCard1Label);
    	
    	Sprite playerInfoCard2 = new Sprite("resources/game_icon_card2.png");
    	playerInfoCard2.relocate(10, playerInfoCard1.getLayoutY() + 100);
    	playerInfoGroup.getChildren().add(playerInfoCard2);
    	
    	Label playerInfoCard2Label = new Label("999");
    	playerInfoCard2Label.relocate(80, playerInfoCard2.getLayoutY() + 18);
    	playerInfoCard2Label.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoCard2Label);
    	
    	Sprite playerInfoCard3 = new Sprite("resources/game_icon_card3.png");
    	playerInfoCard3.relocate(10, playerInfoCard2.getLayoutY() + 100);
    	playerInfoGroup.getChildren().add(playerInfoCard3);
    	
    	Label playerInfoCard3Label = new Label("999");
    	playerInfoCard3Label.relocate(80, playerInfoCard3.getLayoutY() + 18);
    	playerInfoCard3Label.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoGroup.getChildren().add(playerInfoCard3Label);
    	
    	// Auftrag-Gruppe
    	Group playerInfoAuftragGroup = new Group();
    	playerInfoAuftragGroup.relocate(-180, 320);
    	playerInfoGroup.getChildren().add(playerInfoAuftragGroup);
    	
    	playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_MOVED, event ->
			playerInfoAuftragGroup.setLayoutX(160)
	    );
		
    	playerInfoAuftragGroup.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
			playerInfoAuftragGroup.setLayoutX(-180)
		);
    	
    	// Auftrag
    	Sprite playerInfoAuftrag = new Sprite("resources/btn_phase_goal.png");
    	playerInfoAuftrag.relocate(playerInfoAuftragGroup.getLayoutX(), playerInfoAuftragGroup.getLayoutY());
    	playerInfoAuftragGroup.getChildren().add(playerInfoAuftrag);
    	
    	Label playerInfoAuftragLabel = new Label("Vernichte Einheit Rot");
    	playerInfoAuftragLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	playerInfoAuftragLabel.relocate(playerInfoAuftrag.getLayoutX() + 20, playerInfoAuftrag.getLayoutY() + 60);
    	playerInfoAuftragGroup.getChildren().add(playerInfoAuftragLabel);
    	
    	// Phasen-Buttons
    	Group phaseBtnGroup = new Group();
    	phaseBtnGroup.relocate(1150, 50);
    	
    	Sprite phaseBtn1 = new Sprite("resources/btn_phase_add.png");
    	phaseBtn1.setActive(false);
    	phaseBtnGroup.getChildren().add(phaseBtn1);
    	
    	Sprite phaseBtn2 = new Sprite("resources/btn_phase_battle.png");
    	phaseBtn2.setButtonMode(true);
    	phaseBtn2.relocate(phaseBtn1.getLayoutX() + 115, phaseBtn1.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn2);
    	
    	Sprite phaseBtn3 = new Sprite("resources/btn_phase_move.png");
    	phaseBtn3.setButtonMode(true);
    	phaseBtn3.relocate(phaseBtn2.getLayoutX() + 115, phaseBtn2.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn3);
    	
    	Sprite phaseBtn4 = new Sprite("resources/btn_phase_end.png");
    	phaseBtn4.setButtonMode(true);
    	phaseBtn4.relocate(phaseBtn3.getLayoutX() + 180, phaseBtn3.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn4);
    	
		ctn.getChildren().addAll(bg, groupLands, playerName, playerNameLabel, territoryName, territoryNameLabel, playerInfoGroup, phaseBtnGroup, groupTerritoryInfo);
	
		
		startMatch(lobby);
	}
	
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
	}
	
	void updateTerritoryInfo(int id) {
		
		if(territoryInfo.getFill() != countryArray[id].getFill())
			territoryInfo.setFill(countryArray[id].getFill());
		
		gameChangeCountry(id);

	}

	void gameChangePlayer(String s, Paint p) {
		if(!playerNameLabel.getText().equals(s))
			playerNameLabel.setText(s);
		
		if(playerName.getFill() != p)
			playerName.setFill(p);
	}
	
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