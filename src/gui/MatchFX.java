package gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.Country;
import sqlConnection.Lobby;
import sqlConnection.Player;
import sqlConnection.SqlHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Beinhaltet die gesamte Oberfläche die beim Klick auf "Spiel beitreten" aufgerufen wird.
 * Man ist hier in der Lage sich eine Lobby auszusuchen um die Lobby-Oberfläche aufzurufen.
 * 
 * @author Kevin Daniels
 */

public class MatchFX {

	// Globale Variablen
	private GameMechanics gameMechanics;
    private ArrayList<Player> playersInLobby;
	private Pane ctn = new Pane();
	private Country[] getCountryArray = new Country[42];
	private Group inventoryGroup = new Group();
	private Group inventoryMissionGroup = new Group();
	private Group phaseBtnGroup = new Group();
	private Group groupLands = new Group();
	private Group fightGroup = new Group();
	private Group battleA_GroupDices = new Group();
	private Group battleB_GroupDices = new Group();
	private Label[] countryUnitsLabelArray = new Label[getCountryArray.length];
	private Label playerNameLabel = new Label();
	private Label countryNameLabel = new Label();
	private Label inventoryUnitsLabel = new Label("0");
	private Label inventoryCardOneLabel = new Label("0");
	private Label inventoryCountryLabel = new Label("0");
	private Label inventoryCardTwoLabel = new Label("0");
	private Label inventoryCardThreeLabel = new Label("0");
	private Label inventoryMissionLabelOne = new Label();
	private Label inventoryMissionLabelTwo = new Label();
	private Label fightCountryOneLabel = new Label();
	private Label fightCountryTwoLabel = new Label();
	private Label fightCountryOneDiceOne = new Label();
	private Label fightCountryOneDiceTwo = new Label();
	private Label fightCountryOneDiceThree = new Label();
	private Label fightCountryTwoDiceOne = new Label();
	private Label fightCountryTwoDiceTwo = new Label();
	private Label fightCountryOneUnits = new Label();
	private Label fightCountryTwoUnits = new Label();
	private Line[] countryNeighbourLineArray = new Line[10];
	private TextField fightCountryOneInput = new TextField();
	private TextField fightCountryTwoInput = new TextField();
	private Sprite inventoryUnitsBG = new Sprite("resources/game_icon_units.png");
	private Sprite inventoryCountryBG = new Sprite("resources/game_icon_lands.png");
	private Sprite inventoryCardOne = new Sprite("resources/game_icon_card1.png");
	private Sprite inventoryCardTwo = new Sprite("resources/game_icon_card2.png");
	private Sprite inventoryCardThree = new Sprite("resources/game_icon_card3.png");
	private Sprite inventoryMissionBG = new Sprite("resources/btn_phase_goal.png");
	private Sprite phaseBtn1 = new Sprite("resources/btn_phase_add.png");
	private Sprite phaseBtn2 = new Sprite("resources/btn_phase_battle.png");
	private Sprite phaseBtn3 = new Sprite("resources/btn_phase_move.png");
	private Sprite phaseBtn4 = new Sprite("resources/btn_phase_end.png");
	private Sprite fightBtnReady = new Sprite("resources/btn_bereit.png");
	private Polygon playerNameBG = new Polygon();
	private Polygon countryNameBG = new Polygon();
	private Polygon fightArrow = new Polygon();
	private Rectangle[] countryUnitsBGArray = new Rectangle[getCountryArray.length];
	private Rectangle fightCountryOneBG = new Rectangle(960, 1080);
	private Rectangle fightCountryTwoBG = new Rectangle(960, 1080);
	private double[][] worldMapCoordinates;

	/**
	 * Konstruktor, der alle Oberflächen-Objekte erstellt und sie in einen gemeinsamen Container eingefügt wird.
	 */
	public MatchFX() {
		// Beitreten-Container der in der MainApp ausgegeben wird und alle Objekte fürs beitreten beinhaltet.
	    ctn.setCache(true);
	    ctn.setId("Partie");
	    ctn.setPrefSize(1920, 1080);
	    // Setzt einen Bereich, in dem der Inhalt angezeigt wird. Alles was außerhalb der Form ist wird ausgeblendet.
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Hintergrund für die Spiel-Partie
	    ImageView bg = new ImageView("resources/game_bg.png");
	    ctn.getChildren().add(bg);
	    
	    // Gruppe die alle Länder enthält
	    groupLands.setScaleX(.9);
	    groupLands.setScaleY(.9);
	    groupLands.relocate(ctn.getPrefWidth()/2 - 656, ctn.getPrefHeight()/2 - 432);

	    // Schleife um einzelne Länder zu erzeugen
	    for(int i = 0; i < getCountryArray.length; i++) {
				// Fängt mit eins an, da die ID's der Länder in der Datenbank mit eins beginnen
				getCountryArray[i] = new Country(i+1);
		    	getCountryArray[i].setFill(Color.WHITE);
		    	getCountryArray[i].setStroke(Color.WHITE);
		    	getCountryArray[i].setStrokeWidth(0);
		    	getCountryArray[i].setScaleX(1.02);
		    	getCountryArray[i].setScaleY(1.02);
		    	groupLands.getChildren().add(getCountryArray[i]);
	    	}
	    
	    ctn.getChildren().add(groupLands);

	    // Code um sich die Mauskoordinaten zu besorgen (NUR FÜR TEST- UND RECHERCHEZWECKE)
//	    for (int i = 0; i < countryArray.length; i++) {
//			System.out.println(countryArray[i].getCountryName());
//		}
//	    
//	    ctn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//	    	int x = (int) event.getX() - 20;
//	    	int y = (int) event.getY() - 20;
//	    	System.out.println("{"+x+", "+y+"},");
//	    });
	    
	    // Informationen des Spielers der oben Links angezeigt wird (Hintergrund)
    	playerNameBG.getPoints().addAll(0.0, 0.0,
				380.0, 0.0,
				380.0, 50.0,
				340.0, 80.0,
				0.0, 80.0);
    	playerNameBG.setFill(Color.GREY);
    	playerNameBG.setStroke(Color.WHITE);
    	playerNameBG.setStrokeWidth(5);
    	playerNameBG.setStrokeType(StrokeType.INSIDE);
    	playerNameBG.relocate(-5, 50);
    	ctn.getChildren().add(playerNameBG);
    	
    	// Informationen des Spielers der oben Links angezeigt wird (Label)
    	playerNameLabel.relocate(40, 65);
    	playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 40px;");
    	ctn.getChildren().add(playerNameLabel);
    	
    	// Informationen des Landes das unten angezeigt wird (Hintergrund)
    	countryNameBG.getPoints().addAll(0.0, 0.0,
				0.0, 30.0,
				30.0, 60.0,
				530.0, 60.0,
				530.0, 30.0,
				500.0, 0.0);
    	countryNameBG.setFill(Color.GREY);
    	countryNameBG.setStroke(Color.WHITE);
    	countryNameBG.setStrokeWidth(5);
    	countryNameBG.setStrokeType(StrokeType.INSIDE);
    	countryNameBG.relocate(ctn.getPrefWidth()/2 - 265, ctn.getPrefHeight() - 110);
    	ctn.getChildren().add(countryNameBG);
    	
    	// Informationen des Landes das unten angezeigt wird (Label)
    	countryNameLabel.setPrefWidth(530);
    	countryNameLabel.relocate(countryNameBG.getLayoutX(), countryNameBG.getLayoutY() + 13);
    	countryNameLabel.setAlignment(Pos.BASELINE_CENTER);
    	countryNameLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 27px;");
    	ctn.getChildren().add(countryNameLabel);
    	
    	// Verbindungslinien für die Länder
    	for (int i = 0; i < countryNeighbourLineArray.length; i++) {
    		countryNeighbourLineArray[i] = new Line();
    		countryNeighbourLineArray[i].setStrokeWidth(5);
    		countryNeighbourLineArray[i].setStroke(Color.WHITE);
    		countryNeighbourLineArray[i].setVisible(false);
    		ctn.getChildren().add(countryNeighbourLineArray[i]);
		}
    	
    	// Koordinaten der Länder-Mittelpunkte
    	worldMapCoordinates = new double[][] {
			{392, 234},
			{503, 298},
			{519, 503},
			{608, 434},
			{746, 196},
			{529, 221},
			{589, 321},
			{668, 321},
			{512, 393},
			{620, 810},
			{719, 657},
			{587, 681},
			{626, 547},
			{820, 374},
			{848, 277},
			{927, 372},
			{928, 257},
			{928, 460},
			{1041, 314},
			{836, 462},
			{976, 714},
			{1071, 695},
			{973, 567},
			{1093, 827},
			{876, 617},
			{981, 821},
			{1134, 410},
			{1274, 464},
			{1203, 531},
			{1294, 315},
			{1441, 403},
			{1438, 218},
			{1075, 538},
			{1311, 388},
			{1310, 575},
			{1223, 228},
			{1159, 293},
			{1315, 200},
			{1492, 843},
			{1320, 716},
			{1434, 674},
			{1364, 828}
    	};
    	
    	// Erstellt die Einheiten-Anzeige für jedes Land, basierend auf den Koordinaten
    	for (int i = 0; i < worldMapCoordinates.length; i++) {
			countryUnitsBGArray[i] = new Rectangle(40, 40);
			countryUnitsBGArray[i].setStroke(Color.WHITE);
			countryUnitsBGArray[i].setStrokeWidth(3);
			countryUnitsBGArray[i].setFill(Color.GREY);
			countryUnitsBGArray[i].setArcHeight(200);
			countryUnitsBGArray[i].setArcWidth(200);
			countryUnitsBGArray[i].relocate(worldMapCoordinates[i][0], worldMapCoordinates[i][1]);
			ctn.getChildren().add(countryUnitsBGArray[i]);
			
			countryUnitsLabelArray[i] = new Label("99");
			countryUnitsLabelArray[i].setPrefSize(44, 44);
			countryUnitsLabelArray[i].relocate(worldMapCoordinates[i][0], worldMapCoordinates[i][1]);
			countryUnitsLabelArray[i].setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
			ctn.getChildren().add(countryUnitsLabelArray[i]);
		}

    	// Das Inventar des aktuellen Spielers (Gruppe)
    	inventoryGroup.relocate(10, 80);
    	// Das Inventar des aktuellen Spielers (Einheiten, Hintergrund)
    	inventoryUnitsBG.relocate(10, inventoryGroup.getLayoutY());
    	inventoryGroup.getChildren().add(inventoryUnitsBG);
    	// Das Inventar des aktuellen Spielers (Einheiten, Label)
    	inventoryUnitsLabel.relocate(90, inventoryUnitsBG.getLayoutY() + 18);
    	inventoryUnitsLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	inventoryGroup.getChildren().add(inventoryUnitsLabel);
    	// Das Inventar des aktuellen Spielers (Länder, Hintergrund)
    	inventoryCountryBG.relocate(10, inventoryUnitsBG.getLayoutY() + 85);
    	inventoryGroup.getChildren().add(inventoryCountryBG);
    	// Das Inventar des aktuellen Spielers (Länder, Label)
    	inventoryCountryLabel.relocate(90, inventoryCountryBG.getLayoutY() + 18);
    	inventoryCountryLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	inventoryGroup.getChildren().add(inventoryCountryLabel);
    	// Das Inventar des aktuellen Spielers (Karten 1, Hintergrund)
    	inventoryCardOne.relocate(10, inventoryCountryBG.getLayoutY() + 120);
    	inventoryGroup.getChildren().add(inventoryCardOne);
    	// Das Inventar des aktuellen Spielers (Karten 1, Label)
    	inventoryCardOneLabel.relocate(80, inventoryCardOne.getLayoutY() + 18);
    	inventoryCardOneLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	inventoryGroup.getChildren().add(inventoryCardOneLabel);
    	// Das Inventar des aktuellen Spielers (Karten 2, Hintergrund)
    	inventoryCardTwo.relocate(10, inventoryCardOne.getLayoutY() + 100);
    	inventoryGroup.getChildren().add(inventoryCardTwo);
    	// Das Inventar des aktuellen Spielers (Karten 2, Label)
    	inventoryCardTwoLabel.relocate(80, inventoryCardTwo.getLayoutY() + 18);
    	inventoryCardTwoLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	inventoryGroup.getChildren().add(inventoryCardTwoLabel);
    	// Das Inventar des aktuellen Spielers (Karten 3, Hintergrund)
    	inventoryCardThree.relocate(10, inventoryCardTwo.getLayoutY() + 100);
    	inventoryGroup.getChildren().add(inventoryCardThree);
    	// Das Inventar des aktuellen Spielers (Karten 3, Label)
    	inventoryCardThreeLabel.relocate(80, inventoryCardThree.getLayoutY() + 18);
    	inventoryCardThreeLabel.setStyle("-fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
    	inventoryGroup.getChildren().add(inventoryCardThreeLabel);
    	// Das Inventar des aktuellen Spielers (Auftrag, Gruppe)
    	inventoryMissionGroup.relocate(-200, 340);
    	// Das Inventar des aktuellen Spielers (Auftrag, Hintergrund)
    	inventoryMissionBG.relocate(inventoryMissionGroup.getLayoutX(), inventoryMissionGroup.getLayoutY());
    	inventoryMissionBG.setScaleX(1.2);
    	inventoryMissionBG.setScaleY(1.2);
    	inventoryMissionGroup.getChildren().add(inventoryMissionBG);
    	// Das Inventar des aktuellen Spielers (Auftrag, Label 1)
    	inventoryMissionLabelOne.setStyle("-fx-text-align: center; -fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-width: 0px 0px 2px 0px; -fx-border-color: white;");
    	inventoryMissionLabelOne.setPrefSize(390, 100);
    	inventoryMissionLabelOne.setWrapText(true);
    	inventoryMissionLabelOne.relocate(inventoryMissionBG.getLayoutX() + 0, inventoryMissionBG.getLayoutY() - 15);
    	inventoryMissionGroup.getChildren().add(inventoryMissionLabelOne);
    	// Das Inventar des aktuellen Spielers (Auftrag, Label 2)
    	inventoryMissionLabelTwo.setStyle("-fx-text-align: center; -fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 14px; -fx-border-width: 2px 0px 0px 0px; -fx-border-color: white;");
    	inventoryMissionLabelTwo.setPrefSize(390, 100);
    	inventoryMissionLabelTwo.setWrapText(true);
    	inventoryMissionLabelTwo.relocate(inventoryMissionLabelOne.getLayoutX(), inventoryMissionLabelOne.getLayoutY() + 100);
    	inventoryMissionGroup.getChildren().add(inventoryMissionLabelTwo);
    	inventoryGroup.getChildren().add(inventoryMissionGroup);
    	ctn.getChildren().add(inventoryGroup);

    	
    	// Buttons um alle Phasen zu initiieren (Gruppe)
    	phaseBtnGroup.relocate(1150, 50);
    	// Buttons um alle Phasen zu initiieren (Phase 1)
    	phaseBtn1.setActive(false);
    	phaseBtn1.setButtonMode(false);
    	phaseBtnGroup.getChildren().add(phaseBtn1);
    	// Buttons um alle Phasen zu initiieren (Phase 2)
    	phaseBtn2.setActive(false);
    	phaseBtn2.setButtonMode(false);
    	phaseBtn2.relocate(phaseBtn1.getLayoutX() + 115, phaseBtn1.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn2);
    	// Buttons um alle Phasen zu initiieren (Phase 3)
    	phaseBtn3.setActive(false);
    	phaseBtn3.setButtonMode(false);
    	phaseBtn3.relocate(phaseBtn2.getLayoutX() + 115, phaseBtn2.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn3);
    	// Buttons um alle Phasen zu initiieren (Phase 4)
    	phaseBtn4.setActive(false);
    	phaseBtn4.setButtonMode(false);
    	phaseBtn4.relocate(phaseBtn3.getLayoutX() + 180, phaseBtn3.getLayoutY());
    	phaseBtnGroup.getChildren().add(phaseBtn4);
		ctn.getChildren().add(phaseBtnGroup);

		// Die Oberfläche auf der der Kampf stattfindet (Gruppe)
		fightGroup.setVisible(false);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, Hintergrund)
		fightCountryOneBG.relocate(0, 0);
		fightCountryOneBG.setStroke(Color.WHITE);
		fightCountryOneBG.setStrokeWidth(10);
		fightGroup.getChildren().add(fightCountryOneBG);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, Name)
		fightCountryOneLabel.setPrefWidth(960);
		fightCountryOneLabel.relocate(0, 50);
		fightCountryOneLabel.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightGroup.getChildren().add(fightCountryOneLabel);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, Eingabefeld)
		fightCountryOneInput.setPrefSize(120, 100);
		fightCountryOneInput.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightCountryOneInput.relocate(600, 430);
		fightGroup.getChildren().add(fightCountryOneInput);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, max. Einheiten)
		fightCountryOneUnits.setPrefSize(120, 40);
		fightCountryOneUnits.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
		fightCountryOneUnits.relocate(600, 550);
		fightGroup.getChildren().add(fightCountryOneUnits);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, Hintergrund)
		fightCountryTwoBG.relocate(960, 0);
		fightCountryTwoBG.setStroke(Color.WHITE);
		fightCountryTwoBG.setStrokeWidth(10);
		fightGroup.getChildren().add(fightCountryTwoBG);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, Name)
		fightCountryTwoLabel.setPrefWidth(960);
		fightCountryTwoLabel.relocate(960, 50);
		fightCountryTwoLabel.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightGroup.getChildren().add(fightCountryTwoLabel);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, Eingabefeld)
		fightCountryTwoInput.setPrefSize(120, 100);
		fightCountryTwoInput.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightCountryTwoInput.relocate(1200, 430);
		fightCountryTwoInput.setDisable(true);
		fightGroup.getChildren().add(fightCountryTwoInput);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, max. Einheiten)
		fightCountryTwoUnits.setPrefSize(120, 40);
		fightCountryTwoUnits.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
		fightCountryTwoUnits.relocate(1200, 550);
		fightGroup.getChildren().add(fightCountryTwoUnits);
		
		// Pfeil der die Kampfrichtung angibt
		fightArrow.getPoints().addAll(0.0, 0.0,
				0.0, 30.0,
				-230.0, 30.0,
				-230.0, 90.0,
				0.0, 90.0,
				0.0, 120.0,
				70.0, 60.0);
		fightArrow.setFill(Color.WHITE);
		fightArrow.relocate(810, 480);
		fightGroup.getChildren().add(fightArrow);
		
		// Button der die gesetzten Einheiten bestätigen soll
		fightBtnReady.setButtonMode(true);
		fightBtnReady.relocate(960 - 167, 850);
		fightGroup.getChildren().add(fightBtnReady);
		
		// Die Würfel von Land eins (Gruppe)
		battleA_GroupDices.relocate(0, 0);
		// Die Würfel von Land eins (Würfel eins)
		fightCountryOneDiceOne.setPrefSize(150, 150);
		fightCountryOneDiceOne.relocate(100, 265);
		fightCountryOneDiceOne.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleA_GroupDices.getChildren().add(fightCountryOneDiceOne);
		// Die Würfel von Land eins (Würfel zwei)
		fightCountryOneDiceTwo.setPrefSize(150, 150);
		fightCountryOneDiceTwo.relocate(100, 465);
		fightCountryOneDiceTwo.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleA_GroupDices.getChildren().add(fightCountryOneDiceTwo);
		// Die Würfel von Land eins (Würfel drei)
		fightCountryOneDiceThree.setPrefSize(150, 150);
		fightCountryOneDiceThree.relocate(100, 665);
		fightCountryOneDiceThree.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleA_GroupDices.getChildren().add(fightCountryOneDiceThree);
		fightGroup.getChildren().add(battleA_GroupDices);
		// Die Würfel von Land zwei (Gruppe)
		battleB_GroupDices.relocate(0, 0);
		// Die Würfel von Land zwei (Gruppe, Würfel eins)
		fightCountryTwoDiceOne.setPrefSize(150, 150);
		fightCountryTwoDiceOne.relocate(1670, 365);
		fightCountryTwoDiceOne.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleB_GroupDices.getChildren().add(fightCountryTwoDiceOne);
		// Die Würfel von Land zwei (Gruppe, Würfel zwei)
		fightCountryTwoDiceTwo.setPrefSize(150, 150);
		fightCountryTwoDiceTwo.relocate(1670, 565);
		fightCountryTwoDiceTwo.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		battleB_GroupDices.getChildren().add(fightCountryTwoDiceTwo);
		fightGroup.getChildren().add(battleB_GroupDices);
		ctn.getChildren().add(fightGroup);
		
		// Die Partie wird begonnen
		//initializeMatch();
	}

    /**
     * Prozedur, die die Partie beginnt und die Lobby ausliest um die aktuellen Teilnehmer zu integrieren.
     *
     */
	public void initializeMatch(LobbyFX lobbyFX) {
		Lobby lobby = lobbyFX.getLobby();
		int lobbyId = lobby.getLobbyId();

		// Verteilung der Länder auf die Spieler
		// Länder-Array wird in eine Liste konvertiert
		ArrayList<Country> countryList = new ArrayList<>(Arrays.asList(getCountryArray));

        // Lobbyleader setzen auf ersten Spieler in der Lobby
        lobby.setLobbyLeader(lobby.getPlayers().get(0).getPlayerId());
		playersInLobby = lobby.getPlayers();

		int userCount = lobby.getPlayers().size();
		Random rand = new Random();
		// Verteilung der Länder
		for (int i = 0; i < getCountryArray.length; i++) {	
			// Zufälliges Land aus der Länder-Liste wird ausgewählt
			Player currentPlayer = playersInLobby.get(userCount-1);
			Country randomCountry = countryList.get(rand.nextInt(countryList.size()));
			// Werte werden zugewiesen
			randomCountry.setOwner(currentPlayer);
			randomCountry.setFill(Color.web(currentPlayer.getColor()));
			SqlHelper.insertCountryOwner(lobbyId, currentPlayer.getPlayerId(),randomCountry.getCountryId());
			countryList.remove(randomCountry);
			// Wenn die Spieler-Liste am Ende angekommen ist...
			if(userCount == 1) {
				// ...wird die Liste wieder von vorne begonnen
				userCount = playersInLobby.size();
			} else {
				// sonst wird die Liste einen Schritt weiter gegangen
				userCount--;
			}
		}

		// Passt die Einheiten-Anzeige der Länder an
		for (int i = 0; i < getCountryArray.length; i++) {
			countryUnitsBGArray[i].setFill(getCountryArray[i].getFill());
			countryUnitsLabelArray[i].setText(String.valueOf(getCountryArray[i].getUnits()));
		}
		
		// Aktualisiert den aktiven Spieler oben links in der Oberfläche
		updateActivePlayer(playersInLobby.get(0).getName(), Color.web(playersInLobby.get(0).getColor()));
	}
	
	/**
	 * Aktualisiert die Land-Informationen mittig unten auf der Weltkarte
	 * 
	 * @param country Country
	 */
	public void updateCountryInfo(Country country) {
		// Wenn die Farbe des Landes nicht bereits die gewünschte hat...
		if(countryNameBG.getFill() != country.getFill()) {
			// ...wird sie geändert
			countryNameBG.setFill(country.getFill());
		}
		
		// Wenn der Name des Landes nicht bereits den gewünschten hat...
		if(!countryNameLabel.getText().equals(country.getCountryName())) {
			// ...wird er geändert
			countryNameLabel.setText(country.getCountryName());
		}
	}

	/**
	 * Markiert das gewählte Land und deren Nachbarn
	 * 
	 * @param country Country
	 */
	public void markNeighbourCountrys(Country country) {
		// Erstellt einen Farbfilter, der die Länder hervorheben soll
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.75);
		
		// Schleife die alle vorherigen Markierungen zurücksetzt
		for (int i = 0; i < getCountryArray.length; i++) {
			getCountryArray[i].setStrokeWidth(0);
			getCountryArray[i].setEffect(null);
		}

		// Markiert das ausgewählte Land und die benachbarten Länder
		for (int i = 0; i < country.getNeighborIdArray().length; i++) {
			getCountryArray[country.getNeighborIdArray()[i]-1].setStrokeWidth(5);
			getCountryArray[country.getNeighborIdArray()[i]-1].setEffect(colorAdjust);
			country.setEffect(colorAdjust);
			country.setStrokeWidth(5);
		}
	}

	/**
	 * Methode für die Einheiten-Informationen der Länder (Label)
	 * 
	 * @return gibt das Label mit den Einheiten-Information zurück (Label)
	 */
	public Label[] getCountryUnitsLabelArray() {
		return countryUnitsLabelArray;
	}
	
	/**
	 * Methode für die Einheiten-Informationen der Länder (Hintergründe)
	 * 
	 * @return gibt das Label mit den Einheiten-Information zurück (Hintergründe)
	 */
	public Rectangle[] getCountryUnitsBGArray() {
		return countryUnitsBGArray;
	}

	/**
	 * Setzt im Spieler-Interface die noch ungesetzten Einheiten
	 * 
	 * @param i int
	 */
	public void setInventoryUnitsLabel(int i) {
		inventoryUnitsLabel.setText(Integer.toString(i));
	}
	
	/**
	 * Setzt im Spieler-Interface die bereits eroberten Länder
	 * 
	 * @param i int
	 */
	void setInventoryCountryLabel(int i) {
		inventoryCountryLabel.setText(Integer.toString(i));
	}
	
	/**
	 * Aktualisiert den aktiven Spielen im Interface links oben
	 * 
	 * @param s String
	 * @param p Color
	 */
	void updateActivePlayer(String s, Color p) {
		if(!playerNameLabel.getText().equals(s)) {
			playerNameLabel.setText(s);
		}
		
		if(playerNameBG.getFill() != p) {
			playerNameBG.setFill(p);
		}
	}
	
	/**
	 * Der Container für die gesamte Partie-Oberfläche
	 * 
	 * @return gibt den Container zurück
	 */
	public Pane getContainer() {
		return ctn;
	}
	
	/**
	 * Holt sich das komplette Interface
	 * 
	 * @return gibt das Group-Objekt zurück mit allen Objekten
	 */
	public Group getBattleInterface() {
		return fightGroup;
	}
	
	/**
	 * Holt sich den Hintergrund von Land A
	 * 
	 * @return gibt das Rectangle-Objekt zurück
	 */
	public Rectangle getBattleBackgroundA() {
		return fightCountryOneBG;
	}
	
	/**
	 * Holt sich den Hintergrund von Land B
	 * 
	 * @return gibt das Rectangle-Objekt zurück
	 */
	public Rectangle getBattleBackgroundB() {
		return fightCountryTwoBG;
	}
	
	/**
	 * Holt sich die Würfel von Land A
	 * 
	 * @return gibt das Group-Objekt zurück
	 */
	public Group getDicesA() {
		return battleA_GroupDices;
	}
	
	/**
	 * Holt sich die Würfel von Land B
	 * 
	 * @return gibt das Group-Objekt zurück
	 */
	public Group getDicesB() {
		return battleB_GroupDices;
	}
	
	/**
	 * Holt sich den Namen von Land A
	 * 
	 * @return gibt das Label-Objekt zurück
	 */
	public Label getCountryNameA() {
		return fightCountryOneLabel;
	}

	/**
	 * Holt sich den Namen von Land B
	 * 
	 * @return gibt das Label-Objekt zurück
	 */
	public Label getCountryNameB() {
		return fightCountryTwoLabel;
	}
	
	/**
	 * Holt sich die Einheiten von Land A
	 * 
	 * @return gibt das Label-Objekt zurück
	 */
	public Label getCountryUnitsA() {
		return fightCountryOneUnits;
	}
	
	/**
	 * Holt sich die Einheiten von Land B
	 * 
	 * @return gibt das Label-Objekt zurück
	 */
	public Label getCountryUnitsB() {
		return fightCountryTwoUnits;
	}
	
	/**
	 * Holt sich den Bereit-Button des Kampfbildschirms
	 * 
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getBattleReadyBtn() {
		return fightBtnReady;
	}
	
	/**
	 * Holt sich das Eingabefeld von Land A
	 * 
	 * @return gibt das TextField-Objekt zurück
	 */
	public TextField getBattleInputA() {
		return fightCountryOneInput;
	}
	
	/**
	 * Holt sich das Eingabefeld von Land B
	 * 
	 * @return gibt das TextField-Objekt zurück
	 */
	public TextField getBattleInputB() {
		return fightCountryTwoInput;
	}
	
	/**
	 * Aktiviert bzw. deaktiviert die Weltkkarte
	 * 
	 * @param isActive boolean
	 */
	public void activateWorldMap(boolean isActive) {
		// Schleife die alle Länder je nach Wahl aktiviert oder deaktiviert
		for (int i = 0; i < getCountryArray.length; i++) {
			getCountryArray[i].setVisible(isActive);
		}
	}
	
	/**
	 * Holt sich die vier Phasen-Buttons
	 * 
	 * @return gibt das Group-Objekt zurück
	 */
	public Group getPhaseBtnGroup() {
		return phaseBtnGroup;
	}
	
	/**
	 * Aktviert bzw. deaktiviert die Phasen-Buttons nach eigenem gusto
	 * 
	 * @param add boolean
	 * @param fight boolean
	 * @param move boolean
	 * @param end boolean
	 */
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
	
	/**
	 * Holt sich die Spielrunde
	 * 
	 * @return gibt das GameMechanics-Objekt zurück
	 */
	public GameMechanics getGameMechanics() {
		return gameMechanics;
	}

	/**
	 * Setzt die aktuelle Spielrunde
	 * 
	 * @param gm GameMechanics
	 */
	public void setGameMechanics(GameMechanics gm) {
		gameMechanics = gm;
	}

	/**
	 * Holt sich alle Länder
	 * 
	 * @return gibt das Land-Array zurück
	 */
	public Country[] getCountryArray() {
		return getCountryArray;
	}

	/**
	 * Holt sich den Button der ersten Phase
	 * 
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getPhaseBtn1() {
		return phaseBtn1;
	}

	/**
	 * Holt sich den Button der zweiten Phase
	 * 
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getPhaseBtn2() {
		return phaseBtn2;
	}

	/**
	 * Holt sich den Button der dritten Phase
	 * 
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getPhaseBtn3() {
		return phaseBtn3;
	}

	/**
	 * Holt sich den Button der letzten Phase
	 * 
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getPhaseBtn4() {
		return phaseBtn4;
	}

	/**
	 * Holt sich die gesamte Auftrag-Gruppe
	 * 
	 * @return gibt das Group-Objekt zurücl
	 */
	public Group getPlayerInfoAuftragGroup() {
		return inventoryMissionGroup;
	}

	public ArrayList<Country> getCountrys(){
		return (ArrayList<Country>) Arrays.asList(getCountryArray);
	}
}