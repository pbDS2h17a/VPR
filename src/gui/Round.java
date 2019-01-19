package gui;

import javax.swing.plaf.synth.SynthSpinnerUI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sqlConnection.Country;
import sqlConnection.Player;

public class Round {

	private int activePlayerIndex;
	private Player[] playerArray;
	private Country[] countryArray;
	private MatchFX match;
	private boolean assign = true;
	private boolean add = false;
	private boolean fight = false;
	private boolean move = false;
	private Country countryA = null;
	private Country countryB = null;

	public Round(MatchFX match, Player[] playerArray, Country[] countryArray) {
		this.activePlayerIndex = 0;
		this.playerArray = playerArray;
		this.countryArray = countryArray;
		this.match = match;
		
		this.startInitialRound();
	}
	
	Player getActivePlayer() {
		return this.getPlayerArray()[this.activePlayerIndex];
	}
	
	public MatchFX getMatch() {
		return match;
	}

	public void setMatch(MatchFX match) {
		this.match = match;
	}

	void setActivePlayerIndex(int i) {
		this.activePlayerIndex = i;
	}
	
	int getActivePlayerIndex() {
		return this.activePlayerIndex;
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
			
		int firstUnits;
		
		switch (this.getPlayerArray().length) {
			case 2:
				firstUnits = 40;
				break;
				
			case 3:
				firstUnits = 35;
				break;
				
			case 4:
				firstUnits = 30;
				break;
				
			case 5:
				firstUnits = 25;
				break;
				
			case 6:
				firstUnits = 20;
				break;
		}
		
		firstUnits = 1;
		
		for (int i = 0; i < this.getPlayerArray().length; i++) {
			this.getPlayerArray()[i].setUnassignedUnits(firstUnits);
		}
		
		for (int i = 0; i < this.getPlayerArray().length; i++) {
			this.getPlayerArray()[i].setUnassignedUnits(1);
		}
		
		updatePlayerInterface(this.getActivePlayer());
		
		for (int i = 0; i < this.getCountryArray().length; i++) {
			final int COUNT = i;

			this.getCountryArray()[COUNT].addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

				if(this.assign) {
					if(isOwnLand(this.getCountryArray()[COUNT])) {
						this.getActivePlayer().setUnassignedUnits(this.getActivePlayer().getUnassignedUnits() - 1);
						this.getCountryArray()[COUNT].setUnits(this.getCountryArray()[COUNT].getUnits() + 1);
						
						if(this.getActivePlayerIndex() == this.getPlayerArray().length-1) {
							this.setActivePlayerIndex(0);
						} else {
							this.setActivePlayerIndex(this.getActivePlayerIndex() + 1);
						}
					}
				}
				
				else if(this.add) {
					if(isOwnLand(this.getCountryArray()[COUNT])) {
						if(this.getActivePlayer().getUnassignedUnits() > 0) {
							this.getActivePlayer().setUnassignedUnits(this.getActivePlayer().getUnassignedUnits() - 1);
							this.getCountryArray()[COUNT].setUnits(this.getCountryArray()[COUNT].getUnits() + 1);
							this.match.updateTerritoryInfo(COUNT);
						}
					}
				}
				
				else if(this.fight) {
					if(isOwnLand(this.getCountryArray()[COUNT])) {
						if(this.countryA == null) {
							this.countryA = this.getCountryArray()[COUNT];
							this.countryA.setStrokeWidth(10);
						}
						
						else {
							this.countryB = this.getCountryArray()[COUNT];
							
							if(isNeighbour(countryA, countryB) && countryA.getUnits() > 1) {
//								this.countryA.setUnits(this.countryA.getUnits() - 1);
//								this.countryB.setUnits(this.countryB.getUnits() + 1);
							}
							
							this.countryA.setStrokeWidth(0);
							this.countryA = null;
							this.countryB = null;
						}
							
					}
				}
				
				else if(this.move) {
					if(isOwnLand(this.getCountryArray()[COUNT])) {
						if(this.countryA == null) {
							this.countryA = this.getCountryArray()[COUNT];
							this.countryA.setStrokeWidth(10);
						}
						
						else {
							this.countryB = this.getCountryArray()[COUNT];
							
							if(isNeighbour(countryA, countryB) && countryA.getUnits() > 1) {
								this.countryA.setUnits(this.countryA.getUnits() - 1);
								this.countryB.setUnits(this.countryB.getUnits() + 1);
							}
							
							this.countryA.setStrokeWidth(0);
							this.countryA = null;
							this.countryB = null;
						}
							
					}
				}
				
				// if(((MouseEvent) event).getButton().equals(MouseButton.SECONDARY)) WENN RECHTSKLICK
				
				if(this.assign && isFinishedAssigning()) {
					this.getActivePlayer().setUnassignedUnits(this.getActivePlayer().getUnassignedUnits() + this.getActivePlayer().getEinheitenProRunde());
					this.assign = false;
					this.add = true;
					this.setActivePlayerIndex(0);
					phaseAdd();
				}
				
				updatePlayerInterface(this.getActivePlayer());
				this.match.updateTerritoryInfo(this.getCountryArray()[COUNT].getCountryId());
		    });
		}

	}
	
	boolean isNeighbour(Country a, Country b) {
		for (int i = 0; i < a.getNeighborIdArray().length; i++) {
			if(a.getNeighborIdArray()[i] == b.getCountryId()) {
				return true;
			}
		}
		
		return false;
	}
		
	boolean isOwnLand(Country country) {
		if(this.getActivePlayer().getName().equals(country.getOwner())) {
			return true;
		}
		
		return false;
	}
	
	void phaseAdd() {
		this.add = true;
		this.fight = false;
		this.move = false;
		this.match.editPhaseButtons(false, true, true, true);
		updatePlayerInterface(this.getActivePlayer());
	}

	void phaseFight() {
		this.add = false;
		this.fight = true;
		this.move = false;
		this.match.editPhaseButtons(true, false, true, true);
		updatePlayerInterface(this.getActivePlayer());
	}
	
	void phaseMove() {
		this.add = false;
		this.fight = false;
		this.move = true;
		this.match.editPhaseButtons(true, true, false, true);
		updatePlayerInterface(this.getActivePlayer());
	}
	
	void nextTurn() {
		if(this.getActivePlayerIndex() == this.getPlayerArray().length-1) {
			this.setActivePlayerIndex(0);
		}
		else {
			this.setActivePlayerIndex(this.getActivePlayerIndex() + 1);
		}
		
		this.getActivePlayer().setUnassignedUnits(this.getActivePlayer().getUnassignedUnits() + this.getActivePlayer().getEinheitenProRunde());
		phaseAdd();
	}
	
	boolean isFinishedAssigning() {
		for (int i = 0; i < this.getPlayerArray().length; i++) {
			if(this.getPlayerArray()[i].getUnassignedUnits() != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	void updatePlayerInterface(Player activePlayer) {
		this.match.gameChangePlayer(activePlayer.getName(), Color.web(activePlayer.getColor()));
		this.match.gameChangePlayerTerritories(activePlayer.getCountryList().size());
		this.match.gameChangePlayerCard1(activePlayer.getCard1());
		this.match.gameChangePlayerCard2(activePlayer.getCard2());
		this.match.gameChangePlayerCard3(activePlayer.getCard3());
		this.match.gameChangePlayerUnits(activePlayer.getUnassignedUnits());
	}
	
}
