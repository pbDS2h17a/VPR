package gui;

/**
 * @author Dang, Hoang-Ha
 */
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Land extends SVGPath {
	private String owner;
	private int units;
	private int[] neighborIDArray;
	SVGPath svgPath;
	
	public Land() {

	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String Owner) {
		owner = Owner;
	}
	
	public int getUnits() {
		return units;
	}
	
	public void setUnits(int Units) {
		units = Units;
	}
	
	public Paint getColor() {
		return svgPath.getFill();
	}
	
	public void setColor(String C) {
		svgPath.setFill(Color.web(C));
	}
	
	public int[] getNeighborIDArray() {
		return neighborIDArray;
	}
	
	public void setNeighborIDArray(int[] NeighborIDArray) {
		neighborIDArray = NeighborIDArray;
	}
	
}
