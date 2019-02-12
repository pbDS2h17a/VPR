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
	private boolean isFightStarting = false;
	private boolean isStartDicing = false;
	private Country[] countryArray = new Country[42];
	private Group inventoryGroup = new Group();
	private Group inventoryMissionGroup = new Group();
	private Group phaseBtnGroup = new Group();
	private Group groupLands = new Group();
	private Group fightGroup = new Group();
	private Group fightTextGroup = new Group();
	private Group dicesGroupA = new Group();
	private Group dicesGroupB = new Group();
	private Label[] countryUnitsLabelArray = new Label[countryArray.length];
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
	private Rectangle[] countryUnitsBGArray = new Rectangle[countryArray.length];
	private Rectangle fightCountryOneBG = new Rectangle(960, 1080);
	private Rectangle fightCountryTwoBG = new Rectangle(960, 1080);
	private double[][] worldMapCoordinates;
	private Line[] lineArray;

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
	    
    	int[][] seaRoutesCoordinates = new int[][] {
    		{1460, 702, 1470, 763},
    		{1408, 677, 1404, 752},
    		{1394, 658, 1367, 670},
    		{1345, 758, 1355, 787},
    		{1331, 644, 1337, 678},
    		{1116, 792, 1077, 728},
    		{1072, 856, 1038, 856},
    		{822, 620, 780, 628},
    		{967, 525, 994, 555},
    		{816, 261, 779, 238},
    		{846, 300, 834, 323},
    		{870, 290, 896, 295},
    		{898, 310, 848, 363},
    		{799, 399, 811, 471},
    		{701, 228, 671, 263},
    		{1404, 331, 1436, 350},
    		{1387, 399, 1421, 426}
    	};

    	lineArray = new Line[seaRoutesCoordinates.length];
    	for (int i = 0; i < seaRoutesCoordinates.length; i++) {
    		lineArray[i] = new Line(
    				seaRoutesCoordinates[i][0] + 20,
    				seaRoutesCoordinates[i][1] + 20,
    				seaRoutesCoordinates[i][2] + 20,
    				seaRoutesCoordinates[i][3] + 20);
    		
    		lineArray[i].setStroke(Color.WHITE);
    		lineArray[i].setStrokeWidth(5);
    		ctn.getChildren().add(lineArray[i]);
		}

	    // Gruppe die alle Länder enthält
	    groupLands.setScaleX(.9);
	    groupLands.setScaleY(.9);
	    groupLands.relocate(ctn.getPrefWidth()/2 - 656, ctn.getPrefHeight()/2 - 432);

	    // Schleife um einzelne Länder zu erzeugen
	    for(int i = 0; i < countryArray.length; i++) {
			// Fängt mit eins an, da die ID's der Länder in der Datenbank mit eins beginnen
			countryArray[i] = new Country(i+1);
			countryArray[i].setFill(Color.WHITE);
			countryArray[i].setStroke(Color.WHITE);
			countryArray[i].setStrokeWidth(0);
			countryArray[i].setScaleX(1.02);
			countryArray[i].setScaleY(1.02);
			groupLands.getChildren().add(countryArray[i]);
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
		fightTextGroup.setVisible(false);
		fightGroup.setVisible(false);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, Hintergrund)
		fightCountryOneBG.relocate(-960, 0);
		fightCountryOneBG.setStroke(Color.WHITE);
		fightCountryOneBG.setStrokeWidth(10);
		fightGroup.getChildren().add(fightCountryOneBG);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, Name)
		fightCountryOneLabel.setPrefWidth(960);
		fightCountryOneLabel.relocate(0, 50);
		fightCountryOneLabel.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightTextGroup.getChildren().add(fightCountryOneLabel);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, Eingabefeld)
		fightCountryOneInput.setPrefSize(120, 100);
		fightCountryOneInput.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightCountryOneInput.relocate(600, 430);
		fightTextGroup.getChildren().add(fightCountryOneInput);
		// Die Oberfläche auf der der Kampf stattfindet (Land eins, max. Einheiten)
		fightCountryOneUnits.setPrefSize(120, 40);
		fightCountryOneUnits.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
		fightCountryOneUnits.relocate(600, 550);
		fightTextGroup.getChildren().add(fightCountryOneUnits);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, Hintergrund)
		fightCountryTwoBG.relocate(1920, 0);
		fightCountryTwoBG.setStroke(Color.WHITE);
		fightCountryTwoBG.setStrokeWidth(10);
		fightGroup.getChildren().add(fightCountryTwoBG);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, Name)
		fightCountryTwoLabel.setPrefWidth(960);
		fightCountryTwoLabel.relocate(960, 50);
		fightCountryTwoLabel.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightTextGroup.getChildren().add(fightCountryTwoLabel);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, Eingabefeld)
		fightCountryTwoInput.setPrefSize(120, 100);
		fightCountryTwoInput.setStyle("-fx-alignment: center; -fx-background-color: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 50px;");
		fightCountryTwoInput.relocate(1200, 430);
		fightCountryTwoInput.setDisable(true);
		fightTextGroup.getChildren().add(fightCountryTwoInput);
		// Die Oberfläche auf der der Kampf stattfindet (Land zwei, max. Einheiten)
		fightCountryTwoUnits.setPrefSize(120, 40);
		fightCountryTwoUnits.setStyle("-fx-alignment: center; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 30px;");
		fightCountryTwoUnits.relocate(1200, 550);
		fightTextGroup.getChildren().add(fightCountryTwoUnits);
		
		// Pfeil der die Kampfrichtung angibt
		fightArrow.getPoints().addAll(
				0.0, 0.0,
				0.0, 30.0,
				-230.0, 30.0,
				-230.0, 90.0,
				0.0, 90.0,
				0.0, 120.0,
				70.0, 60.0);
		fightArrow.setFill(Color.WHITE);
		fightArrow.relocate(810, 480);
		fightTextGroup.getChildren().add(fightArrow);
		
		// Button der die gesetzten Einheiten bestätigen soll
		fightBtnReady.setButtonMode(true);
		fightBtnReady.relocate(960 - 167, 850);
		fightTextGroup.getChildren().add(fightBtnReady);
		
		// Die Würfel von Land eins (Gruppe)
		dicesGroupA.relocate(-400, 0);
		// Die Würfel von Land eins (Würfel eins)
		fightCountryOneDiceOne.setPrefSize(150, 150);
		fightCountryOneDiceOne.relocate(100, 265);
		fightCountryOneDiceOne.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		dicesGroupA.getChildren().add(fightCountryOneDiceOne);
		// Die Würfel von Land eins (Würfel zwei)
		fightCountryOneDiceTwo.setPrefSize(150, 150);
		fightCountryOneDiceTwo.relocate(100, 465);
		fightCountryOneDiceTwo.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		dicesGroupA.getChildren().add(fightCountryOneDiceTwo);
		// Die Würfel von Land eins (Würfel drei)
		fightCountryOneDiceThree.setPrefSize(150, 150);
		fightCountryOneDiceThree.relocate(100, 665);
		fightCountryOneDiceThree.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		dicesGroupA.getChildren().add(fightCountryOneDiceThree);
		fightTextGroup.getChildren().add(dicesGroupA);
		// Die Würfel von Land zwei (Gruppe)
		dicesGroupB.relocate(2320, 0);
		// Die Würfel von Land zwei (Gruppe, Würfel eins)
		fightCountryTwoDiceOne.setPrefSize(150, 150);
		fightCountryTwoDiceOne.relocate(0, 365);
		fightCountryTwoDiceOne.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		dicesGroupB.getChildren().add(fightCountryTwoDiceOne);
		// Die Würfel von Land zwei (Gruppe, Würfel zwei)
		fightCountryTwoDiceTwo.setPrefSize(150, 150);
		fightCountryTwoDiceTwo.relocate(0, 565);
		fightCountryTwoDiceTwo.setStyle("-fx-border-color: white; -fx-border-width: 10; -fx-alignment: center; -fx-background-color: #008137; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 100px;");
		dicesGroupB.getChildren().add(fightCountryTwoDiceTwo);
		fightTextGroup.getChildren().add(dicesGroupB);
		fightGroup.getChildren().add(fightTextGroup);
		ctn.getChildren().add(fightGroup);
		

		// Die Partie wird begonnen
		//distributeCountries();
	}

    /**
     * Prozedur, die die Partie beginnt und die Lobby ausliest um die aktuellen Teilnehmer zu integrieren.
     * 
     * @param lobbyFX LobbyFX
     */
	public void distributeCountries(LobbyFX lobbyFX) {
		Lobby lobby = lobbyFX.getLobby();
		int lobbyId = lobby.getLobbyId();

		// Verteilung der Länder auf die Spieler
		// Länder-Array wird in eine Liste konvertiert
		ArrayList<Country> countryList = new ArrayList<>(Arrays.asList(countryArray));


        // Lobbyleader setzen auf ersten Spieler in der Lobby
        lobby.setLobbyLeader(lobby.getPlayers().get(0));
		playersInLobby = lobby.getPlayers();

		int userCount = lobby.getPlayers().size();
		Random rand = new Random();
		// Verteilung der Länder
		for (int i = 0; i < countryArray.length; i++) {

			// Zufälliges Land aus der Länder-Liste wird ausgewählt
			Player currentPlayer = playersInLobby.get(userCount-1);
			Country randomCountry = countryList.get(rand.nextInt(countryList.size()));
			// Werte werden zugewiesen
			randomCountry.setOwner(currentPlayer);
			//randomCountry.setFill(Color.web(currentPlayer.getColorValue()));
			System.out.println("Länder in DB schreiben");
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
		initializeCountryGUI();

		// Aktualisiert den aktiven Spieler oben links in der Oberfläche
		updateActivePlayer(playersInLobby.get(0).getName(), Color.web(playersInLobby.get(0).getColorValue()));
	}

	public void initializeCountryGUI() {
		for (int i = 0; i < countryArray.length; i++) {
			countryUnitsBGArray[i].setFill(countryArray[i].getFill());
			countryUnitsLabelArray[i].setText(String.valueOf(countryArray[i].getUnits()));
			countryArray[i].setUnitLabel(countryUnitsLabelArray[i]);
			countryArray[i].setRectangle(countryUnitsBGArray[i]);
		}
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
	 * @param b boolean
	 */
	public void markNeighbourCountrys(Country country, boolean b) {

		// Erstellt einen Farbfilter, der die Länder hervorheben soll
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.7);
		
		// Schleife die alle vorherigen Markierungen zurücksetzt
		for (int i = 0; i < countryArray.length; i++) {
			countryArray[i].setStrokeWidth(0);
			countryArray[i].setEffect(null);
		}

		// Markiert das ausgewählte Land und die benachbarten Länder
		for (int i = 0; i < country.getNeighborIdArray().length; i++) {
			countryArray[country.getNeighborIdArray()[i]-1].setStrokeWidth(5);
			countryArray[country.getNeighborIdArray()[i]-1].setEffect(colorAdjust);
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
		return dicesGroupA;
	}
	
	/**
	 * Holt sich die Würfel von Land B
	 * 
	 * @return gibt das Group-Objekt zurück
	 */
	public Group getDicesB() {
		return dicesGroupB;
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
		for (int i = 0; i < countryArray.length; i++) {
			countryArray[i].setVisible(isActive);
			countryUnitsBGArray[i].setVisible(isActive);
			countryUnitsLabelArray[i].setVisible(isActive);
		}

		for (int i = 0; i < lineArray.length; i++) {
			lineArray[i].setVisible(isActive);
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
		return countryArray;
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
	 * @return gibt das Group-Objekt zurück
	 */
	public Group getPlayerInfoAuftragGroup() {
		return inventoryMissionGroup;
	}

	/**
	 * Holt sich die Kampfbildschirm-Gruppe die die Texte enthält
	 *
	 * @return gibt das Group-Objekt zurück
	 */
	public Group getFightTextGroup() {
		return fightTextGroup;
	}

	/**
	 * Kontrolliert ob die Kampfanimation gestartet werden soll
	 *
	 * @return gibt den true/false-Wert zurück
	 */
	public boolean isFightStarting() {
		return isFightStarting;
	}

	/**
	 * Setzt den true/false-Wert ob die Kampfanimation gestartet werden soll
	 *
	 * @param isFightStarting boolean
	 */
	public void setFightStarting(boolean isFightStarting) {
		this.isFightStarting = isFightStarting;
	}

	/**
	 * Kontrolliert ob das würfeln gestartet werden soll
	 *
	 * @return gibt den true/false-Wert zurück
	 */
	public boolean isStartDicing() {
		return isStartDicing;
	}

	/**
	 * Setzt den true/false-Wert ob das würfeln gestartet werden soll
	 *
	 * @param isStartDicing boolean
	 */
	public void setStartDicing(boolean isStartDicing) {
		this.isStartDicing = isStartDicing;
	}

}