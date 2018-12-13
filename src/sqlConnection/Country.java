package sqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Country
{
	private int countryId;
	private int countryContinentID;
	private String name;
	private List<Integer> neighbor = new ArrayList<Integer>();
	private int armys;

	public Country(int id) throws SQLException{
		this.countryId=id;
		this.name=SqlHelper.getCountryName(id);
		}
	
//	public static String getNameFromCountryId(Statement stmt, int id) throws SQLException {
//		ResultSet rs =stmt.executeQuery("select country_name from country where country_id ="+id);
//	 		 rs.next(); //legt cursor auf nächste zeile, wenn leer --> false	
//	 			 return rs.getString(1);
//	}
	
	public List<Integer> getNeighbor() {
		return this.neighbor;
	}
	
	public int getId(){
		return this.countryId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getCountryContinentID(){
		return this.countryContinentID;
	}

	public int getArmys()
	{
		return armys;
	}

	public void setArmys(int armys)
	{
		this.armys = armys;
	}
}
