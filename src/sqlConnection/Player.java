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
	private final List<Country> countryList;
	private final int playerId;
	private final int lobbyId;
	private final Lobby lobby;
	private final int slotId;
	private final int unitsPerRound = 9;
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
		System.out.println("Ein Spieler wurde Typ A erstellt");
		this.name = String.format("Spieler %d", slotId);
		this.countryList = new ArrayList<>();
		this.lobby = lobby;
		this.lobbyId = lobby.getLobbyId();
		this.slotId = slotId;
		// Spieler in DB einf�gen
		this.playerId = SqlHelper.insertPlayer(name,lobbyId);
	}

	/**
	 * Konstruktor f�rs erstellen von Spielern die der Lobby beitreten
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
	 * getter f�r den Spieler-Namen
	 * @return den Namen als String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * setter f�r den Spieler-Namen
	 * @param name den Namen als String
	 */
	public void setName(String name) {
		this.name = name;
		lobby.getLobbyFX().guiChangePlayerName(this.slotId, this.name);
		SqlHelper.updatePlayerName(this.lobbyId, this.playerId, this.name);
	}

	/**
	 * getter f�r den Spieler-Slot in der Spielerliste
	 * @return die Id des Slots als int
	 */
	public int getSlotId() {
		return slotId;
	}
	
	/**
	 * getter f�r die Spieler-Einheiten
	 * @return die Spielereinheiten als int
	 */
	public int getUnitsPerRound() {
		return unitsPerRound;
	}

	/**
	 * Entfernt ein Land von den L�nderliste des Spielers
	 */
	public void removeCountry(Country country) {
		this.countryList.remove(country);
	}

	/**
	 * F�gt ein Land zu der L�nderliste des Spielers hinzu
	 */
	public void addCountry(Country country) {
		this.countryList.add(country);
		SqlHelper.updateCountryOwner(this.lobbyId, this.playerId, country.getCountryId());
	}

	/**
	 * getter f�r die Spieler-Farbe
	 * @return die Farbe als String
	 */
	public String getColorValue() {
		return colorValue;
	}
	
	/**
	 * setter f�r die Spieler-Farbe
	 * @param colorValue die Farbe als String
	 */
	public void setColorValue(String colorValue) {
		try {
			SqlHelper.updateColor(this.playerId, colorValue, this.lobbyId);
			this.colorValue = colorValue;
			lobby.getLobbyFX().guiChangeColor(this.getSlotId(), colorValue);
		} catch (SQLException e) {
			String grayString = String.format("%02x%02x%02x", Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue());
			lobby.getLobbyFX().guiChangeColor(this.getSlotId(), grayString);
		}

	}

	/**
	 * getter f�r die Country-Liste
	 * @return die coutryList als Liste vom Typ Country
	 */
	public List<Country> getCountryList() {
		return countryList;
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
	 * �berschreibt die toString-Methode
	 * @return String mit den Angaben �ber die Id, den Namen, der Farbe, den Einheiten und den L�ndern
	 */
	@Override
	public String toString() {
		String output = String.format("Player\n" +
				"Id=%d, " +
				"Name=%s, " +
				"Farbe=%s, " +
				"UnitsPerRound=%d\n" +
				"L�nder=[",playerId,
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
	 * getter f�r die nicht vergebenen Einheiten
	 * @return die Anzahl als int
	 */
	public int getUnassignedUnits() {
		return unassignedUnits;
	}

	/**
	 * setter f�r die nicht vergebenen Einheiten
	 */
	public void setUnassignedUnits(int unassignedUnits) {
		this.unassignedUnits = unassignedUnits;
	}


}

