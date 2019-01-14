package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlHelper {
	
	private static Statement stmt = null;
	private static Connection con;
	
	/*
	 * String Array der Logindaten der Datenbank
	 * 0=Addresse
	 * 1=Datenbankname
	 * 2=Password
	 * Erstellt ein Statement mit den Werten
	 */
	private static String[] loginStringArray =  {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"};

	/**
	 * Versucht ein neues Statement zu erstellen
	 */
	private static void createStatement() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(loginStringArray[0],loginStringArray[1],loginStringArray[2]);
			stmt = con.createStatement();
		} catch (ClassNotFoundException e) {
			System.out.println("Driver nicht richtig eingebunden!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Sql Error!");
			e.printStackTrace();
		}
	}

	/**
	 * Gibt ein Statement zurück
	 * Checkt ob das Statement vorhande ist (nicht NULL)
	 * Sonst erstellt es ein neues Statement
	 * @return aktuelles Statement der Verbindung
	 */
	public static Statement getStatement()  {
		if(stmt == null) {
			createStatement();
		}

		return stmt;
	}
	
	public static String getCountryName (int countryId) throws SQLException {
		ResultSet rs = getStatement().executeQuery("select country_name from country where country_id ="+countryId);
	
		 rs.next(); 
			 return rs.getString(1);	
	}
	
	public static int getCountryContinentId(int countryId) throws SQLException{
		ResultSet rs = getStatement().executeQuery("select continent_id from country where country_id ="+countryId);
		
		 rs.next(); 
		 	return rs.getInt(1);	
	}

	public static List<Integer> getCountryNeighbor(int countryId){
		return null;
	}

	public static String getCountrySVG(int countryId) throws SQLException {

		ResultSet rs = getStatement().executeQuery("SELECT svg FROM country WHERE country_id = "+countryId);
		rs.next();
			return  rs.getString(1);
	}
	//Langsamer als getAllCountrySVG2 (400% performance gain)
	@Deprecated
	public static String[] getAllCountrySVG() {
		
		String[] allCountrySVG = new String[42];
		for (int i = 0; i < 42; i++) {
			try {
				allCountrySVG[i] = SqlHelper.getCountrySVG((i+1));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return allCountrySVG;
	}

	public static String[] getAllCountrySVG2() {
		String[] data = new String[42];
		int i = 0;
		try {
			ResultSet rs = getStatement().executeQuery("SELECT svg FROM country");
			while(rs.next()) {
				data[i] = rs.getString(1);
				i++;
			}
		} catch (SQLException e) {
			//do nothing
		}

		return data;
	}
	
	public static void createLobby (Player player) throws SQLException, ClassNotFoundException {
		String dbStatement = "INSERT INTO lobby (host_id) values (" + player.getId() + ");";
		getStatement().executeQuery(dbStatement);
	}
	
	public static String getContintentName(int continentID) throws SQLException{		
		ResultSet rs = getStatement().executeQuery("select name from continent where continent_id ="+continentID+";");
		rs.next();
		return rs.getString("name");
	}
	
	public static int Bonus(int continentID) throws SQLException{
		ResultSet rs = getStatement().executeQuery("select bonus from tinent where tinent_id ="+continentID+";");
		rs.next();
		return rs.getInt("bonus");
	}
	
	public static List <String> ContinentCountries(int continentID) throws SQLException{
		List <String> countries = new ArrayList<>();
		ResultSet rs = getStatement().executeQuery("select name from country where continent_id ="+continentID+";");
		while(rs.next()){	   		
	   		countries.add(rs.getString("name"));	   				   			
	   		}
		rs.next();
		return countries;
	}
	

	public static int getPlayerID(String name) throws SQLException{
		ResultSet rs = getStatement().executeQuery("select player_id from player where name ="+name+";");
		
		 rs.next(); 
		 return rs.getInt(1);	
	}
	
	public static int getCardCountryId(int cardId) throws SQLException{
		ResultSet rs = getStatement().executeQuery("select country_id from card where card_id ="+cardId+";");
		
		rs.next();
			return rs.getInt("country_id");
	}

	public static int getCardValue(int value) throws SQLException{
		ResultSet rs = getStatement().executeQuery("select value from card where value="+value+";");
		
		rs.next();
		return rs.getInt("value");
	}

	public static String getMissionDescription(int missionID) throws SQLException {
		ResultSet rs = getStatement().executeQuery("select description from mission where mission_id ="+missionID+";");
		rs.next();
		return rs.getString("description");
	}

}
