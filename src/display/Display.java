package display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import log.Logger;
import objects.Board;
import objects.Piece;
import objects.Player;

public class Display extends Canvas implements Runnable {

	public static void main(String[] args) {
		// game is 7 columns, 6 rows + 1 row for moving around pieces
		int scale = 100;
		new Display(7 * scale, 7 * scale, 6, 7);
	}

	private static final long serialVersionUID = 1L;

	private static class Window extends JFrame {

		private static final long serialVersionUID = 1L;

		public Window(int w, int h, Display d) {
			setSize(w, h);
			setPreferredSize(new Dimension(w, h));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			setLocationRelativeTo(null);
			setResizable(false);

			add(d);
			setVisible(true);
		}

	}

	private static class Handler {

		private List<Piece> pieces;

		public Handler(Piece... p) {
			pieces = new LinkedList<Piece>();
			add(p);
		}

		public void render(Graphics g) {
			for (Piece p : pieces) {
				p.render(g);
			}
		}

		public void tick() {
			for (Piece p : pieces) {
				p.tick();
			}
		}

		public Handler add(Piece... ps) {
			for (Piece pss : ps) {
				pieces.add(pss);
			}
			return this;
		}

		public Handler remove(Piece... ps) {
			for (Piece pss : ps) {
				pieces.remove(pss);
			}
			return this;
		}

	}

	public static int DEFAULT_HEIGHT = 700;
	public static int DEFAULT_WIDTH = 700;
	public static int DEFAULT_ROWS = 6;
	public static int DEFAULT_COLS = 7;

	private Thread thread;
	private boolean isRunning;

	private final MouseTracker mouseTracker;

	private final Player player;
	private final Board board;

	private final Handler handler;

	// private final int rows, cols;

	/**
	 * TODO: take in # rows and # cols
	 */

	public Display() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_ROWS, DEFAULT_COLS);
	}

	public Display(int width, int height, int rows, int cols) {

		Logger.clear();

		if (rows < 1 || cols < 1) {
			throw new IllegalArgumentException("Rows and Cols must be greater than or equal to 1.");
		}
		
		// this.rows = rows;
		// this.cols = cols;

		// local variable, window is unneeded after constructor
		// as opposed to new Window().add(this), think about swing/awt structure
		new Window(width, height, this);

		handler = new Handler();

		mouseTracker = new MouseTracker();

		addMouseListener(mouseTracker);
		addMouseMotionListener(mouseTracker);

		player = new Player(getWidth(), getHeight(), rows, cols, mouseTracker);
		board = new Board(player, getWidth(), getHeight(), rows, cols, mouseTracker);

		handler.add(player, board);

		isRunning = false;

		// starts the thread (run())
		start();
	}

	private void tick() {
		handler.tick();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		super.paint(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// g.translate(getWidth() / 2, getHeight() / 2);

		handler.render(g);

		// g.translate(-getWidth() / 2, -getHeight() / 2);

		bs.show();
		g.dispose();
	}

	public boolean getDown() {
		return mouseTracker.down();
	}

	public boolean get(int e) {
		return mouseTracker.get(e);
	}

	public float getMouseX() {
		return mouseTracker.getX();
	}

	public float getMouseY() {
		return mouseTracker.getY();
	}

	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}

	private final synchronized void start() {
		if (isRunning)
			return;

		thread = new Thread(this);
		thread.start(); // This calls the Game.run() method because the instance of Game was passed in
		isRunning = true;
	}

	private final synchronized void stop() {
		// Prevents stoppage if thread has already been terminated
		if (!isRunning)
			return;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isRunning = false;
	}

}