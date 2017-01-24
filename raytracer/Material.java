package raytracer;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import raytracer.util.DataHolder;

public class Material implements DataHolder {
	public String tag;
	public double diffWeight, specWeight, specExp;
    public Color col;
    public double emit;

    protected Radiance calculatedEmit;

    public Material(Color c) {
        this(c, 1, 1, Double.POSITIVE_INFINITY);
    }
    public Material(Color c, double dw, double sw, double exp) {
        diffWeight = dw;
        specWeight = sw;
        specExp = exp;
        emit = 0;
        calculatedEmit = null;
        col = c;
    }
    public Material(Color c, double e) {
        diffWeight = specWeight = specExp = 0;
        emit = e;
        calculatedEmit = null;
        col = c;
    }
    public Material() {
        this(new Color(255,255,255));
    }
	public String getName() {
		return tag;
	}
	public Map<String, Object> getDataMap() {
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("01 COLOR", col);
		map.put("02 DIFFUSE_WEIGHT", diffWeight);
		map.put("03 SPECULAR_WEIGHT", specWeight);
		map.put("04 SPECULAR_EXPONENT", specExp);
		map.put("05 EMIT", emit);
		return map;
	}
    public Radiance filterColor(Radiance rad) {
        double r = ((col.getRed()) / 255f);
        double g = ((col.getGreen()) / 255f);
        double b = ((col.getBlue()) / 255f);
        return rad.scale(r,g,b);
    }
    public static Radiance colToRad(Color col) {
        return new Radiance(col.getRed() / 255.0,
                            col.getGreen() / 255.0,
                            col.getBlue() / 255.0);
    }
    public Radiance getEmission() {
        if (calculatedEmit == null) calculatedEmit = colToRad(col).scale(emit);
        return calculatedEmit;
    }

}
