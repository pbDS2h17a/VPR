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
import javafx.scene.input.KeyCode;
import javafx.scene.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
		TextField tf = new TextField();
		Button send = new Button("Send");
		HBox hb = new HBox();
		
		send.setOnAction(a -> send(tf));
		tf.setOnKeyPressed(k -> {
			if(k.getCode() == KeyCode.ENTER) send(tf);
		});
		
		
		hb.getChildren().add(tf);
		hb.getChildren().add(send);
		bp.setBottom(hb);
		
		VBox vbWindow = new VBox();
		
		Task<Void> task = new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{				
				while(true)
				{
					ResultSet r = stmt.executeQuery("SELECT fromIP,message FROM chat;");
//							+ 						" WHERE '"+localIP+"' LIKE toIP"
//							+	 					" AND "+(System.currentTimeMillis()-2000)%3600000+" < timestamp;");
					
					List<List<String>> set = ResultSetManager.toList(r);
					
					if(set != null){
						Platform.runLater(() -> {
							
							for (int i = vbWindow.getChildren().size()-1; i >= 0; i--)
							{
								vbWindow.getChildren().remove(i);
							}
							
							for (List<String> list : set)
							{
								vbWindow.getChildren().add(new Label(list.get(0) + ": " + list.get(1)));
							}
						});
					}
					
					Thread.sleep(2000);
				}
				
			}
			
		};
		Thread readThread = new Thread(task);
		readThread.setDaemon(true);
		readThread.start();
	
		ScrollPane chatHistory = new ScrollPane(vbWindow);
		chatHistory.setPrefSize(200,400);
		bp.setTop(chatHistory);
		
		Scene sc= new Scene(bp);
		arg0.setScene(sc);
		arg0.show();
	}

	private void send(TextField tf){
		try { stmt.executeUpdate("INSERT into chat(timestamp, message, fromIP, toIP)"
				+ "			VALUES("+System.currentTimeMillis()%3600000+", '"+ tf.getText() +"', '"+ localIP +"', '%');");
		}
		catch(SQLException s) {s.printStackTrace();}
		tf.setText("");
	}
}
