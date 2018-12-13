package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqlHelper {

	public static Statement stmt;
	public static Connection con;
	
	
	public static void setStatement(String[] connection) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(connection[0],connection[1],connection[2]);
		stmt= con.createStatement();
	}
	
	
	public static String getCountryName (int countryId) throws SQLException {
		ResultSet rs =stmt.executeQuery("select country_name from country where country_id ="+countryId);
		 rs.next(); 
			 return rs.getString(1);	
	}
	
	
	public static void createLobby (Player player) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		con = DriverManager.getConnection("jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");
		stmt = con.createStatement();
		
		String dbStatement = "INSERT INTO lobby (host_id) values (" + player.getId() + ");";
		stmt.executeQuery(dbStatement);
	}
	
	
	public static List<Integer> getCountryNeighbor() {
		
		// provisorisch
		return null;
		
	}

}
