package raytracer.geom;

public class Vector {
    public double x, y, z;
    private int normDebug;
    private double length;
	private Vector perp;
    public Vector(double x1, double y1, double z1) {
        x = x1;
        y = y1;
        z = z1;
        normDebug = 0;
        length = -1;
    }
    public Vector(Point a) {
        this(new Point(0,0,0), a);
    }
    public Vector(Point a, Point b) {
        x = b.x - a.x;
        y = b.y - a.y;
        z = b.z - a.z;
        normDebug = 0;
        length = -1;
    }
    public Vector(double x1, double y1, double z1, int n) {
        this(x1, y1, z1);
        normDebug = n;
    }
    public double length() {
        if(length >= 0) {
            return length;
        }
        return (double)Math.sqrt(Math.pow(x,2)
                              + Math.pow(y,2)
                              + Math.pow(z,2));
    }
    public Vector negative() {return new Vector(-x, -y, -z);}
    public Vector normalize() {
        if(normDebug > 1) {
            System.out.println("Ray calls normalize too much. " + normDebug + " " + this);
            return this;
        }
        double len = length();
        return new Vector(x/len, y/len, z/len, normDebug+1);
    }
	public Vector reflect(Vector normal) {
		return reflect(normal, false);
	}
    public Vector reflect(Vector normal, boolean toward) {
        if(toward) return add(normal.scale(2f*dot(normal)));
        else return negative().add(normal.scale(2f*dot(normal)));
    }
    //object[0] : vector
    //      [1] : angle
    // RETURN NULL vector: vector exceeds critical angle; apply internal reflection.
    // RETURN NULL angle: n1 == n2.
    public Object[] refract(Vector normal, double n1, double n2) {
        if(n1 == n2) {
            Vector refraction = new Vector(x,y,z);
            Object[] result = {refraction, null};
        }
        double dn = dot(normal);
        if(dn > 0) normal = normal.negative();
        double ratio = n1 / n2;
        double ang2 = Math.asin(ratio*Math.sqrt(1-Math.pow(dn,2)));
        if(((Double)ang2).isNaN()) {
            Object[] result = {null, null};
            return result;
        } else {
            Vector refraction = scale(ratio).add(normal.scale( (double)(ratio*(Math.abs(dn)) - Math.cos(ang2)) ));
            Object[] result = {refraction, ang2};
            return result;
        }
    }
    public static Vector createNormal(Point a, Point b, Point c) {
        return ((new Vector(b,a)).cross(new Vector(b,c))).normalize();
    }
    public Vector add(double x1, double y1, double z1) {return new Vector(x+x1, y+y1, z+z1);}
    public Vector add(Vector b) {return add(b.x, b.y, b.z);}
    public Vector subtract(Vector b) {return add(-b.x, -b.y, -b.z);}
	public double angle(Vector b) {return Math.acos(this.angleCos(b));}
	public double angleCos(Vector b) {return this.dot(b)/(this.length()*b.length());}
    public double dot(Vector b) {return (x*b.x) + (y*b.y) + (z*b.z);}
    public Vector cross(Vector b) {
        return new Vector((y*b.z)-(z*b.y),
                          (z*b.x)-(x*b.z),
                          (x*b.y)-(y*b.x));
    }
    public Vector scale(double k) {return new Vector(x*k, y*k, z*k);}
    public Vector rotate(Vector a, double theta) {
        if(a.equals(this)) return this;
        //PRECONDITION: a is a unit vector
        if(theta % (2*Math.PI) == 0) return this;
        double c = (double)Math.cos(theta);
        double s = (double)Math.sin(theta);
        double t = 1-c;
        //double nX = ( (x)*((t*a.x*a.x)+c) ) + ( (y)*((t*a.x*a.y)-(s*a.z)) ) + ( (z)*((t*a.x*a.y)+(s*a.y)) ) ;
        //double nY = ( (x)*((t*a.x*a.y)+(s*a.z)) ) + ( (y)*((t*a.y*a.y)+c) ) + ( (z)*((t*a.y*a.z)-(s*a.x)) ) ;
        //double nZ = ( (x)*((t*a.x*a.z)-(s*a.y)) ) + ( (y)*((t*a.y*a.z)+(s*a.x)) ) + ( (z)*((t*a.z*a.z)+c) ) ;

        Vector ortho = subtract(a.scale(dot(a)/a.dot(a)));
        Vector v1 = ortho.scale(c);
        Vector v2 = a.cross(this).scale(s);
        Vector v3 = a.scale(dot(a));
        Vector newVector = v1.add(v2).add(v3);

        //return new Vector(nX, nY, nZ);
        return newVector;
    }
    public Vector getHorPerpendicular() {
		if(perp != null) return perp;
        if(x == 0 && z == 0) perp = new Vector(1,0,0);
        else perp = new Vector(z,0,-x);
		return perp;
	}
	public Vector divert(double ang) {
		Vector diverted = this.rotate(this.getHorPerpendicular().normalize(), Math.sqrt(Math.random())*ang).rotate(this.normalize(), Math.random()*Math.PI*2);
		return diverted;
	}
	public static Vector generateRandVect() {
		return new Vector(Math.asin((Math.random()-.5)*2),Math.asin((Math.random()-.5)*2),Math.asin((Math.random()-.5)*2));
//		Vector top = new Vector(0,0,1);
//		Vector y = new Vector(0,1,0);
	}

    public Point toPoint() {return new Point(x,y,z);}
    public String toString() {return "vector: [ " + x + ", " + y + ", " + z + " ]";}
}
