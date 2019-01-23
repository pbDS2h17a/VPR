package sqlConnection;

import java.sql.SQLException;
import java.util.Stack;
import java.util.ArrayList;

public class CardStack{	
	
	
	public ArrayList<Card> fillCardList()throws SQLException{
		ArrayList<Card>Cards = new ArrayList<Card>();
		for (int i = 1; i <= 42; i++) {
			Cards.add(new Card(i));
		}		
		return Cards;
	}
	
	public Stack <Card> fillCardStack()throws SQLException{
		Stack <Card> CardStack = new Stack<>();
		ArrayList<Card> orderedCardList = fillCardList();
		
		while (orderedCardList.size()>0){
			int index = (int)(Math.random()*orderedCardList.size());	
					
			CardStack.push(orderedCardList.get(index));
			orderedCardList.remove(index);
		}
		return CardStack;
	}	
	
}
