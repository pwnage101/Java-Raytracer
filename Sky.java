package raytracer;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import raytracer.geom.*;
import raytracer.util.DataHolder;

public class Sky implements DataHolder {
    public Color zenith, horizon, nadir;
    public double intensity; // >= 0

    private Radiance flatTop, flatBot, flat;

    private static final double HALF_PI = Math.PI/2;
    private Radiance zen, hor, nad;

    public Sky() {
        zenith = new Color(100,120,180);
        horizon = new Color(190,210,240);
        nadir = new Color(100,100,120);
        update();
        intensity = 1.5;
    }
    public Sky(Color z, Color h, Color n) {
        zenith = z;
        horizon = h;
        nadir = n;
        update();
        intensity = 2.0;
    }
	public String getName() {
		return "";
	}
	public Map<String, Object> getDataMap() {
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("01 ZENITH", zenith);
		map.put("02 HORIZON", horizon);
		map.put("03 NADIR", nadir);
		map.put("04 INTENSITY", intensity);
		return map;
	}

    private void update() {
        zen = Radiance.colToRad(zenith);
        hor = Radiance.colToRad(horizon);
        nad = Radiance.colToRad(nadir);

        if(hor.equals(zen)) {
            if(hor.equals(nad)) {
                flat = hor.scale(intensity);
            } else {
                flatTop = hor.scale(intensity);
            }
        } else if(hor.equals(nad)) {
            flatBot = hor.scale(intensity);
        }
    }
    //PRECONDITION: unit vector
    public Radiance getEmission(Vector outgoing) {
        if(flat != null) return flat;
        double dot = outgoing.normalize().dot(new Vector(0,1,0));
        double ang = Math.acos(dot);
        double hNess;
        Radiance temp;
        if(ang < HALF_PI) {
            if(flatTop != null) return flatTop;
            hNess = Math.pow(ang / HALF_PI, 4);
            temp = zen;
        } else {
            if(flatBot != null) return flatBot;
            hNess = Math.pow((Math.PI - ang) / HALF_PI, 4);
            temp = nad;
        }
        double r = (((hNess)*hor.r) + ((1-hNess)*temp.r));
        double g = (((hNess)*hor.g) + ((1-hNess)*temp.g));
        double b = (((hNess)*hor.b) + ((1-hNess)*temp.b));
        return (new Radiance(r,g,b)).scale(intensity);
    }
}