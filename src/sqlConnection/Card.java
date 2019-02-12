package sqlConnection;

import java.sql.SQLException;


public class Card
{
	//Attribute
	private int cardId;
	private int value;
	private String countryName;
	
	//Getters/Setters
	public int getCardId()
	{
		return cardId;
	}

	public void setCardID(int cardId) {
		this.cardId = cardId;
	}
	

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	
	//Konstruktor
	public Card(int cardId) throws SQLException 
	{
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
