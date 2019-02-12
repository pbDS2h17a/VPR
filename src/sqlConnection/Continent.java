package sqlConnection;

class Continent {
	// Attribute
	private int continentId;
	private String name;
	private Country[] countryArray;
	private int additionalUnits;


	private void initalizeContinent() {
		this.name = SqlHelper.getContintentName(continentId);
		int[] countryIdArray = SqlHelper.getContinentCountryIds(continentId);
		this.additionalUnits = SqlHelper.getBonus(continentId);
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
		sb.append("getBonus: ");
		sb.append(additionalUnits);
		return sb.toString();
	}
	
}
