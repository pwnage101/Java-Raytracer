package raytracer;

import java.awt.*;
import java.util.Queue;
import java.awt.image.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class RPanel extends JPanel {
    public Boolean highlight;

    public Image img, back;
    private int delta, width, height;
    private int cX, cY;
    private Color cCol;
    private Graphics imgGr,backGr;

    public final Queue<Rectangle> paintQueue;

    public RPanel(int width1, int height1) {
        width = width1;
        height = height1;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        back = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imgGr = img.getGraphics();
        imgGr.setColor(new Color(190,190,190));
        imgGr.fillRect(0, 0, width, height);
        backGr = back.getGraphics();
        delta = height / 10;
        paintQueue = new LinkedList<Rectangle>();
        highlight = false;
    }
    public void update(Graphics g) {paint(g);}
    public void paint(Graphics g) {
            g.drawImage(back, 0, 0, this);
    }
    public void paint(Color col, int x, int y) {
        cX = x;
        cY = y;
        cCol = col;
        Graphics g = img.getGraphics();
        g.setColor(col);
        g.fillRect(x,y,1,1);
        //if((x+1) % delta == 0 && (y+3 >= height)) {
        //    repaint();
        //}

    }
    public void refresh() throws InterruptedException {
        backGr.drawImage(img, 0, 0, this);
        refresh();
        //paintQueue.add(null);
        //paintQueue.notify();
    }
    public void shift(int x, int y, int w, int h) {
        backGr.setClip(x,y,w,h);
        backGr.drawImage(img, 0, 0, this);
    }
    public void refresh(int x, int y, int w, int h) {
        repaint(x,y,w,h);
        //synchronized(paintQueue) {
        //    paintQueue.add(new Rectangle(x,y,w,h));
        //    paintQueue.notify();
        //}
    }
    public void highlight(int x, int y, int w, int h) {
        if(highlight) {
			backGr.setClip(x,y,w+1,h+1);
			backGr.setColor(Color.cyan);
			backGr.drawRect(x,y,w,h);
        }
    }
}
