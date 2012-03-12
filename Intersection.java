package raytracer;

import raytracer.geom.Point;
import raytracer.geom.Ray;
import raytracer.geom.Shape;

public class Intersection {
	public Shape shape;
	public Point pt;
	public double u;
	public double dist;
	public Ray r;
	public Intersection(Shape s, Point p, double ua, Ray ra) {
		shape = s; pt = p; u = ua; r = ra;
		dist = -1;
	}
	public Shape getShape() { return shape; }
	public Point getPt() { if(pt==null)pt=r.getPtAlong(u); return pt; }
	public double getU() { return u; }
	public double getDist() { if(dist<0)dist=u*r.dir.length(); return dist;}
	public Ray getRay() { return r; }
	public String toString() {
		return shape + ", " + pt + ", " + dist;
	}
}
