package network;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import sqlConnection.Player;

/**
 * Stellt die grafische Benutzeroberfläche für den Chat bereit.
 * @author Erik Schaumlöffel
 * @author PeRoScKu
 */
public class ChatInterface{
	
	/**
	 * Fensterbreite
	 */
	private final static int PANE_WIDTH = 300;
	/**
	 * Fensterhöhe
	 */
	private final static int PANE_HEIGHT = 500;
	/**
	 * Chatteilnehmer
	 */
	private Player player;
	/**
	 * Verwendeter Chat
	 * @see {@link network.ChatManager}
	 */
	private ChatManager cm;
	/**
	 * Zeitstempel, für die Zeit, bei der der Spieler dem Chat beigetreten ist. 
	 */
	private long timestamp; 
	/**
	 * Oberste Pane
	 */
	private BorderPane chatPane;
	/**
	 * Nachrichteneingabefeld
	 */
	private TextField inputField;
	/**
	 * Absendebutton
	 */
	private Button sendButton;
	/**
	 * Zurücksetzen des Chatfensterinhalts
	 */
	private Button resetButton;
	/**
	 * HBox, die alle Kontrollelemente enthält
	 */
	private HBox controlBar;
	/**
	 * VBox, die den Chatverlauf enthält (jede Nachricht in einer HBox)
	 */
	private VBox chatHistory;
	/**
	 * AnchorPane zum Verankern der ScrollPane am unteren Rand
	 */
	private AnchorPane anchor;
	/**
	 * ScrollPane, die den ChatInhalt darstellt
	 */
	private ScrollPane scrollWindow;
	/**
	 * Zeichenzähler
	 */
	private Label charCount;
	/**
	 * HBox, die das charCount-Label,
	 * sowie eine Region fill mit HGrow-Attribut enthält.
	 */
	private HBox charCountContainer;
	/**
	 * Schiebt das Label in der HBox nach rechts
	 */
	private Region fill;
	
	/**
	 * Erstellt ein Chatfenster
	 * @param player Am Chat teilnehmender Spieler.
	 */
	public ChatInterface(Player player) {
		this.player = player;
		this.cm = new ChatManager(this.player.getLobbyId(), this.player.getPlayerId());
		this.timestamp = cm.getTimestamp();
		
		this.chatPane = new BorderPane();
		this.inputField = new TextField();
		this.sendButton = new Button(">");
		this.resetButton = new Button("X");
		this.controlBar = new HBox();
		this.chatHistory = new VBox();
		this.anchor = new AnchorPane();
		this.scrollWindow = new ScrollPane(this.anchor);
		this.fill = new Region();
		this.charCount = new Label("0/255");
		this.charCountContainer = new HBox();
		
		this.controlBar.getChildren().add(this.inputField);
		this.controlBar.getChildren().add(this.sendButton);
		this.controlBar.getChildren().add(this.resetButton);
		this.anchor.getChildren().add(this.chatHistory);
		this.charCountContainer.getChildren().add(this.fill);
		this.charCountContainer.getChildren().add(this.charCount);
		this.chatHistory.getChildren().add(this.charCountContainer);
		this.chatPane.setBottom(this.controlBar);
		
		//Größe und Verhalten der Nodes
		this.controlBar.setPrefHeight(40);
		this.inputField.setPrefHeight(this.controlBar.getPrefHeight());
		this.sendButton.setMinHeight(40);
		this.resetButton.setMinHeight(40);
		this.inputField.setPromptText("Nachricht eingeben...");
		HBox.setHgrow(this.inputField, Priority.ALWAYS);
		HBox.setHgrow(this.fill, Priority.ALWAYS);
		this.scrollWindow.setPrefSize(PANE_WIDTH,PANE_HEIGHT);
		this.scrollWindow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		this.anchor.setMinSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
		AnchorPane.setBottomAnchor(this.chatHistory, 5.0);
		this.chatPane.setTop(this.scrollWindow);
		this.sendButton.setMinWidth(80);
		this.charCountContainer.setMinWidth(this.scrollWindow.getPrefWidth());
		
		//CSS-Klassen
		this.chatPane.getStyleClass().add("chat");
		this.controlBar.getStyleClass().add("chat-controls");
		this.scrollWindow.getStyleClass().add("scroll-window");
		this.chatHistory.getStyleClass().add("chat-history");
		this.sendButton.getStyleClass().add("chat-button");
		this.sendButton.getStyleClass().add("chat-send-button");
		this.resetButton.getStyleClass().add("chat-button");
		this.resetButton.getStyleClass().add("chat-reset-button");
		this.inputField.getStyleClass().add("chat-textfield");
		this.anchor.getStyleClass().add("chat-anchor"); 
		this.charCountContainer.getStyleClass().add("chat-char-count-container");
		this.charCount.getStyleClass().add("chat-char-count");
		
		//Schriftarten und -farben
		Font cons20 = Font.font("Console", FontWeight.BOLD, 20);
		this.sendButton.setFont(cons20);
		this.sendButton.setTextFill(Color.WHITE);
		this.resetButton.setFont(cons20);
		this.resetButton.setTextFill(Color.WHITE);
		this.charCount.setFont(Font.font("Console", 14));
		this.charCount.setTextFill(new Color(0.8,0.8,0.8,0.6));
		
		// Eventhandler
		this.sendButton.setOnAction(a -> send());
		this.resetButton.setOnAction(a -> reset());
		this.inputField.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send();
		});
		this.inputField.setOnKeyReleased(k -> {
			this.charCount.setText(String.format("%d/255", inputField.getText().length()));
			if(inputField.getText().length() > 255) {
				this.inputField.setText(this.inputField.getText().substring(0,255));
			}
		});
		//Binding des Scroll-Status der ScrollPane an die Höhe der vBox, die darin liegt
		// -> Automatisches Scrollen
		scrollWindow.vvalueProperty().bind(chatHistory.heightProperty());
				
		// neuen Thread starten
		Thread readThread = new Thread(getUpdateTask());
		readThread.setDaemon(true);
		readThread.start();
	}


	/**
	 * Schickt die eingegebene Nachricht ab.
	 * @param inputField Inhalt des Textfeldes
	 */
	private void send() {
		if(this.inputField.getText() == "") return;
		try { 
			cm.sendMessage(this.inputField.getText());
		} catch(SQLException s) {
				s.printStackTrace();
			}
		this.inputField.setText("");
		this.charCount.setText("0/255");
	}
	
	/**
	 * Chatfensterinhalt löschen.
	 */
	private void reset() {
		this.timestamp = this.cm.getTimestamp();
		//Liste nicht ganz leeren, damit charCount vorhanden bleibt
		this.chatHistory.getChildren().remove(0, this.chatHistory.getChildren().size()-1);
		//this.chatHistory.setPrefSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
	}
	
	/**
	 * Chatfensterpane holen
	 * @return Chatfenster
	 */
	public BorderPane getPane() {
		return this.chatPane;
	}
	
	/**
	 * Zweiter Task, der im gegebenen Zeitintervall die Datenbank nach neuen Einträgen abfragt und den Chat aktualisiert.
	 * @return Diesen Task
	 */
	public Task<Void> getUpdateTask() {
		// Task anlegen, der vom neuen Thread alle 2s ausgeführt werden soll
		Task<Void> task = new Task<Void>() {
			@Override
			// "The call method actually performs the background thread logic."
			protected Void call() throws Exception {
				while(true) {
					// Ergebnisse der Query im ResultSet speichern
					List<List<String>> set =  cm.getChatHistory(timestamp);
					// Wenn Ergebnis != null: Ergebnisse ausgeben
					if(set != null) {
						Platform.runLater(() -> update(set));
					}
					// Zwei Sekunden warten -> Abfrage der Datenbank erfolgt alle zwei Sekunden
					Thread.sleep(500);
				}
			}
		};
		return task;
	}
	
	/**
	 * Aktualisieren des Chatfensters
	 * @param chat Aktueller Chatverlauf
	 */
	private void update(List<List<String>> chat) {
		// Alte Einträge löschen
		this.chatHistory.getChildren().clear();
		// Gesamtes Abfrageergebnis in GUI/Console schreiben
		for (List<String> message : chat) {
			HBox textHb = new HBox();
			Text text = new Text(this.cm.formatMessage(message));
			textHb.getStyleClass().add("chat-message-container");
			textHb.setBorder( 
					new Border(new BorderStroke(Color.web(
							this.cm.getMessagePlayerColor(message) == null ? Player.DEFAULT_COLOR : cm.getMessagePlayerColor(message)),
							BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0, 0.0, 0.0, 5.0), Insets.EMPTY)));
			text.getStyleClass().add("chat-message");
			
			text.wrappingWidthProperty().set(chatPane.getPrefWidth());
			text.setFill(Color.WHITE);
			textHb.setMinWidth(this.scrollWindow.getPrefWidth());
			textHb.getChildren().add(text);
			this.chatHistory.getChildren().add(textHb);
			textHb.setAlignment(Pos.BOTTOM_LEFT);
		}
		this.chatHistory.getChildren().add(this.charCountContainer);
	}
}
