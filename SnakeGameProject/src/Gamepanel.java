import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Gamepanel extends JPanel implements Runnable, KeyListener 
{
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 500, HEIGHT = 500;
	
	private int point = 0;
	
	private Thread thread;
	
	private boolean running;
	
	private boolean right = true, left = false, up = false, down = false;
	
	private BodyPart b;
	private ArrayList<BodyPart> snake;
	
	private Apple apple;
	private ArrayList<Apple> apples;
	
	private Random r;
	
	private int xCoor = 10, yCoor = 10, size = 15;
	
	private int ticks = 0;
	
	public Gamepanel() {
		setFocusable(true);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		addKeyListener(this);
		
		snake = new ArrayList<BodyPart> ();
		
		apples = new ArrayList<Apple> ();
		
		r = new Random();
		
		start();
	}
	
	public void start() {
		running = true;
		thread = new Thread (this);
		thread.start();
	}
	
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick( ) {
		if (snake.size() == 0) {
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
		}
		ticks++;
		
		if (ticks > 250000) {
			if (right) xCoor++;
			if (left) xCoor--;
			if (up) yCoor--;
			if (down) yCoor++;
			
			ticks = 0;
			
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
			
			if (snake.size() > size) {
				snake.remove(0);
			}
		}
		
		if (apples.size() == 0) {
			int xCoor = r.nextInt(49);
			int yCoor = r.nextInt(49);
			
			apple = new Apple(xCoor, yCoor, 10);
			apples.add(apple);
		}
		
		// Hit apple and increase width
		for (int i = 0; i < apples.size(); i++) {
			if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
				size+=2;
				point++;
				apples.remove(i);
				i++;
			}
		}
		
		// Collision on Snake
		for (int i = 0; i < snake.size(); i++) {
			if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
				if (i != snake.size() - 1) {
					JOptionPane.showMessageDialog (null, "Point: " + point + " \n Game Over");
					stop();
				}
			}
		}
		
		// Collision on Border
		if (xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49) {
			JOptionPane.showMessageDialog (null, "Point: " + point + " \n Game Over");
			stop();
		}
	}
	
	//public void paint() {
		
	//}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (int i = 0; i < WIDTH/10; i++) {
			g.drawLine(i*10, 0, i*10, HEIGHT);
		}
		
		for (int i = 0; i < HEIGHT/10; i++) {
			g.drawLine(0, i * 10, HEIGHT, i*10);
		}
		
		for (int i = 0; i < snake.size(); i++) {
			snake.get(i).draw(g);	
		}
		
		for (int i = 0; i < apples.size(); i++) {
			apples.get(i).draw(g);
		}
	}
	
	public void run( ) {
		while (running) {
			tick();
			repaint();
		}
	}
	
	public void keyPressed (KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT && !left) {
			right = true;
			up = false;
			down = false;
		}
		if (key == KeyEvent.VK_LEFT && !right) {
			left = true;
			up = false;
			down = false;
		}
		if (key == KeyEvent.VK_UP && !down) {
			up = true;
			right = false;
			left = false;
		}
		if (key == KeyEvent.VK_DOWN && !up) {
			down = true;
			right = false;
			left = false;
		}
	}
	public void keyReleased (KeyEvent arg0) {
		
	}
	public void keyTyped (KeyEvent arg0) {
		
	}
}
