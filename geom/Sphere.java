/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package raytracer.geom;

import java.util.Map;
import java.util.TreeMap;
import raytracer.Intersection;
import raytracer.Material;

public class Sphere extends Shape {
    public Point center;
    public double radius;
    public Sphere(Point c, double r, Material m) {
        center = c;
        radius = r;
        mat = m;
    }
//	public Map<String, Object> getDataMap() {
//		TreeMap<String,Object> map = new TreeMap<String,Object>();
//		map.put("01 CENTER", center);
//		map.put("02 RADIUS", radius);
//		return map;
//	}
    public Vector normal(Point p) {
        return new Vector(center, p).normalize();
    }

    public String toString() {
        return "[ Sphere centered at " + center + " and radius of " + radius + " ]";
    }

	@Override
    public Point getRandomPoint() {
		double x1 = (Math.random()-.5)*2;
		double x2 = (Math.random()-.5)*2;
		double s = (x1*x1) + (x2*x2);
		if(s >= 1) return getRandomPoint();
		double w = 1-s;
        double x = 2*x1*Math.sqrt(w);
		double y = 2*x2*Math.sqrt(w);
		double z = 1-(2*s);
		return new Point(x,y,z).scale(radius).add(center);
    }

	@Override
	public double getSurfaceArea() {
		return 4*Math.PI*Math.pow(radius, 2);
	}
	public boolean useNormalForIllum() {return true;}

	@Override
	public Intersection getIntersection(Ray r) {
		Vector scenter = new Vector(center, r.origin);
        //Intersection of Sphere and Line     =       Quadratic Function of Distance
        double A = r.dir.dot(r.dir);                       // Remember This From High School? :
        double B = 2.0f * r.dir.dot(scenter);                //    A x^2 +     B x +               C  = 0
        double C = scenter.dot(scenter) - (double) Math.pow(radius, 2);          // (r'r)x^2 - (2s'r)x + (s's - radius^2) = 0
        double D = B*B - 4*A*C;                     // Precompute Discriminant

        if (D > 0.0) {                              //Solution Exists only if sqrt(D) is Real (not Imaginary)
            double sign = (C < -0.00001) ? 1 : -1;    //Ray Originates Inside Sphere If C < 0
            double val = (double) ((-B + sign * Math.sqrt(D)) / (2 * A)); //Solve Quadratic Equation for Distance to Intersection
            if(val>0)return new Intersection(this,null,val,r);
        }

        return null;
	}

}
