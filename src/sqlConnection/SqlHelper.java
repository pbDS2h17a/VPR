package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import network.ResultSetManager;

public class SqlHelper {

	public static Statement stmt;
	public static Connection con;
	
	// String Array der Logindaten der Datenbank
	public static String[] loginStringArray =  {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"};
	
	/* setStatement
	 * erhält ein String Array mit Drei werten
	 * 0=Addresse
	 * 1=Datenbankname
	 * 2=Password
	 * Erstellt ein Statement mit den Werten
	 */
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
	
	public static int getCountryContinentId(int countryId) throws SQLException{
		ResultSet rs =stmt.executeQuery("select continent_id from country where country_id ="+countryId);
		
		 rs.next(); 
		 	return rs.getInt(1);	
	}

	public static List<Integer> getCountryNeighbor(int countryId){
		return null;
	}
	
	public static void createLobby (Player player) throws SQLException, ClassNotFoundException {
//		Class.forName("com.mysql.cj.jdbc.Driver"); 
//		con = DriverManager.getConnection("jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");
//		stmt = con.createStatement();
		
		String dbStatement = "INSERT INTO lobby (host_id) values (" + player.getId() + ");";
		stmt.executeQuery(dbStatement);
	}
	
	public static String getContintentName(int continentID) throws SQLException{		
		ResultSet rs = stmt.executeQuery("select name from continent where continent_id ="+continentID+";");
		rs.next();
		return rs.getString("name");
	}
	
	public static int Bonus(int continentID) throws SQLException{
		ResultSet rs = stmt.executeQuery("select bonus from continent where continent_id ="+continentID+";");
		rs.next();
		return rs.getInt("bonus");
	}
	
	public static List <String> ContinentCountries(int continentID) throws SQLException{
		List <String> countries = new ArrayList<>();
		ResultSet rs = stmt.executeQuery("select name from country where continent_id ="+continentID+";");
		while(rs.next()){	   		
	   		countries.add(rs.getString("name"));	   				   			
	   		}
		rs.next();
		return countries;
	}

	public static String getMissionDescription(int missionID) throws SQLException {
		ResultSet rs = stmt.executeQuery("select description from mission where mission_id ="+missionID+";");
		rs.next();
		return rs.getString("description");
	}
	
	public static List<List<String>> getChatHistory(long timestamp, int lid) throws SQLException {
		ResultSet r = stmt.executeQuery(String.format("SELECT p.name, c.timestamp, c.message FROM player p, chat c WHERE p.pid = c.pid AND c.lid = %d AND c.timestamp > %d;", lid, timestamp));
		return ResultSetManager.toList(r);
	}
	
	public static void sendMessage(String message, int pid, int lid) throws SQLException
	{
		stmt.executeUpdate(String.format("INSERT into %s(timestamp, message, pid, lid) VALUES(CURDATE(), '%s', %d, %d);", message, pid, lid));
	}
	
	public static List<List<String>> getLobbies() throws SQLException
	{
		//TODO: WHERE player_6 IS NULL
		ResultSet r = stmt.executeQuery("SELECT * FROM lobby;");
		return ResultSetManager.toList(r);
	}
}
