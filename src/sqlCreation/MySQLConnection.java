package sqlCreation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sqlConnection.Country;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

public class MySQLConnection {
	

	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String[] data = FileReader.readFile("src\\resources\\stammdaten4.csv");
		String[] continentData = FileReader.getContinent(data);
		String[] countryData = FileReader.getCountry(data);
		SqlHelper.setStatement(new String[] {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"});
		SqlQuery.stmt = SqlHelper.stmt;
		
		// home
		// "jdbc:mysql://192.168.178.27/testdb","root","password"
		// BIB verbindung
		// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"
		
		Country c1 = new Country(1);
		Country c2 = new Country(2);
		Country c3 = new Country(3);
		
		List<Country> countries = new ArrayList<>();
		countries.add(c1);
		countries.add(c2);
		countries.add(c3);
		
		Player p1 = new Player(1, "Spieler1", "Blau",countries,0);
		Player p2= new Player(2, "Spieler2", "Rot");
		Player p3 = new Player(3, "Spieler3", "Grün");
		
		SqlQuery.dropCountry();
		SqlQuery.dropContinent();
		SqlQuery.dropNeighbor();
		SqlQuery.dropPlayer();
		SqlQuery.dropPlayerCountry();
		
		SqlQuery.createContinent();
		SqlQuery.createCountry();
		SqlQuery.createNeighbor();
		SqlQuery.createPlayer();
		SqlQuery.createPlayerCountry();
		
		SqlQuery.fillPlayer(p1);
		SqlQuery.fillPlayer(p2);
		SqlQuery.fillPlayer(p3);
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
	
		System.out.println(c1.getName());
		
	}



}
