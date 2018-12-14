package data;

import java.util.ArrayList;

import controller.SearchController;
import controller.SkillDataController;

public class ColorData {
	private  static ColorData instance;
	ArrayList<ArrayList<String>> colorValue = new ArrayList<ArrayList<String>>();
	ArrayList<String> red = new ArrayList<String>();
	ArrayList<String> green = new ArrayList<String>();
	ArrayList<String> blue = new ArrayList<String>();
	ArrayList<String> violet = new ArrayList<String>();
	ArrayList<String> yellow = new ArrayList<String>();
	ArrayList<String> black = new ArrayList<String>();
	
	public ColorData() {
		
		addValue();
		
	}
	
	static public ColorData getInstance() {
		if (instance == null) {
			instance = new ColorData();
		}
		return instance;
	}
	
	public void addValue() {
		red.add("#EB690B");
		red.add("#F19654");
		red.add("#F5B485");
		red.add("#F9D2B5");
		red.add("#FBE1CE");
		
		
		colorValue.add(red);
		
		green.add("#97A719");
		green.add("#B6C15E");
		green.add("#CBD38C");
		green.add("#E0E4BA");
		green.add("#EAEDD1");
		
		colorValue.add(green);
		
		blue.add("#376A86");
		blue.add("#7396AA");
		blue.add("#9BB4C2");
		blue.add("#C3D2DA");
		blue.add("#D7E1E7");
		
		colorValue.add(blue);
		
		violet.add("#7E5C7F");
		violet.add("#A48DA5");
		violet.add("#BEADBF");
		violet.add("#D8CED8");
		violet.add("#E5DEE5");
		
		colorValue.add(violet);
		
		yellow.add("#C1AC74");
		yellow.add("#D3C59D");
		yellow.add("#E0D5B9");
		yellow.add("#ECE6D5");
		yellow.add("#F3EEE3");
		
		colorValue.add(yellow);
		
		black.add("#595959");
		black.add("#7F7F7F");
		black.add("#BFBFBF");
		black.add("#D9D9D9");
		black.add("#EEEEEE");
		
		colorValue.add(black);
		
	}

	/**
	 * @return the colorValue
	 */
	public ArrayList<ArrayList<String>> getColorValue() {
		return colorValue;
	}

	/**
	 * @param colorValue the colorValue to set
	 */
	public void setColorValue(ArrayList<ArrayList<String>> colorValue) {
		this.colorValue = colorValue;
	}
	
	
}
