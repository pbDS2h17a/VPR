package network;

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
	private final Player player;
	/**
	 * Verwendeter Chat
	 * @see {@link network.ChatManager}
	 */
	private final ChatManager cm;
	/**
	 * Zeitstempel, für die Zeit, bei der der Spieler dem Chat beigetreten ist. 
	 */
	private long timestamp; 
	/**
	 * GUI-Element
	 */
	private final BorderPane bp;
	/**
	 * Nachrichteneingabefeld
	 */
	private final TextField tf;
	/**
	 * Chatverlauf
	 */
	private final VBox chatHistory;
	/**
	 * GUI-Element
	 */
	private final ScrollPane scrollWindow;
	/**
	 * Zeichenzähler
	 */
	private final Label charCount;
	private final HBox charCountContainer;

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
		/**
		 * Absendebutton
		 */
		Button send = new Button(">");
		/**
		 * Zurücksetzen des Chatfensterinhalts
		 */
		Button reset = new Button("X");
		/**
		 * GUI-Element
		 */
		HBox hb = new HBox();
		this.chatHistory = new VBox();
		/**
		 * GUI-Element
		 */
		AnchorPane anchor = new AnchorPane();
		this.scrollWindow = new ScrollPane(anchor);
		Region fill = new Region();
		this.charCount = new Label("0/255");
		this.charCountContainer = new HBox();
		
		hb.getChildren().add(this.tf);
		hb.getChildren().add(send);
		hb.getChildren().add(reset);
		anchor.getChildren().add(this.chatHistory);
		this.charCountContainer.getChildren().add(fill);
		this.charCountContainer.getChildren().add(this.charCount);
		this.chatHistory.getChildren().add(this.charCountContainer);
		this.bp.setBottom(hb);
		
		//Größe und Verhalten der Nodes
		hb.setPrefHeight(40);
		this.tf.setPrefHeight(hb.getPrefHeight());
		send.setMinHeight(40);
		reset.setMinHeight(40);
		this.tf.setPromptText("Nachricht eingeben...");
		HBox.setHgrow(this.tf, Priority.ALWAYS);
		HBox.setHgrow(fill, Priority.ALWAYS);
		this.scrollWindow.setPrefSize(PANE_WIDTH,PANE_HEIGHT);
		this.scrollWindow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		anchor.setMinSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
		AnchorPane.setBottomAnchor(this.chatHistory, 5.0);
		this.bp.setTop(this.scrollWindow);
		send.setMinWidth(80);
		this.charCountContainer.setMinWidth(this.scrollWindow.getPrefWidth());
		
		//CSS-Klassen
		this.bp.getStyleClass().add("chat");
		hb.getStyleClass().add("chat-controls");
		this.scrollWindow.getStyleClass().add("scroll-window");
		this.chatHistory.getStyleClass().add("chat-history");
		send.getStyleClass().add("chat-button");
		send.getStyleClass().add("chat-send-button");
		reset.getStyleClass().add("chat-button");
		reset.getStyleClass().add("chat-reset-button");
		this.tf.getStyleClass().add("chat-textfield");
		anchor.getStyleClass().add("chat-anchor");
		this.charCountContainer.getStyleClass().add("chat-char-count-container");
		this.charCount.getStyleClass().add("chat-char-count");
		
		//Schriftarten und -farben
		Font cons20 = Font.font("Console", FontWeight.BOLD, 20);
		send.setFont(cons20);
		send.setTextFill(Color.WHITE);
		reset.setFont(cons20);
		reset.setTextFill(Color.WHITE);
		this.charCount.setFont(Font.font("Console", 14));
		this.charCount.setTextFill(new Color(0.8,0.8,0.8,0.6));
		
		// Eventhandler
		send.setOnAction(a -> send());
		reset.setOnAction(a -> reset());
		this.tf.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send();
		});
		this.tf.setOnKeyReleased(k -> {
			this.charCount.setText(String.format("%d/255", tf.getText().length()));
			if(tf.getText().length() > 255) {
				this.tf.setText(this.tf.getText().substring(0,255));
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
	 */
	private void send() {
		if(this.tf.getText().equals("")) {
			return;
		}

		cm.sendMessage(this.tf.getText());

		this.tf.setText("");
		this.charCount.setText("0/255");
	}
	
	/**
	 * Chatfensterinhalt löschen.
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
	 * Zweiter Task, der im gegebenen Zeitintervall die Datenbank nach neuen Einträgen abfragt und den Chat aktualisiert.
	 * @return Diesen Task
	 */
	private Task<Void> getUpdateTask() {
		// Task anlegen, der vom neuen Thread alle 2s ausgeführt werden soll
		return new Task<Void>() {
			@Override
			// "The call method actually performs the background thread logic."
			protected Void call() throws Exception {
				//noinspection InfiniteLoopStatement
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
