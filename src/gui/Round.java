package gui;

import javax.swing.plaf.synth.SynthSpinnerUI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sqlConnection.Country;
import sqlConnection.Player;

public class Round {

	private int activePlayer;
	private Player[] playerArray;
	private Country[] countryArray;
	private MatchFX match;

	public Round(MatchFX match, int activePlayer, Player[] playerArray, Country[] countryArray) {
		this.activePlayer = activePlayer;
		this.playerArray = playerArray;
		this.countryArray = countryArray;
		this.match = match;
		
		this.startInitialRound();
	}
	
	public MatchFX getMatch() {
		return match;
	}

	public void setMatch(MatchFX match) {
		this.match = match;
	}

	void setActivePlayer(int i) {
		this.activePlayer = i;
	}
	
	int getActivePlayer() {
		System.out.println(this.activePlayer);
		return this.activePlayer;
	}
	
	public Player[] getPlayerArray() {
		return playerArray;
	}

	public void setPlayerArray(Player[] playerArray) {
		this.playerArray = playerArray;
	}

	public Country[] getCountryArray() {
		return countryArray;
	}

	public void setCountryArray(Country[] countryArray) {
		this.countryArray = countryArray;
	}

	void startInitialRound() {
		
		System.out.println(this.getPlayerArray().toString());
		
		for (int i = 0; i < this.getPlayerArray().length; i++) {
			this.getPlayerArray()[i].setUnassignedUnits(12);
		}
		
		for (int i = 0; i < this.getCountryArray().length; i++) {
			final int tmp = i;

			this.getCountryArray()[tmp].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				int playerCount = this.getActivePlayer();
				
				System.out.println("Owner: " + this.getCountryArray()[tmp].getOwner() + " | Aktiv: " + this.getPlayerArray()[playerCount].getName());
				
				if(this.getCountryArray()[tmp].getOwner().equals(this.getPlayerArray()[playerCount].getName())) {
					
					int count = this.getActivePlayer();
					playerTurn(count);
				}
			    	
		    });
		}
		
		playerTurn(0);

//        event.consume();
//        pane.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
		
		
//		countryList[0].addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//		    @Override
//		    public void handle(MouseEvent mouseEvent) {
//		    	
//		    	System.out.println(mouseEvent.getSource().toString());
//		    	
//		    	int tmpCount = count;
//		    	playerList[count].setUnassignedUnits(playerList[count].getUnassignedUnits() - 1);
//		    	matchObject.gameChangePlayerUnits(playerList[count].getUnassignedUnits());
//		    	
//		    	if(tmpCount == playerList.length-1) {
//		    		tmpCount = 0;
//		    	} else {
//		    		tmpCount++;
//		    	}
//		    	
//		    	boolean nextPlayer = false;
//		    	
//		    	for (int i = 0; i < playerList.length; i++) {
//					if(playerList[i].getUnassignedUnits() != 0) {
//						break;
//					}
//					
//					nextPlayer = true;
//				}
//		    	
//		    	if(nextPlayer) {
//		    		mouseEvent.consume();
//		    		startInitialRound(count, playerList, matchObject, countryList);
//		    	}
//		    	
//		    }
//		});

	}
	
	void playerTurn(int activePlayer) {
		this.match.gameChangePlayer(this.getPlayerArray()[activePlayer].getName(), Color.web(this.getPlayerArray()[activePlayer].getColor()));
		this.match.gameChangePlayerUnits(this.getPlayerArray()[activePlayer].getUnassignedUnits());
		this.match.gameChangePlayerTerritories(this.getPlayerArray()[activePlayer].getCountryList().size());
		this.match.gameChangePlayerCard1(this.getPlayerArray()[activePlayer].getCard1());
		this.match.gameChangePlayerCard2(this.getPlayerArray()[activePlayer].getCard2());
		this.match.gameChangePlayerCard3(this.getPlayerArray()[activePlayer].getCard3());
		
		this.match.gameChangePlayer(this.getPlayerArray()[activePlayer].getName(), Color.web(this.getPlayerArray()[activePlayer].getColor()));
    	
		if(activePlayer == this.getPlayerArray().length-1) {
			this.setActivePlayer(0);
		} else {
			this.setActivePlayer(this.getActivePlayer() + 1);
		}
		
//		for (int i = 0; i < countryArray.length; i++) {
//			final int tmp = i;
//
//			countryArray[tmp].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//				
//				if(countryArray[tmp].getOwner().equals(activePlayer.getName())) {
//					
//					countryArray[tmp].setUnits(countryArray[tmp].getUnits() + 1);
//					activePlayer.setUnassignedUnits(activePlayer.getUnassignedUnits() - 1);
//			    	this.match.gameChangePlayerUnits(activePlayer.getUnassignedUnits());
//					
//			    	if(count == playerArray.length-1) {
//			    		count = 0;
//			    	} else {
//			    		count++;
//			    	}
//			    				    	
//			        event.consume();
//
//			    	playerTurn(playerArray, playerArray[count], this.match, countryArray);
//				}
//				
//		    });
//		}
	}
	
}
