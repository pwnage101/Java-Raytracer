package raytracer;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.*;
import raytracer.geom.*;
import raytracer.util.DataHolder;

public class RenderThread extends Thread {
    private Camera cam;
    public RenderThread(Camera c) {
        cam = c;
    }
    public void run() {
        Rectangle patch;
		try{
			while(this.isAlive()) {
				synchronized(cam.patchQueue) {
					if(cam.patchQueue.isEmpty()) cam.refill();
					patch = cam.patchQueue.remove();
				}
				renderClip(patch,cam.inv);
			}
		} catch(InterruptedException e) {
			System.out.println("Thread Killed.");
		}
    }
    private void renderClip(Rectangle p, double inv) throws InterruptedException {
        renderClip(p.x,p.y,p.width,p.height,inv);
    }
    private void renderClip(int x, int y, int width, int height, double inv) throws InterruptedException {
        cam.getPanel().highlight(x,y,width-1,height-1);
        cam.getPanel().refresh(x,y,width,height);
        for(int y1 = y; y1 < height+y; y1++) {
            for(int x1 = x; x1 < width+x; x1++) {
                List<Ray> rays = cam.getRandRays(x1,y1);
                LinkedList<Radiance> rads = new LinkedList<Radiance>();
                for (Ray r : rays) {
                    rads.add(cam.Raytrace(r));
                }
                Radiance avg = Radiance.avg(rads);
                cam.allRads[x1][y1] = cam.allRads[x1][y1].add(avg);
                cam.window.setPixel(cam.getScaledColor(cam.allRads[x1][y1].scale(inv)), x1, y1);
				if(isInterrupted()) throw new InterruptedException();
			}
        }
        cam.getPanel().shift(x,y,width, height);
        cam.getPanel().refresh(x,y,width,height);
    }
    
}
