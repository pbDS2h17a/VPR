package sqlConnection;
/**
 * 
 * @author pbs2h17apa
 * Erstellte Objekte sollen einen Kontinent darstellten
 */
public class Continent {
	// Attribute
	private int continentId;
	private String name;
	private int[] countryIdArray;
	private Country[] countryArray;
	private int additionalUnits;
	

	/**
	 * 
	 * @param continentId: ID die den Kontinent identifiziert
	 */
	public Continent(int continentId) {
		this.continentId = continentId;
		initalizeContinent();
	}
	
	/**
	 *  Hilfsfunktion die mittels der continentId den Namen, die Länder und den Bonus des jeweilige Kontinent erfragt
	 */
	private void initalizeContinent() {
		this.name = SqlHelper.getContintentName(continentId);
		this.countryIdArray = SqlHelper.getContinentCountryIds(continentId) ;
		this.additionalUnits = SqlHelper.getBonus(continentId);
	}

	public int getId()
	{
		return continentId;
	}
	

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
	
	// formartierte Ausgabe
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
		sb.append("getBonus: ");
		sb.append(additionalUnits);
		return sb.toString();
	}
	
}
