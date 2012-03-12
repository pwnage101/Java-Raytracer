package raytracer;

import java.util.*;
import raytracer.geom.*;
import raytracer.geom.Shape;
import raytracer.geom.Vector;
import raytracer.util.DataHolder;

public class World implements DataHolder {
	private LinkedList<Shape> shapes;
    private LinkedList<Tri> triangles;
    private LinkedList<Sphere> spheres;
    private Sky sky;
	private Atmosphere atmosphere;

	private Material currentMat;
	private ArrayList<Shape> emitters;

	public World(Sky sky, Atmosphere atmosphere) {
		shapes = new LinkedList<Shape>();
		triangles = new LinkedList<Tri>();
        spheres = new LinkedList<Sphere>();
        this.sky = sky;
		this.atmosphere = atmosphere;
	}
    public World() {
        this(new Sky(), new Atmosphere());
    }
	public String getName() {
		return "";
	}
	public Map<String, Object> getDataMap() {
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("01 MATERIALS", gatherMats());
		map.put("02 TRIANGLES", triangles);
		map.put("03 SPHERES", spheres);
		map.put("04 SKY", sky);
		map.put("05 ATMOSPERE", atmosphere);
		return map;
	}
	public LinkedList<Material> gatherMats() {
		LinkedList<Material> list = new LinkedList<Material>();
		int i = 0;
		for(Shape s : shapes) {
			if(!list.contains(s.mat)) {
				s.mat.tag = "m" + String.valueOf(i++);
				list.add(s.mat);
			}
		}
		return list;
	}
    public void setSky(Sky s) {sky = s;}
	public void setAtmosphere(Atmosphere a) {atmosphere = a;}

    public void addTriangle(Tri t) {
         triangles.add(t);
		 shapes.add(t);
    }
    public void addTriangle(Point p1,Point p2,Point p3) {
		addTriangle(new Tri(p1,p2,p3,currentMat));
	}
	public void addSphere(Sphere s) {
        spheres.add(s);
		shapes.add(s);
    }
    public void addSphere(Point c, double r) {
		addSphere(new Sphere(c,r,currentMat));
	}

	public void setMat(Material mat) {currentMat = mat;}

    public void addSquare(Point d1, Point d2, Point r, Material mat) {
        Vector through = new Vector(r, d1.getPtBetween(d2));
        Point r2 = r.add(through.scale(2).toPoint());
        addTriangle(new Tri(d1, d2, r, mat));
        addTriangle(new Tri(d1, d2, r2, mat));
    }
    public void addSquare(Point d1, Point d2, Point r) {
		addSquare(d1,d2,r,currentMat);
	}
	public void addSquare(Point p1, Point p3, Vector normal, Material mat) {
        normal = normal.normalize();
        Point mid = p1.getPtBetween(p3);
        Vector cross = normal.cross(new Vector(mid, p3));
        Point p2 = mid.add(cross.toPoint());
        Point p4 = mid.add(cross.negative().toPoint());
        addTriangle(new Tri(p1,p3,p2, mat));
        addTriangle(new Tri(p1,p3,p4, mat));
    }
    public void addSquare(Point p1, Point p3, Vector normal) {
		addSquare(p1,p3,normal,currentMat);
	}
	public void addSquare(Point b1, Point b2, Vector dir, double height, Material mat) {
        Point otherEnd = dir.normalize().scale(height).toPoint();
        Point top1 = b1.add(otherEnd);
        Point top2 = b2.add(otherEnd);
        addTriangle(new Tri(b1, b2, top1, mat));
        addTriangle(new Tri(b2, top1, top2, mat));
    }
    public void addSquare(Point b1, Point b2, Vector dir, double height) {
		addSquare(b1,b2,dir,height,currentMat);
	}
	public void addBottomlessPyramid(Point p1, Point p3, Vector dir, double height, Material mat) {
        dir = dir.normalize();
        Point mid = p1.getPtBetween(p3);
        Point tip = mid.add(dir.scale(height).toPoint());
        Vector diag2 = dir.cross(new Vector(p1,p3)).scale(1.0/2);
        Point p2 = mid.add(diag2.toPoint());
        Point p4 = mid.add(diag2.negative().toPoint());
        addTriangle(new Tri(p1,p2,tip, mat));
        addTriangle(new Tri(p2,p3,tip, mat));
        addTriangle(new Tri(p3,p4,tip, mat));
        addTriangle(new Tri(p4,p1,tip, mat));
    }
    public void addBottomlessPyramid(Point p1, Point p3, Vector dir, double height) {
		addBottomlessPyramid(p1,p3,dir,height,currentMat);
	}
	public void addBottomlessCube(Point b1, Point b3, Vector dir, double height, Material mat) {
        dir = dir.normalize();
        Point mid = b1.getPtBetween(b3);
        Point top = dir.scale(height).toPoint();
        Vector diag2 = dir.cross(new Vector(b1,b3)).scale(1.0/2);
        Point b2 = mid.add(diag2.toPoint());
        Point b4 = mid.add(diag2.negative().toPoint());
        addSquare(b1,b2,dir,height,mat);
        addSquare(b2,b3,dir,height,mat);
        addSquare(b3,b4,dir,height,mat);
        addSquare(b4,b1,dir,height,mat);
        addSquare(b1.add(top),b3.add(top),dir,mat);
    }
	public void addBottomlessCube(Point b1, Point b3, Vector dir, double height) {
		addBottomlessCube(b1,b3,dir,height,currentMat);
	}

	public Intersection segIntersect(Ray r, double d, Shape e1, Shape e2) {
		Intersection i = rayIntersect(r,e1,e2);
		if(i==null || i.getDist() > d) return null;
		return i;
	}
	public Intersection rayIntersect(Ray r, Shape e1, Shape e2) {
		Intersection i = new Intersection(null,null,Double.POSITIVE_INFINITY,null);
		final Intersection oldI = i;
		Intersection tempI = null;
        for (Shape s : shapes) {
			if(!s.equals(e1) && !s.equals(e2)) {
				tempI = s.getIntersection(r);
				if(tempI != null && tempI.getU() < i.getU()) {
					i = tempI;
				}
			}
        }
		if(!i.equals(oldI)) return i;
		return null;
	}
	public Radiance raytrace(Ray r, Shape exclude, int samples, int depth, boolean emit) {
		if(depth < 1) return new Radiance(0,0,0);
		Intersection i = rayIntersect(r,exclude,null);
//		double scatter = -1;
//		if(atmosphere != null) scatter = atmosphere.generateTravel();
//		Point newPt = r.normalize().getPtAlong(scatter);
		if(i == null) {
			if(sky!=null) return sky.getEmission(r.dir);
			return new Radiance(0,0,0);
		} else {
			Radiance integral = new Radiance(0,0,0);
			if(i.getShape().mat.emit > 0 && emit) return i.getShape().mat.getEmission();
			Vector n = i.getShape().normal(i.getPt());
			if(n.dot(r.dir) > 0) n = n.negative();
			integral = integral.add( indirect(r, i.getShape(), i.getPt(), n, samples, depth))
							   .add( direct(i.getShape(),i.getPt(),n,samples) );
			return i.getShape().mat.filterColor(integral);
		}
	}
    public Radiance Raytrace(Ray r, Shape exclude, int samples, int depth, boolean emit) {
        if(depth < 1) return new Radiance(0,0,0);
		Intersection i = rayIntersect(r,exclude,null);
		if(true) {
			double scatter = -1;
			if(atmosphere != null) scatter = atmosphere.generateTravel();
			Point newPt = r.normalize().getPtAlong(scatter);
			if(i == null) {
				if(sky!=null) return sky.getEmission(r.dir);
				return new Radiance(0,0,0);
			} else {
				Radiance integral = new Radiance(0,0,0);
				if(i.getShape().mat.emit > 0 && emit) return i.getShape().mat.getEmission();
				Vector n = i.getShape().normal(i.getPt());
				if(n.dot(r.dir) > 0) n = n.negative();
				integral = integral.add( indirect(r, i.getShape(), i.getPt(), n, samples, depth))
								   .add( direct(i.getShape(),i.getPt(),n,samples) );
				return i.getShape().mat.filterColor(integral);
			}
		} else {
			//return Radiance.colToRad(obstruction.mat.col).scale(1f/5f);//.scale(5/Math.abs(r.dir.dot(obstruction.normal(intersection))));
//            double intensity = Math.abs(obstruction.normal(intersection).dot(r.dir.normalize()));
			if(i==null) return new Radiance();
			double distance = i.getU()*r.dir.length();
			double intensity = (Math.pow(distance, 2))/100;
			if(distance == Double.POSITIVE_INFINITY) return new Radiance(255,0,0);

//			double intensity = 1;
//			if(distance > 50) return new Radiance(intensity,0,0);
            return new Radiance(intensity,intensity,intensity);
		}
    }
    
    public Radiance specularSample(Shape s, Vector v, Point intersection, Vector n, int samples, int depth) {
        Vector reflect = v.reflect(n);
        Vector perp = reflect.getHorPerpendicular();
        //Vector perp = reflect.cross(n);

        double r1 = Math.random() * 2 * Math.PI;
        double r2 = Math.acos(Math.pow(Math.random(),1/(1+s.mat.specExp)));
        
        Ray newRay = new Ray(intersection, reflect.rotate(perp.normalize(), r2).rotate(reflect.normalize(), r1));

        if(newRay.dir.dot(n) < 0) {
            return new Radiance(0,0,0);
        }
        Radiance temp = Raytrace(newRay, s, samples, depth-1, true);
//		Vector sunVect = sun.generateVector();
//		double dot = reflect.normalize().dot(sunVect.normalize());
//		if(dot > 0 && !rayIntersects(new Ray(intersection,sunVect),s)){
//			temp = temp.add(sun.getEmission().scale(Math.pow(dot, 10000)));
//		}
		return temp;
    }
    public Radiance diffuseSample(Shape s, Vector v, Point intersection, Vector n, int samples, int depth) {
        Vector perp = n.getHorPerpendicular();

        double r1 = (.5-Math.random()) * 2 * Math.PI;
        double r2 = Math.acos(Math.sqrt(Math.random()));

        Ray newRay = new Ray(intersection, n.rotate(perp.normalize(), r2).rotate(n.normalize(), r1));
        Radiance temp = Raytrace(newRay, s, samples, depth-1, false);
		//sun
//		if(sun != null && depth > 1) {
//			Vector sunVect = sun.generateVector();
//			double dot = n.normalize().dot(sunVect.normalize());
////			System.out.println(new Ray(intersection,sunVect));
//			if(dot > 0 && !rayIntersects(new Ray(intersection,sunVect),s))
//				temp = temp.add(sun.getEmission().scale(dot*Math.PI*Math.pow(sun.size,2)));
//		}
		return temp;
    }
    private Radiance getAtmosphericSamples(Ray r, int samples, int depth) {
		Radiance tot = Raytrace(r, null, samples, depth-1, true);
//		if(sun != null) {
//			Ray sunRay = new Ray(r.origin, sun.generateVector());
//			if(!rayIntersects(sunRay,null))
//				tot = tot.add(sun.getEmission().scale((1)*(Math.PI*Math.pow(sun.size,2))));
//		}
		return tot;
	}
    private Radiance direct(Shape s, Point intersection, Vector n, int samples) {
		Point lightPt;
		Shape light;
		Radiance estimation = new Radiance(0,0,0);
		for(int i=0;i<samples;i++) {
			light = getRandomLight();
			lightPt = light.getRandomPoint();
			Ray l = new Ray(intersection,lightPt);
			double brdf = l.dir.normalize().dot(n.normalize());
			double pa = light.normal(lightPt).normalize().dot(l.dir.negative().normalize());
			if(!light.useNormalForIllum())pa = Math.abs(pa);
			if(brdf > 0 && pa > 0) {
				Radiance Le = light.mat.getEmission();
				double dist = intersection.dist(lightPt);
				double g = 1/Math.pow(dist,2);
				double v = (segIntersect(l,dist,s,light)!=null?0:1);
				double p = light.getSurfaceArea(); //FIX TO VISIBLE AREA?
				estimation = estimation.add(Le.scale(brdf*g*v*(s.mat.diffWeight/2)*p*pa));
			}
		}
		estimation = estimation.scale(1/samples);
		return estimation;
	}
	private Radiance indirect(Ray r, Shape s, Point intersection, Vector n, int samples, int depth) {
		Vector v = r.dir.negative();
		List<Radiance> rads = new LinkedList<Radiance>();
        double r1;
        double kk = s.mat.diffWeight+s.mat.specWeight;
        double kd = s.mat.diffWeight/(kk);
        //double kd = s.mat.diffWeight/2;
        //double ks = s.mat.specWeight/2;
        //double kk = kd + ks;
        Radiance tempRad;
        Ray newRay;
        if(s.mat.specWeight == 2 && s.mat.specExp > 100000) {
            tempRad = specularSample(s, v, intersection, n, samples, depth);
            return tempRad;
        }
        for(int i = 0; i < samples; i++) {
            r1 = Math.random();
            if(r1 < kd) {
                tempRad = diffuseSample(s, v, intersection, n, samples, depth);
            } else /*if(r1 < kk)*/ {
                tempRad = specularSample(s, v, intersection, n, samples, depth);
            } //else {
                //tempRad = new Radiance(0,0,0);
            //}
            rads.add(tempRad);
        }
        Radiance tot = Radiance.avg(rads).scale(kk/2);
        return tot;
    }
	private Shape getRandomLight() {
		if(emitters==null) fillEmitters();
		int index = (int)(Math.random()*emitters.size());
		return emitters.get(index);
	}
	private void fillEmitters() {
		emitters = new ArrayList<Shape>();
		for(Shape s : shapes) {
			if(s.mat.emit>0) emitters.add(s);
		}
	}
}
