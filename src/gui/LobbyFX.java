package gui;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.Player;

/**
 * @author Daniels, Kevin
 * @author pbs2h17ale
 */

public class LobbyFX {

	/**
	 * @param ctn			 : Pane
	 * @param btnReady		 : Sprite
	 * @param btnBack		 : Sprite
	 * @param btnCheck		 : Sprite
	 * 
	 * @param inputNameBG	 : Sprite
	 * @param inputName		 : TextField
	 * @param inputNameLabel : Label
	 * 
	 * @param groupColors	 : Group
	 * @param colorLabel	 : Label
	 * @param groupSlots	 : Group
	 * @param groupRoles	 : Group
	 * 
	 * @param slotArray		 : ImageView[]
	 * @param labelArray	 : Label[]
	 * @param triangleArray	 : Polygon[]
	 * @param slotRolesArray : Sprite[]
	 * @param colorArray	 : String[]
	 * @param colorRectArray : Rectangle[]
	 */
	private Pane ctn;
	private Sprite btnReady;
	private Sprite btnBack;
	private Sprite btnCheck;
	
	// Daten
	private int playerCount = 0;
	private final int MAX_PLAYER_COUNT = 6;
	
	private Player[] players = new Player[MAX_PLAYER_COUNT];
	private int lobbyId;

	//FX
	private ImageView[] slotArray = new ImageView[MAX_PLAYER_COUNT];
	private Label[] labelArray = new Label[slotArray.length];
    private Polygon[] triangleArray = new Polygon[slotArray.length];
    private Sprite[] slotRolesArray = new Sprite[slotArray.length];
    private String[] colorArray = {"#FFD800", "#C42B2B", "#26BF00", "#0066ED", "#000000", "#EF4CE7"};
    private Rectangle[] colorRectArray = new Rectangle[colorArray.length];

    
	/**
	 * Constructor.
	 */
	public LobbyFX() {	
	    // Lobby-Container (Child von Anwendungs_CTN)
	    ctn = new Pane();
	    ctn.setId("Lobby");
	    ctn.setCache(true);
	    ctn.setPrefSize(1920, 1080);
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Zur??Button
	    btnBack = new Sprite("resources/btn_zurueck.png");
	    btnBack.setButtonMode(true);
	    btnBack.relocate(50, 50);
	    ctn.getChildren().add(btnBack);
	    
	    // Bereit-Button
	    btnReady = new Sprite("resources/btn_bereit.png");
	    btnReady.relocate(ctn.getPrefWidth() - 400, ctn.getPrefHeight() - 200);
	    btnReady.setActive(false);
	    ctn.getChildren().add(btnReady);
	    
	    // Namens-Input Hintergrund
	    Sprite inputNameBG = new Sprite("resources/input_bg.png");
	    inputNameBG.relocate(ctn.getPrefWidth()/2 - 653/2 - 160, ctn.getPrefHeight() - 200);		
	    ctn.getChildren().add(inputNameBG);
	    
	    // Namens-Input TextField
	    TextField inputName = new TextField();
	    inputName.setPrefSize(653, 119);
	    inputName.setStyle("-fx-background-color: transparent; -fx-font-size: 60px; -fx-alignment: center;  -fx-font-weight: bold; -fx-text-fill: white;");
	    inputName.relocate(inputNameBG.getLayoutX(), inputNameBG.getLayoutY());
	    ctn.getChildren().add(inputName);
	    
	    // Namens-Input Check
	    btnCheck = new Sprite("resources/btn_confirm.png");
	    btnCheck.relocate(inputNameBG.getLayoutX() + 653 - 7, ctn.getPrefHeight() - 241);
	    btnCheck.setButtonMode(true);
	    ctn.getChildren().add(btnCheck);
	    
	    // Namens-Input Label
	    Label inputNameLabel = new Label("Name eingeben");
	    inputNameLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    inputNameLabel.relocate(inputNameBG.getLayoutX() + 20, inputNameBG.getLayoutY() - 50);
	    ctn.getChildren().add(inputNameLabel);
	    
	    // Farben-Gruppe
	    Group groupColors = new Group();
	    groupColors.relocate(ctn.getPrefWidth() - 300, 100);
	    
	    for(int i = 0; i < colorArray.length; i++)  {
	    	colorRectArray[i] = new Rectangle(90, 90);
	    	colorRectArray[i].setStroke(Color.WHITE);
	    	colorRectArray[i].setStrokeWidth(5);
	    	colorRectArray[i].setStrokeType(StrokeType.INSIDE);
	    	colorRectArray[i].setFill(Color.web(colorArray[i]));
	    	
	    	if(i > 0) {
	    		if(i % 2 != 0) {
	    			colorRectArray[i].relocate(colorRectArray[i-1].getLayoutX() + 90, colorRectArray[i-1].getLayoutY());
	    		} else {
	    			colorRectArray[i].relocate(colorRectArray[i-1].getLayoutX() - 90, colorRectArray[i-1].getLayoutY() + 90);
	    		}
	    	}
	    	
	    	final int tmp = i;

	    	colorRectArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    		lobbyChangeColor(1, colorRectArray[tmp].getFill())
	    	);
	    	
	    	groupColors.getChildren().add(colorRectArray[i]);
	    }
	    
	    ctn.getChildren().add(groupColors);
	    
	    // Farben Label
	    Label colorLabel = new Label("Farbe auswählen");
	    colorLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    colorLabel.setRotate(90);
	    colorLabel.relocate(groupColors.getLayoutX() + 70, groupColors.getLayoutY() + 110);
	    ctn.getChildren().add(colorLabel);
	    
	    // Slot-Gruppe
	    Group groupSlots = new Group();
	    groupSlots.relocate(360, 50);
	    
	    for(int i = 0; i < slotArray.length; i++) {
	    	slotArray[i] = new Sprite("resources/lobby_player_name.png"); 
	    	((Sprite) slotArray[i]).setActive(false);
	    	((Sprite) slotArray[i]).setButtonMode(false);
	    	
	    	if(i > 0)
	    		if(i % 3 != 0)
	    			slotArray[i].relocate(slotArray[i-1].getLayoutX() + 390, slotArray[i-1].getLayoutY());
	    		else
	    			slotArray[i].relocate(slotArray[i-1].getLayoutX() - 780, slotArray[i-1].getLayoutY() + 370);
	    	
	    	groupSlots.getChildren().add(slotArray[i]);
	    }

	    // Slot-Gruppe Farben
	    for(int i = 0; i < triangleArray.length; i++) {
	    	triangleArray[i] = new Polygon();
	    	triangleArray[i].getPoints().addAll(new Double[]{
	            0.0, 0.0,
	            0.0, 78.0,
	            78.0, 0.0 });
	    	triangleArray[i].setFill(Color.GREY);
	    	triangleArray[i].setStroke(Color.WHITE);
	    	triangleArray[i].setStrokeWidth(5);
	    	triangleArray[i].setStrokeType(StrokeType.INSIDE);
	    	triangleArray[i].relocate(slotArray[i].getLayoutX() + 30, slotArray[i].getLayoutY() + 30);
	    	
	    	groupSlots.getChildren().add(triangleArray[i]);
	    }

	    // Slot Gruppe Labels
	    for(int i = 0; i < labelArray.length; i++) {
	    	labelArray[i] = new Label();
	    	labelArray[i].setPrefSize(260, 50);
	    	labelArray[i].setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 35px; -fx-alignment: center");
	    	labelArray[i].relocate(slotArray[i].getLayoutX() + 10, slotArray[i].getLayoutY() + 290);
	    	
	    	groupSlots.getChildren().add(labelArray[i]);
	    }
	    
	    ctn.getChildren().add(groupSlots);
	    
	    // Rollen festlegen
	    Group groupRoles = new Group();
	    
	    for(int i = 0; i < slotRolesArray.length; i++) {
	    	
	    	final int tmp = i;
	    	
		    	if(i == 0) {
		    		slotRolesArray[i] = new Sprite("resources/btn_lobby_host.png");
		    		slotRolesArray[i].setButtonMode(false);
		    		
			    	slotRolesArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
		    		lobbyAddPlayer(tmp)
		    	);
	    	}
	    	else {
	    		slotRolesArray[i] = new Sprite("resources/btn_lobby_kick.png");
	    		slotRolesArray[i].setButtonMode(true);
	    		slotRolesArray[i].setVisible(false);

		    	slotRolesArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
		    		lobbyRemovePlayer(tmp)
		    	);
	    	}

	    	slotRolesArray[i].relocate(groupSlots.getLayoutX() + slotArray[i].getLayoutX() + 280, slotArray[i].getLayoutY() + 100);
	    	
	    	groupRoles.getChildren().add(slotRolesArray[i]);
	    }
	    
	    ctn.getChildren().add(groupRoles);
	    
	    btnCheck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			lobbyChangeName(1, inputName.getText());
	    });
	    	    
	    lobbyAddPlayer(0);
	    lobbyAddPlayer(1);
	}
	
	/**
	 * @param id   : Integer
	 * @param name : String
	 * Checks if the lobby name is not empty.
	 * If so: Sets the labelArray[id] name.
	 * Checks if the triangleArray[id] fill is not grey.
	 * If so: Sets the btnReady to active and the ButtonMode to true.
	 */
	public void lobbyChangeName(int id, String name) {
		if(!name.isEmpty()) {
			labelArray[id].setText(name);			
			if(triangleArray[id].getFill() != Color.GREY) {
				btnReady.setActive(true);
				btnReady.setButtonMode(true);
			}
		}
	}
	
	/**
	 * @param id  	: Integer
	 * @param paint : Paint
	 * Sets the triangleArray[id] fill to the input paint.
	 * Checks if the labelArray[id] name is not empty. 
	 * If so: Sets the btnReady to active and the ButtonMode to true.
	 */
	private void lobbyChangeColor(int id, Paint paint) {
		triangleArray[id].setFill(paint);
		
		if(!labelArray[id].getText().isEmpty()) {
			btnReady.setActive(true);
			btnReady.setButtonMode(true);
		}
	}
	
	/**
	 * @param id : Integer
	 * If a 
	 */
	private void lobbyAddPlayer(int id) {
		((Sprite) slotArray[id]).setActive(true);
		slotRolesArray[id].setVisible(true);
	}
	
	/**
	 * @param id : Integer
	 * Remove player from slotArray[id].
	 * Gray out triangleArray[id].
	 * Clear labelArray[id] text.
	 */
	private void lobbyRemovePlayer(int id) {
		((Sprite) slotArray[id]).setActive(false);
		slotRolesArray[id].setVisible(false);
		((Sprite) slotArray[id]).setActive(false);
		triangleArray[id].setFill(Color.GREY);
		labelArray[id].setText(null);
	}

	/**
	 * @return ctn : Pane
	 * Returns the pane for the MainApp.
	 */
	public Pane getContainer() {
		return ctn;
	}
	
	/**
	 * @return btnReady : Sprite
	 * Returns the sprite for the initializeClickEventHandlers() in the MainApp.
	 */
	public Sprite getBtnReady() {
		return btnReady;
	}

	/**
	 * @return btnBack : Sprite
	 * Returns the sprite for the initializeClickEventHandlers() in the MainApp.
	 */
	public Sprite getBtnBack() {
		return btnBack;
	}
	
	/**
	 * @return btnCheck : Sprite
	 * Returns the sprite for the initializeClickEventHandlers() in the MainApp.
	 */
	public Sprite getBtnCheck() {
		return btnCheck;
	}
	
	public int getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(int lobbyId) {
		this.lobbyId = lobbyId;
	}
	
	public void addPlayer(Player player) {
		if (playerCount < MAX_PLAYER_COUNT) {
			players[playerCount] = player;
			playerCount++;
		}
	}
	
	public Player[] getPlayers() {
		return this.players;
	}
	
}