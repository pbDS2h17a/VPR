package sqlCreation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
		Statement stmt = getStatement(new String[] {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"});
		// home
		// "jdbc:mysql://192.168.178.27/testdb","root","password"
		// BIB verbindung
		// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"
		
		SqlQuery.dropCountry(stmt);
		SqlQuery.dropContinent(stmt);
		SqlQuery.dropNeighbor(stmt);
		SqlQuery.dropPlayer(stmt);
		
		SqlQuery.createContinent(stmt);
		SqlQuery.createCountry(stmt);
		SqlQuery.createNeighbor(stmt);
		SqlQuery.createPlayer(stmt);
		
		SqlQuery.fillContinent(stmt, continentData);
		SqlQuery.fillCountry(stmt, countryData);
	
		
	}



}
