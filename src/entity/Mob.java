package entity;

import java.awt.Color;

import engine.GameEngine;
import graphics.Render;
import graphics.Sprite;
import level.Level;

public abstract class Mob extends Entity {

	public static final int DOWN = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;

	private int animTimer;
	private int dir;
	private int xFrom;
	private int yFrom;
	private int xOffs;
	private int yOffs;
	private int speed;

	private boolean[][] tileMap;
	private boolean stopAnim;
	protected boolean isMoving;
	protected boolean isRunning;

	protected Sprite spriteDownStill;
	protected Sprite spriteDown1;
	protected Sprite spriteDown2;
	protected Sprite spriteLeftStill;
	protected Sprite spriteLeft1;
	protected Sprite spriteLeft2;
	protected Sprite spriteUpStill;
	protected Sprite spriteUp1;
	protected Sprite spriteUp2;
	protected Sprite spriteRightStill;
	protected Sprite spriteRight1;
	protected Sprite spriteRight2;

	protected Sprite nameLabel;

	public Mob(int x, int y, Sprite sprite, int speed, String name) {
		super(x, y, sprite);
		this.speed = speed;
		nameLabel = new Sprite(name, Color.black, new Color(Render.ALPHA), 8);
		loadSprites();
	}

	protected abstract void loadSprites();

	protected boolean canMove(int dx, int dy) {
		try {
			if (dy == -1) return !(tileMap[x][y - 1] | tileMap[x + 1][y - 1]);
			if (dy == 1) return !(tileMap[x][y + 2] | tileMap[x + 1][y + 2]);
			if (dx == -1) return !(tileMap[x - 1][y] | tileMap[x - 1][y + 1]);
			if (dx == 1) return !(tileMap[x + 2][y] | tileMap[x + 2][y + 1]);
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	protected void move(int dx, int dy) {
		if (isMoving) return;
		isMoving = true;
		xFrom = x;
		yFrom = y;
		x += dx;
		y += dy;
		if (dx > 0) dir = RIGHT;
		else if (dx < 0) dir = LEFT;
		if (dy < 0) dir = UP;
		else if (dy > 0) dir = DOWN;
	}

	public void render(Render render) {
		if (!isMoving) {
			render.renderSprite(x << GameEngine.TILE_BASE, y << GameEngine.TILE_BASE, sprite);
		} else {
			render.renderSprite((xFrom << GameEngine.TILE_BASE) + xOffs, (yFrom << GameEngine.TILE_BASE) + yOffs, sprite);
			if (++animTimer % (isRunning ? speed / 2 : speed) == 0) {
				sprite = nextSprite();
				animTimer = 0;
				xOffs = getOffs()[0];
				yOffs = getOffs()[1];
				if (stopAnim) isMoving = stopAnim = false;
			}
		}
		render.renderSprite((x << GameEngine.TILE_BASE) - 8, (y << GameEngine.TILE_BASE) - 8, nameLabel);
		mobSpecificRender(render);
	}

	protected void mobSpecificRender(Render render) {

	}

	private int[] getOffs() {
		int x = 0;
		int y = 0;
		switch (dir) {
		case UP:
			y = sprite.equals(spriteUp1) ? -5 : sprite.equals(spriteUp2) ? -11 : 0;
			break;
		case DOWN:
			y = sprite.equals(spriteDown1) ? 5 : sprite.equals(spriteDown2) ? 11 : 0;
			break;
		case LEFT:
			x = sprite.equals(spriteLeft1) ? -5 : sprite.equals(spriteLeft2) ? -11 : 0;
			break;
		case RIGHT:
			x = sprite.equals(spriteRight1) ? 5 : sprite.equals(spriteRight2) ? 11 : 0;
			break;
		}
		return new int[] {
				x, y
		};
	}

	private Sprite nextSprite() {
		switch (dir) {
		case UP:
			if (sprite.equals(spriteUp2)) stopAnim = true;
			return sprite.equals(spriteUpStill) ? spriteUp1 : sprite.equals(spriteUp1) ? spriteUp2 : spriteUpStill;
		case LEFT:
			if (sprite.equals(spriteLeft2)) stopAnim = true;
			return sprite.equals(spriteLeftStill) ? spriteLeft1 : sprite.equals(spriteLeft1) ? spriteLeft2 : spriteLeftStill;
		case DOWN:
			if (sprite.equals(spriteDown2)) stopAnim = true;
			return sprite.equals(spriteDownStill) ? spriteDown1 : sprite.equals(spriteDown1) ? spriteDown2 : spriteDownStill;
		case RIGHT:
			if (sprite.equals(spriteRight2)) stopAnim = true;
			return sprite.equals(spriteRightStill) ? spriteRight1 : sprite.equals(spriteRight1) ? spriteRight2 : spriteRightStill;
		default:
			return sprite;
		}
	}

	protected void face(int dir) {
		if (dir < DOWN) this.dir = DOWN;
		else if (dir > UP) this.dir = UP;
		else
			this.dir = dir;
		switch (dir) {
		case DOWN:
			sprite = spriteDownStill;
			break;
		case LEFT:
			sprite = spriteLeftStill;
			break;
		case RIGHT:
			sprite = spriteRightStill;
			break;
		case UP:
			sprite = spriteUpStill;
			break;
		}
	}

	protected boolean isNear(int x, int y) {
		return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)) <= 3;
	}

	public void loadTileMap(Level level) {
		tileMap = new boolean[level.width][level.height];
		for (int yy = 0; yy < level.height; yy++)
			for (int xx = 0; xx < level.width; xx++)
				tileMap[xx][yy] = level.tiles[xx][yy].solid;
	}
}
