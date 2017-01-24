package raytracer;

import java.awt.Color;
import java.util.List;
import java.util.Set;

public class Radiance {
    public double r, g, b; // >= 0

    public final static double WAVE_RED = .000000650f;
    public final static double WAVE_GREEN = .000000510f;
    public final static double WAVE_BLUE = .000000475f;

    public Radiance(double r1, double g1, double b1) {
        r = r1;
        g = g1;
        b = b1;
    }
	public Radiance() {this(0,0,0);}
	
    public double avg() {return (r+g+b)/3f;}
    public static Radiance avg(List<Radiance> list) {
        if(list == null) return new Radiance(0,0,0);
		if(list.size() == 1) return list.get(0);
		Radiance tot = new Radiance(0,0,0);
        for(Radiance rad : list) {
            tot = tot.add(rad);
        }
        return tot.scale(1f/list.size());
    }
    public Radiance scale(double k) {
        return new Radiance(k*r, k*g, k*b);
    }
    public Radiance scale(double r1, double g1, double b1) {
        return new Radiance(r1*r, g1*g, b1*b);
    }
    public Radiance add(double r1, double g1, double b1) {
        return new Radiance(r+r1, g+g1, b+b1);
    }
    public Radiance add(Radiance rad) {
        return add(rad.r, rad.g, rad.b);
    }
    public Radiance subtract(Radiance rad) {
        return add(-rad.r,-rad.g,-rad.b);
    }

    public static Radiance colToRad(Color col) {
        return new Radiance(col.getRed() / 255f,
                            col.getGreen() / 255f,
                            col.getBlue() / 255f);
    }
    public static Color radToCol(Radiance r) {
        double nr, ng, nb;
        if(r.r > 1) nr = 1f; else nr = r.r;
        if(r.g > 1) ng = 1f; else ng = r.g;
        if(r.b > 1) nb = 1f; else nb = r.b;
     //   System.out.println(nr + ", " + ng + ", " + nb);
        return new Color((float)nr, (float)ng, (float)nb);
    }
    public boolean isZero() {
        if(r == 0 && g == 0 && b == 0) return true;
        return false;
    }
    public boolean equals(Object other) {
        Radiance o = (Radiance) other;
        return r == o.r && g == o.g && b == o.b;
    }
    public Color toColor() {
        return new Color((int)Math.min((r*255),255),
                         (int)Math.min((g*255),255),
                         (int)Math.min((b*255),255));
    }
    public String toString() {
        return "[ " + r + ", " + g + ", " + b + " ]";
    }
}
