package entity.strangers;

import entity.NPC;
import graphics.Sprite;
import level.Level;

public class Lvl2_Dave extends NPC {

	public Lvl2_Dave() {
		super(0, 3, Sprite.pDownStill, 15, "lvl2_dave");
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

	public void script(Level level) {
		if (!isMoving) if (x < 10) if (canMove(1, 0)) move(1, 0);
	}
}
