package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
	private int battleUnitsA;
	private int battleUnitsB;
	private int additionalAttacker;

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
	
	public boolean isAssign() {
		return assign;
	}

	public void setAssign(boolean assign) {
		this.assign = assign;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isFight() {
		return fight;
	}

	public void setFight(boolean fight) {
		this.fight = fight;
	}

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public int getAdditionalAttacker() {
		return additionalAttacker;
	}

	public void setAdditionalAttacker(int additionalAttacker) {
		this.additionalAttacker = additionalAttacker;
	}

	public Country getCountryA() {
		return countryA;
	}

	public void setCountryA(Country countryA) {
		this.countryA = countryA;
	}

	public Country getCountryB() {
		return countryB;
	}

	public void setCountryB(Country countryB) {
		this.countryB = countryB;
	}

	public int getBattleUnitsA() {
		return battleUnitsA;
	}

	public void setBattleUnitsA(int battleUnitsA) {
		this.battleUnitsA = battleUnitsA;
	}

	public int getBattleUnitsB() {
		return battleUnitsB;
	}

	public void setBattleUnitsB(int battleUnitsB) {
		this.battleUnitsB = battleUnitsB;
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
			
			default:
				firstUnits = 20;
		}
		
		firstUnits = 2;
		
		for (int i = 0; i < this.getPlayerArray().length; i++) {
			this.getPlayerArray()[i].setUnassignedUnits(firstUnits);
		}
		
		updatePlayerInterface(this.getActivePlayer());
		

		

	}
	
	public void startFight() {
		this.match.getBattleInputA().setText("0");
		this.match.getBattleInputB().setText("0");
		
		this.match.getBattleInputA().setDisable(false);
		this.match.getBattleInputB().setDisable(true);
		
		this.match.getBattleBackgroundA().setFill(this.countryA.getFill());
		this.match.getCountryNameA().setText(this.countryA.getCountryName());
		this.match.getCountryUnitsA().setText("/ " + String.valueOf(this.countryA.getUnits()));
		
		this.match.getBattleBackgroundB().setFill(this.countryB.getFill());
		this.match.getCountryNameB().setText(this.countryB.getCountryName());
		this.match.getCountryUnitsB().setText("/ " + String.valueOf(this.countryB.getUnits()));
		
		this.match.activateWorldMap(false);
		this.match.getPhaseBtnGroup().setVisible(false);
		this.match.getBattleInterface().setVisible(true);
	}

	public void endFight() {
		this.countryA.setStrokeWidth(0);
		this.countryA = null;
		this.countryB = null;
		this.match.getBattleReadyBtn().setActive(true);
		this.match.activateWorldMap(true);
		this.match.getPhaseBtnGroup().setVisible(true);
		this.match.getBattleInterface().setVisible(false);
	}

	public void updateFightResults(Integer[][] rolledDices, Country countryAttack, Country countryDefense) {

		List<Integer> diceListA = new ArrayList<Integer>();
		for (int i = 0; i < rolledDices[0].length; i++) {
			diceListA.add(rolledDices[0][i]);
		}
		
		List<Integer> diceListB = new ArrayList<Integer>();
		for (int i = 0; i < rolledDices[1].length; i++) {
			diceListB.add(rolledDices[1][i]);
		}
		
		int lostUnitsA = 0;
		int lostUnitsB = 0;
		int limit = diceListA.size();
		
		if(diceListA.size() > diceListB.size()) {
			limit = diceListB.size();
		}
		
		for (int i = 0; i < limit; i++) {
			if(diceListA.get(i) > diceListB.get(i)) {
				lostUnitsB++;
			}
			else {
				lostUnitsA++;
			}
		}
		
		System.out.println("A verlorene Einheiten: " + lostUnitsA);
		System.out.println("B verlorene Einheiten: " + lostUnitsB);
		
		int[] fightA = {diceListA.size(), lostUnitsA};
		int[] fightB = {diceListB.size(), lostUnitsB};
		
		countryAftermath(fightA, fightB, countryAttack, countryDefense);
	}

	public void countryAftermath(int[] fightA, int[] fightB, Country countryAttack, Country countryDefense) {
		if(countryDefense.getUnits() - fightB[1] == 0) {
			countryDefense.setUnits(fightA[0] - fightA[1] + this.additionalAttacker);
			countryDefense.setOwner(countryAttack.getOwner());
			countryDefense.setFill(countryAttack.getFill());
			
			countryAttack.setUnits(countryAttack.getUnits() - fightA[0] - this.additionalAttacker);
		}
		
		else {
			countryAttack.setUnits(countryAttack.getUnits() - fightA[1]);
			countryDefense.setUnits(countryDefense.getUnits() - fightB[1]);
		}
		
		this.additionalAttacker = 0;
		
		System.out.println("A Einheiten nachher: " + countryAttack.getUnits());
		System.out.println("B Einheiten nachher: " + countryDefense.getUnits());
		System.out.println();
	}

	public Integer[][] rollTheDice(int battleUnitsA, int battleUnitsB) {
		int limitA = battleUnitsA;
		int limitB = battleUnitsB;
		this.additionalAttacker = 0;
		
		if(battleUnitsA > 3) {
			limitA = 3;
			this.additionalAttacker = battleUnitsA - 3;
		}
		
		if(battleUnitsB > 2) {
			limitB = 2;
		}
		
		Integer[] dicesA = new Integer[limitA];
		Integer[] dicesB = new Integer[limitB];
		
		for (int i = 0; i < dicesA.length; i++) {
			dicesA[i] = MatchFX.randomInt(1, 6);
		}
		
		for (int i = 0; i < dicesB.length; i++) {
			dicesB[i] = MatchFX.randomInt(1, 6);
		}
		
		Arrays.sort(dicesA, Collections.reverseOrder());
		Arrays.sort(dicesB, Collections.reverseOrder());
		
		// Ausgabe zum Testen:
		System.out.print("A würfelte: ");
		for (int i = 0; i < dicesA.length; i++) {
			System.out.print(dicesA[i] + ", ");
		}
		System.out.println();
		
		System.out.print("B würfelte: ");
		for (int i = 0; i < dicesB.length; i++) {
			System.out.print(dicesB[i] + ", ");
		}
		System.out.println();
		
		Integer[][] diceResults = {dicesA, dicesB};
		
		return diceResults;
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
