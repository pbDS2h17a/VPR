package sqlConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lea, PEROSCKU
 * Player-Klasse
 */


public class Player {
	
	private String name;
	private String color;
	private List<Country> countryList;
	private int unitsPerRound;
	private int id;
	private String adress;
	private int lobbyId;
	private int unassignedUnits;
	private int card1;
	private int card2;
	private int card3;
	
	public Player(int id, String name, String color) {
		this.id = id; //SqlHelper.getPlayerID(name);
		this.name = name;
		this.color = color;
		this.countryList = new ArrayList<>();
		this.unitsPerRound = 9;
		this.unassignedUnits = 0;
		this.card1 = 0;
		this.card2 = 0;
		this.card3 = 0;
	}
	
	public Player(int id, String name, String color, List<Country> countryList, int units) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.countryList = countryList;
		this.unitsPerRound = units;
		this.setLobbyId(lobbyId);
		this.setAdress(adress);
	}

	
	// Getter und Setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getEinheitenProRunde() {
		return unitsPerRound;
	}

	public String getColor() {
		return color;
	}


	public void setColor(String color) {
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

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(int lobbyId)
	{
		this.lobbyId = lobbyId;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", color=" + color + ", countryList=" + countryList + ", unitsPerRound="
				+ unitsPerRound + ", id=" + id + ", adress=" + adress + ", lobbyId=" + lobbyId + ", unassignedUnits="
				+ unassignedUnits + ", card1=" + card1 + ", card2=" + card2 + ", card3=" + card3 + "]";
	}

	public int getUnassignedUnits() {
		return unassignedUnits;
	}

	public void setUnassignedUnits(int unassignedUnits) {
		this.unassignedUnits = unassignedUnits;
	}

	public int getCard1() {
		return card1;
	}

	public void setCard1(int card1) {
		this.card1 = card1;
	}

	public int getCard2() {
		return card2;
	}

	public void setCard2(int card2) {
		this.card2 = card2;
	}

	public int getCard3() {
		return card3;
	}

	public void setCard3(int card3) {
		this.card3 = card3;
	}
	
	

}

