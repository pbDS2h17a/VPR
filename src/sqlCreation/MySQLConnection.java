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
		
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);

		
	}



}
