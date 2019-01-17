package network;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	private VBox vbWindow;
	private ScrollPane chatHistory;
	private static boolean isScrolledToBottom = true;
	
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
		
		// Eventhandler
		this.send.setOnAction(a -> send(this.tf));
		this.reset.setOnAction(a -> reset());
		this.tf.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send(this.tf);
		});
		this.chatHistory.setOnScroll(s -> readScrollState(this.chatHistory));
		
		this.hb.getChildren().add(this.tf);
		this.hb.getChildren().add(this.send);
		this.hb.getChildren().add(this.reset);
		this.bp.setBottom(this.hb);
		this.vbWindow = new VBox();
		this.chatHistory = new ScrollPane(this.vbWindow);
		
		this.chatHistory.setPrefSize(PANE_WIDTH,PANE_HEIGHT);
		this.vbWindow.setPrefSize(this.chatHistory.getPrefWidth(), this.chatHistory.getPrefHeight());
		setStyle();
		this.bp.setTop(this.chatHistory);
		
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
		try { 
			cm.sendMessage(tf.getText(), pid, lid);
		} catch(SQLException s) {
				s.printStackTrace();
			}
		tf.setText("");
	}
	
	/**
	 * Deletes all entries of the specified table and sets the specified TextField = "".
	 * @param tableName	Name of the table to truncate
	 * @param tf				TextField to set = ""
	 */
	private void deleteAll (String tableName, TextField tf) {
		SqlHelper.clearTable(tableName);
		tf.setText("");
	}
	
	
	private void reset() {
		timestamp = cm.getTimestamp();
	}
	
	public BorderPane getPane() {
		return this.bp;
	}
	
	public Task<Void> getUpdateTask() {
		//
		//
		//	Update in cm auslagern
		//	Oder: Klasse, die vBox extends erstellen
		//
		//
		// Zweiter Thread für Datenbankabfragen
				Task<Void> task = new Task<Void>() {
					@Override
					// "The call method actually performs the background thread logic."
					protected Void call() throws Exception {
						while(true) {
							// Ergebnisse der Query im ResultSet speichern
							System.out.println(timestamp);
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
		for (int i = this.vbWindow.getChildren().size()-1; i >= 0; i--) {
			this.vbWindow.getChildren().remove(i);
		}
		// Gesamtes Abfrageergebnis in GUI/Console schreiben
		for (List<String> message : chat) {
			this.vbWindow.getChildren().add(new Label( this.cm.formatMessage(message)));
		}
		//nach unten scrollen (1.0 = 100% bottom)
		if(this.isScrolledToBottom) {
			this.chatHistory.setVvalue(1.0);
		}
	}
	
	private void readScrollState(ScrollPane sp) {
		if (sp.getVvalue() == 1.0) {
			this.isScrolledToBottom = true;
		}
		else {
			this.isScrolledToBottom = false;
		}
	}
	
	private void setStyle() {
		String lightgreen = "-fx-background-color: rgba(0,146,69,.5);";//#009245
		String darkgreen = "-fx-background-color: rgba(0,129,55,1);";// #008137
		String whiteBorder = "-fx-border: 2px solid white;";
		this.chatHistory.setStyle(lightgreen);
		this.vbWindow.setStyle(lightgreen);
//		this.bp.setStyle(darkgreen);
//		this.hb.setStyle(lightgreen);
	}
}
