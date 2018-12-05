package sqllite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


 
/**
 * @author pbs2h17awb
 *
 */
public class CreateTable {
 
	private static int counter = 1;
	
	// String Arrays aller Länder
	// Kontinent als erster Eintrag
	private static String[] countryNamesAfrica = {"Afrika", "Ägypten","Nordwestafrika","Ostafrika","Kongo","Südafrika"};
	private static String[] countryNamesAsia = {"Asien", "Japan","Kamtschatka","Jakutien","Irkusk","Mongolei","Sibierien",
			"Ural","China","Afghanistan","Siam","Indien","Mittlerer-Osten"};
	private static String[] countryNamesAustralia = {"Australien", "Philippinen","Neu Guinea","Indonesien","Ostaustralien",
			"Westostralien","Neu Seeland"};
	private static String[] countryNamesEurope = {"Europe", "Ukraine", "Skandinavien", "Svalbord", "Island", "Großbritanien",
			"Mitteleuropa", "Südeuropa", "Westeuropa"};
	private static String[] countryNamesSouthAmerica = {"Südamerika", "Brasilien", "Venezuela", "Peru", "Argentinien", "Falkland Inseln"};
	private static String[] countryNamesNorthAmerica = {"Nordamerika", "Mittelamerika", "Oststaaten", "Weststaaten", "Hawaii",
			"Quebec", "Ontario", "Alberta", "Nordwest-Territorium","Alaska", "Grönland", "Vikigtaluk"};
	private static HashMap<String, Integer> continentNames = new HashMap<>();
	
	public static void fillContinent(Statement statement) throws SQLException {
		ArrayList<String> continentNamesString = new ArrayList<String>(continentNames.keySet());
		for (int i = 0; i < 6; i++) {
			String keyString = continentNamesString.get(i);
			statement.executeUpdate("INSERT INTO continent " + 
								// ID		Name			Kontinent
					"VALUES ("+(i+1)+", '"+keyString+"',"+continentNames.get(keyString)+")");
		}      
	}
	
	public static void fillCountry(Statement statement, String[] countryNameArray) throws SQLException {
		for (int i = 1 ; i < countryNameArray.length; i++) {
			statement.executeUpdate("INSERT INTO country"
					+ " VALUES ("+counter+",\n"
					+ "'"+countryNameArray[i]+"',\n"
					+ "'"+countryNameArray[0]+"'\n"
					+ ")");
			counter++;
		}
		
	}
	
	//Erstellt Kontinent und Länder 
    public static void createNewTable(Statement statement) throws SQLException {   
        String sqlDropContinent = "DROP TABLE IF EXISTS continent;";
        String sqlDropCountry = "DROP TABLE IF EXISTS country;";
        
        // SQL statement for creating a new table
        String sqlCreateCountry = "CREATE TABLE IF NOT EXISTS country (\n"
                + "	countryid integer,\n"
                + "	name text PRIMARY KEY,\n"
                + " continent text,\n"
//                + " army integer,\n"
//                + " player text,\n"
                + " foreign key(continent) references continent(continentid)\n"
                + ");";
        
        String sqlCreateContinent = "CREATE TABLE IF NOT EXISTS continent (\n"
        		+ "continentid integer,\n"
                + "name text PRIMARY KEY,\n"
                + "bonus integer\n"
//                + "color text,\n"
                + ");";
        
        	// lösche alle tables
        	statement.execute(sqlDropContinent);
        	statement.execute(sqlDropCountry);
        	
            // create a new table
            statement.execute(sqlCreateCountry);
            statement.execute(sqlCreateContinent);   	

    }
   
	public static void main(String[] args) {
	    // Path zur DB
		// Wenn keine DB vorhanden ist, wird sie erstellt
	    
	    // Kontinent Namen und Bonus punkte werden in die Hashmap eingetragen
		continentNames.put("Europa",5);
		continentNames.put("Asien",7);
		continentNames.put("Nordamerika",5);
		continentNames.put("Südamerika",2);
		continentNames.put("Afrika",3);
		continentNames.put("Australien",2);	
		
		// Check ob die JDBC richtig eingebunden ist
    	try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	    
    	// Versucht eine Verbindung herzustellen
	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/pbs2h17awb?"
                + "user=pbs2h17awb&password=2vfTcNDp");
	    		
	    		//"jdbc:mysql://localhost/pbs2h17awb","pbs2h17awb","2vfTcNDp");
    		Statement statement = conn.createStatement()) {
	    
	    	//Methoden aufrufen um Daten einzufügen
			createNewTable(statement);
			fillContinent(statement);
			fillCountry(statement, countryNamesEurope);
			fillCountry(statement, countryNamesAfrica);
			fillCountry(statement, countryNamesNorthAmerica);
			fillCountry(statement, countryNamesSouthAmerica);
			fillCountry(statement, countryNamesAsia);
			fillCountry(statement, countryNamesAustralia);
			
		    } catch (SQLException e) {
		        System.out.println(e.getMessage());
		    }
		}
 
}