package gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Beinhaltet die gesamte Oberfl�che die beim Spiel starten erstellt wird. 
 * Man ist hier in der Lage sich das Tutorial anzusehen, eine Lobby zu erstellen oder einer Lobby beizutreten.
 * 
 * @author Kevin Daniels
 */
public class TitleFX {
	
	// Globale Variablen
	private Pane ctn = new Pane();
	private Sprite logo = new Sprite("resources/logo.png");
	private Sprite btnCreate = new Sprite("resources/btn_erstellen.png");
	private Sprite btnJoin = new Sprite("resources/btn_beitreten.png");
	private Sprite btnTutorial = new Sprite("resources/btn_tutorial.png");
	private boolean isLogoAnimated = true;

	/**
	 * Konstruktor, der alle Oberfl�chen-Objekte erstellt und sie in einen gemeinsamen Container eingef�gt wird.
	 */
	public TitleFX() {
		// Beitreten-Container der in der MainApp ausgegeben wird und alle Objekte f�rs beitreten beinhaltet.
	    ctn = new Pane();
	    ctn.setPrefSize(1920, 1080);
	    // Setzt einen Bereich, in dem der Inhalt angezeigt wird. Alles was au�erhalb der Form ist wird ausgeblendet.
	    ctn.setClip(new Rectangle(ctn.getPrefWidth(), ctn.getPrefHeight()));
	    
	    // Logo des Spiels
	    logo.relocate(ctn.getPrefWidth()/2 - 1073/2, ctn.getPrefHeight()/2 - 416/2 - 200);
	    logo.setVy(.25);
	    ctn.getChildren().add(logo);
	    
	    // Button zum erstellen einer neuen Lobby
	    btnCreate.setButtonMode(true);
	    btnCreate.relocate(ctn.getPrefWidth()/2 - 303/2, ctn.getPrefHeight() - 303 - 100);		
	    ctn.getChildren().add(btnTutorial);
	    
	    // Button zum Ansehen des Tutorials
	    btnTutorial.setButtonMode(true);
	    btnTutorial.relocate(btnCreate.getLayoutX() - 303 - 50, btnCreate.getLayoutY());	
	    ctn.getChildren().add(btnCreate);
	    
	    // Button zum beitreten einer bereits erstellten Lobby
	    btnJoin.setButtonMode(true);
	    btnJoin.relocate(btnCreate.getLayoutX() + 303 + 50, btnCreate.getLayoutY());
		ctn.getChildren().add(btnJoin);
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
	 * Der Button der eine neue Lobby erstellt
	 * 
	 * @return gibt den Button zum Lobby erstellen zur�ck
	 */
	public Sprite getBtnCreate() {
		return btnCreate;
	}
	
	/**
	 * Der Button der bestehende Lobbys auflistet
	 * 
	 * @return gibt den Button zum Lobby beitreten zur�ck
	 */
	public Sprite getBtnJoin() {
		return btnJoin;
	}

	/**
	 * Der Button das Tutorial auflistet
	 * 
	 * @return gibt den Button zum Tutorial beitreten zur�ck
	 */
	public Sprite getBtnTutorial() {
		return btnTutorial;
	}

	/**
	 * Methode, die fragt ob das Logo animiert werden soll
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	public boolean isLogoAnimated() {
		return isLogoAnimated;
	}

	/**
	 * Prozedur, die den true/false-Wert, ob das Logo animiert werden soll ver�ndert
	 * 
	 * @param isLogoAnimated boolean
	 */
	public void setLogoAnimated(boolean isLogoAnimated) {
		this.isLogoAnimated = isLogoAnimated;
	}
	
	/**
	 * Das Logo des Spiels im Hauptmen�
	 * 
	 * @return gibt das Logo-Objekt zur�ck
	 */
	public Sprite getLogo() {
		return logo;
	}
	
}