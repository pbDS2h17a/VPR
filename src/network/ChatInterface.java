package network;

import java.sql.SQLException;
import java.util.List;

import com.sun.prism.paint.Color;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sqlConnection.SqlHelper;


public class ChatInterface{
	private final static int PANE_WIDTH = 400;
	private final static int PANE_HEIGHT = 400;
	private int pid;
	private int lid;
	private ChatManager cm;
	private long timestamp; 
	private BorderPane bp;
	private TextField tf;
	private Button send;
	private Button reset;
	private HBox hb;
	private VBox chatHistory;
	private ScrollPane scrollWindow;
	
	public ChatInterface(int player_id, int lobby_id) {
		this.pid = player_id;
		this.lid = lobby_id;
		this.cm = new ChatManager(this.lid, this.pid);
		this.timestamp = 0;//cm.getTimestamp();
		
		this.bp = new BorderPane();
		this.tf = new TextField();
		this.send = new Button("Senden");
		this.reset = new Button("Reset");
		this.hb = new HBox();
		
		this.hb.getChildren().add(this.tf);
		this.hb.getChildren().add(this.send);
		this.hb.getChildren().add(this.reset);
		hb.setHgrow(tf, Priority.ALWAYS);
		this.bp.setBottom(this.hb);
		this.chatHistory = new VBox();
		this.scrollWindow = new ScrollPane(this.chatHistory);
		
		this.scrollWindow.setPrefSize(PANE_WIDTH,PANE_HEIGHT);
		this.chatHistory.setPrefSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
		this.bp.setTop(this.scrollWindow);
		
		//CSS-Klassen
		this.bp.getStyleClass().add("chat");
		this.hb.getStyleClass().add("chat-controls");
		this.scrollWindow.getStyleClass().add("scroll-window");
		this.chatHistory.getStyleClass().add("chat-history");
		this.send.getStyleClass().add("chat-button");
		this.reset.getStyleClass().add("chat-button");
		this.tf.getStyleClass().add("chat-textfield");
		
		// Eventhandler
		this.send.setOnAction(a -> send(this.tf));
		this.reset.setOnAction(a -> reset());
		this.tf.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send(this.tf);
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
	 * Sends the input text of the specified TextField to the database. 
	 * @param tf	TextField which content shall be send to the database.
	 */
	private void send(TextField tf) {
		if(tf.getText() == "") return;
		try { 
			cm.sendMessage(tf.getText(), pid, lid);
		} catch(SQLException s) {
				s.printStackTrace();
			}
		tf.setText("");
	}
	
//	/**
//	 * Deletes all entries of the specified table and sets the specified TextField = "".
//	 * @param tableName	Name of the table to truncate
//	 * @param tf				TextField to set = ""
//	 */
//	private void deleteAll (String tableName, TextField tf) {
//		SqlHelper.clearTable(tableName);
//		tf.setText("");
//	}
	
	
	private void reset() {
		timestamp = cm.getTimestamp();
		chatHistory.getChildren().clear();
		this.chatHistory.setPrefSize(this.scrollWindow.getPrefWidth(), this.scrollWindow.getPrefHeight());
	}
	
	public BorderPane getPane() {
		return this.bp;
	}
	
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
					Thread.sleep(2000);
				}
			}
		};
		return task;
				
	}
	
	private void update(List<List<String>> chat) {
		// Alte Einträge löschen
		this.chatHistory.getChildren().clear();
		// Gesamtes Abfrageergebnis in GUI/Console schreiben
		for (List<String> message : chat) {
			this.chatHistory.getChildren().add(new Label( this.cm.formatMessage(message)));
		}
	}
}
