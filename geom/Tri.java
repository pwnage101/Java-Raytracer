package raytracer.geom;

import java.awt.Color;
import java.util.Map;
import java.util.TreeMap;
import raytracer.Intersection;
import raytracer.Material;

public class Tri extends Shape {
    public Point p1, p2, p3;
    public Vector normal;
    public Plane plane;
    public Tri(Point a, Point b, Point c) {
        this(a,b,c,new Color(150,250,150));
    }
    public Tri(Point a, Point b, Point c, Color col) {
        this(a,b,c,col,0);
    }
    public Tri(Point a, Point b, Point c, Color col, float em) {
        p1 = a;
        p2 = b;
        p3 = c;
        normal = Vector.createNormal(p1,p2,p3);
        plane = null;
        mat = new Material(col);
        mat.emit = em;
    }
    public Tri(Point a, Point b, Point c, Material m) {
        p1 = a;
        p2 = b;
        p3 = c;
        normal = Vector.createNormal(p1,p2,p3);
        plane = null;
        mat = m;
    }
//	public Map<String, Object> getDataMap() {
//		Map<String,Object> map = super.getDataMap();
//		map.put("01 P1", p1);
//		map.put("02 P2", p2);
//		map.put("03 P3", p3);
//		return map;
//	}
    public Plane getPlane() {
        if(plane == null) plane = new Plane(normal.normalize(), p2);
        return plane;
    }

    @Override
    public Vector normal(Point p) {
        return normal;
    }

    @Override
    public Point getRandomPoint() {
        Vector v = new Vector(p1,p2);
		Vector w = new Vector(p1,p3);
		double a1 =0, a2 = 0;
		do{
			a1=Math.random();
			a2=Math.random();
		}while(a1+a2>1);
		return v.scale(a1).add(w.scale(a2)).toPoint().add(p1);
    }

	@Override
	public double getSurfaceArea() {
		Vector v = new Vector(p1,p2);
		Vector w = new Vector(p1,p3);
		return .5*w.dot(v);
	}
	public boolean useNormalForIllum() {return false;}

	public String toString() {
		return p1 + " " + p2 + " " + p3;
	}

	@Override
	public Intersection getIntersection(Ray r) {
		double intU = getPlane().intersects(r);
		if(intU <= 0) {return null;}
		Point intersection = r.getPtAlong(intU);
//		System.out.println(intU);
//		System.out.println(intersection);
		Vector v = new Vector(p2,p3);
        Vector u = new Vector(p2,p1);
        Vector w = new Vector(p2, intersection);
        double uu = u.dot(u);
        double vv = v.dot(v);
        double uv = u.dot(v);
        double wv = w.dot(v);
        double wu = w.dot(u);
        double denom = (double)(Math.pow(uv,2) - ((uu)*(vv)));

        double s = ( ((uv)*(wv)) - ((vv)*(wu)) ) / denom;
        if(s > 1 || s < 0) return null;
        double t = ( ((uv)*(wu)) - ((uu)*(wv)) ) / denom;
        if(t < 0 || s+t > 1) return null;

        return new Intersection(this,intersection,intU,r);
	}
}
