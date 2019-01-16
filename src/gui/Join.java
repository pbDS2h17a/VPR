package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.SqlHelper;

public class Join {
	
	private Pane ctn;
	private Sprite btnBack;
	private int[] lobbyIdArray = SqlHelper.getAllLobbyId();
	private Label[] listUsers = new Label[lobbyIdArray.length];
	
	public Join() {
		
		// Beitreten-Container (Child von Anwendungs_CTN)
	    ctn = new Pane();
	    ctn.setId("Beitreten");
	    ctn.setCache(true);
	    ctn.setPrefSize(1920, 1080);
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Zur??Button
	    btnBack = new Sprite("resources/btn_zurueck.png");
	    btnBack.relocate(50, 50);
	    btnBack.setButtonMode(true);
	    
	    // Listen-Hintergrund
	    Rectangle listBG = new Rectangle(900, 650);
	    listBG.setFill(Color.web("rgba(113, 188, 120, .85)"));
	    listBG.relocate(ctn.getPrefWidth()/2 - listBG.getWidth()/2, ctn.getPrefHeight()/2 - listBG.getHeight()/2);
	    listBG.setStroke(Color.WHITE);
	    listBG.setStrokeWidth(5);
	    listBG.setStrokeType(StrokeType.INSIDE);
	    ctn.getChildren().add(listBG);
	    
	    // Listen aufrufen
	    for(int i = 0; i < lobbyIdArray.length; i++) {
	    	String s = "";
	    	
	    	s += "Lobby Nummer " + i + " (klicken zum beitreten)";
	    	
	    	listUsers[i] = new Label(s);
	    	listUsers[i].setPrefWidth(850);
	    	listUsers[i].setStyle("-fx-font-size: 40px; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: white;");
	    	listUsers[i].relocate(listBG.getLayoutX() + 25, listBG.getLayoutY() + 25);
	    	
	    	if(i != 0)
	    		listUsers[i].relocate(listBG.getLayoutX() + 25, listUsers[i-1].getLayoutY() + 62);
	    	
	    	ctn.getChildren().add(listUsers[i]);
	    }
	    // Namens-Input Label
	    Label listLabel = new Label("Partie aussuchen");
	    listLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    listLabel.relocate(listBG.getLayoutX() + 25, listBG.getLayoutY() - 50);
	  
		ctn.getChildren().addAll(btnBack, listLabel);
	}

	public Pane getContainer() {
		return ctn;
	}

	public Sprite getBtnBack() {
		return btnBack;
	}
	
	public Label[] getBtnLobby() {
		return listUsers;
	}
	
}