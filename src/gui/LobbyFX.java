package gui;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.Lobby;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniels, Kevin
 * @author pbs2h17ale
 */
public class LobbyFX {

	// Globale Variablen
	private Pane ctn = new Pane();
    private Lobby lobby = new Lobby();
	private ImageView[] slotViewArray = new ImageView[lobby.getMAX_PLAYER_COUNT()];
    private TextField inputName = new TextField();
    private Group groupColors = new Group();
    private Group groupSlots = new Group();
    private Group groupRoles = new Group();
	private Label[] labelArray = new Label[lobby.getMAX_PLAYER_COUNT()];
    private Label colorLabel = new Label("Farbe auswählen");
    private Label inputNameLabel = new Label("Name eingeben");
    private Rectangle[] colorRectArray = new Rectangle[lobby.getMAX_PLAYER_COUNT()];
    private Polygon[] triangleArray = new Polygon[lobby.getMAX_PLAYER_COUNT()];
    private Sprite[] slotRolesArray = new Sprite[lobby.getMAX_PLAYER_COUNT()];
	private Sprite btnReady = new Sprite("resources/btn_bereit.png");
	private Sprite btnBack = new Sprite("resources/btn_zurueck.png");
	private Sprite btnCheck = new Sprite("resources/btn_confirm.png");
	private Sprite inputNameBG = new Sprite("resources/input_bg.png");
	private String[] colors = SqlHelper.getAllColors();

	/**
	 * Konstruktor, der alle Oberflächen-Objekte erstellt und sie in einen gemeinsamen Container eingefügt wird.
	 */
	public LobbyFX() {
		// Lobby-Container der in der MainApp ausgegeben wird und alle Objekte fürs beitreten beinhaltet.
	    ctn.setCache(true);
	    ctn.setPrefSize(1920, 1080);
	    // Setzt einen Bereich, in dem der Inhalt angezeigt wird. Alles was außerhalb der Form ist wird ausgeblendet.
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Zurück-Button, der zum Hauptmenü führt
	    btnBack.setButtonMode(true);
	    btnBack.relocate(50, 50);
	    ctn.getChildren().add(btnBack);
	    
	    // Bereit-Button, der das Spiel starten soll und zur Weltkkarte führt
	    btnReady.relocate(ctn.getPrefWidth() - 400, ctn.getPrefHeight() - 200);
	    btnReady.setActive(false);
	    ctn.getChildren().add(btnReady);
	    
	    // Eingabefeld für den Spieler-Namen (Hintergrund)
	    inputNameBG.relocate(ctn.getPrefWidth()/2 - 653/2 - 160, ctn.getPrefHeight() - 200);		
	    ctn.getChildren().add(inputNameBG);
	    // Eingabefeld für den Spieler-Namen (Eingabefeld)
	    inputName.setPrefSize(653, 119);
	    inputName.setStyle("-fx-background-color: transparent; -fx-font-size: 60px; -fx-alignment: center;  -fx-font-weight: bold; -fx-text-fill: white;");
	    inputName.relocate(inputNameBG.getLayoutX(), inputNameBG.getLayoutY());
	    ctn.getChildren().add(inputName);
	    // Eingabefeld für den Spieler-Namen (Bestätigungs-Button)
	    btnCheck.relocate(inputNameBG.getLayoutX() + 653 - 7, ctn.getPrefHeight() - 241);
	    btnCheck.setButtonMode(true);
	    ctn.getChildren().add(btnCheck);
	    // Eingabefeld für den Spieler-Namen (Titel)
	    inputNameLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    inputNameLabel.relocate(inputNameBG.getLayoutX() + 20, inputNameBG.getLayoutY() - 50);
	    ctn.getChildren().add(inputNameLabel);
	    
	    // Die farbigen Rechtecke die die Farben darstellen soll, die ein Spieler auswählen kann
	    groupColors.relocate(ctn.getPrefWidth() - 300, 100);
	    // Schleife die die Quadrate erstellt und sie mit den Farben aus der Datenbank füllt
	    for(int i = 0; i < colors.length; i++)  {
	    	colorRectArray[i] = new Rectangle(90, 90);
	    	colorRectArray[i].setStroke(Color.WHITE);
	    	colorRectArray[i].setStrokeWidth(5);
	    	colorRectArray[i].setStrokeType(StrokeType.INSIDE);
	    	colorRectArray[i].setFill(Color.web(colors[i]));
	    	
	    	// Abfrage, die einen Zeilenumbruch ermöglicht, so dass die Farben in einem 2x4 Grid dargestellt werden können
	    	if(i > 0) {
	    		if(i % 2 != 0) {
	    			colorRectArray[i].relocate(colorRectArray[i-1].getLayoutX() + 90, colorRectArray[i-1].getLayoutY());
	    		} else {
	    			colorRectArray[i].relocate(colorRectArray[i-1].getLayoutX() - 90, colorRectArray[i-1].getLayoutY() + 90);
	    		}
	    	}

	    	groupColors.getChildren().add(colorRectArray[i]);
	    }
	    ctn.getChildren().add(groupColors);
	    
	    // Label für die Farben-Gruppe
	    colorLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    colorLabel.setRotate(90);
	    colorLabel.relocate(groupColors.getLayoutX() + 70, groupColors.getLayoutY() + 110);
	    ctn.getChildren().add(colorLabel);
	    
	    // Slots, die mit Spielern gefüllt werden mit 2-6 freien Plätzen (Gruppe)
	    groupSlots.relocate(360, 50);
	    // Slots, die mit Spielern gefüllt werden mit 2-6 freien Plätzen (Hintergrund)
	    for(int i = 0; i < slotViewArray.length; i++) {
	    	slotViewArray[i] = new Sprite("resources/lobby_player_name.png");
	    	((Sprite) slotViewArray[i]).setActive(false);
	    	((Sprite) slotViewArray[i]).setButtonMode(false);
	    	
	    	// Abfrage, die einen Zeilenumbruch ermöglicht, so dass die Farben in einem 3x2 Grid dargestellt werden können
	    	if(i > 0)
	    		if(i % 3 != 0)
	    			slotViewArray[i].relocate(slotViewArray[i-1].getLayoutX() + 390, slotViewArray[i-1].getLayoutY());
	    		else
	    			slotViewArray[i].relocate(slotViewArray[i-1].getLayoutX() - 780, slotViewArray[i-1].getLayoutY() + 370);
	    	
	    	groupSlots.getChildren().add(slotViewArray[i]);
	    }

	    // Dreiecke die links oben vom Slot angezeigt werden und die Farbe des Spielers indizieren soll
	    for(int i = 0; i < triangleArray.length; i++) {
	    	triangleArray[i] = new Polygon();
	    	triangleArray[i].getPoints().addAll(0.0, 0.0,
					0.0, 78.0,
					78.0, 0.0);
	    	triangleArray[i].setFill(Color.GREY);
	    	triangleArray[i].setStroke(Color.WHITE);
	    	triangleArray[i].setStrokeWidth(5);
	    	triangleArray[i].setStrokeType(StrokeType.INSIDE);
	    	triangleArray[i].relocate(slotViewArray[i].getLayoutX() + 30, slotViewArray[i].getLayoutY() + 30);
	    	
	    	groupSlots.getChildren().add(triangleArray[i]);
	    }

	    // Labels, die die Namen in der Spieler in ihren Slots anzeigen soll
	    for(int i = 0; i < labelArray.length; i++) {
	    	labelArray[i] = new Label();
	    	labelArray[i].setPrefSize(260, 50);
	    	labelArray[i].setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 35px; -fx-alignment: center");
	    	labelArray[i].relocate(slotViewArray[i].getLayoutX() + 10, slotViewArray[i].getLayoutY() + 290);
	    	
	    	groupSlots.getChildren().add(labelArray[i]);
	    }
	    
	    ctn.getChildren().add(groupSlots);
	    
	    /*
	     * Icons die rechts-oben vom Slot angezeigt werden und die Rolle des Spielers angibt
	     * Stern -> Spielleiter
	     * Kreuz -> Spieler
	     */
	    for(int i = 0; i < slotRolesArray.length; i++) {
	    	if(i == 0) {
	    		slotRolesArray[i] = new Sprite("resources/btn_lobby_host.png");
	    		slotRolesArray[i].setButtonMode(false);
		    		slotRolesArray[i].setButtonMode(false);
	    	}

	    	else {
	    		slotRolesArray[i] = new Sprite("resources/btn_lobby_kick.png");
	    		slotRolesArray[i].setButtonMode(true);
	    		slotRolesArray[i].setVisible(false);
	    	}

	    	slotRolesArray[i].relocate(groupSlots.getLayoutX() + slotViewArray[i].getLayoutX() + 280, slotViewArray[i].getLayoutY() + 100);
	    	groupRoles.getChildren().add(slotRolesArray[i]);
	    }
	    ctn.getChildren().add(groupRoles);

	}

	public int getNextSlotId() {
		return this.lobby.getNextSlotId();
	}

	/**
	 * Ändert den Namen eines Slots, basierend auf seiner ID
	 *
	 * @param slotId int
	 * @param name String
	 */
	public void changePlayerName(int slotId, String name) {
		// Wenn kein leerer String übergeben wurde...
		if(!name.isEmpty() && name != null) {
			// ...wird der Name gesetzt
			labelArray[slotId].setText(name);
			lobby.changePlayerName(slotId, name);
			// Wenn der Slot auch schon eine Farbe hat...
			if(triangleArray[slotId].getFill() != Color.GREY) {
				// ...wird der Bereit-Button aktiviert
				btnReady.setActive(true);
				btnReady.setButtonMode(true);

			}
		}
	}
	
	/**
	 * Ändert die Farbe eines Slots, basierend auf seiner ID
	 *
	 * @param slotId int
	 * @param paint Paint
	 */
	public void guiChangeColor(int slotId, Paint paint) {
		// Füllt das Dreieck des Slots mit der gewählten Farbe
		triangleArray[slotId].setFill(paint);
		// Wenn auch schon ein Name übergeben wurde...
		if(!labelArray[slotId].getText().isEmpty()) {
			// ...wird der Bereit-Button aktiviert
			btnReady.setActive(true);
			btnReady.setButtonMode(true);
		}
	}

	/**
	 * Fügt einen Spieler im Slot hinzu, basierend auf seiner ID
	 *
	 * @param slotId int
	 */
	public void guiAddPlayer(int slotId) {
		// Aktiviert den Slot und zeigt sein Icon zum entfernen an
		((Sprite) slotViewArray[slotId]).setActive(true);
		slotRolesArray[slotId].setVisible(true);
	}
	

	/**
	 * Entfernt einen Spieler aus dem Slot, basierend auf seiner ID
	 *
	 * @param slotId int
	 */
	public void guiRemovePlayer(int slotId) {
		// Deaktiviert den Slot und entfernt sein Icon zum entfernen
		((Sprite) slotViewArray[slotId]).setActive(false);
		slotRolesArray[slotId].setVisible(false);
		((Sprite) slotViewArray[slotId]).setActive(false);
		// Entfernt die Farbe und den Namen des zu löschenden Spielers
		triangleArray[slotId].setFill(Color.GREY);
		labelArray[slotId].setText(null);
	}

	/**
	 * Der Container für die gesamte Lobby-Oberfläche
	 *
	 * @return gibt den Container zurück
	 */
	public Pane getContainer() {
		return ctn;
	}

	/**
	 * Methode, die sich die Lobby-Funktionen besorgt
	 *
	 * @return das Lobby-Objekt mit allen Methoden
	 */
	public Lobby getLobby() {
		return this.lobby;
	}

	/**
	 * Methode für den Bereit-Button der Lobby
	 *
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getBtnReady() {
		return btnReady;
	}

	/**
	 * Methode für den Zurück-Button vom Lobby zum Hauptmenü
	 *
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getBtnBack() {
		return btnBack;
	}
	
	/**
	 * Methode für den Bestätigen-Button für die Namens-Eingabe
	 *
	 * @return gibt das Sprite-Objekt zurück
	 */
	public Sprite getBtnCheck() {
		return btnCheck;
	}

	/**
	 * Methode für alle Icons in den Slots für den Spielleiter und Spieler
	 *
	 * @return gibt alle Icons als Sprite-Array zurück
	 */
	public Sprite[] getSlotRolesArray() {
		return slotRolesArray;
	}

	/**
	 * Methode für alle Farben die ausgewählt werden können
	 *
	 * @return gibt alle Farben als Rectangle-Array zurück
	 */
	public Rectangle[] getColorRectArray() {
		return colorRectArray;
	}

	/**
	 * Methode um sich alle Farben aus der Datenbank zu besorgen
	 *
	 * @return gibt alle Farben als String-Array zurück
	 */
	public String[] getColors() {
		return colors;
	}

	/**
	 * Methode für das Namens-Eingabefeld
	 *
	 * @return gibt das Eingabefeld als TextField-Objekt zurück
	 */
	public TextField getInputName() {
		return inputName;
	}


}