package network;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Chat
{

	public static void main(String[] args)throws ClassNotFoundException, SQLException 
	{
		String localIP = "127.0.0.1";
		try
		{
			localIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");
		
		Statement stmt=con.createStatement();
		
//		stmt.executeUpdate("DROP TABLE chat");
		String sqlCreateChat = "CREATE TABLE IF NOT EXISTS chat (" +
				" id INTEGER auto_increment, " +
                " message VARCHAR(255), " +
				" fromIP VARCHAR(20)," +
				" toIP VARCHAR(20)," +
				" PRIMARY KEY (id)" +
				");";
		
		
		
		stmt.executeUpdate(sqlCreateChat);
		
		stmt.executeUpdate("INSERT into chat(message, fromIP, toIP)"
				+ "			VALUES("+System.currentTimeMillis()/1000+", 'Hallo Jona', '"+ localIP +"', '172.18.2.110');");
		stmt.executeUpdate("INSERT into chat(message, fromIP, toIP)"
				+ "			VALUES("+System.currentTimeMillis()/1000+", 'Hallo alle', '"+ localIP +"', '%');");
	}

}
