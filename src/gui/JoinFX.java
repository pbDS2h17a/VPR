package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import sqlConnection.SqlHelper;

/**
 * Beinhaltet die gesamte Oberfl�che die beim Klick auf "Spiel beitreten" aufgerufen wird.
 * Man ist hier in der Lage sich eine Lobby auszusuchen um die Lobby-Oberfl�che aufzurufen.
 * 
 * @author Kevin Daniels
 */
public class JoinFX {

	// Globale Variablen
	private Pane ctn = new Pane();
	private Sprite btnBack = new Sprite("resources/btn_zurueck.png");
	private Rectangle listBG = new Rectangle(900, 650);	
	private int[] lobbyIdArray = SqlHelper.getAllLobbyId();
	
	public int[] getLobbyIdArray() {
		return lobbyIdArray;
	}

	private Label[] listUsers = new Label[lobbyIdArray.length];
	private Label listLabel = new Label("Partie aussuchen");
	
	/**
	 * Konstruktor, der alle Oberfl�chen-Objekte erstellt und sie in einen gemeinsamen Container eingef�gt wird.
	 */
	public JoinFX() {
		
		// Beitreten-Container der in der MainApp ausgegeben wird und alle Objekte f�rs beitreten beinhaltet.
	    ctn = new Pane();
	    ctn.setCache(true);
	    ctn.setPrefSize(1920, 1080);
	    // Setzt einen Bereich, in dem der Inhalt angezeigt wird. Alles was au�erhalb der Form ist wird ausgeblendet.
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    ctn.setVisible(false);
	    
	    // Button um zur�ck auf ins Hauptmen� zu kommen
	    btnBack = new Sprite("resources/btn_zurueck.png");
	    btnBack.relocate(50, 50);
	    btnBack.setButtonMode(true);
	    ctn.getChildren().add(btnBack);
	    
	    // Liste: Hintergrund f�r die Lobbys
	    listBG.setFill(Color.web("rgba(113, 188, 120, .85)"));
	    listBG.relocate(ctn.getPrefWidth()/2 - listBG.getWidth()/2, ctn.getPrefHeight()/2 - listBG.getHeight()/2);
	    listBG.setStroke(Color.WHITE);
	    listBG.setStrokeWidth(5);
	    listBG.setStrokeType(StrokeType.INSIDE);
	    ctn.getChildren().add(listBG);
	    
	    // Liste: Einzelne Lobby-Label erstellen (bis zu 9)
	    for(int i = 0; i < lobbyIdArray.length; i++) {
			String s = "";

			s += "Lobby " + lobbyIdArray[i] + " - " + SqlHelper.getLeaderName(lobbyIdArray[i]);

	    	listUsers[i] = new Label(s);
	    	listUsers[i].setPrefWidth(850);
	    	listUsers[i].setStyle("-fx-font-size: 40px; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: white;");
	    	listUsers[i].relocate(listBG.getLayoutX() + 25, listBG.getLayoutY() + 25);
	    	
	    	if(i != 0) {
	    		listUsers[i].relocate(listBG.getLayoutX() + 25, listUsers[i-1].getLayoutY() + 62);
	    	}

	    	ctn.getChildren().add(listUsers[i]);
	    }
	    
	    // Liste: �berschrift f�r die gesamte Liste
	    listLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    listLabel.relocate(listBG.getLayoutX() + 25, listBG.getLayoutY() - 50);
		ctn.getChildren().add(listLabel);
	}

	/**
	 * Der Container f�r die gesamte Beitreten-Oberfl�che
	 * 
	 * @return gibt den Container zur�ck
	 */
	public Pane getContainer() {
		return ctn;
	}
	
	/**
	 * Der Button der zur�ck zu Lobby f�hrt
	 * 
	 * @return gibt den Zur�ck-Button zur�ck
	 */
	public Sprite getBtnBack() {
		return btnBack;
	}
	
	/**
	 * Ein mit den potentiellen Lobby gef�lltes Array
	 * 
	 * @return gibt das Array mit den Lobbys zur�ck
	 */
	public Label[] getUserList() {
		return listUsers;
	}
	
}