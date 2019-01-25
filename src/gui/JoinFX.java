package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.SqlHelper;

/**
 * @author Daniels, Kevin
 * @author pbs2h17ale
 */

public class JoinFX {
	
	/**
	 * @param ctn	   		 : Pane
	 * @param btnBack  		 : Sprite
	 * @param btnCheck 		 : Sprite
	 * @param inputNameBG 	 : Sprite
	 * @param inputName 	 : TextField
	 * @param inputNameLabel : Label
	 */
	private Pane ctn = new Pane();
	private Sprite btnBack = new Sprite("resources/btn_zurueck.png");
	private Rectangle listBG = new Rectangle(900, 650);
	private Label listLabel = new Label("Partie aussuchen");	
	private int[] lobbyIdArray = SqlHelper.getAllLobbyId();
	private Label[] listUsers = new Label[lobbyIdArray.length];
	
	/**
	 * Constructor.
	 */
	public JoinFX() {
		
		// Beitreten-Container (Child von Anwendungs_CTN)
	    ctn = new Pane();
	    ctn.setId("Beitreten");
	    ctn.setCache(true);
	    ctn.setPrefSize(1920, 1080);
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Zurueck-Button
	    btnBack = new Sprite("resources/btn_zurueck.png");
	    btnBack.relocate(50, 50);
	    btnBack.setButtonMode(true);
	    ctn.getChildren().add(btnBack);
	    
	    // Listen-Hintergrund
	    listBG.setFill(Color.web("rgba(113, 188, 120, .85)"));
	    listBG.relocate(ctn.getPrefWidth()/2 - listBG.getWidth()/2, ctn.getPrefHeight()/2 - listBG.getHeight()/2);
	    listBG.setStroke(Color.WHITE);
	    listBG.setStrokeWidth(5);
	    listBG.setStrokeType(StrokeType.INSIDE);
	    ctn.getChildren().add(listBG);
	    
	    // Listen aufrufen
	    for(int i = 0; i < lobbyIdArray.length; i++) {
	    	String s = "";
	    	
	    	s += "Lobby Nummer " + lobbyIdArray[i] + " (klicken zum beitreten)";
	    	
	    	listUsers[i] = new Label(s);
	    	listUsers[i].setPrefWidth(850);
	    	listUsers[i].setStyle("-fx-font-size: 40px; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: white;");
	    	listUsers[i].relocate(listBG.getLayoutX() + 25, listBG.getLayoutY() + 25);
	    	
	    	if(i != 0)
	    		listUsers[i].relocate(listBG.getLayoutX() + 25, listUsers[i-1].getLayoutY() + 62);
  	
	    	ctn.getChildren().add(listUsers[i]);
	    }
	    
	    // Namens-Input Label
	    listLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    listLabel.relocate(listBG.getLayoutX() + 25, listBG.getLayoutY() - 50);
		ctn.getChildren().add(listLabel);
	}
	
	/**
	 * @return ctn : Pane
	 * Returns the pane for the MainApp.
	 */
	public Pane getContainer() {
		return ctn;
	}
	
	/**
	 * @return btnBack : Sprite
	 * Returns the sprite for the initializeClickEventHandlers() in the MainApp.
	 */
	public Sprite getBtnBack() {
		return btnBack;
	}
	
	/**
	 * @return listUsers : Label[]
	 * Returns the Label-Array for the initializeClickEventHandlers() in the MainApp.
	 */
	public Label[] getUserList() {
		return listUsers;
	}
	
}