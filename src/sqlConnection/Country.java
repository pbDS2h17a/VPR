package sqlConnection;

import java.sql.SQLException;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

/**
 * 
 * @author Ebert
 *
 */
public class Country extends SVGPath {
	
	/**
	 * Land ID als Integer
	 */
	private int countryId;
	/**
	 * Besitzer als Player Objekt
	 */
	private Player owner;
	/**
	 * Anzahl Einheiten als Integer
	 */
	private int units;
	/**
	 * Kontinent ID des Landes als Integer
	 */
	private int countryContinentId;
	/**
	 * Name des Landes als String
	 */
	private String countryName;
	/**
	 * Nachbar-Array mit Land ID als Integer 
	 */
	private int[] neighborIdArray;
	
	/**
	 * String der die Form des Landes f�r GUI beschreibt
	 */
	private SVGPath svgPath;



	/**
	 * Land ctor
	 * @param id Land ID als Integer
	 * @throws SQLException
	 */
	public Country(int id) throws SQLException {
		this.countryId = id;
		this.countryContinentId = SqlHelper.getCountryContinentId(id);
		this.countryName = SqlHelper.getCountryName(id);
		this.neighborIdArray = SqlHelper.getCountryNeighbor(id);
		super.setContent(SqlHelper.getCountrySVG(id));
		// null = noch kein Besitzer
		this.owner = null;
		this.units = 1;
	}
	
	/**
	 * Getter Nachbarl�nder IDs
	 * @return Int Array mit Land ID's der Nachbarl�nder
	 */
	public int[] getNeighborIdArray() {
		return this.neighborIdArray;
	}
	
	/**
	 * Getter Land ID
	 * @return Land ID als Integer
	 */
	public int getCountryId(){
		return this.countryId;
	}
	
	/**
	 * Getter Land-Name
	 * @return Name des Landes als String
	 */
	public String getCountryName() {
		return this.countryName;
	}
	
	/**
	 * Getter Kontinent ID
	 * @return Kontinent ID des Landes
	 */
	public int getCountryContinentId(){
		return this.countryContinentId;
	}

	/**
	 * Getter Menge Einheiten
	 * @return Menge Einheiten als Integer
	 */
	public int getUnits() {
		units = SqlHelper.getCountryUnits(this.countryId, this.owner.getLobbyId());
		return units;
	}

	/**
	 * Setter Menge Einheiten
	 * Schreibt auch direkt in die Datenbank
	 * @param units Neue Einheitenmenge als Integer
	 */
	public void setUnits(int units) {
		this.units = units;
		SqlHelper.updateUnits(owner.getLobbyId(), this.countryId, units);
	}
	
	/**
	 * Getter Farbe des Landes
	 * @return Farbe des Landes (SVGPath)
	 */
	public Paint getColor() {
		return svgPath.getFill();
	}
	
	/**
	 * Setter Farbe des Landes
	 * @param C Neue Farbe als String
	 */
	public void setColor(String C) {
		svgPath.setFill(Color.web(C));
	}
	
	/**
	 * Setter Besitzer des Landes
	 * Schreibt Daten direkt in die Datenbank
	 * @param newOwner : Player Objekt
	 */
	public void setOwner(Player newOwner) {
		// Land wird vom Aktuellen Besitzer entfernt
		if(this.owner != null) {
			this.owner.removeCountry(this);
		}

		// Beseitzer wird auf den neuen Spieler gesetzt
		this.owner = newOwner;
		// Dem neuen Besitzer wird das Land hinzugef�gt
		newOwner.addCountry(this);
		// Der Besitzer des Landes �ndert sich
		SqlHelper.updateCountryOwner(newOwner.getLobbyId(), newOwner.getPlayerId(),  this.countryId);
	}
	
	/**
	 * Getter Besitzer 
	 * @return Besitzer 
	 */
	public Player getOwner() {
		owner = SqlHelper.getCountyOwner(this.countryId, owner.getLobby());
		return this.owner;
	}

	
	
	
}
