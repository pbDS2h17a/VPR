package sqlConnection;

import java.util.ArrayList;
import java.sql.SQLException;

public class StartRisiko
{

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		// TODO Auto-generated method stub
		ArrayList <Continent> continente = new ArrayList <>();
		
//		Class.forName("com.mysql.cj.jdbc.Driver"); 
//		
//		Connection con = DriverManager.getConnection(  
//				"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp");  
		//String [] connection = {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17awb","pbs2h17awb","2vfTcNDp"};
		for (int i = 1; i < 6; i++) {
			continente.add(new Continent(i));	
		}
		
		for (Continent continent : continente) {
			System.out.println(continent);
		}
		
//		Card card = new Card();
//		System.out.println(card);
		
			
		            
	    // loop through the result set
//        while (rs.next()) {
//        	
//           int  id = rs.getInt("continent_id");
//           String name = rs.getString("continent_name");
//           int bonus = rs.getInt("bonus");
//           List <String> laender = new ArrayList<>();
//           	String urlLaender = "SELECT * FROM country";
//	   		Statement stmtLaender = con.createStatement();
//	   		ResultSet rsLaender   = stmtLaender.executeQuery(urlLaender);
//           
//	   		while(rsLaender.next()){	   		
//	   			if(id==rsLaender.getInt("country_continent_id")){
//	   				laender.add(rsLaender.getString("country_name"));
//	   			}	   			
//	   		}
//           kontinente.add(new Continent(id,name,laender,bonus));
//        }
//       	for (Continent k : kontinente)
//		{
//			System.out.println(k);
//		}
//       
//        url = "SELECT * FROM country";
//        ResultSet rsc    = stmt.executeQuery(url);
//        
//		// loop through the result set
//		while (rsc.next()) {
//			
//			int country_id = rsc.getInt("country_id");
//			String country_name = rsc.getString("country_name");
//			int country_continent_id = rsc.getInt("country_continent_id");
//
		ArrayList <Mission> missions = new ArrayList <>();
		for (int i = 0; i < 17; i++) {
			try{
			missions.add(new Mission(i));
			}catch(SQLException sql){
				System.out.println("Id nicht vergeben!");
			};
		}
		for (Mission mission : missions) {		
				System.out.println(mission);			
		}
	}
}
