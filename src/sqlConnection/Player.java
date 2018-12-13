//LEA-MARIE MOENIKES
package sqlConnection;

import java.util.ArrayList;
import java.util.List;

public class Player
{
	private String name;
	private String color;
	private List<Country> countryList = null;
	private int unitsPerRound;
	private int id;
	private String adress;
	private int lobbyId;
	
	public Player(int id, String name, String color)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.countryList = new ArrayList<>();
		this.unitsPerRound = 0;
	}
	
	public Player(int id, String name, String color, List<Country> countryList, int units, String adress, int lobbyId)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.countryList = countryList;
		this.unitsPerRound = units;
		this.setLobbyId(lobbyId);
		this.setAdress(adress);
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
	
	
	public int getEinheitenProRunde()
	{
		return unitsPerRound;
	}

	public void setEinheitenProRunde(int einheitenProRunde)
	{
		this.unitsPerRound = einheitenProRunde;
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

	public String getAdress()
	{
		return adress;
	}

	public void setAdress(String adress)
	{
		this.adress = adress;
	}

	public int getLobbyId()
	{
		return lobbyId;
	}

	public void setLobbyId(int lobbyId)
	{
		this.lobbyId = lobbyId;
	}

}

