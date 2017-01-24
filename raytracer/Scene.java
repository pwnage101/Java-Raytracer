package raytracer;

import java.awt.Color;
import java.util.*;
import raytracer.geom.*;
import raytracer.geom.Vector;
import raytracer.util.DataHolder;

public class Scene implements DataHolder {
	public String name;
	public World world;
	public Camera camera;
	public RenderWindow renderWindow;

	public Scene(String name, World world, Camera camera, RenderWindow rw) {
		this.name = name;
		this.world = world;
		this.camera = camera;
		renderWindow = rw;
		rw.scene = this;
	}
	public Scene(String name, RenderWindow rw) {
		this(name,new World(),new Camera(),rw);
	}
	public Map<String, Object> getDataMap() {
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("01 NAME", name);
		map.put("02 WORLD", world);
		map.put("04 CAMERA", camera);
		return map;
	}
	public String getName() {
		return name;
	}
	public void render(int x, int y, int aa, int ps, int d, int nt) {
//		renderWindow = new RenderWindow(this,x,y);
//		renderWindow.setVisible(true);
//		new Thread(renderWindow).start();
		camera.polaroid(x,y,world, renderWindow, aa, ps, d, nt);
	}
	public void resume() {
//		renderWindow.setVisible(true);
//		new Thread(renderWindow).start();
		camera.polaroid();
	}

	public void makeCornellBox() {
		camera.setAperture(0);
		//camera.setExposure(1);
		//camera.setFocus(4);
		//camera.setZoom(1);
		world.setSky(null);
		//world.setAtmosphere(new Atmosphere(5,new Color(255,255,255), 50));
		world.setAtmosphere(null);

        Material plainWall = new Material(new Color(255, 255, 255),1.5,0,0);
        //Material plainWall = new Material(new Color(255, 255, 190),1,1,Double.POSITIVE_INFINITY);

        Material glossyWall = new Material(new Color(255,255,255),0,1,200);

        Material redWall = new Material(new Color(255,20,20),1.5,0,0);
        //Material redWall = new Material(new Color(255,20,20),1,1,Double.POSITIVE_INFINITY);

        Material greenWall = new Material(new Color(20,255,20),1.5,0,0);
        //Material greenWall = new Material(new Color(20,255,20),1,1,Double.POSITIVE_INFINITY);

        Material whiteDiffuse = new Material(new Color(255,255,255),1,0,0);
        //Material whiteDiffuse = new Material(new Color(255,255,255),1,1,Double.POSITIVE_INFINITY);

        Material glossyBlue = new Material(new Color(85,85,255),.5,1,10);

        Material totalReflect = new Material(new Color(255,255,255),0,1,Double.POSITIVE_INFINITY);

        Material light = new Material(new Color(255,255,150),20);


        // front wall
        world.addSquare(new Point( 2d, -2d, 10d),
                  new Point(-2d, -2d, 10d),
                  new Vector(0d,1d,0d),
                  4d,
                  plainWall);

        // back wall
//        world.addSquare(new Point( 2d, -2d, -0.5d),
//                  new Point(-2d, -2d, -0.5d),
//                  new Vector(0d,1d,0d),
//                  4d,
//                  plainWall);

        // ceiling
        world.addSquare(new Point( 2d, 2d, 8d),
                  new Point(-2d, 2d, 8d),
                  new Vector(0d,0d,-1d),
                  8.5d,
                  plainWall);

        // floor
        world.addSquare(new Point( 2d, -2d, 8d),
                  new Point(-2d, -2d, 8d),
                  new Vector(0d,0d,-1d),
                  8.5d,
                  plainWall);

        // left (red) wall
        world.addSquare(new Point(-2d, 2d,8d),
                  new Point(-2d,-2d,8d),
                  new Vector(0d,0d,-1d),
                  8.5d,
                  redWall);


        // right (green) wall
        world.addSquare(new Point(2d, 2d,8d),
                 new Point(2d,-2d,8d),
                 new Vector(0d,0d,-1d),
                 8.5d,
                 greenWall);

        // ceiling light
        Point p1 = new Point(-.5,1.90f,5.5);
        Point p2 = new Point(-.5,1.90f,6.5);
        Point p3 = new Point( .5,1.90f,6.5);
        Point p4 = new Point( .5,1.90f,5.5);
        world.addSquare(p1,p2,new Vector(0,1,0), 0.3f, plainWall);
        world.addSquare(p2,p3,new Vector(0,1,0), 0.3f, plainWall);
        world.addSquare(p3,p4,new Vector(0,1,0), 0.3f, plainWall);
        world.addSquare(p4,p1,new Vector(0,1,0), 0.3f, plainWall);
        world.addSquare(p1,p3,p2,light);

        // test diffusion
        world.addBottomlessCube(new Point(1.7d, -2d, 6.3d),
                  new Point(0.1d, -2d, 6.0d),
                  new Vector(0d,1d,0d),
                  2d,
                  whiteDiffuse);

        //top sphere
        world.addSphere(new Sphere(new Point(0.9,.5,6.15),
                  0.5,
                  glossyBlue));

        //sphere
        world.addSphere(new Sphere(new Point(-0.5,-1.4,5.0),
                             0.6,
                             totalReflect));
    }
	public void makeCopyBox() {
		world.setSky(null);
		world.setAtmosphere(null);
		Material white = new Material(new Color(255,255,255),2,0,0);
		Material red = new Material(new Color(255,0,0),2,0,0);
		Material green = new Material(new Color(0,255,0),2,0,0);
		Material light = new Material(new Color(255,255,100),30);

	}
    public void makeOutdoorScene() {
        Material glossyWall = new Material(new Color(255,255,230),0,1.75,100);
        Material diffuseWall = new Material(new Color(255,255,230),1.75,0,100);

        Material whiteDiffuse = new Material(new Color(255,255,255),1,0,100);
        Material glossyBlue = new Material(new Color(85,85,255),.5,1.1,10);
        Material totalReflect = new Material(new Color(255,255,255),1,0,Double.POSITIVE_INFINITY);

        Material dimLight = new Material(new Color(255,255,255),5);
        Material light = new Material(new Color(255,255,255),50);
        Material brightLight = new Material(new Color(255,255,100),300);

        // floor
        world.addSquare(new Point( 250d, -2d, -250d),
                  new Point(-250d, -2d, -250d),
                  new Vector(0d,0d,1d),
                  500d,
                  glossyWall);

        // test diffusion
        world.addBottomlessCube(new Point(1.7d, -2d, 9.3d),
                  new Point(0.1d, -2d, 9.0d),
                  new Vector(0d,1d,0d),
                  2.5d,
                  whiteDiffuse);

        //top sphere
        world.addSphere(new Sphere(new Point(0.9,1,9.15),
                  0.5,
                  glossyBlue));

        //sphere
        world.addSphere(new Sphere(new Point(-0.5,-1.3,8.0),
                             0.7,
                             totalReflect));

        //sun
        //addSphere(new Sphere(new Point(1000.0,1000.0,-1000.0),
         //                    300,//125
         //                    light));

        //temporary
        world.addSphere(new Sphere(new Point(2.9,1,8.15),
                             0.6,
                             dimLight));
        world.addSphere(new Sphere(new Point(-3.5,-1.3,8.0),
                             0.6,
                             dimLight));
    }
    public void makeDofScene() {
		world.setSky(null);
		world.setAtmosphere(null);
		camera.setAperture(0);
		//camera.setZoom(camera.zoom/2);
		//world.setAtmosphere(new Atmosphere(50,Color.WHITE,15));
        Material diffuseWall = new Material(new Color(255,255,230),.5,0,Double.POSITIVE_INFINITY);
		Material redWall = new Material(new Color(255,0,0),.5,0,Double.POSITIVE_INFINITY);
        Material light = new Material(new Color(255,255,220),40);// WAS 200
		//Material light = new Material(new Color(255,255,230),.5,0,Double.POSITIVE_INFINITY);

        Material blueMirror = new Material(new Color(50,50,220),1,.7,Double.POSITIVE_INFINITY);
        Material lightBlueMirror = new Material(new Color(150,150,255),1,.7,Double.POSITIVE_INFINITY);
        Material redMirror = new Material(new Color(220,50,50),1,.7,Double.POSITIVE_INFINITY);
        Material purpleMirror = new Material(new Color(200,50,200),1,.7,Double.POSITIVE_INFINITY);
        Material orangeMirror = new Material(new Color(255,140,50),1,.7,Double.POSITIVE_INFINITY);
        Material greenMirror = new Material(new Color(50,255,50),1,.7,Double.POSITIVE_INFINITY);
        //Material greenMirror = new Material(new Color(50,255,50),30);
        Material whiteMirror = new Material(new Color(255,255,255),1,.7,Double.POSITIVE_INFINITY);

        //DOF testers
//        world.addSphere(new Sphere(new Point(-.7,-.6,2),.4,lightBlueMirror));
        world.addSphere(new Sphere(new Point(1.2,-.6,3),.4,whiteMirror));
        world.addSphere(new Sphere(new Point(.3,-.7,4),.3,greenMirror));
		//world.addSphere(new Sphere(new Point(0,-.5,3.5),.3,greenMirror));
        world.addSphere(new Sphere(new Point(-2,.5,6),1.5,purpleMirror));
        world.addSphere(new Sphere(new Point(1,-.2,6),.8,blueMirror));
        world.addSphere(new Sphere(new Point(-1,0,10),1,orangeMirror));
        world.addSphere(new Sphere(new Point(5,1,15),2,redMirror));
		//ground
		double r = 2000;
		Point c = new Point(.3,-.7,4).add(0,-r-.3,0);
		world.addSphere(new Sphere(c,r,diffuseWall));
        //sun
        world.addSphere(new Sphere(new Point(2000.0,1000.0,-1000.0),
                             300,
                             light));
    }
	public void makeFogScene() {
		world.setSky(null);
		world.setAtmosphere(new Atmosphere(15, new Color(255,255,255),50));
		Material light = new Material(new Color(255,255,255), 30);
		Material grey = new Material(new Color(150,150,150),1,0,0);
		Material black = new Material(new Color(20,20,20),0.2,0,0);

//		world.addSquare(new Point(-100,100,11),new Point(100,100,11),new Vector(0,-1,0),200,black);
		world.addSphere(new Sphere(new Point(0,0,10),.5,light));
//		world.addSquare(new Point(-1,0,8),new Point(1,0,8),new Vector(0,-1,0),2,black);
//		world.addSphere(new Sphere(new Point(.5,.5,8),.5,grey));

		world.addBottomlessCube(new Point(.292,-.51,9), new Point(1,.51,9.708),
						  new Vector(-1,0,1),
						  2,
						  grey);
	}
	public void makeIndoorFogScene() {
		//world.setSun(new Sun(.06,340,new Color(255,255,200),new Vector(1,1,0.2).normalize()));
		world.setAtmosphere(new Atmosphere(50000, new Color(255,255,255), 10));

		Material plainWall = new Material(new Color(255,255,255), 1,0,0);

		world.addSquare(new Point( 250d, -2d, -250d),
                  new Point(-250d, -2d, -250d),
                  new Vector(0d,0d,1d),
                  500d,
                  plainWall);
		// front wall
        world.addSquare(new Point( 2d, -2d, 8d),
                  new Point(-2d, -2d, 8d),
                  new Vector(0d,1d,0d),
                  4d,
                  plainWall);

        // back wall
        world.addSquare(new Point( 2d, -2d, -0.5d),
                  new Point(-2d, -2d, -0.5d),
                  new Vector(0d,1d,0d),
                  4d,
                  plainWall);

        // ceiling
        world.addSquare(new Point( 2d, 2d, 7.5d),
                  new Point(-2d, 2d, 7.5d),
                  new Vector(0d,0d,-1d),
                  8d,
                  plainWall);

        // floor
        world.addSquare(new Point( 2d, -2d, 8d),
                  new Point(-2d, -2d, 8d),
                  new Vector(0d,0d,-1d),
                  8.5d,
                  plainWall);

        // left wall
        world.addSquare(new Point(-2d, 2d,8d),
                  new Point(-2d,-2d,8d),
                  new Vector(0d,0d,-1d),
                  8.5d,
                  plainWall);


        // right wall
        world.addSquare(new Point(2d, 2d,8d),
                 new Point(2d,-2d,8d),
                 new Vector(0d,0d,-1d),
                 8.5d,
                 plainWall);
	}
	public void makeTestScene() {
		//world.setSun(new Sun(.06,340,new Color(255,255,200),new Vector(0,0,1).normalize()));
		world.setAtmosphere(new Atmosphere(5, new Color(255,255,255), 50));

	}
}
