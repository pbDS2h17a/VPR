package sqlConnection;

import java.sql.SQLException;
import java.util.Stack;
import java.util.ArrayList;


public class CardStack extends Stack<Card>{	

	public CardStack() {
		this.fillCardStack();
	}

	public ArrayList<Card> fillCardList(){
		ArrayList<Card>Cards = new ArrayList<Card>();
		for (int i = 1; i <= 42; i++) {
			try{
				Cards.add(new Card(i));
			}catch (SQLException SE){
				
			}
			
		}		
		return Cards;
	}
	
	public void fillCardStack(){
		
		ArrayList<Card> orderedCardList = fillCardList();
		
		while (orderedCardList.size()>0){
			int index = (int)(Math.random()*orderedCardList.size());	
					
			this.push(orderedCardList.get(index));
			orderedCardList.remove(index);
		}
	}	
	
	public Card takeAndRemoveCard(String countryName, Stack <Card> shuffledStack){
		Card takedCard = null;
		for (Card card : shuffledStack) {
			if(card.getCountryName().equals(countryName)){
				takedCard = card;
				shuffledStack.remove(card);
				break;
			}			
		}
		return takedCard;
	}
	
	
}
