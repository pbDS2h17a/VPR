package sqlConnection;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lea, PEROSCKU
 * Player-Klasse
 */


public class Player {
	
	private String name;
	private String colorValue;
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
	
	public static final String DEFAULT_COLOR = String.format("%02x%02x%02x", Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue());


	/**
	 * Konstruktor fürs erstmalige Erstellen eines Spielers beim beitreten bzw erstellen einer Lobby
	 * Die Daten werden sowohl in Java und der Datenbank gespeichert
	 * playerId wird durch Autoincrement erzeugt und aus der Datenbank gelesen
	 * @param lobby
	 */
	public Player(Lobby lobby, int slotId) {
		System.out.println("Ein Spieler wurde Typ A erstellt");
		this.name = String.format("Spieler %d", slotId+1);
		this.countryList = new ArrayList<>();
		this.colorValue = DEFAULT_COLOR;
		this.lobby = lobby;
		this.lobbyId = lobby.getLobbyId();
		this.slotId = slotId;
		// Spieler in DB einfügen
		this.playerId = SqlHelper.insertPlayer(name,lobbyId);
	}

	/**
	 * Konstruktor fürs erstellen von Spielern die der Lobby beitreten
	 * Die Daten werden nur Java gespeichert, da sie bereits in der DB vorhanden sind
	 * @param playerId
	 * @param name
	 * @param lobby
	 */
	public Player(int playerId, String name, Lobby lobby, String colorValue) {
		System.out.println("Ein Spieler wurde Typ B erstellt");
		this.playerId = playerId;
		this.name = name;
		this.lobby = lobby;
		this.lobbyId = lobby.getLobbyId();
		this.slotId = lobby.getNextSlotId();
		this.countryList = new ArrayList<>();
		this.colorValue = colorValue;
	}

	
	/**
	 * getter für den Spieler-Namen
	 * @return den Namen als String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * setter für den Spieler-Namen
	 * @param name den Namen als String
	 */
	public void setName(String name) {
		this.name = name;
		lobby.getLobbyFX().guiChangePlayerName(this.slotId, this.name);
		SqlHelper.updatePlayerName(this.lobbyId, this.playerId, this.name);
	}

	/**
	 * getter für den Spieler-Slot in der Spielerliste
	 * @return die Id des Slots als int
	 */
	public int getSlotId() {
		return slotId;
	}
	
	/**
	 * getter für die Spieler-Einheiten
	 * @return die Spielereinheiten als int
	 */
	public int getUnitsPerRound() {
		return unitsPerRound;
	}

	/**
	 * setter für die Spieler-Einheiten
	 * @param die Spielereinheiten als int
	 */
	public void setUnitsPerRound(int unitsPerRound) {
		this.unitsPerRound = unitsPerRound;
	}

	/**
	 * Entfernt ein Land von den Länderliste des Spielers
	 * @param das Land als Country-Objekt
	 */
	public void removeCountry(Country country) {
		this.countryList.remove(country);
	}

	/**
	 * Fügt ein Land zu der Länderliste des Spielers hinzu
	 * @param das Land als Country-Objekt
	 */
	public void addCountry(Country country) {
		this.countryList.add(country);
		SqlHelper.updateCountryOwner(this.lobbyId, this.playerId, country.getCountryId());
	}

	/**
	 * getter für die Spieler-Farbe
	 * @return die Farbe als String
	 */
	public String getColorValue() {
		return colorValue;
	}
	
	/**
	 * setter für die Spieler-Farbe
	 * @param colorValue die Farbe als String
	 */
	public void setColorValue(String colorValue) {
		try {
			SqlHelper.updateColor(this.playerId, colorValue, this.lobbyId);
			this.colorValue = colorValue;
			lobby.getLobbyFX().guiChangeColor(this.getSlotId(), colorValue);
		} catch (SQLException e) {
			lobby.getLobbyFX().guiChangeColor(this.getSlotId(), DEFAULT_COLOR);
		}

	}

	/**
	 * getter für die Country-Liste
	 * @return die coutryList als Liste vom Typ Country
	 */
	public List<Country> getCountryList() {
		return countryList;
	}

	/**
	 * setter für den Country-Liste
	 * @param countryList die countryList als Liste vom Typ Country
	 */
	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}
	
	/**
	 * getter für den Spieler-Id
	 * @return die Id als Integer
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * getter für den Lobby-Id des Spielers
	 * @return die Lobby-Id als Integer
	 */
	public int getLobbyId() {
		return lobbyId;
	}

	/**
	 * setter für die Lobby-Id des Spielers
	 * @param lobbyId die Lobby-Id als Integer
	 */
	public void setLobbyId(int lobbyId) {
		this.lobbyId = lobbyId;
	}

	/**
	 * Überschreibt die toString-Methode
	 * @return String mit den Angaben über die Id, den Namen, der Farbe, den Einheiten und den Ländern
	 */
	@Override
	public String toString() {
		String output = String.format("Player\n" +
				"Id=%d, " +
				"Name=%s, " +
				"Farbe=%s, " +
				"UnitsPerRound=%d\n" +
				"Länder=[",playerId,
				name, colorValue, unitsPerRound);


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

	/**
	 * getter für die nicht vergebenen Einheiten
	 * @return die Anzahl als int
	 */
	public int getUnassignedUnits() {
		return unassignedUnits;
	}

	/**
	 * setter für die nicht vergebenen Einheiten
	 * @param die Anzahl als int
	 */
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

	/**
	 * getter für die Lobby des Spielers
	 * @return die Lobby als Lobby-Objekt
	 */
	public Lobby getLobby() {
		return lobby;
	}


}

