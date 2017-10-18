package entity;

import java.awt.Rectangle;

import engine.GameEngine;
import engine.Input;
import engine.Main;
import graphics.Render;
import graphics.Sprite;
import level.Level;

public class Player extends Mob {

	private Input input;

	public Player(int x, int y, Sprite sprite, Input input, String name) {
		super(x, y, sprite, 15, name);
		this.input = input;
	}

	public void tick(Level level) {
		if (!isMoving) {
			if (input.keyW) if (canMove(0, -1)) move(0, -1);
			else
				face(UP);
			if (input.keyA) if (canMove(-1, 0)) move(-1, 0);
			else
				face(LEFT);
			if (input.keyS) if (canMove(0, 1)) move(0, 1);
			else
				face(DOWN);
			if (input.keyD) if (canMove(1, 0)) move(1, 0);
			else
				face(RIGHT);
			isRunning = input.keyShift;
		}
		if (input.keyEscape) Main.game.getLevel().hideHeader = true;
		if (input.keyInteract && input.leftClick && isNear(input.mouseTileX, input.mouseTileY)) {
			String notifier = level.notifiers[input.mouseTileX][input.mouseTileY];
			if (notifier != null) Main.game.popup.say(notifier);
			input.leftClick = false;
		}
		if (level.exit != null && level.nextLevel != null && level.exit.intersects(new Rectangle(x, y, 2, 2))) {
			level.removeEntity(this);
			Main.game.setLevel(level.nextLevel);
		}
	}

	public void mobSpecificRender(Render render) {
		if (input.keyInteract) render.renderSprite(input.mouseTileX << GameEngine.TILE_BASE, input.mouseTileY << GameEngine.TILE_BASE, isNear(input.mouseTileX, input.mouseTileY) ? Sprite.selectorSprite : Sprite.selectorSpriteGray);
	}

	protected void loadSprites() {
		spriteDownStill = Sprite.pDownStill;
		spriteDown1 = Sprite.pDown1;
		spriteDown2 = Sprite.pDown2;
		spriteLeftStill = Sprite.pLeftStill;
		spriteLeft1 = Sprite.pLeft1;
		spriteLeft2 = Sprite.pLeft2;
		spriteUpStill = Sprite.pUpStill;
		spriteUp1 = Sprite.pUp1;
		spriteUp2 = Sprite.pUp2;
		spriteRightStill = Sprite.pRightStill;
		spriteRight1 = Sprite.pRight1;
		spriteRight2 = Sprite.pRight2;
	}
}
