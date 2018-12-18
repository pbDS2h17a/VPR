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

	public Country(int id,int armys) throws SQLException{
		this.countryId=id;
		this.countryContinentID=SqlHelper.getCountryContinentId(id);
		this.name=SqlHelper.getCountryName(id);
		this.neighbor=SqlHelper.getCountryNeighbor(0);
		}
	
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
