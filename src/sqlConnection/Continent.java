package sqlConnection;

import java.sql.SQLException;
import java.util.List;

public class Continent
{
	//Attribute
	public int id;
	public String name;
	public List <String> countries;
	public int additionalUnits;
	
	//Getters/Setters
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public List <String> getCountries()
	{
		return countries;
	}
	public void setCountries(List <String> countries)
	{
		this.countries = countries;
	}
	public int getAdditionalUnits()
	{
		return additionalUnits;
	}
	public void setAdditionalUnits(int additionalUnits)
	{
		this.additionalUnits = additionalUnits;
	}
	
	//Konstruktor
	public Continent(int id)throws SQLException
	{
		this.id = id;
		this.name = SqlHelper.getContintentName(id);
		this.countries =SqlHelper.ContinentCountries(id) ;
		this.additionalUnits = SqlHelper.Bonus(id);
	}
	@Override
	public String toString()
	{
		return "Kontinent " + name + ": Länder:" + countries + " Bonus:"
				+ additionalUnits;
	}
	
	
	
	
}
