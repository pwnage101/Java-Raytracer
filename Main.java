package raytracer;

import java.awt.Color;
import java.io.*;
import raytracer.geom.*;
import raytracer.util.LoadDialog;


public class Main {
    public static void main(String[] args) {

//		Vector d1 = new Vector(-3,2,-1);
//		Vector o1 = new Vector(2,-1,4);
//		Vector d2 = new Vector(1,-3,-4);
//		Vector o2 = new Vector(5,0,1);
//		Vector n = d1.cross(d2);
//		Vector v = o2.subtract(o1);
//		System.out.println("DOT:" + Math.abs((v.dot(n.normalize()))));
//		System.exit(0);

		RenderWindow renderWindow = new RenderWindow(600,600);
		renderWindow.setVisible(true);
		new Thread(renderWindow).start();

		Scene s = new Scene("newScene",renderWindow);

		s.makeCornellBox();
        //s.makeOutdoorScene();
        //s.makeDofScene();
		//tw.makeFogScene();
		//tw.makeIndoorFogScene();
		//tw.makeTestScene();

        s.render(600,600,1, 1, 8, 8);

    }
}
