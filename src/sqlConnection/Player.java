package sqlConnection;

import java.util.ArrayList;
import java.util.List;

public class Player
{
	private String name;
	private String color;
	private List<Country> countryList = null;
	private int einheitenProRunde;
	private int id;
	
	public Player(int id, String name, String color)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.countryList = new ArrayList<>();
		this.einheitenProRunde = 0;
	}
	
	public Player(int id, String name, String color, List<Country> countryList, int einheiten)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.countryList = countryList;
		this.einheitenProRunde = einheiten;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}
	//getter u setter fuer die laender fehlen noch
	
	
	public int getEinheitenProRunde()
	{
		return einheitenProRunde;
	}

	public void setEinheitenProRunde(int einheitenProRunde)
	{
		this.einheitenProRunde = einheitenProRunde;
	}

	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

