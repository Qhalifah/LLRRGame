package engine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entity.Player;
import graphics.Popup;
import graphics.Render;
import graphics.Sprite;
import level.Level;

public class GameEngine extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int TILE_SIZE = 16;
	public static final int TILE_BASE = (int) log2(TILE_SIZE);

	public static String TITLE = "Lost Abroad";

	public static int WIDTH = 1440;
	public static int HEIGHT = 858;
	public static int SCALE = 3;

	private Thread thread;
	private JFrame frame;
	private BufferedImage img;
	private Render render;
	private Level currentLevel;
	private Player player;
	private Input input;
	public Popup popup;

	private int[] pixels;

	private boolean running;

	public GameEngine(String playerName, String title) {
		frame = new JFrame(TITLE = title);
		frame.add(this);
		frame.setPreferredSize(new Dimension(WIDTH + 6, HEIGHT + 35));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
		img = new BufferedImage(WIDTH / SCALE, HEIGHT / SCALE, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		render = new Render(img.getWidth(), img.getHeight());
		input = new Input();
		player = new Player(0, 0, Sprite.pDownStill, input, playerName);
		popup = new Popup();
		frame.addKeyListener(input);
		frame.addMouseListener(input);
		frame.addMouseMotionListener(input);
		frame.addMouseWheelListener(input);
		try {
			frame.setIconImage(ImageIO.read(GameEngine.class.getResource("/icon.png")));
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		addMouseWheelListener(input);
		start();
	}

	public void setLevel(Level level) {
		currentLevel = level;
		currentLevel.addEntity(player);
		player.x = level.spawnPoint.x;
		player.y = level.spawnPoint.y;
	}

	private synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if (running) return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	public void run() {
		long last = System.nanoTime();
		final double ns = 1_000_000_000.0 / 60.0;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - last) / ns;
			last = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			render();
		}
		stop();
	}

	public void close() {
		save();
		running = false;
	}

	private void save() {
		// save game here
	}

	private void tick() {
		if (currentLevel == null) {
			Level.loadLevels();
			setLevel(Level.emptyLot);
		}
		currentLevel.tick();
		popup.tick(currentLevel);
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		if (currentLevel == null) return;
		Graphics g = bs.getDrawGraphics();
		currentLevel.render(render);
		popup.render(render);
		render.renderSprite(input.mouseX / SCALE, input.mouseY / SCALE, Sprite.mouseSprite);
		for (int i = 0; i < render.pixels.length; i++)
			pixels[i] = render.pixels[i];
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public Level getLevel() {
		return currentLevel;
	}

	public static final double log2(double d) {
		return Math.log(d) / Math.log(2);
	}
}
