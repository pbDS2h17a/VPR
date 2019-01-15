package network;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sqlConnection.SqlHelper;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class ChatInterface extends Application {

	static String myIP = "127.0.0.1";
	static int myID = 0;
	static int lobbyID = 0;
	static ChatManager cm;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		cm = new ChatManager(myIP, myID, lobbyID);
		// GUI launchen
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws SQLException {
		BorderPane bp = new BorderPane();
		TextField tf = new TextField();
		Button send = new Button("Senden");
		HBox hb = new HBox();
		
		// Nachricht abschicken (action)
		send.setOnAction(a -> send(tf));
		tf.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send(tf);
		});
		
		hb.getChildren().add(tf);
		hb.getChildren().add(send);
		bp.setBottom(hb);
		VBox vbWindow = new VBox();
		
		// Zweiter Thread für Datenbankabfragen
		Task<Void> task = new Task<Void>() {
			@Override
			// "The call method actually performs the background thread logic."
			protected Void call() throws Exception {
				while(true) {
					// Ergebnisse der Query im ResultSet speichern
					
					List<List<String>> set = cm.getChatHistory(0);
					// Wenn Ergebnis != Null: Ergebnisse ausgeben
					if(set != null) {
						Platform.runLater(() -> {
							// Alte Einträge löschen
							for (int i = vbWindow.getChildren().size()-1; i >= 0; i--) {
								vbWindow.getChildren().remove(i);
							}
							System.out.println("test");
							// Gesamtes Abfrageergebnis in GUI/Console schreiben
							for (List<String> list : set) {
//								System.out.println(list);
								vbWindow.getChildren().add(new Label(list.get(0) + " [" 
										+ list.get(1).substring(0,5) + "]" + ": " + list.get(2)));
							}
						});
					}
					// Zwei Sekunden warten -> Abfrage der Datenbank erfolgt alle zwei Sekunden
					Thread.sleep(2000);
				}
			}
		};
		// Thread starten
		Thread readThread = new Thread(task);
		readThread.setDaemon(true);
		readThread.start();
		// GUI Stuff
		ScrollPane chatHistory = new ScrollPane(vbWindow);
		chatHistory.setPrefSize(200,400);
		bp.setTop(chatHistory);
		Scene sc= new Scene(bp);
		arg0.setScene(sc);
		arg0.show();
	}

	/**
	 * Sends the input text of the specified TextField to the database. 
	 * @param tf	TextField which content shall be send to the database.
	 */
	private void send(TextField tf) {
		if (tf.getText().toLowerCase().equals("reset")) {
			deleteAll("chat", tf);
			System.out.println("test 2");
		}
		else {
			LocalDateTime now = LocalDateTime.now();
			String date = String.format("%2d:%2d:%2d", now.getHour(), now.getMinute(), now.getSecond());
			try { 
				stmt.executeUpdate("INSERT into chat(timestamp, message, from_ip, to_ip)"
				+ " VALUES('"+date+"', '"+ tf.getText() +"', '"+ localIP +"', '%');");
			} catch(SQLException s) {
					s.printStackTrace();
				}
			tf.setText("");
		}
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
}
