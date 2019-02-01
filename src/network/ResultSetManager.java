package network;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hilfsklasse für die Rückgabewerte der Datenbank.
 * @author Erik Schaumlöffel
 * @author PeRoScKu
 */
public class ResultSetManager
{
	/**
	 * Konvertiert ein ResultSet in eine List<List<String>>
	 * @param r Auszulesendes ResultSet
	 * @return <List<List<String>> der Abfrageergebniswerte aus dem ResultSet
	 * @throws SQLException
	 */
	public static List<List<String>> toList(ResultSet r) throws SQLException
	{
		List<List<String>> set;
		try { if(!r.next()) throw new SQLException(); } 
		catch (SQLException s) { return null; }
		set = new ArrayList<>();
		//Erste Zeile -> Bestimmung der Länge der Datensätze
		List<String> zeile = new ArrayList<>();
		int length = 1;
		while(true) {
			try { zeile.add(r.getString(length)); length++; }
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
