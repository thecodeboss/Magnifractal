import java.lang.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.MouseInfo;
import java.awt.Point;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.*;

public class Magnifractal extends JPanel implements MouseWheelListener, KeyListener {
	static JFrame frame = new JFrame("Mandelbrot");
	static int WIDTH = 800;
	static int HEIGHT = 600;
	static double ZOOM_RATE = 0.95;
	static double B_ZOOM_RATE = 0.99995;
	static double I_ZOOM_RATE = 0.985;

	int MAX_ITERATIONS = 500;
	double THRESHOLD = 4.0;

	double LEFT = -2.25;
	double TOP = 1.1;
	double BOTTOM = -1.1;
	double RIGHT = 0.65;
	double BRIGHTNESS = 10.0;
	double RESOLUTION = 0.5;

	BufferedImage image = new BufferedImage((int)(WIDTH*RESOLUTION),
								(int)(HEIGHT*RESOLUTION),
								BufferedImage.TYPE_INT_RGB);

	public static void main(String args[]) {
		Magnifractal w = new Magnifractal();
		w.init();
	}

	public void init() {
		frame.add(this);
		frame.setSize(WIDTH, HEIGHT + 20);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		addMouseWheelListener(this);
		frame.addKeyListener(this);
	}

	public void mandelbrot() {
		for (int i=0; i<HEIGHT*RESOLUTION; i++) {
			double y = TOP + (BOTTOM-TOP)*i/(HEIGHT*RESOLUTION);
			for (int j=0; j<WIDTH*RESOLUTION; j++) {
				image.setRGB(j, i, Color.BLACK.getRGB());
				double x = LEFT + (RIGHT-LEFT)*j/(WIDTH*RESOLUTION);
				ComplexNumber z = new ComplexNumber(0.0, 0.0);
				ComplexNumber c = new ComplexNumber(x, y);
				double m = BRIGHTNESS*255.0/(double)MAX_ITERATIONS;
				for (int k=0; k<MAX_ITERATIONS; k++) {
					z = z.Multiply(z).Add(c);
					if (z.Mag2() > THRESHOLD) {
						int val = Math.min(255, (int) (k*m));
						int color = interp(Color.BLUE, Color.ORANGE, Color.YELLOW, Color.WHITE,
											0.07f, 0.25f,
											(float)k/(float)MAX_ITERATIONS).getRGB();
						image.setRGB(j, i, color);
						break;
					}
				}
			}
		}
	}

	public Color interp(Color c1, Color c2, float f) {
		float r = (c2.getRed() - c1.getRed())*f + c1.getRed();
		float g = (c2.getGreen() - c1.getGreen())*f + c1.getGreen();
		float b = (c2.getBlue() - c1.getBlue())*f + c1.getBlue();
		return new Color((int)r, (int)g, (int)b);
	}

	public Color interp(Color c1, Color c2, Color c3, Color c4, float a, float b, float f) {
		if (f < a) return interp(c1, c2, f/a);
		else if (f < b) return interp(c2, c3, (f - a)/(b - a));
		else return interp(c3, c4, (f - b)/(1f - b));
	}

	public float clamp(float f, float min, float max) {
		return Math.max(min, Math.min(max, f));
	}

	@Override
	public void paint(Graphics g) {
		mandelbrot();
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(1.0/RESOLUTION, 1.0/RESOLUTION);
		g2d.drawImage(image, 0, 0, this);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		int steps = event.getWheelRotation();
		Point mouse = this.getMousePosition();
		double w = RIGHT - LEFT;
		double h = TOP - BOTTOM;
		double rel_cx = (mouse.x / (double)WIDTH);
		double rel_cy = (mouse.y / (double)HEIGHT);
		double cx = rel_cx * w + LEFT;
		double cy = (1.0 - rel_cy) * h + BOTTOM;
		if (steps > 0) {
			w /= ZOOM_RATE;
			h /= ZOOM_RATE;
			BRIGHTNESS /= B_ZOOM_RATE;
			MAX_ITERATIONS = (int) (MAX_ITERATIONS * I_ZOOM_RATE);
		} else {
			w *= ZOOM_RATE;
			h *= ZOOM_RATE;
			BRIGHTNESS *= B_ZOOM_RATE;
			MAX_ITERATIONS = (int) (MAX_ITERATIONS / I_ZOOM_RATE);
		}
		LEFT = cx - rel_cx * w;
		RIGHT = cx + (1.0 - rel_cx) * w;
		TOP = cy + rel_cy * h;
		BOTTOM = cy - (1.0 - rel_cy) * h;
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case 82: // r
				RESOLUTION = ((RESOLUTION*4) % 12.0)/4.0;
				RESOLUTION += 0.25;
				image = new BufferedImage((int)(WIDTH*RESOLUTION),
											(int)(HEIGHT*RESOLUTION),
											BufferedImage.TYPE_INT_RGB);
				repaint();
				break;
			case 83: // s
				RESOLUTION -= 0.25;
				RESOLUTION = Math.max(0.25, RESOLUTION);
				image = new BufferedImage((int)(WIDTH*RESOLUTION),
											(int)(HEIGHT*RESOLUTION),
											BufferedImage.TYPE_INT_RGB);
				repaint();
				break;
			case 84: // t
				RESOLUTION = 2.0;
				image = new BufferedImage((int)(WIDTH*RESOLUTION),
											(int)(HEIGHT*RESOLUTION),
											BufferedImage.TYPE_INT_RGB);
				repaint();
				break;
			case 85: // u
				RESOLUTION = 0.25;
				image = new BufferedImage((int)(WIDTH*RESOLUTION),
											(int)(HEIGHT*RESOLUTION),
											BufferedImage.TYPE_INT_RGB);
				repaint();
				break;
			case 37: // left
				break;
			case 38: // up
				break;
			case 39: // right
				break;
			case 40: // down
				break;
			default:
				break;
		}
		//System.out.println(e.getKeyCode() + " pressed");
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
