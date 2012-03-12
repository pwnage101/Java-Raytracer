package raytracer.util;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import raytracer.Material;
import raytracer.Radiance;
import raytracer.Scene;
import raytracer.geom.*;

public class FileIO {
//	private Frame parent;
//	private Image img;
//	private Scene sc;
//	private String s1, s2;
//	private BufferedWriter bw;
//	private Object o1;
//
//	public static int 0 = IMAGE_PNG;
//	public static int 1 = SCENE_FILE;
//
//	public void run() {
//
//	}
	public static void imageToPNG(Frame parent, Image img) {
			FileDialog saveMenu = new FileDialog(parent,"Save Image",FileDialog.SAVE);
			saveMenu.setVisible(true);
			String name = saveMenu.getFile();
			String dir = saveMenu.getDirectory();
			if(name != null) {
				try {
					ImageIO.write((RenderedImage)img, "png", new File(dir+name+
					((name.endsWith(".png")||name.endsWith(".PNG"))?"":".png")));
					System.out.println("Image save successfull!");
				} catch (IOException ex) {
					System.out.println("Image save error.");
					System.out.println("Error: " + ex.getMessage());
				}
			}
	}
	public static void SceneToFile(Frame parent, Scene s) {
			LoadDialog l = new LoadDialog(parent, "Save Scene","SAVING");
			FileDialog saveMenu = new FileDialog(parent,"Save Scene",FileDialog.SAVE);
			saveMenu.setVisible(true);
			String name = saveMenu.getFile();
			String dir = saveMenu.getDirectory();
			if(name != null) {
				l.setVisible(true);
				l.beginAnimation();
				try {
					SceneToFile(s, dir + addExtension(name, "ass"));
					System.out.println("Scene save successfull!");
				} catch (IOException ex) {
					System.out.println("Scene save error.");
					System.out.println("Error: " + ex.getMessage());
				}
				l.dispose();
			}
	}
	private static void SceneToFile(Scene s, String path) throws IOException {
		FileWriter fwriter = new FileWriter(path);
		BufferedWriter writer = new BufferedWriter(fwriter);
		objToString(s,writer,0);
		writer.close();
	}
	public static Scene SceneFromFile(Frame parent) {
		return null;
	}

	private static String addExtension(String name, String ext) {
		return name+((name.endsWith("." + ext)||name.endsWith("." + ext.toUpperCase()))?"":"."+ext);
	}
	public static void objToString(Object o, BufferedWriter w, int depth) throws IOException {
		if(o instanceof DataHolder) {
			String indent = getIndent(depth);
			DataHolder dh = (DataHolder)o;
			Map<String,Object> map = dh.getDataMap();
			Iterator<String> iter = map.keySet().iterator();
			String s;
			while(iter.hasNext()) {
				s = iter.next();
				w.write(indent + s + ":");
				if(map.get(s) instanceof DataHolder)
					w.newLine();
				objToString(map.get(s),w,depth+1);
				if(iter.hasNext()) w.newLine();
			}
		} else if(o instanceof String){
			w.write((String)o);
		} else if(o instanceof Double) {
			w.write( Double.toString((Double)o));
		} else if(o instanceof Integer) {
			w.write( Integer.toString((Integer)o));
		} else if(o instanceof Long) {
			w.write( Long.toString((Long)o));
		} else if(o instanceof raytracer.geom.Point) {
			raytracer.geom.Point p = (raytracer.geom.Point)o;
			w.write("["+p.x+","+p.y+","+p.z+"]");
		} else if(o instanceof Vector) {
			Vector v = (Vector)o;
			w.write("["+v.x+","+
						v.y+","+
						v.z+"]");
		} else if(o instanceof Color) {
			Color col = (Color)o;
			w.write("["+col.getRed()+","+
						col.getGreen()+","+
						col.getBlue()+"]");
		} else if(o instanceof Radiance) {
			Radiance rad = (Radiance) o;
			w.write(doubleToString(rad.r)+
					doubleToString(rad.g)+
					doubleToString(rad.b));
		} else if(o instanceof Radiance[][]) {
			Radiance[][] rads = ((Radiance[][]) o).clone();
			for(int r = 0; r < rads.length; r++){
				for(int c = 0; c < rads[0].length; c++) {
					objToString(rads[r][c],w,depth);
				}
			}
		} else if(o instanceof Tri) {
			Tri t = (Tri)o;
			objToString(t.mat.tag,w,0);
			objToString(t.p1,w,0);
			objToString(t.p2,w,0);
			objToString(t.p3,w,0);
		} else if(o instanceof Sphere) {
			Sphere s = (Sphere)o;
			w.write(s.mat.tag);
			objToString(s.center,w,0);
			objToString(s.radius,w,0);
		} else if(o instanceof LinkedList) {
			w.newLine();
			String indent = getIndent(depth);
			LinkedList list = (LinkedList)o;
			boolean dh = false;
			if(list.getFirst() instanceof DataHolder) dh = true;
			Iterator iter = list.iterator();
			Object s;
			while(iter.hasNext()) {
				s = iter.next();
				if(dh) {
					w.write(indent+((DataHolder)s).getName());
					w.newLine();
				} else {
					w.write(indent);
				}
				objToString(s,w,depth+1);
				if(iter.hasNext()) {
					w.newLine();
					if(dh) w.newLine();
				}
			}
		} else if(o == null) {
			w.write("null");
		}
	}
	public static Object valueOfStr(String s) {
		return null;
	}
	public static String doubleToString(double d) {
    	Long bits = Double.doubleToLongBits(d);
    	String st = longBitsToString(bits);
    	int add = 4-st.length();
		for(int i = 0; i < add; i++) {
			st = "0" + st;
		}
		return st;
    }
    public static String longBitsToString(Long L) {
    	String convert = "";
    	long remain;
    	long digit = Character.MAX_VALUE;
    	while(L!=0) {
    		remain = L%digit;
    		convert = (char)remain + convert;
    		L /= digit;
    	}
    	return convert;
    }
    public static double stringToDouble(String s) {
    	char ch;
		int remove = 0;
		for(int i = 0; i < s.length(); i++) {
			ch = s.charAt(i);
			if(ch == '0') remove++;
			else break;
		}
		s = s.substring(remove,s.length());
    	Long bits = stringToLongBits(s);
    	return Double.longBitsToDouble(bits);
    }
    public static long stringToLongBits(String s) {
    	long total = 0;
    	long digit = 1;
    	for(int i = s.length()-1; i >= 0; i--) {
    		total += digit*((int)s.charAt(i));
    		digit *= Character.MAX_VALUE;
    	}
    	return total;
    }

	private static String getIndent(int depth) {
		String s = "";
		for(int i = 0; i < depth; i++) {
			s += "	";
		}
		return s;
	}
}
