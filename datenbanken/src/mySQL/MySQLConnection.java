package mySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String path = "H:\\VPR\\stammdatenDeutschVer3.csv";
		String[] data = FileReader.readFile(path);
		String[] continentData = FileReader.getContinent(data);
		String[] countryData = FileReader.getCountry(data);
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		
		// home
		// "jdbc:mysql://192.168.178.27/testdb","root","password"
		// BIB verbindung
		// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"  
		
		Connection con = DriverManager.getConnection("jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");  
		Statement stmt=con.createStatement();
		
		SqlQuery.dropCountry(stmt);
		SqlQuery.dropContinent(stmt);
		SqlQuery.dropNeighbor(stmt);
		
		SqlQuery.createContinent(stmt);
		SqlQuery.createCountry(stmt);
		SqlQuery.createNeighbor(stmt);
		
		SqlQuery.fillContinent(stmt, continentData);
		SqlQuery.fillCountry(stmt, countryData);
		
		
	}

}
