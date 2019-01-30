package sqlConnection;

import java.sql.SQLException;
import java.util.Stack;
import java.util.ArrayList;

/**
 * @author pbs2h17apa, pbs2h17aeb
 * Klasse zur Erstellung eines gemischten Kartenstapels
 */

public class CardStack extends Stack<Card>{	
	
	public CardStack() {
		this.fillCardStack();
	}

	/**
	 * 
	 * @return Geordnete Liste der Klasse Card
	 */
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
	
	/**
	 * Füllt das Objekt per Zufall
	 */
	public void fillCardStack(){
		
		ArrayList<Card> orderedCardList = fillCardList();
		
		while (orderedCardList.size()>0){
			int index = (int)(Math.random()*orderedCardList.size());	
					
			this.push(orderedCardList.get(index));
			orderedCardList.remove(index);
		}
	}
	
	
	/**
	 * 	Funktion die dem Stapel eine Karte hinzufügt.
	 * @param card Karte die dem Stapel hinzugefügt
	 * @param cs Kartenstapel der sich selbst aufrufen muss
	 */
	public void pushCard(Card card, CardStack cs){		
		Stack <Card> tmp = new Stack<Card>();		
		while(cs.size()>0){
			tmp.push(cs.pop());
		}	
		cs.push(card);			
		while(tmp.size()>0) {
			cs.push(tmp.pop());
		}		
	}		

	
}
