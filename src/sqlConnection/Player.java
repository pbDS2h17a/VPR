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
	private int playerId;
	private String adress;
	private int lobbyId;
	private int unassignedUnits;
	private int card1;
	private int card2;
	private int card3;
	private Lobby lobby;

	public Player(String name, Lobby lobby) {
		this.name = name;
		this.countryList = new ArrayList<>();
		this.lobby = lobby;
		this.lobbyId = lobby.getLobbyId();
		this.unitsPerRound = 9;
		this.unassignedUnits = 0;
		this.card1 = 0;
		this.card2 = 0;
		this.card3 = 0;
		this.playerId = SqlHelper.insertPlayer(name,lobbyId);
	}

	// Konstruktor f�rs sp�teres laden eines Spielstands
	public Player(int playerId, String name, String color, List<Country> countryList, int units) {
		this.playerId = playerId;
		this.name = name;
		this.color = color;
		this.countryList = countryList;
		this.setUnitsPerRound(units);
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
	
	public int getUnitsPerRound()
	{
		return unitsPerRound;
	}


	public void setUnitsPerRound(int unitsPerRound)
	{
		this.unitsPerRound = unitsPerRound;
	}

	public void removeCountry(Country country) {
		this.countryList.remove(country);
	}

	public void addCountry(Country country) {
		this.countryList.add(country);
		SqlHelper.updateCountryOwner(this.lobbyId, this.playerId, country.getCountryId());
	}

	public String getColor()
	{
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
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
		String output = String.format("Player\n" +
				"Id=%d, " +
				"Name=%s, " +
				"Farbe=%s, " +
				"UnitsPerRound=%d\n" +
				"L�nder=[",playerId,
				name, color, unitsPerRound);


		for (Country c : countryList) {
			output += c.getCountryName();
			output += ",";
		}
		output += "]";
		return output;
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

	public Lobby getLobby() {
		return lobby;
	}


}

