package sqlConnection;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Lea-Marie Mönikes
 * Überprüfung des Usernames
 */

public class Username extends Application{


/**
 * Test-GUI, dient nur zu Testzwecken
 * Hier kann der Username eingegeben werden
 * @param bekommt die Stage übergeben
 */

	//
	@Override
	public void start(Stage stage) throws Exception
	{
//		stage.setWidth(300);
//		stage.setHeight(300);
//
//		Pane pane = new Pane();
//		Scene scene = new Scene(pane);
//
//		TextField t1 = new TextField();
//	
//		t1.relocate(0,10);
//
//
//		Statement stmt = SqlHelper.getStatement();
//
//		Button b1 = new Button("Username");
//		b1.relocate(30, 100);
//		pane.getChildren().add(b1);
//		pane.getChildren().add(t1);
//		pane.getChildren().add(t2);
//
//		b1.setOnAction(a ->{
//			String username = t1.getText();
//			try
//			{
//				checkUsername(stmt,username);
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		});
//
//		stage.setScene(scene);
//
//		stage.show();
	}
}
//	**
// * Main zum Launch der GUI 
// * @param bekommt String[] args übergeben
// */
//	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		launch(args);
//
//	}
//
///**
//	 * Überprüft, ob der Name schon in der Datenbank steht
//	 * @param das aktuelle Statement stmt und den Username als String
//	 */
//	public void checkUsername(Statement stmt, String username) throws ClassNotFoundException, SQLException{
//
//		ArrayList <String> playerNames = new ArrayList<>();
//
//      String tableNames = "SELECT * FROM player";
//		ResultSet rs = stmt.executeQuery(tableNames);
//
//       while (rs.next()) {
//            playerNames.add(rs.getString("name"));
//       }
//
//       boolean isOk = true;
//
//       if(playerNames.size()> 0){
//
//	       for (int i = 0; i < playerNames.size(); i++){
//	        	if(playerNames.get(i).equals(username)){
//	            	isOk = false;
//	            	break;
//	            }
//			}
//        }
//
//        if(isOk || playerNames.isEmpty()){
//        	Player player = new Player(playerNames.size()+1,username,1);
//        	SqlQuery.fillPlayer(player);
//    	}
//
//	}
//
//}