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

public class Username extends Application{
	
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setWidth(300);
		stage.setHeight(300);
		
		Pane pane = new Pane();
		Scene scene = new Scene(pane);
		
		TextField t1 = new TextField();
		t1.relocate(0,10);
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		Connection con = DriverManager.getConnection(  
				"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");  
		Statement stmt = con.createStatement();
		
		Button b1 = new Button("Username");
		b1.relocate(30, 50);
		pane.getChildren().add(b1);
		pane.getChildren().add(t1);
	
		b1.setOnAction(a ->{
			String username = t1.getText();
			try
			{
				checkUsername(stmt,username);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	
		stage.setScene(scene);
		
		
		stage.show();
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		launch(args);
		
		/*
		String[] arr = new String[5];
		
		String sql = "SELECT * FROM continent";
		ResultSet r = stmt.executeQuery(sql);
            
		int counter = 0;
        // loop through the result set
        while (r.next()) {   	
            arr[counter] = r.getString("continent_name");
            counter++;
        }
        
        for (String string : arr)
		{
			System.out.println(string);
		}
          */ 

	}
	
	//LEA-MARIE MOENIKES
	
	public void checkUsername(Statement stmt, String username) throws ClassNotFoundException, SQLException{
		ArrayList <String> playerNames = new ArrayList<>();
        
        String sql2 = "SELECT * FROM continent";
		ResultSet rs = stmt.executeQuery(sql2);
           
        // geht durch die tabelle und holt alle Kontinenten-Namen
        while (rs.next()) {   	
            playerNames.add(rs.getString("continent_name"));
        }
        
        System.out.println(playerNames.toString());
       
        //ueberprufung ob der name schon in der DB existiert
        boolean isOk = true;
        
        for (int i = 0; i < playerNames.size(); i++)
		{
        	if(playerNames.get(i).equals(username)){
            	isOk = false;
            	break;
            }
		}
  
        //zusätzliche loeschen
        String insert2 = "DELETE FROM continent WHERE continent_name = 'LeaTest' OR continent_name = 'TestLea' OR continent_name ='LeaTestet'";
        
        //wenn der name ok ist, wird er in die DB geschoben und als Player gesetzt
        if(isOk){
	    	String insert = "INSERT INTO continent (continent_id, continent_name, bonus)" +
			"VALUES ("+playerNames.size()+", '"+username+"', 34);";
	    	stmt.executeUpdate(insert);
	    	Player p1 = new Player(username,"blue",3); // hier muss farbe und die einheiten noch eingebunden werden
	    	System.out.println("ok");
    	}
	
	}

}
