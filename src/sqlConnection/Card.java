package sqlConnection;

public class Card
{
	//Attribute
	private int cardId;
	
	//Getters/Setters
	public int getCardId()
	{
		return cardId;
	}

	public void setCardID(int cardId)
	{
		this.cardId = cardId;
	}
	
	//Konstruktor
	public Card() 
	{
		this.cardId = (int)(Math.random()*3)+1;	
	}

	@Override
	public String toString()
	{
		return String.format("CardId: %d", cardId);
	}	
}
