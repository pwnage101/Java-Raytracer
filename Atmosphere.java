package raytracer;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import raytracer.util.DataHolder;

public class Atmosphere implements DataHolder {
	public double travel;
	public Color color;
	public double height;

	public Atmosphere(double travel, Color color, double height) {
		this.travel = travel;
		this.color = color;
		this.height = height;
	}
	public Atmosphere() {
		travel = 15;
		color = new Color(255,255,255);
		height = 50;
	}
	public String getName() {
		return "";
	}
	public Map<String, Object> getDataMap() {
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("01 TRAVEL", travel);
		map.put("02 COLOR", color);
		map.put("03 HEIGHT", height);
		return map;
	}
	public double generateTravel() {
//		return Math.tan(Math.random()*(Math.PI/2)) * travel;
		return (travel)*Math.log(-1/(Math.random()-1));
	}
	public double getHeight() {
		return height;
	}
}
