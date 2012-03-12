package raytracer;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import raytracer.geom.*;
import raytracer.Radiance;
import raytracer.util.DataHolder;

public class Camera implements DataHolder{
    public Point pos;
    public double zoom;
    public double exposure;
    public double aperture;
    public double focus;

    //currend render:
    private int width, height;
	public int AA, PATHS, DEPTH, NUMTHREADS;
    public World world;
    public Radiance[][] allRads;
    public RenderWindow window;
    public int loop;
	
	private double wid, hei;
    public double inv;
    public double planeScale;

	//private Thread render;
    private LinkedList<RenderThread> threads;
    private LinkedList<Rectangle> patches;
    public final Queue<Rectangle> patchQueue;

	public Camera(Point pos, double zoom, double exposure, double aperture, double focus) {
		patches = new LinkedList<Rectangle>();
        patchQueue = new LinkedList<Rectangle>();
        threads = new LinkedList<RenderThread>();
		this.pos = pos;
		this.zoom = zoom;
		this.exposure = exposure;
		this.aperture = aperture;
		this.focus = focus;
	}
    public Camera(Point pos) {
		this(pos, 1,1,.001,6);
    }
	public Camera() {
		this(new Point(0,0,0));
	}
	public void killThreads() {
		for(RenderThread t : threads) {
			t.interrupt();
		}
	}
	public String getName() {
		return "";
	}
	public Map<String, Object> getDataMap() {
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("01 POS", pos);
		map.put("02 ZOOM",zoom);
		map.put("03 EXPOSURE", exposure);
		map.put("04 APERTURE", aperture);
		map.put("05 FOCUS", focus);
		map.put("06 WIDTH", width);
		map.put("07 HEIGHT", height);
		map.put("08 AA", AA);
		map.put("09 PATHS", PATHS);
		map.put("10 DEPTH", DEPTH);
		map.put("11 NUMTHREADS", NUMTHREADS);
		map.put("12 LOOP", loop);
		map.put("13 ALLRADS", allRads);
		return map;
	}

    public void setPosition(Point pt) {
        pos = pt;
    }
    public void setZoom(double z) {
        zoom = z;
    }
    public void setExposure(double e) {
        exposure = e;
    }
    public void setAperture(double a) {
        aperture = a;
    }
    public void setFocus(double f) {
        focus = f;
    }
    public void setSize(int x) {setSize(x,x);}
    public void setSize(int width1, int height1) {
        width = width1;
        height = height1;
        wid = 1;
        hei = height*wid/width;
        planeScale = focus/zoom;
    }
    public Point getRandAptPt() {
        double y = Math.sqrt(Math.random()) * aperture;
        double r1 = Math.random() * 2 * Math.PI;
        Point p = new Point(Math.sin(r1)*y, Math.cos(r1)*y, 0);
        return p.add(pos);
//		double a2 = 2*aperture;
//		return new Point(pos.x+((Math.random()-.5)*a2),pos.y+((Math.random()-.5)*a2),pos.z);
    }
    public Point getSquarePt(double x1, double y1) {
        double X = ((x1/width - 0.5)*wid)*planeScale;
        double Y = ((y1/height - 0.5)*hei)*planeScale;
        Point p = new Point(X,-Y,0);
        return p.add(pos).add(new Point(0,0,focus));
    }
    public List<Ray> getRandRays(int x, int y) {
        List<Ray> r = new LinkedList<Ray>();
        for(int i = 0; i < AA; i++) {
            r.add(new Ray(getRandAptPt(),
                          getSquarePt(x + 1.05*(.5-Math.random()), y + 1.05*(.5-Math.random()))));
        }
        return r;
    }
    /*public Ray getSquareRay(double x1, double y1) {
        double X = ((x1/width - 0.5)*wid);
        double Y = ((y1/height - 0.5)*hei);
        return new Ray(pos, new Point(X, -Y, zoom));
    }
    public List<Ray> getRandRays(int x, int y) {
        List<Ray> r = new LinkedList<Ray>();
        int totSamps = (int)Math.pow(AA, 2);
        for(int i = 0; i < AA*AA; i++) {
            r.add(getSquareRay(x + 1.05*(.5-Math.random()), y + 1.05*(.5-Math.random())));
        }
        return r;
    }*/
    
    public void refill() {
        for(Rectangle p : patches) {
            patchQueue.add(p);
        }
        loop++;
        inv = 1.0/loop;
        window.statsWindow.setSamples(loop-1);
    }
    public RPanel getPanel() {
        return window.pan;
    }
	public void polaroid(int x, int y, World tw, RenderWindow w, int AAsamp, int pathSamp, int depth, int numThreads) {
		setSize(x,y);
		world = tw; AA = AAsamp; PATHS = pathSamp; DEPTH = depth; NUMTHREADS = numThreads;
        window = w;
		polaroid();
	}
    public void polaroid() {
        for(int i = 0; i < NUMTHREADS; i++) {
            threads.add(new RenderThread(this));
        }

		allRads = new Radiance[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				allRads[x][y] = new Radiance(0,0,0);
			}
		}

        int amtRads = 0;
        int i = 1;
        int d = 50;
        int ny = 0, nx = 0;
        for(int y = 0; y < height; y+=d) {
            ny = y + d;
            for(int x = 0; x < width; x+=d) {
                nx = x + d;
                patches.add(new Rectangle(x,y,Math.min(width, nx)-x,Math.min(height, ny)-y));
            }
        }

        loop = 0;
        
        for(RenderThread t : threads) {
            t.start();
        }
    }
    public Radiance Raytrace(Ray r) {
        return world.Raytrace(r, null, PATHS, DEPTH, true);
    }

    public Color getScaledColor(Radiance r) {
        r = r.scale(exposure);
        Radiance scaled = new Radiance(rgbToSrgb(r.r),rgbToSrgb(r.g),rgbToSrgb(r.b));
        return scaled.toColor();
    }
    public double rgbToSrgb(double val) {
        if(val <= .0031308) return 12.92*val;
        return ((1+.055)*Math.pow(val,1/2.4))-.055;
    }

}
