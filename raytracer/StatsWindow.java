package raytracer;

import raytracer.util.BasicWindow;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class StatsWindow extends BasicWindow implements Runnable {
	public int updateInterval = 1000;
	private long start;
	private long lastUpdate;

	private int samples;
	private boolean samplesUpdated;

	private JLabel timeLabel;
	private JLabel samplesLabel;

	public StatsWindow() {
		super("Stats",190,70,false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		start = System.currentTimeMillis();
		samples = 0;
		samplesUpdated = false;
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		timeLabel = new JLabel("TIME: 00:00:00");
		samplesLabel = new JLabel("Samples/px: 0");

		this.getContentPane().add(timeLabel);
		this.getContentPane().add(samplesLabel);
	}
	public void run() {
		lastUpdate = System.currentTimeMillis();
		timeLabel.setText("TIME: ");
		samplesLabel.setText("Samples/px: ");
		samplesUpdated = true;
		while(this.isVisible()) {
			try { Thread.sleep(updateInterval - (lastUpdate - start) % updateInterval);
			} catch (InterruptedException ex) {}
			lastUpdate = System.currentTimeMillis();

			timeLabel.setText("TIME: " + getCurrentTime());
			if(samplesUpdated) {
				samplesLabel.setText("Samples/px: " + samples);
				samplesUpdated = false;
			}
		}
	}
	public void setSamples(int n) {samples = n; samplesUpdated = true;}
	private String getCurrentTime() {
		long secs = (lastUpdate - start) / 1000;
		int hrs = (int) (secs / (3600));
		secs %= 3600;
		int mins = (int) (secs / 60);
		secs %= 60;
		String h = String.valueOf(hrs);
		String m = String.valueOf(mins);
		String s = String.valueOf(secs);
		return (h.length()<2 ? "0"+h : h) + ":" +
			   (m.length()<2 ? "0"+m : m) + ":" +
			   (s.length()<2 ? "0"+s : s);
	}

}
