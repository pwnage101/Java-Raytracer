package raytracer.geom;

import raytracer.geom.Vector;

public class Point {
    public double x, y, z;
    public Point(double x1, double y1, double z1) {
        x = x1;
        y = y1;
        z = z1;
    }
    public double dist(Point pt) {
        return (double)Math.sqrt( Math.pow(pt.x - x,2)
                               + Math.pow(pt.y - y,2)
                               + Math.pow(pt.z - z,2));
    }
    public Point add(Point b) {return add(b.x, b.y, b.z);}
    public Point add(Vector b) {return add(b.x, b.y, b.z);}
    public Point add(double x1, double y1, double z1) {
        return new Point(x+x1, y+y1, z+z1);
    }
	public Point scale(double k) {return new Point(x*k,y*k,z*k);}
    public Point getPtBetween(Point p) {
        return new Point((x+p.x)/2,(y+p.y)/2,(z+p.z)/2);
    }
    public Vector toVector() {return new Vector(x,y,z);}
    public String toString() {return "point: [ " + x + ", " + y + ", " + z + " ]";}
}
