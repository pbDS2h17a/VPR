package sqlConnection;

import java.sql.SQLException;

/**
 *	Die Klasse "Card" erstellt die Spielkarten.
 *	@author Kai Papke
 *	@author Jessica Young
*/

public class Card
{
	//Attribute
	/**
	 * Karten-Id als Integer
	 * Kartenwert als Integer
	 * Name des Landes als String
	 */	
	private int cardId;
	private int value;
	private String countryName;
	
	//Getters/Setters
	/**
	 * Getter Karten-Id
	 * @return Karten-Id
	 */
	public int getCardId() {
		return cardId;
	}
	
	/**
	 * Setter Karten-Id
	 * @param cardId Karten-Id
	 */
	public void setCardID(int cardId) {
		this.cardId = cardId;
	}
	
	/**
	 * Getter Name des Landes
	 * @return Name des Landes
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Setter Name des Landes
	 * @param countryName Name des Landes
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	//Konstruktor
	/**
	 * Holt sich von der Datenbank den Kartenwert und den Namen des Landes
	 * @param cardId Karten-Id
	 * @throws SQLException
	 */
	public Card(int cardId) throws SQLException {
		this.cardId = cardId;
		this.value = SqlHelper.getCardValue(cardId);		
		this.countryName=SqlHelper.getCountryName(cardId);
	}

	@Override
	public String toString() {
		String valueName;
		switch(value){
		case 1: 
			valueName="Infrantrie";
			break;
		case 2:
			valueName="Kavalarie";
			break;		
		case 3:
			valueName="Artillerie";
			break;
		default: 	
			valueName="Fehler";
		}
		return "CardNo.:" + cardId + " UnitName:" + valueName+ " CountryName:" + countryName;
	}
}