package sqlCreation;

import java.sql.SQLException;
import sqlConnection.SqlHelper;

public class MySQLConnection {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String[] data = FileReader.readFile("src\\resources\\stammdaten4.csv");
		String[] continentData = FileReader.getContinent(data);
		String[] countryData = FileReader.getCountry(data);
		SqlHelper.setStatement(SqlHelper.loginStringArray);
		SqlQuery.stmt = SqlHelper.stmt;
		
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
