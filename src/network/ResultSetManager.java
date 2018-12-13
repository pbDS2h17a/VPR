import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetManager
{
	public static List<List<String>> toList(ResultSet r) throws SQLException
	{
		List<List<String>> set = new ArrayList<>();
		
		try { r.next(); } 
		catch (SQLException s) { return set; }
		
		//Erste Zeile -> Bestimmung der Länge der Datensätze
		List<String> zeile = new ArrayList<>();
		int length = 1;
		while(true) {
			try { zeile.add(r.getString(length).trim()); length++; }
			catch (SQLException s) { break; }
		}
		
		set.add(zeile);
		
		while(r.next())
		{
			zeile = new ArrayList<>();
			for (int i = 1; i < length; i++)
			{
				zeile.add(r.getString(i).trim());
			}
			set.add(zeile);
		}
		
		return set;
	}

}
