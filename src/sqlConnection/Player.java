package sqlConnection;

public class Player
{
	private String name;
	private String color;
	//private Country [] countryArr;
	private int einheitenProRunde;
	
	public Player(String name, String color, /*country[] countryArr,*/ int einheiten){
		this.name = name;
		this.color = color;
		//this.countryArr = countryArr;
		this.einheitenProRunde = einheiten;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}
	//getter u setter fuer die laender fehlen noch
	
	
	public int getEinheitenProRunde()
	{
		return einheitenProRunde;
	}

	public void setEinheitenProRunde(int einheitenProRunde)
	{
		this.einheitenProRunde = einheitenProRunde;
	}
}

