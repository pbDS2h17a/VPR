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
 * Stellt die grafische Benutzeroberfl�che f�r den Chat bereit.
 * @author Erik Schauml�ffel
 * @author PeRoScKu
 */
public class ChatInterface{
	
	/**
	 * Fensterbreite
	 */
	private final static int PANE_WIDTH = 300;
	/**
	 * Fensterh�he
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
	 * Zeitstempel, f�r die Zeit, bei der der Spieler dem Chat beigetreten ist. 
	 */
	private long timestamp; 
	/**
	 * GUI-Element
	 */
	private BorderPane bp;
	/**
	 * Nachrichteneingabefeld
	 */
	private TextField tf;
	/**
	 * Absendebutton
	 */
	private Button send;
	/**
	 * Zur�cksetzen des Chatfensterinhalts
	 */
	private Button reset;
	/**
	 * GUI-Element
	 */
	private HBox hb;
	/**
	 * Chatverlauf
	 */
	private VBox chatHistory;
	/**
	 * GUI-Element
	 */
	private AnchorPane anchor;
	/**
	 * GUI-Element
	 */
	private ScrollPane scrollWindow;
	/**
	 * Zeichenz�hler
	 */
	private Label charCount;
	private HBox charCountContainer;
	private Region fill;
	
	/**
	 * Erstellt ein Chatfenster
	 * @param player Am Chat teilnehmender Spieler.
	 */
	public ChatInterface(Player player) {
		this.player = player;
		this.cm = new ChatManager(this.player.getLobbyId(), this.player.getPlayerId());
		this.timestamp = cm.getTimestamp();
		
		this.bp = new BorderPane();
		this.tf = new TextField();
		this.send = new Button(">");
		this.reset = new Button("X");
		this.hb = new HBox();
		this.chatHistory = new VBox();
		this.anchor = new AnchorPane();
		this.scrollWindow = new ScrollPane(this.anchor);
		this.fill = new Region();
		this.charCount = new Label("0/255");
		this.charCountContainer = new HBox();
		
		this.hb.getChildren().add(this.tf);
		this.hb.getChildren().add(this.send);
		this.hb.getChildren().add(this.reset);
		this.anchor.getChildren().add(this.chatHistory);
		this.charCountContainer.getChildren().add(this.fill);
		this.charCountContainer.getChildren().add(this.charCount);
		this.chatHistory.getChildren().add(this.charCountContainer);
		this.bp.setBottom(this.hb);
		
		//Gr��e und Verhalten der Nodes
		this.hb.setPrefHeight(40);
		this.tf.setPrefHeight(this.hb.getPrefHeight());
		this.send.setMinHeight(40);
		this.reset.setMinHeight(40);
		this.tf.setPromptText("Nachricht eingeben...");
		HBox.setHgrow(this.tf, Priority.ALWAYS);
		HBox.setHgrow(this.fill, Priority.ALWAYS);
		this.scrollWindow.setPrefSize(PANE_WIDTH,PANE_HEIGHT);
		this.scrollWindow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		this.anchor.setMinSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
		AnchorPane.setBottomAnchor(this.chatHistory, 5.0);
		this.bp.setTop(this.scrollWindow);
		this.send.setMinWidth(80);
		this.charCountContainer.setMinWidth(this.scrollWindow.getPrefWidth());
		
		//CSS-Klassen
		this.bp.getStyleClass().add("chat");
		this.hb.getStyleClass().add("chat-controls");
		this.scrollWindow.getStyleClass().add("scroll-window");
		this.chatHistory.getStyleClass().add("chat-history");
		this.send.getStyleClass().add("chat-button");
		this.send.getStyleClass().add("chat-send-button");
		this.reset.getStyleClass().add("chat-button");
		this.reset.getStyleClass().add("chat-reset-button");
		this.tf.getStyleClass().add("chat-textfield");
		this.anchor.getStyleClass().add("chat-anchor"); 
		this.charCountContainer.getStyleClass().add("chat-char-count-container");
		this.charCount.getStyleClass().add("chat-char-count");
		
		//Schriftarten und -farben
		Font cons20 = Font.font("Console", FontWeight.BOLD, 20);
		this.send.setFont(cons20);
		this.send.setTextFill(Color.WHITE);
		this.reset.setFont(cons20);
		this.reset.setTextFill(Color.WHITE);
		this.charCount.setFont(Font.font("Console", 14));
		this.charCount.setTextFill(new Color(0.8,0.8,0.8,0.6));
		
		// Eventhandler
		this.send.setOnAction(a -> send());
		this.reset.setOnAction(a -> reset());
		this.tf.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send();
		});
		this.tf.setOnKeyReleased(k -> {
			this.charCount.setText(String.format("%d/255", tf.getText().length()));
			if(tf.getText().length() > 255) {
				this.tf.setText(this.tf.getText().substring(0,255));
			}
		});
		//Binding des Scroll-Status der ScrollPane an die H�he der vBox, die darin liegt
		// -> Automatisches Scrollen
		scrollWindow.vvalueProperty().bind(chatHistory.heightProperty());
				
		// neuen Thread starten
		Thread readThread = new Thread(getUpdateTask());
		readThread.setDaemon(true);
		readThread.start();
	}


	/**
	 * Schickt die eingegebene Nachricht ab.
	 * @param tf Inhalt des Textfeldes
	 */
	private void send() {
		if(this.tf.getText() == "") return;
		try { 
			cm.sendMessage(this.tf.getText());
		} catch(SQLException s) {
				s.printStackTrace();
			}
		this.tf.setText("");
		this.charCount.setText("0/255");
	}
	
	/**
	 * Chatfensterinhalt l�schen.
	 */
	private void reset() {
		timestamp = cm.getTimestamp();
		chatHistory.getChildren().clear();
		this.chatHistory.setPrefSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
	}
	
	/**
	 * Chatfensterpane holen
	 * @return Chatfenster
	 */
	public BorderPane getPane() {
		return this.bp;
	}
	
	/**
	 * Zweiter Task, der im gegebenen Zeitintervall die Datenbank nach neuen Eintr�gen abfragt und den Chat aktualisiert.
	 * @return Diesen Task
	 */
	public Task<Void> getUpdateTask() {
		// Task anlegen, der vom neuen Thread alle 2s ausgef�hrt werden soll
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
		// Alte Eintr�ge l�schen
		this.chatHistory.getChildren().clear();
		// Gesamtes Abfrageergebnis in GUI/Console schreiben
		for (List<String> message : chat) {
			HBox textHb = new HBox();
			Text text = new Text(this.cm.formatMessage(message));
			textHb.getStyleClass().add("chat-message-container");
			textHb.setBorder(new Border(new BorderStroke(Color.web(this.player.getColorValue()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0, 0.0, 0.0, 5.0), Insets.EMPTY)));
			text.getStyleClass().add("chat-message");
			
			text.wrappingWidthProperty().set(bp.getPrefWidth());
			text.setFill(Color.WHITE);
			textHb.setMinWidth(this.scrollWindow.getPrefWidth());
			textHb.getChildren().add(text);
			this.chatHistory.getChildren().add(textHb);
			textHb.setAlignment(Pos.BOTTOM_LEFT);
		}
		this.chatHistory.getChildren().add(this.charCountContainer);
	}
}
