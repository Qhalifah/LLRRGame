package level.levels;

import java.awt.Color;
import java.awt.Point;

import entity.NPC;
import graphics.Sprite;
import level.Level;
import level.LevelHeaders;

public class RoadInFrontOfLotLevel extends Level {

	public RoadInFrontOfLotLevel() {
		super("levels/roadInFrontOfLot.png", new Sprite(LevelHeaders.load("roadInFrontOfLot"), Color.yellow, Color.gray, 10), null, null, new Point(27, 1));
		addEntity(NPC.roadInFrontOfLotStranger);
	}
}
