package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import engine.GameEngine;

public class Sprite {

	public static final Sprite grassSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 0, 0, SpriteSheet.terrain);
	public static final Sprite flowersSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 0, 1, SpriteSheet.terrain);
	public static final Sprite dirtSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 1, 0, SpriteSheet.terrain);
	public static final Sprite water1Sprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 2, 0, SpriteSheet.terrain);
	public static final Sprite water2Sprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 2, 1, SpriteSheet.terrain);
	public static final Sprite sandSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 3, 0, SpriteSheet.terrain);
	public static final Sprite wood1Sprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 4, 0, SpriteSheet.terrain);
	public static final Sprite wood2Sprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 4, 1, SpriteSheet.terrain);
	public static final Sprite stoneSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 5, 0, SpriteSheet.terrain);
	public static final Sprite mudSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 6, 0, SpriteSheet.terrain);
	public static final Sprite brickSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 7, 0, SpriteSheet.terrain);
	public static final Sprite roadSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 8, 0, SpriteSheet.terrain);
	public static final Sprite curbHorizontalTopSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 9, 0, SpriteSheet.terrain);
	public static final Sprite curbHorizontalBottomSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 10, 0, SpriteSheet.terrain);
	public static final Sprite curbVerticalTopSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 11, 0, SpriteSheet.terrain);
	public static final Sprite curbVerticalBottomSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 12, 0, SpriteSheet.terrain);
	public static final Sprite coastTopLeftSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 3, 1, SpriteSheet.terrain);
	public static final Sprite coastBottomLeftSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 3, 2, SpriteSheet.terrain);
	public static final Sprite coastBottomRightSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 3, 3, SpriteSheet.terrain);
	public static final Sprite coastTopRightSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 3, 4, SpriteSheet.terrain);
	public static final Sprite posterBrickSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 13, 0, SpriteSheet.terrain);

	public static final Sprite pDownStill = new Sprite(32, 32, 0, 0, SpriteSheet.player1);
	public static final Sprite pDown1 = new Sprite(32, 32, 1, 0, SpriteSheet.player1);
	public static final Sprite pDown2 = new Sprite(32, 32, 2, 0, SpriteSheet.player1);
	public static final Sprite pLeftStill = new Sprite(32, 32, 0, 1, SpriteSheet.player1);
	public static final Sprite pLeft1 = new Sprite(32, 32, 1, 1, SpriteSheet.player1);
	public static final Sprite pLeft2 = new Sprite(32, 32, 2, 1, SpriteSheet.player1);
	public static final Sprite pRightStill = new Sprite(32, 32, 0, 2, SpriteSheet.player1);
	public static final Sprite pRight1 = new Sprite(32, 32, 1, 2, SpriteSheet.player1);
	public static final Sprite pRight2 = new Sprite(32, 32, 2, 2, SpriteSheet.player1);
	public static final Sprite pUpStill = new Sprite(32, 32, 0, 3, SpriteSheet.player1);
	public static final Sprite pUp1 = new Sprite(32, 32, 1, 3, SpriteSheet.player1);
	public static final Sprite pUp2 = new Sprite(32, 32, 2, 3, SpriteSheet.player1);

	public static final Sprite debugSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 0, 0, SpriteSheet.uiElements);
	public static final Sprite selectorSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 1, 0, SpriteSheet.uiElements);
	public static final Sprite selectorSpriteGray = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 0, 1, SpriteSheet.uiElements);
	public static final Sprite mouseSprite = new Sprite(GameEngine.TILE_SIZE, GameEngine.TILE_SIZE, 1, 1, SpriteSheet.uiElements);

	public static final Sprite[] playerFrames = new Sprite[] {
			pDownStill, pDown1, pDown2, pLeftStill, pLeft1, pLeft2, pRightStill, pRight1, pRight2, pUpStill, pUp1, pUp2
	};

	public int width;
	public int height;
	public int[] pixels;

	public Sprite(int width, int height, int x, int y, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		for (int yy = 0; yy < height; yy++) {
			int yOffs = y * height + yy;
			if (yOffs < 0 || yOffs >= sheet.height) continue;
			for (int xx = 0; xx < width; xx++) {
				int xOffs = x * width + xx;
				if (xOffs < 0 || xOffs >= sheet.width) continue;
				pixels[xx + yy * width] = sheet.pixels[xOffs + yOffs * sheet.width];
			}
		}
	}

	public Sprite(BufferedImage source) {
		this.width = source.getWidth();
		this.height = source.getHeight();
		pixels = new int[width * height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				pixels[x + y * width] = source.getRGB(x, y);
	}

	public Sprite(String text, Color fg, Color bg, int fontSize) {
		this.width = getDimensions(text, fontSize)[0];
		this.height = getDimensions(text, fontSize)[1];
		String[] lines = new String[] {
				text
		};
		if (text.contains("\n")) {
			lines = text.split("\n");
			for (int i = 0; i < lines.length; i++)
				lines[i] = lines[i].substring(0, lines[i].length() - 1);
		}
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(bg);
		g.fillRect(0, 0, width, height);
		g.setColor(fg);
		g.setFont(new Font("Arial", 0, fontSize));
		int p = -4;
		for (String line : lines)
			g.drawString(line, 0, p += g.getFontMetrics().getHeight());
		g.dispose();
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public Sprite(int w, int h, int col) {
		this.width = w;
		this.height = h;
		this.pixels = new int[w * h];
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = col;
	}

	public Sprite resize(int scale) {
		int w = width * scale;
		int h = height * scale;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				img.setRGB(x, y, pixels[x + y * width]);
		Image temp = img.getScaledInstance(w, h, Image.SCALE_REPLICATE);
		BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = dest.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		return new Sprite(dest);
	}

	private static int[] getDimensions(String text, int fontSize) {
		String[] lines = new String[] {
				text
		};
		if (text.contains("\n")) {
			lines = text.split("\n");
			for (int i = 0; i < lines.length; i++)
				lines[i] = lines[i].substring(0, lines[i].length() - 1);
		}
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setFont(new Font("Arial", 0, fontSize));
		FontMetrics fm = g.getFontMetrics();
		String longest = lines[0];
		for (String line : lines)
			if (fm.stringWidth(line) > fm.stringWidth(longest)) longest = line;
		int w = fm.stringWidth(longest);
		int h = (fm.getHeight()) * lines.length + 0;
		g.dispose();
		return new int[] {
				w == 0 ? 1 : w, h == 0 ? 1 : h
		};
	}

	public BufferedImage asImage() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				img.setRGB(x, y, pixels[x + y * width]);
		return img;
	}
}
