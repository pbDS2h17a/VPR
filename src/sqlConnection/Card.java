package sqlConnection;


class Card
{
	//Attribute
	private final int cardId;
	private final int value;
	private final String countryName;


	//Konstruktor
	public Card(int cardId) {
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
