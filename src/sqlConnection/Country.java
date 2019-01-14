package sqlConnection;

import java.sql.SQLException;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

public class Country extends SVGPath {
	private int countryId;
	private int ownerId;
	private String owner;
	private int units;
	private int countryContinentID;
	private String countryName;
//	private int units;
	private int[] neighborIdArray;
	private SVGPath svgPath;
	
	public Country(int id) throws SQLException {
		this.countryId = id;
//		this.units = units;
		this.countryContinentID = SqlHelper.getCountryContinentId(id);
		this.countryName = SqlHelper.getCountryName(id);
		this.neighborIdArray = SqlHelper.getCountryNeighbor(id);
		super.setContent(SqlHelper.getCountrySVG(id));
		// -1 = noch kein Besitzer
		this.ownerId = -1;
	}
	
	public int[] getNeighborIdArray() {
		return this.neighborIdArray;
	}
	
	public int getCountryId(){
		return this.countryId;
	}
	
	public String getCountryName() {
		return this.countryName;
	}
	
	
	
	public int getCountryContinentID() {
		return this.countryContinentID;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int armys) {
		this.units = armys;
	}
	
	public Paint getColor() {
		return svgPath.getFill();
	}
	
	public void setColor(String C) {
		svgPath.setFill(Color.web(C));
	}
	
	public void setOwnerId(int playerId) {
		this.ownerId = playerId;
	}
	
	public void setOwner(String name) {
		this.owner = name;
	}
	
	public int getOwnerId() {
		return this.ownerId;
	}

	public String getOwner() {
		return this.owner;
	}
	
	
}
