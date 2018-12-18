package network;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Chat
{

	public static void main(String[] args)throws ClassNotFoundException, SQLException 
	{
		
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");
		
		Statement stmt=con.createStatement();
		

		ResultSet r = stmt.executeQuery("SELECT fromIP,nachricht FROM chat");
		
//		while(r.next())
//		{
//			System.out.println(r.getString(4)+": "+r.getString(3));
//		}
		
		List<List<String>> set = ResultSetManager.toList(r);
		for (List<String> zeile : set)
		{
			System.out.printf("%s: %s%n", zeile.get(0), zeile.get(1));
		}
	}

}
