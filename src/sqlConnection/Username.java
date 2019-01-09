package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sqlCreation.SqlQuery;

/**
 * @author Lea-Marie Mönikes
 * Überprüfung des Usernames
 */

public class Username extends Application{
	
	
	//als Test für die GUI, kann nach einbindung gelöscht werden, dient nur zu Testzwecken
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setWidth(300);
		stage.setHeight(300);
		
		Pane pane = new Pane();
		Scene scene = new Scene(pane);
		
		TextField t1 = new TextField();
		TextField t2 = new TextField();
		t1.relocate(0,10);
		t2.relocate(0, 40);
		
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		SqlHelper.setStatement(SqlHelper.loginStringArray);
		Statement stmt = SqlHelper.stmt;
	
		Button b1 = new Button("Username");
		b1.relocate(30, 100);
		pane.getChildren().add(b1);
		pane.getChildren().add(t1);
		pane.getChildren().add(t2);
	
		b1.setOnAction(a ->{
			String username = t1.getText();
			String color = t2.getText();
			try
			{
				checkUsername(stmt,username,color);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	
		stage.setScene(scene);
		
		stage.show();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		launch(args);

	}
	
	
	public void checkUsername(Statement stmt, String username, String color) throws ClassNotFoundException, SQLException{
		
		
		ArrayList <String> playerNames = new ArrayList<>();
        
        String tableNames = "SELECT * FROM player"; 
		ResultSet rs = stmt.executeQuery(tableNames);
           
        // Alle Spielernamen werden aus der Tabelle geholt
        while (rs.next()) {   	
            playerNames.add(rs.getString("name")); 
        }
       
        //Ueberprufung ob der Name schon in der DB existiert
        boolean isOk = true;
        
        if(playerNames.size()> 0){
        
	        for (int i = 0; i < playerNames.size(); i++)
			{
	        	if(playerNames.get(i).equals(username)){
	            	isOk = false;
	            	break;
	            }
			}
        }
        //TODO hier fehlt noch die übergebene Farbe, LobbyID?,IP?
        
        //Wenn ok, Eintrag in die Datenbank
        if(isOk || playerNames.isEmpty()){
        	Player player = new Player(playerNames.size()+1,username,"blau"); 
        	
        	SqlQuery.fillPlayer(player); 
        	
    	}
        
        for (int i = 0; i < playerNames.size(); i++)
		{
        	System.out.println(playerNames.indexOf(i));
		}
        
	
	}

}
