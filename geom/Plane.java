package raytracer.geom;

import raytracer.geom.Point;
import raytracer.geom.Vector;

public class Plane {
    public double D;
    public Vector normal;
    public Plane(Point p1, Point p2, Point p3) {
        normal = ((new Vector(p2,p1)).cross(new Vector(p2,p3))).normalize();
        D = -(normal.x*p2.x) - (normal.y*p2.y) - (normal.z*p2.z);
    }
    public Plane(Vector vector, Point origin) {
        normal = vector.normalize();
        D = -(normal.x*origin.x) - (normal.y*origin.y) - (normal.z*origin.z);
    }
    public double distance(Point pt) {
        return Math.abs((normal.x*pt.x)+(normal.y*pt.y)+(normal.z*pt.z)+(D)
                              / Math.sqrt((normal.x * normal.x) + (normal.y * normal.y) + (normal.z * normal.z)));
    }
	public double intersects(Ray r) {
		//Line equation: p = org + u * dir
        //Plane equation: p * normal - k = 0
        //u = (k - org * normal) / (dir * normal)
        double u = (-D - normal.dot(r.origin.toVector())) / (normal.dot(r.dir));
		if(!Double.isNaN(u) && u > 0) return u;
		else return -1;
	}
	public String toString() {
		return D + " " + normal;
	}
}