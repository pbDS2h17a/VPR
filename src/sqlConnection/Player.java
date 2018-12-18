//LEA-MARIE MOENIKES
// Peroscku
package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class Player {
	
	
	private String name;
	private String color;
	private List<Country> countryList;
	private int unitsPerRound;
	private int id;
	private String adress;
	private int lobbyId;
	
	
	public Player(int id, String name, String color) {
		this.id = id;
		this.name = name;
		this.countryList = new ArrayList<>();
		this.unitsPerRound = 0;
		
		// extracting the three rgb-values from the string color and creating an object Color
		//String[] sa = color.split(",");
		//this.color =Color.rgb(Integer.parseInt(sa[0]),Integer.parseInt(sa[1]),Integer.parseInt(sa[2]));
	}
	

	public Player(int id, String name, String color, List<Country> countryList, int units) {
		this.id = id;
		this.name = name;
		this.countryList = countryList;
		this.unitsPerRound = units;
		this.setLobbyId(lobbyId);
		this.setAdress(adress);
		
		// extracting the three rgb-values from the string color and creating an object Color
		//String[] sa = color.split(",");
		//this.color =Color.rgb(Integer.parseInt(sa[0]),Integer.parseInt(sa[1]),Integer.parseInt(sa[2]));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Color getColor() {
//		return color;
//	}
//
//	public void setColor(Color color) {
//		this.color = color;
//	}
	
	
	public int getEinheitenProRunde() {
		return unitsPerRound;
	}

	public String getColor()
	{
		return color;
	}


	public void setColor(String color)
	{
		this.color = color;
	}


	public void setEinheitenProRunde (int einheitenProRunde) {
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

