package sqlConnection;

import java.util.List;

public class Kontinent
{
	//Attribute
	public int id;
	public String name;
	public List <String> laender;
	public int zusatzeinheiten;
	
	//Getters/Setters
	public int getId()
	{
		
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public List <String> getLand()
	{
		return laender;
	}
	public void setLand(List <String> land)
	{
		this.laender = land;
	}
	public int getZusatzeinheiten()
	{
		return zusatzeinheiten;
	}
	public void setZusatzeinheiten(int zusatzeinheiten)
	{
		this.zusatzeinheiten = zusatzeinheiten;
	}
	
	//Konstruktor
	public Kontinent(int id, String name, List <String> laender, int zusatzeinheiten)
	{
		this.id = id;
		this.name = name;
		this.laender = laender;
		this.zusatzeinheiten = zusatzeinheiten;
	}
	@Override
	public String toString()
	{
		return "Kontinent " + name + ": Länder:" + laender + " Bonus:"
				+ zusatzeinheiten;
	}
	
	
	
	
}
