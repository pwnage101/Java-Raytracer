package raytracer;

import java.util.logging.Level;
import java.util.logging.Logger;
import raytracer.util.BasicWindow;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import raytracer.util.DataHolder;
import raytracer.util.FileIO;

public class RenderWindow extends BasicWindow implements Runnable, KeyListener, WindowFocusListener{
    public Scene scene;
	public RPanel pan;
	public StatsWindow statsWindow;

	private int action;
	private final Object lock = new Object();

    public RenderWindow(int width, int height) {
        super("RenderWindow",width, height,false);
		pan = new RPanel(width, height);
        getContentPane().add(pan);
		statsWindow = new StatsWindow();
		repositionStats();
		new Thread(statsWindow).start();
		this.addKeyListener(this);
		this.addWindowFocusListener(this);
		action = -1;
    }
    public void setPixel(Color col, int x, int y) {
        pan.paint(col, x, y);
    }
    // RETURNS: resulting render
    public Image getImage() {
        return pan.img;
    }

	// EVENTS:
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 83 && e.isControlDown() && e.isShiftDown()){
			synchronized(lock) {
				action = 2;//openSaveScene();
				lock.notifyAll();
			}
		} else if(e.getKeyCode() == 83 && e.isControlDown()) {
			synchronized(lock) {
				action = 1;//openSaveImage();
				lock.notifyAll();
			}
		} else if(e.getKeyCode() == 83) {
			toggleStats();
		} else if(e.getKeyCode() == 79 && e.isControlDown()) {
			synchronized(lock) {
				action = 3;//openOpenScene();
				lock.notifyAll();
			}
		}
	}
	public void windowGainedFocus(WindowEvent e) {
		if(statsWindow.getState() == Frame.ICONIFIED)
			statsWindow.setState(Frame.NORMAL);
		//new Thread(this).start();
		System.out.println("FOCUS GAINED");
	}
	public void windowLostFocus(WindowEvent e) {
	}

	//ACTIONS:
	public void openSaveImage() {
		FileIO.imageToPNG(this, getImage());
	}
	public void openSaveScene() {
		FileIO.SceneToFile(this, scene);
	}
	public void openOpenScene() {
		Scene newScene = FileIO.SceneFromFile(this);
		if(newScene != null) {
			scene.camera.killThreads();
			scene = newScene;
		}
	}
	public void repositionStats() {
		System.out.println("repositionStats");
		Point loc = this.getLocation();
		loc.translate(this.getWidth(),0);
		statsWindow.setLocation(loc);
	}
	public void toggleStats() {
		statsWindow.setVisible(!statsWindow.isVisible());
		if(statsWindow.isVisible()) {
			repositionStats();
			new Thread(statsWindow).start();
		}
	}
	public void takeAction() {
		if(action >= 0) {
			if(action == 1) openSaveImage();
			else if(action == 2) openSaveScene();
			else if(action == 3) openOpenScene();
			action = -1;
		}
	}
	public void run() {
		while(Thread.currentThread().isAlive()) {
			try {
				synchronized(lock) {
					lock.wait();
					takeAction();
				}
			} catch (InterruptedException ex) {
				
			}
		}
	}
}
