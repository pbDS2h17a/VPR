package mySQL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

	static String[] readFile(String path) {
		String data = ""; 
	    try {
			data = new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return data.split("\n");
	}
	
	static String[] getContinent(String[] data) {
		String[] continentData = new String[5];
		for (int i = 0; i < 5; i++) {
			continentData[i] = data[(i+1)].trim();
		}
		
		return continentData;
		
	}
	
	static String[] getCountry(String[] data) {
		String[] countryData = new String[42];
		for (int i = 0; i < 42; i++) {
			countryData[i] = data[(i+8)].trim();
		}
		
		return countryData;
		
	}
	
}
