package raytracer.geom;

import java.util.Map;
import java.util.TreeMap;
import raytracer.Intersection;
import raytracer.Material;
import raytracer.util.DataHolder;

public abstract class Shape {
    public Material mat;
    public abstract Vector normal(Point p);
    public abstract Point getRandomPoint();
	public abstract double getSurfaceArea();
	public abstract Intersection getIntersection(Ray r);
	public abstract boolean useNormalForIllum();
//	public Map<String, Object> getDataMap() {
//		TreeMap<String,Object> map = new TreeMap<String,Object>();
//		map.put("01 MATERIAL", mat);
//		return map;
//	}
}
