package network;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetManager
{
	public static List<List<String>> toList(ResultSet r) throws SQLException
	{
		List<List<String>> set;
		
		try { r.next(); } 
		catch (SQLException s) { return null; }
		
		set = new ArrayList<>();
		
		//Erste Zeile -> Bestimmung der Länge der Datensätze
		List<String> zeile = new ArrayList<>();
		int length = 1;
		while(true) {
			try { zeile.add(r.getString(length).trim()); length++; }
			catch (SQLException s) { break; }
		}
		
		if(length == 1 && zeile.size() == 0) {return null;}
		
		set.add(zeile);
		
		while(r.next())
		{
			zeile = new ArrayList<>();
			for (int i = 1; i < length; i++)
			{
				String s = null;
				try{s = r.getString(i);}
				catch(NullPointerException n)
				{
					zeile.add(null);
					continue;
				}
				zeile.add(s.trim());
			}
			set.add(zeile);
		}
		r.close();
		return set;
	}

}
