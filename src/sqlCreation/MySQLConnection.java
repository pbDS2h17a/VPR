package sqlCreation;

import java.io.IOException;
import java.sql.SQLException;
import sqlConnection.SqlHelper;

public class MySQLConnection {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		FileReader.readFile("src\\resources\\stammdaten4.csv");
		String[] continentData = FileReader.getContinent();
		String[] countryData = FileReader.getCountry();
		String[] missionData = FileReader.getMission();
		String[] cardData = FileReader.getCard();
		String[] colorData = FileReader.getColor();
		
		SqlHelper.setStatement(SqlHelper.loginStringArray);
		SqlQuery.stmt = SqlHelper.stmt;
		
		SqlQuery.dropCountry();
		SqlQuery.dropContinent();
		SqlQuery.dropNeighbor();
		SqlQuery.dropPlayer();
		SqlQuery.dropPlayerCountry();
		SqlQuery.dropCard();
		SqlQuery.dropMission();
		SqlQuery.dropColor();
		
		SqlQuery.createContinent();
		SqlQuery.createCountry();
		SqlQuery.createNeighbor();
		SqlQuery.createPlayer();
		SqlQuery.createPlayerCountry();
		SqlQuery.createCard();
		SqlQuery.createMission();
		SqlQuery.createColor();
		
		
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
		SqlQuery.fillNeighbor(countryData);
		SqlQuery.fillCard(cardData);
		SqlQuery.fillMissions(missionData);
		SqlQuery.fillColor(colorData);

		
	}

}
