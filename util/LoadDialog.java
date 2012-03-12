package raytracer.util;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.*;

public class LoadDialog extends BasicWindow implements Runnable, WindowFocusListener {
	JLabel noteLabel;
	String note;
	Frame parent;
	public LoadDialog(Frame parent, String title, String note) {
		super(title,200,60,false);
		
		this.parent = parent;
		//this.setUndecorated(true);
		parent.addWindowFocusListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(parent);
		this.note = note;
		noteLabel = new JLabel(note);
		this.getContentPane().add(noteLabel);
	}
	public void beginAnimation() {
		new Thread(this).start();
	}
	public void run() {
		int i = 0;
		String[] dots = {" ."," . ."," . . ."};
		try {
			while(this.isVisible()) {
				noteLabel.setText(note + dots[i%3]);
				i++;
				Thread.sleep(700);
			}
		} catch (InterruptedException ex) {}
	}

	public void windowGainedFocus(WindowEvent e) {
		if(this.getState() == Frame.ICONIFIED)
			this.setState(Frame.NORMAL);
		this.toFront();
		this.setLocationRelativeTo(parent);
	}
	public void windowLostFocus(WindowEvent e) {}
	
}
