package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlHelper
{

	public static Statement stmt;

	public static void setStatement(String[] connection) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(connection[0],connection[1],connection[2]);
		stmt= con.createStatement();
	}
	
	public static String getCountryName(int countryId) throws SQLException{
		ResultSet rs =stmt.executeQuery("select name from country where country_id ="+countryId);
		 rs.next(); 
			 return rs.getString(1);	
	}
	
	/*public static List<Integer> getCountryNeighbor(){
		
	}*/
	
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

}
