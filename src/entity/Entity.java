package entity;

import graphics.Render;
import graphics.Sprite;
import level.Level;

public abstract class Entity {

	public int x;
	public int y;

	public Sprite sprite;

	public Entity(int x, int y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	public abstract void tick(Level level);

	public abstract void render(Render render);
}
