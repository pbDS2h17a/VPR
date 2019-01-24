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
	 * Besitzer ID als Integer
	 */
	private int ownerId;
	/**
	 * Name des Besitzers als String
	 */
	private String owner;
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
	 * String der die Form des Landes für GUI beschreibt
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
		// -1 = noch kein Besitzer
		this.ownerId = -1;
		this.units = 1;
	}
	
	/**
	 * Getter Nachbarländer IDs
	 * @return Int Array mit Land ID's der Nachbarländer
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
		return units;
	}

	/**
	 * Setter Menge Einheiten
	 * @param units Neue Einheitenmenge als Integer
	 */
	public void setUnits(int units) {
		this.units = units;
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
	 * @param playerId ID des Besitzers
	 */
	public void setOwnerId(int playerId) {
		this.ownerId = playerId;
	}
	
	/**
	 * Setter Name von Besitzer des Landes
	 * @param name Name des Besitzers
	 */
	public void setOwner(String name) {
		this.owner = name;
	}
	
	/**
	 * Getter Besitzer ID
	 * @return Besitzer ID
	 */
	public int getOwnerId() {
		return this.ownerId;
	}

	/**
	 * Getter Besitzer Namen
	 * @return Name des Besitzers
	 */
	public String getOwner() {
		return this.owner;
	}
	
	
}
