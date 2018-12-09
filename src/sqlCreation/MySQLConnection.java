package sqlCreation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sqlConnection.Country;
import sqlConnection.Player;

public class MySQLConnection {
	
	public static Statement getStatement(String[] connection) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(connection[0],connection[1],connection[2]);
		return con.createStatement();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String[] data = FileReader.readFile("src\\resources\\stammdaten4.csv");
		String[] continentData = FileReader.getContinent(data);
		String[] countryData = FileReader.getCountry(data);
		SqlQuery.stmt = getStatement(new String[] {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"});
		
		// home
		// "jdbc:mysql://192.168.178.27/testdb","root","password"
		// BIB verbindung
		// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"
		
		Country c1 = new Country(1,SqlQuery.stmt);
		Country c2 = new Country(2,SqlQuery.stmt);
		Country c3 = new Country(3,SqlQuery.stmt);
		
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
	
		
	}



}
