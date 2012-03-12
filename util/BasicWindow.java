package raytracer.util;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JFrame;

public class BasicWindow extends JFrame {
    public BasicWindow(String name) {this(name, 640, 480, true);}
    public BasicWindow(String name, int width, int height, boolean resizable) {
        super(name);
		this.setPreferredSize(new Dimension(width,height));
		this.setResizable(resizable);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			Insets ins = getInsets();
        setSize(getPreferredSize().width + ins.left + ins.right, getPreferredSize().height + ins.bottom + ins.top);
		}
	}
}
