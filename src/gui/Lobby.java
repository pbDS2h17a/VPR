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

public class Lobby {

	// Globale Variablen
	private Pane ctn;
	private Sprite btnReady;
	private Sprite btnBack;
	private Sprite btnCheck;
	
	ImageView[] slotArray = new ImageView[6];
    Label[] labelArray = new Label[slotArray.length];
    Polygon[] triangleArray = new Polygon[slotArray.length];
    Sprite[] slotRolesArray = new Sprite[slotArray.length];
    String[] colorArray = {"#FFD800", "#C42B2B", "#26BF00", "#0066ED", "#000000", "#EF4CE7"};
    Rectangle[] colorRectArray = new Rectangle[colorArray.length];
	
	public Lobby() {
		
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
	    
	    // Bereit-Button
	    btnReady = new Sprite("resources/btn_bereit.png");
	    btnReady.relocate(ctn.getPrefWidth() - 400, ctn.getPrefHeight() - 200);
	    btnReady.setIsActive(false);
	    
	    // Namens-Input Hintergrund
	    Sprite inputNameBG = new Sprite("resources/input_bg.png");
	    inputNameBG.relocate(ctn.getPrefWidth()/2 - 653/2 - 160, ctn.getPrefHeight() - 200);		
	    
	    // Namens-Input TextField
	    TextField inputName = new TextField();
	    inputName.setPrefSize(653, 119);
	    inputName.setStyle("-fx-background-color: transparent; -fx-font-size: 60px; -fx-alignment: center;  -fx-font-weight: bold; -fx-text-fill: white;");
	    inputName.relocate(inputNameBG.getLayoutX(), inputNameBG.getLayoutY());
	    
	    // Namens-Input Check
	    btnCheck = new Sprite("resources/btn_confirm.png");
	    btnCheck.relocate(inputNameBG.getLayoutX() + 653 - 7, ctn.getPrefHeight() - 241);
	    btnCheck.setButtonMode(true);
	    
	    // Namens-Input Label
	    Label inputNameLabel = new Label("Name eingeben");
	    inputNameLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    inputNameLabel.relocate(inputNameBG.getLayoutX() + 20, inputNameBG.getLayoutY() - 50);
	    
	    // Farben-Gruppe
	    Group groupColors = new Group();
	    groupColors.relocate(ctn.getPrefWidth() - 300, 100);
	    
	    for(int i = 0; i < colorArray.length; i++)  {
	    	colorRectArray[i] = new Rectangle(90, 90);
	    	colorRectArray[i].setStroke(Color.WHITE);
	    	colorRectArray[i].setStrokeWidth(5);
	    	colorRectArray[i].setStrokeType(StrokeType.INSIDE);
	    	colorRectArray[i].setFill(Color.web(colorArray[i]));
	    	
	    	if(i > 0)
	    		if(i % 2 != 0)
	    			colorRectArray[i].relocate(colorRectArray[i-1].getLayoutX() + 90, colorRectArray[i-1].getLayoutY());
	    		else
	    			colorRectArray[i].relocate(colorRectArray[i-1].getLayoutX() - 90, colorRectArray[i-1].getLayoutY() + 90);
	    	
	    	final int tmp = i;

	    	colorRectArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    		lobbyChangeColor(1, colorRectArray[tmp].getFill())
	    	);
	    	
	    	groupColors.getChildren().add(colorRectArray[i]);
	    }
	    
	    // Farben Label
	    Label colorLabel = new Label("Farbe ausw婬en");
	    colorLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    colorLabel.setRotate(90);
	    colorLabel.relocate(groupColors.getLayoutX() + 70, groupColors.getLayoutY() + 110);
	    
	    // Slot-Gruppe
	    Group groupSlots = new Group();
	    groupSlots.relocate(360, 50);
	    
	    for(int i = 0; i < slotArray.length; i++) {
	    	slotArray[i] = new Sprite("resources/lobby_player_name.png"); 
	    	((Sprite) slotArray[i]).setIsActive(false);
	    	((Sprite) slotArray[i]).setButtonMode(false);
	    	
	    	if(i > 0)
	    		if(i % 3 != 0)
	    			slotArray[i].relocate(slotArray[i-1].getLayoutX() + 390, slotArray[i-1].getLayoutY());
	    		else
	    			slotArray[i].relocate(slotArray[i-1].getLayoutX() - 780, slotArray[i-1].getLayoutY() + 370);
	    	
	    	groupSlots.getChildren().add(slotArray[i]);
	    }
	    
	    
	    // Rollen festlegen
	    Group groupRoles = new Group();
	    
	    for(int i = 0; i < slotRolesArray.length; i++) {
	    	
	    	final int tmp = i;
	    	
	    	if(i == 0) {
	    		slotRolesArray[i] = new Sprite("resources/btn_lobby_host.png");
	    		slotRolesArray[i].setButtonMode(false);
	    		
		    	slotRolesArray[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
	    		lobbyAddPlayer(1)
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
		
	    ctn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> 
			lobbyChangeName(1, inputName.getText())
	    );
	    
	    ctn.getChildren().addAll(btnBack, btnReady, inputNameBG, inputName, inputNameLabel, btnCheck, groupColors, colorLabel, groupSlots, groupRoles);
	    
	    lobbyAddPlayer(0);
	    lobbyAddPlayer(1);
	}
	
	public Pane getContainer() {
		return ctn;
	}
	
	public void lobbyChangeName(int id, String name) {
		if(!name.isEmpty()) {
			labelArray[id].setText(name);
			
			if(triangleArray[id].getFill() != Color.GREY) {
				btnReady.setIsActive(true);
				btnReady.setButtonMode(true);
			}
		}
	}
	
	private void lobbyChangeColor(int id, Paint paint) {
		triangleArray[id].setFill(paint);
		
		if(!labelArray[id].getText().isEmpty()) {
			btnReady.setIsActive(true);
			btnReady.setButtonMode(true);
		}
	}
	
	private void lobbyAddPlayer(int id) {
		((Sprite) slotArray[id]).setIsActive(true);
		slotRolesArray[id].setVisible(true);
	}
	
	private void lobbyRemovePlayer(int id) {
		((Sprite) slotArray[id]).setIsActive(false);
		slotRolesArray[id].setVisible(false);
		((Sprite) slotArray[id]).setIsActive(false);
		triangleArray[id].setFill(Color.GREY);
		labelArray[id].setText(null);
	}

	public Sprite getBtnReady() {
		return btnReady;
	}

	public Sprite getBtnBack() {
		return btnBack;
	}
	
	public Sprite getBtnCheck() {
		return btnCheck;
	}
	
}