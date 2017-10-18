package level;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import engine.GameEngine;
import entity.Entity;
import entity.Mob;
import graphics.Render;
import graphics.Sprite;
import launcher.Launcher;
import launcher.ResourcesDownloader;
import level.levels.EmptyLotLevel;
import level.levels.RoadInFrontOfLotLevel;

public abstract class Level {

	public static Level emptyLot;
	public static Level roadInFrontOfLot;

	public static void loadLevels() {
		roadInFrontOfLot = new RoadInFrontOfLotLevel();
		emptyLot = new EmptyLotLevel();
	}

	// All levels must be 30x18 tiles

	public int width;
	public int height;

	public boolean hideHeader;
	private boolean animate;

	public Rectangle exit;
	public Level nextLevel;
	public Sprite header;
	public Point spawnPoint;

	public Tile[][] tiles;
	public String[][] notifiers;
	public ArrayList<Entity> entities = new ArrayList<Entity>();

	public Level(String path, Sprite header, Rectangle exit, Level nextLevel, Point spawnPoint) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(ResourcesDownloader.resPath + path));
		} catch (IOException e) {
			System.err.println("This level doesn't exist: \"" + path + "\"\nShutting down.");
			System.exit(1);
		}
		width = img.getWidth();
		height = img.getHeight();
		if (width != 30 || height != 18) {
			System.err.println("Error, the level is not the required size (30x18 tiles)");
			System.exit(1);
		}
		tiles = new Tile[width][height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				tiles[x][y] = Tile.getTile(img.getRGB(x, y));
		notifiers = new String[width][height];
		loadEvents(path);
		this.header = header == null ? new Sprite(1, 1, Render.ALPHA) : header;
		this.exit = exit;
		this.nextLevel = nextLevel;
		this.spawnPoint = spawnPoint;
	}

	private void loadEvents(String path) {
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				notifiers[x][y] = null;
		try (BufferedReader br = new BufferedReader(new FileReader(ResourcesDownloader.resPath + (path.replace("levels", "text/" + Launcher.lang).replace(".png", ".events"))))) {
			String line = br.readLine();
			while (line != null) {
				String[] data = line.split("~");
				addEvent(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2]);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			System.out.println("No events found for level \"" + path + "\" for language " + Launcher.lang);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
		if (e instanceof Mob) ((Mob) e).loadTileMap(this);
	}

	public void addEvent(int x, int y, String event) {
		notifiers[x][y] = event;
	}

	public void removeEntity(Entity e) {
		for (Iterator<Entity> i = entities.iterator(); i.hasNext();)
			if (i.next().equals(e)) i.remove();
	}

	public void tick() {
		try {
			for (Iterator<Entity> i = entities.iterator(); i.hasNext();) {
				Entity e = i.next();
				e.tick(this);
			}
		} catch (ConcurrentModificationException e) {
			return;
		}
	}

	public void render(Render render) {
		if (++Tile.ANIM_TIMER % 50 == 0) {
			animate = !animate;
			Tile.ANIM_TIMER = 0;
			if (animate) {
				for (Tile tile : Tile.animatedTiles) {
					tile.sprite = tile.frames[tile.frame];
					if (++tile.frame % tile.frames.length == 0) tile.frame = 0;
				}
			}
		}
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				render.renderSprite(x << GameEngine.TILE_BASE, y << GameEngine.TILE_BASE, tiles[x][y].sprite);
		for (Entity e : entities)
			e.render(render);
		if (header != null && !hideHeader) render.renderSprite(0, 0, header);
	}
}
