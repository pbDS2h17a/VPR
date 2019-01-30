package sqlConnection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * @author Lea, PEROSCKU
 * Player-Klasse
 */


public class Player {
	
	private String name;
	private String color;
	private List<Country> countryList;
	private int playerId;
	private int lobbyId;
	private Lobby lobby;
	private int slotId;
	private int unitsPerRound = 9;
	private int unassignedUnits = 0;
	private int card1 = 0;
	private int card2 = 0;
	private int card3 = 0;


	/**
	 * Konstruktor f�rs erstmalige Erstellen eines Spielers beim beitreten bzw erstellen einer Lobby
	 * Die Daten werden sowohl in Java und der Datenbank gespeichert
	 * playerId wird durch Autoincrement erzeugt und aus der Datenbank gelesen
	 * @param lobby
	 */
	public Player(Lobby lobby, int slotId) {
		System.out.println("Ein Spieler wurde erstellt");
		this.name = String.format("Spieler %d", slotId);
		this.countryList = new ArrayList<>();
		this.lobby = lobby;
		this.lobbyId = lobby.getLobbyId();
		this.slotId = slotId;
		// Spieler in Db einf�gen
		this.playerId = SqlHelper.insertPlayer(name,lobbyId);
	}

	/**
	 * Konstruktor f�rs erstellen von Spielern die der Lobby beitreten
	 * Die Daten werden nur Java gespeichert, da sie bereits in der DB vorhanden sind
	 * @param playerId
	 * @param name
	 * @param lobby
	 */
	public Player(int playerId, String name, Lobby lobby) {
		this.playerId = playerId;
		this.name = name;
		this.lobby = lobby;
		this.lobbyId = lobby.getLobbyId();
	}

	
	/**
	 * getter f�r den Spieler-Namen
	 * @return den Namen als String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * setter f�r den Spieler-Namen
	 * @param den Namen als String
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getSlotId() {
		return slotId;
	}
	
	public int getUnitsPerRound() {
		return unitsPerRound;
	}

	public void setUnitsPerRound(int unitsPerRound) {
		this.unitsPerRound = unitsPerRound;
	}

	public void removeCountry(Country country) {
		this.countryList.remove(country);
	}

	public void addCountry(Country country) {
		this.countryList.add(country);
		SqlHelper.updateCountryOwner(this.lobbyId, this.playerId, country.getCountryId());
	}

	/**
	 * getter f�r die Spieler-Farbe
	 * @return die Farbe als String
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * setter f�r die Spieler-Farbe
	 * @param die Farbe als String
	 */
	public void setColor(String color) {
		this.color = color;
		SqlHelper.insertColor(this.playerId, this.color, this.lobbyId);
	}

	/**
	 * getter f�r die Country-Liste
	 * @return die coutryList als Liste vom Typ Country
	 */
	public List<Country> getCountryList() {
		return countryList;
	}

	/**
	 * setter f�r den Country-Liste
	 * @param die countryList als Liste vom Typ Country
	 */
	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}
	
	/**
	 * getter f�r den Spieler-Id
	 * @return die Id als Integer
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * getter f�r den Lobby-Id des Spielers
	 * @return die Lobby-Id als Integer
	 */
	public int getLobbyId() {
		return lobbyId;
	}

	/**
	 * setter f�r die Lobby-Id des Spielers
	 * @param die Lobby-Id als Integer
	 */
	public void setLobbyId(int lobbyId) {
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

	@Override
	public boolean equals(Object o) {
		if(o.getClass() != this.getClass()) {
			return false;
		}
		Player player = (Player) o;
		return this.getPlayerId() == player.getPlayerId();
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

