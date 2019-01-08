package gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Join {
	
	private Pane ctn;
	private Sprite btnBack;
	private Sprite btnCheck;
	
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
	    
	    // Namens-Input Hintergrund
	    Sprite inputNameBG = new Sprite("resources/input_bg.png");
	    inputNameBG.relocate(ctn.getPrefWidth()/2 - 653/2 - 160, ctn.getPrefHeight()/2 - 119/2);		
	    
	    // Namens-Input TextField
	    TextField inputName = new TextField();
	    inputName.setPrefSize(653, 119);
	    inputName.setStyle("-fx-background-color: transparent; -fx-font-size: 60px; -fx-alignment: center;  -fx-font-weight: bold; -fx-text-fill: white;");
	    inputName.relocate(inputNameBG.getLayoutX(), inputNameBG.getLayoutY());

	    // Namens-Input Check-Button
	    btnCheck = new Sprite("resources/btn_confirm.png");
	    btnCheck.relocate(inputNameBG.getLayoutX() + 653 - 7, inputNameBG.getLayoutY() - 40);
	    btnCheck.setButtonMode(true);

	    // Namens-Input Label
	    Label inputNameLabel = new Label("IP eingeben");
	    inputNameLabel.setStyle("-fx-font-family: Impact; -fx-text-fill: white; -fx-font-size: 40px");
	    inputNameLabel.relocate(inputNameBG.getLayoutX() + 20, inputNameBG.getLayoutY() - 50);
	  
		ctn.getChildren().addAll(btnBack, inputNameBG, inputName, btnCheck, inputNameLabel);
	}

	public Pane getContainer() {
		return ctn;
	}

	public Sprite getBtnCheck() {
		return btnCheck;
	}

	public Sprite getBtnBack() {
		return btnBack;
	}
	
}