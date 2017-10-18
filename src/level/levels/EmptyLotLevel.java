package level.levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import graphics.Sprite;
import level.Level;
import level.LevelHeaders;

public class EmptyLotLevel extends Level {

	public EmptyLotLevel() {
		super("levels/emptyLot.png", new Sprite(LevelHeaders.load("emptyLot"), Color.yellow, Color.gray, 10), new Rectangle(27, 16, 2, 1), Level.roadInFrontOfLot, new Point(10, 10));
	}
}
