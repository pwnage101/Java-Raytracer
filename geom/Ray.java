package raytracer.geom;

public class Ray {
	public Point origin;
	public Vector dir;
	public Ray(double x, double y, double z, double vx, double vy, double vz) {
        origin = new Point(x,y,z);
        dir = new Vector(vx,vy,vz);
    }
    public Ray(Point originIn, Vector dirIn) {
        origin = originIn;
        dir = dirIn;
    }
    public Ray(Point p1, Point p2) {
        origin = p1;
        dir = new Vector(p1, p2);
    }
    public Ray normalize() {
        return new Ray(origin, dir.normalize());
    }
    public Point getPtAlong(double u) {
        if(u < 0) return null;
        return origin.add(dir.scale(u));
    }
	/*
	public double intersects(Plane pl) {
        //Line equation: p = org + u * dir
        //Plane equation: p * normal - k = 0
        //u = (k - org * normal) / (dir * normal)
        double u = (-pl.D - pl.normal.dot(origin.toVector())) / (pl.normal.dot(dir));
		if(!Double.isNaN(u) && u > 0) return u;
		else return -1;
    }
    public double intersectsD(Tri tri) {
        double u = intersects(tri.getPlane());
        Point intersection = getPtAlong(u);
        if(intersection == null) return -1;
        if(intersects(tri, intersection) == true) return u;
        else return -1;
    }
	public boolean intersects(Tri tri) {
        Point intersection = normalize().getPtAlong(intersects(tri.getPlane()));
        if(intersection == null) return false;
        return intersects(tri, intersection);
    }
    public boolean intersects(Tri tri, Point intersection) {
        Vector v = new Vector(tri.p2,tri.p3);
        Vector u = new Vector(tri.p2,tri.p1);
        Vector w = new Vector(tri.p2, intersection);
        double uu = u.dot(u);
        double vv = v.dot(v);
        double uv = u.dot(v);
        double wv = w.dot(v);
        double wu = w.dot(u);
        double denom = (double)(Math.pow(uv,2) - ((uu)*(vv)) );

        double s = ( ((uv)*(wv)) - ((vv)*(wu)) ) / denom;
        if(s > 1 || s < 0) return false;
        double t = ( ((uv)*(wu)) - ((uu)*(wv)) ) / denom;
        if(t < 0 || s+t > 1) return false;

        return true;
    }
    public double intersect(Sphere s) {
        Vector scenter = new Vector(s.center, origin);
        //Intersection of Sphere and Line     =       Quadratic Function of Distance
        double A = dir.dot(dir);                       // Remember This From High School? :
        double B = 2.0f * dir.dot(scenter);                //    A x^2 +     B x +               C  = 0
        double C = scenter.dot(scenter) - (double) Math.pow(s.radius, 2);          // (r'r)x^2 - (2s'r)x + (s's - radius^2) = 0
        double D = B*B - 4*A*C;                     // Precompute Discriminant

        if (D > 0.0) {                              //Solution Exists only if sqrt(D) is Real (not Imaginary)
            double sign = (C < -0.00001) ? 1 : -1;    //Ray Originates Inside Sphere If C < 0
            return (double) ((-B + sign * Math.sqrt(D)) / (2 * A)); //Solve Quadratic Equation for Distance to Intersection
            // if that looks bad multiply it by the length of the ray
        }

        return -1f;
    }*/

	public String toString() {
        return "Origin: " + origin + '\n' + "Direction: " + dir;
    }
}