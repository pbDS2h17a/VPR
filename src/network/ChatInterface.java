package network;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.application.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ChatInterface extends Application
{
	static Connection con;
	static Statement stmt;
	static String localIP = "127.0.0.1";
	static ResultSet lastSet;

	public static void main(String[] args) throws ClassNotFoundException, SQLException 
	{
		try
		{
			localIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		
		con = DriverManager.getConnection(
					"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");
		stmt = con.createStatement();

		launch(args);

	}

	@Override
	public void start(Stage arg0) throws Exception
	{
		BorderPane bp = new BorderPane();
		VBox chatVerlauf = new VBox();
		chatVerlauf.setPrefSize(100, 200);
		TextField tf = new TextField();
		Button send = new Button("Send");
		HBox hb = new HBox();
		
		send.setOnAction(a -> {
			try { stmt.executeUpdate("INSERT into chat(timestamp, nachricht, fromIP, toIP)"
					+ "			VALUES("+System.currentTimeMillis()%3600000+", '"+ tf.getText() +"', '"+ localIP +"', '%');");
			}
			catch(SQLException s) {s.printStackTrace();}
			tf.setText("");
		});
		
		bp.setTop(chatVerlauf);
		hb.getChildren().add(tf);
		hb.getChildren().add(send);
		bp.setBottom(hb);
		
		Task<Void> task = new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				while(true)
				{
					ResultSet r = stmt.executeQuery("SELECT fromIP,nachricht FROM chat"
							+ 						" WHERE '"+localIP+"' LIKE toIP"
							+	 					" AND "+(System.currentTimeMillis()-2000)%3600000+" < timestamp;");
					ArrayList<String> currentSet = new ArrayList<>();
					
					Platform.runLater(() -> {
						try {while(r.next()) {
							chatVerlauf.getChildren().add(new Label(r.getString(1) + ": " + r.getString(2)));
						}}
						catch(SQLException s) {s.printStackTrace();}
					});
					Thread.sleep(2000);
				}
				
			}
			
		};
		Thread readThread = new Thread(task);
		readThread.setDaemon(true);
		readThread.start();
	
		
		Scene sc= new Scene(bp);
		arg0.setScene(sc);
		arg0.show();
	}

}