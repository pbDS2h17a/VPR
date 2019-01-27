package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import sqlConnection.Country;
import sqlConnection.Player;

public class Round {

	private ArrayList<Player> playerList;
	private MatchFX match;
	private Country countryA = null;
	private Country countryB = null;
	private boolean assign = true;
	private boolean add = false;
	private boolean fight = false;
	private boolean move = false;
	private int activePlayerIndex;
	private int battleUnitsA;
	private int battleUnitsB;
	private int additionalAttacker;

	public Round(MatchFX match, ArrayList<Player> playerList) {
		this.activePlayerIndex = 0;
		this.playerList = playerList;
		this.match = match;
		
		this.startInitialRound(playerList);
	}
	
	public void manageCountryClick(int index) {

		// Wenn man in der 0. Phase (einzeln setzen) steckt...
		if(isAssign()) {
			// Wenn einem das Land gehört...
			if(isOwnLand(match.getCountryArray()[index])) {
				// ...werden die ungesetzten Einheiten um eins reduziert
				getActivePlayer().setUnassignedUnits(getActivePlayer().getUnassignedUnits() - 1);
				// ...bekommt das Land eine Einheit dazu
				match.getCountryArray()[index].setUnits(match.getCountryArray()[index].getUnits() + 1);
				match.getCountryUnitsLabelArray()[index].setText(String.valueOf(match.getCountryArray()[index].getUnits()));
				
				// Wenn man am Ende der Spieler-Liste angekommen ist...
				if(getActivePlayerIndex() == getPlayerList().size() -1) {
					// ...ist der 1. Spieler wieder der aktive Spieler
					setActivePlayerIndex(0);
				} else {
					// sonst wird der nächste Spieler der aktive Spieler
					setActivePlayerIndex(getActivePlayerIndex() + 1);
				}
			}
		}
		
		// Wenn man in der 1. Phase (setzen) steckt...
		else if(isAdd()) {
			// Wenn einem das Land gehört und noch nicht unverteilte Einheiten im Inventar sind...
			if(isOwnLand(match.getCountryArray()[index]) && getActivePlayer().getUnassignedUnits() > 0) {
				// ...wird ein Spieler aus dem Inventar verschoben...
				getActivePlayer().setUnassignedUnits(getActivePlayer().getUnassignedUnits() - 1);
				// ...und dem Land hinzugefügt 
				match.getCountryArray()[index].setUnits(match.getCountryArray()[index].getUnits() + 1);
				match.getCountryUnitsLabelArray()[index].setText(String.valueOf(match.getCountryArray()[index].getUnits()));
			}
		}
		
		// Wenn man in der 2. Phase (kämpfen) steckt...
		else if(isFight()) {
			// Wenn noch nicht das erste Land ausgewählt wurde...
			if(getCountryA() == null) {
				// Wenn es das eigene Land ist und sich mehr als eine Einheit darauf befindet...
				if(isOwnLand(match.getCountryArray()[index]) && match.getCountryArray()[index].getUnits() > 1) {
					// ...wird das erste Land ausgewählt
					setCountryA(match.getCountryArray()[index]);
				}
			}
			
			// Sonst wenn es nicht das eigene Land ist und benachbart ist...
			else if(!isOwnLand(match.getCountryArray()[index]) && isNeighbour(getCountryA(), match.getCountryArray()[index])) {
				// ...wird das zweite Land ausgewählt und der Kampf gestartet
				setCountryB(match.getCountryArray()[index]);
				startFight();
			}
		}
		
		// Wenn man in der 3. Phase (bewegen) steckt...
		else if(isMove()) {
			// Wenn es das eigene Land ist...
			if(isOwnLand(match.getCountryArray()[index])) {
				// Wenn noch nicht das erste Land ausgewählt wurde...
				if(getCountryA() == null) {
					// ...wird das erste Land ausgewählt
					setCountryA(match.getCountryArray()[index]);
				}
				
				// Sonst...
				else {
					// ...wird das zweite Land ausgewählt
					setCountryB(match.getCountryArray()[index]);
					// ...Wenn das Land benachbart ist und das erste Land mehr als eine Einheit beinhaltet
					if(isNeighbour(getCountryA(), getCountryB()) && getCountryA().getUnits() > 1) {
						// ...wird eine Einheit vom ersten Land zum zweiten Land verschoben
						getCountryA().setUnits(getCountryA().getUnits() - 1);
						match.getCountryUnitsLabelArray()[getCountryA().getCountryId()-1].setText(String.valueOf(getCountryA().getUnits()));
						getCountryB().setUnits(getCountryB().getUnits() + 1);
						match.getCountryUnitsLabelArray()[getCountryB().getCountryId()-1].setText(String.valueOf(getCountryB().getUnits()));
					}
					// ...werden das erste und zweite Land zurückgesetzt
					setCountryA(null);
					setCountryB(null);
				}	
			}
		}
		
		// Wenn man alle Einheiten in der 0. Phase (einzeln setzen) verteilt hat...
		if(isAssign() && isFinishedAssigning()) {
			// ...erhält der erste Spieler seine Einheiten pro Runde
			getActivePlayer().setUnassignedUnits(getActivePlayer().getUnassignedUnits() + getActivePlayer().getUnitsPerRound());
			// ...wird die 0. Phase beendet und die 1. Phase begonnen
			setAssign(false);
			setAdd(true);
			phaseAdd();
		}
		
		// Aktualisiert das Interface
		updatePlayerInterface(getActivePlayer());
		match.updateCountryInfo(match.getCountryArray()[index]);
	}

	private void startInitialRound(ArrayList<Player> playerArray) {
			
		int firstUnits;

		for (Player p : playerArray) {
			System.out.println(p.toString());
		}


		switch (playerArray.size()) {
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
		
		firstUnits = 1;
		
		for (int i = 0; i < this.getPlayerList().size(); i++) {
			this.getPlayerList().get(i).setUnassignedUnits(firstUnits);
		}
		
		updatePlayerInterface(this.getActivePlayer());

	}
	
	private boolean isFinishedAssigning() {
		for (int i = 0; i < this.getPlayerList().size(); i++) {
			if(this.getPlayerList().get(i).getUnassignedUnits() != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public void phaseAdd() {
		this.add = true;
		this.fight = false;
		this.move = false;
		this.match.editPhaseButtons(false, true, true, true);
		updatePlayerInterface(this.getActivePlayer());
	}

	public void phaseFight() {
		this.add = false;
		this.fight = true;
		this.move = false;
		this.match.editPhaseButtons(false, false, true, true);
		updatePlayerInterface(this.getActivePlayer());
	}
	
	public void phaseMove() {
		this.add = false;
		this.fight = false;
		this.move = true;
		this.match.editPhaseButtons(false, false, false, true);
		updatePlayerInterface(this.getActivePlayer());
	}
	
	public void nextTurn() {
		if(this.getActivePlayerIndex() == this.getPlayerList().size() -1) {
			this.setActivePlayerIndex(0);
		}
		else {
			this.setActivePlayerIndex(this.getActivePlayerIndex() + 1);
		}
		
		this.getActivePlayer().setUnassignedUnits(this.getActivePlayer().getUnassignedUnits() + this.getActivePlayer().getUnitsPerRound());
		phaseAdd();
	}

	public void startFight() {
		UnaryOperator<Change> integerFilter = change -> {
		    String newText = change.getControlNewText();
		    if (newText.matches("-?([1-9][0-9]*)?")) {
		        return change;
		    }
		    return null;
		};
		this.match.getBattleInputA().setText("1");
		this.match.getBattleInputB().setText("1");
		addTextLimiter(this.match.getBattleInputA(), 2);
		this.match.getBattleInputA().setTextFormatter(
	    new TextFormatter<Integer>(new IntegerStringConverter(), 1, integerFilter));
		
		this.match.getBattleInputA().setDisable(false);
		this.match.getBattleInputB().setDisable(true);
		
		this.match.getBattleBackgroundA().setFill(this.countryA.getFill());
		this.match.getCountryNameA().setText("Angreifer\n" + this.countryA.getCountryName());
		this.match.getCountryUnitsA().setText("/ " + (this.countryA.getUnits()-1));
		
		this.match.getBattleBackgroundB().setFill(this.countryB.getFill());
		this.match.getCountryNameB().setText("Verteidiger\n" + this.countryB.getCountryName());
		this.match.getCountryUnitsB().setText("/ " + this.countryB.getUnits());
		
		this.match.activateWorldMap(false);
		this.match.getPhaseBtnGroup().setVisible(false);
		this.match.getBattleInterface().setVisible(true);
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

	public void countryAftermath(int[] fightA, int[] fightB, Country cAtk, Country cDef) {
		if(cDef.getUnits() - fightB[1] == 0) {
			cDef.setUnits(fightA[0] - fightA[1] + additionalAttacker);
			cDef.setOwner(cAtk.getOwner());
			cDef.setFill(cAtk.getFill());
			cAtk.setUnits(cAtk.getUnits() - fightA[0] - additionalAttacker);
			match.getCountryUnitsBGArray()[cDef.getCountryId()-1].setFill(cDef.getFill());
			match.getCountryUnitsLabelArray()[cDef.getCountryId()-1].setText(String.valueOf(cDef.getUnits()));
			match.getCountryUnitsBGArray()[cAtk.getCountryId()-1].setFill(cAtk.getFill());
			match.getCountryUnitsLabelArray()[cAtk.getCountryId()-1].setText(String.valueOf(cAtk.getUnits()));
		}
		
		else {
			cAtk.setUnits(cAtk.getUnits() - fightA[1]);
			cDef.setUnits(cDef.getUnits() - fightB[1]);
			match.getCountryUnitsLabelArray()[cDef.getCountryId()-1].setText(String.valueOf(cDef.getUnits()));
			match.getCountryUnitsLabelArray()[cAtk.getCountryId()-1].setText(String.valueOf(cAtk.getUnits()));

		}
		
		additionalAttacker = 0;
		
		System.out.println("A Einheiten nachher: " + cAtk.getUnits());
		System.out.println("B Einheiten nachher: " + cDef.getUnits());
		System.out.println();

		//Debug ausgabe
		for (Player p : playerList) {
			System.out.println(p);
		}
	}

	public void endFight() {
		this.countryA.setStrokeWidth(0);
		this.countryA = null;
		this.countryB = null;
		this.match.getBattleReadyBtn().setActive(true);
		this.match.activateWorldMap(true);
		this.match.getPhaseBtnGroup().setVisible(true);
		this.match.getBattleInterface().setVisible(false);
		updatePlayerInterface(this.getActivePlayer());
	}

	public Integer[][] rollTheDice(int battleUnitsA, int battleUnitsB) {
		int limitA = battleUnitsA;
		int limitB = battleUnitsB;
		additionalAttacker = 0;
		
		if(battleUnitsA > 3) {
			limitA = 3;
			additionalAttacker = battleUnitsA - 3;
		}
		
		if(battleUnitsB > 2) {
			limitB = 2;
		}
		
		Integer[] dicesA = new Integer[limitA];
		Integer[] dicesB = new Integer[limitB];
		
		for (int i = 0; i < dicesA.length; i++) {
			dicesA[i] = randomInt(1, 6);
		}
		
		for (int i = 0; i < dicesB.length; i++) {
			dicesB[i] = randomInt(1, 6);
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

	public boolean isNeighbour(Country a, Country b) {
		return IntStream.of(a.getNeighborIdArray()).anyMatch(x -> x == b.getCountryId());
	}
		
	public boolean isOwnLand(Country country) {
        return this.getActivePlayer().equals(country.getOwner());

    }

	public void updatePlayerInterface(Player activePlayer) {
		this.match.updateActivePlayer(activePlayer.getName(), Color.web(activePlayer.getColor()));
		this.match.setInventoryCountryLabel(activePlayer.getCountryList().size());
		this.match.setInventoryUnitsLabel(activePlayer.getUnassignedUnits());
	}
	
	public Player getActivePlayer() {
		return playerList.get(activePlayerIndex);
	}

	public void setActivePlayerIndex(int i) {
		this.activePlayerIndex = i;
	}

	public int getActivePlayerIndex() {
		return this.activePlayerIndex;
	}
	
	public ArrayList<Player> getPlayerList() {
		return this.playerList;
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

	public static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}

	private static int randomInt(int min, int max) {
	    return (int)(Math.random() * (max - min + 1)) + min;
	}
}
