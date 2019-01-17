package sqlConnection;

import java.sql.SQLException;

public class Continent {
	// Attribute
	private int continentId;
	private String name;
	private int[] countryIdArray;
	private Country[] countryArray;
	private int additionalUnits;
	
	//Konstruktor
	public Continent(int continentId) {
		this.continentId = continentId;
		initalizeContinent();
	}
	
	private void initalizeContinent() {
		try {
			this.name = SqlHelper.getContintentName(continentId);
			this.countryIdArray = SqlHelper.ContinentCountries(continentId) ;
			this.additionalUnits = SqlHelper.Bonus(continentId);
		} catch (SQLException e) {
			System.out.println("initalizeContinent");
			e.printStackTrace();
		}
		
	}
	
	// Getters und Setters
	public int getContinentId() {	
		return continentId;
	}
	
	public void setContinentId(int continentId) {
		this.continentId = continentId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int[] getCountryIdArray() {
		return countryIdArray;
	}
	
	public void setCountryIdArray (int[] countriesId) {
		this.countryIdArray = countriesId;
	}
	
	public Country[] getCountryArray() {
		return countryArray;
	}
	
	public void setCountryArray (Country[] countries) {
		this.countryArray = countries;
	}

	
	public int getAdditionalUnits() {
		return additionalUnits;
	}
	
	public void setAdditionalUnits(int additionalUnits) {
		this.additionalUnits = additionalUnits;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Kontinent: ");
		sb.append(name);
		sb.append("\n");
		sb.append("Länder: ");
		for( Country country : countryArray) {
			sb.append(country.getCountryName());
			sb.append(",");
		}
		sb.append("\n");
		sb.append("Bonus: ");
		sb.append(additionalUnits);
		return sb.toString();
	}
	
}
