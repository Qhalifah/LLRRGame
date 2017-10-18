package level;

import java.util.ArrayList;

import graphics.Sprite;

public abstract class Tile {

	public static int ANIM_TIMER;

	public static final ArrayList<Tile> gameTiles = new ArrayList<Tile>();
	public static final ArrayList<Tile> animatedTiles = new ArrayList<Tile>();

	public static final GrassTile grassTile = new GrassTile();
	public static final FlowersTile flowersTile = new FlowersTile();
	public static final DirtTile dirtTile = new DirtTile();
	public static final WaterTile waterTile = new WaterTile();
	public static final SandTile sandTile = new SandTile();
	public static final Wood1Tile wood1Tile = new Wood1Tile();
	public static final Wood2Tile wood2Tile = new Wood2Tile();
	public static final StoneTile stoneTile = new StoneTile();
	public static final MudTile mudTile = new MudTile();
	public static final BrickTile brickTile = new BrickTile();
	public static final RoadTile roadTile = new RoadTile();
	public static final CurbHorizontalTop curbHorizontalTopTile = new CurbHorizontalTop();
	public static final CurbHorizontalBottom curbHorizontalBottomTile = new CurbHorizontalBottom();
	public static final CurbVerticalTop curbVerticalTopTile = new CurbVerticalTop();
	public static final CurbVerticalBottom curbverticalBottomTile = new CurbVerticalBottom();
	public static final CoastTopRight coastTopRightTile = new CoastTopRight();
	public static final CoastTopLeft coastTopLeftTile = new CoastTopLeft();
	public static final CoastBottomRight coastBottomRightTile = new CoastBottomRight();
	public static final CoastBottomLeft coastBottomLeftTile = new CoastBottomLeft();
	public static final PosterBrickTile posterBrickTile = new PosterBrickTile();

	public Sprite[] frames;
	public Sprite sprite;
	public Sprite sprite2x;
	public String name;

	public int frame;
	public int colourCode;

	public boolean solid;
	public boolean hasAction;

	public Tile(Sprite sprite, boolean solid, int colourCode) {
		this.sprite = sprite;
		this.solid = solid;
		this.colourCode = colourCode;
		sprite2x = sprite.resize(2);
		name = getClass().getSimpleName();
		frames = new Sprite[0];
		hasAction = false;
		gameTiles.add(this);
	}

	public Tile(Sprite[] frames, boolean solid, int colourCode) {
		this.sprite = frames[0];
		this.frames = frames;
		this.solid = solid;
		this.colourCode = colourCode;
		sprite2x = frames[0].resize(2);
		name = getClass().getSimpleName();
		hasAction = false;
		gameTiles.add(this);
		animatedTiles.add(this);
	}

	public int getID() {
		return gameTiles.indexOf(this);
	}

	public String toString() {
		return LevelHeaders.addSpacesAtCapitals(name.endsWith("Tile") ? name.substring(0, name.length() - "Tile".length()) : name);
	}

	public static Tile getTile(int colour) {
		for (Tile tile : gameTiles)
			if (tile.colourCode == colour) return tile;
		return grassTile;
	}

	public static class GrassTile extends Tile {

		public GrassTile() {
			super(Sprite.grassSprite, false, 0xff00ff00);
		}
	}

	public static class FlowersTile extends Tile {

		public FlowersTile() {
			super(Sprite.flowersSprite, false, 0xffffff00);
		}
	}

	public static class DirtTile extends Tile {

		public DirtTile() {
			super(Sprite.dirtSprite, false, 0xffaf6400);
		}
	}

	public static class WaterTile extends Tile {

		public WaterTile() {
			super(new Sprite[] {
					Sprite.water1Sprite, Sprite.water2Sprite
			}, true, 0xff0000fe);
		}
	}

	public static class SandTile extends Tile {

		public SandTile() {
			super(Sprite.sandSprite, false, 0xffffe499);
		}
	}

	public static class Wood1Tile extends Tile {

		public Wood1Tile() {
			super(Sprite.wood1Sprite, true, 0xffffae00);
		}
	}

	public static class Wood2Tile extends Tile {

		public Wood2Tile() {
			super(Sprite.wood2Sprite, false, 0xff75593f);
		}
	}

	public static class StoneTile extends Tile {

		public StoneTile() {
			super(Sprite.stoneSprite, false, 0xff9e9e9e);
		}
	}

	public static class MudTile extends Tile {

		public MudTile() {
			super(Sprite.mudSprite, false, 0xff8a5e00);
		}
	}

	public static class BrickTile extends Tile {

		public BrickTile() {
			super(Sprite.brickSprite, true, 0xff8c8c8c);
		}
	}

	public static class RoadTile extends Tile {

		public RoadTile() {
			super(Sprite.roadSprite, false, 0xff9d9d9d);
		}
	}

	public static class CurbHorizontalTop extends Tile {

		public CurbHorizontalTop() {
			super(Sprite.curbHorizontalTopSprite, false, 0xff9d9d00);
		}
	}

	public static class CurbHorizontalBottom extends Tile {

		public CurbHorizontalBottom() {
			super(Sprite.curbHorizontalBottomSprite, false, 0xff9d9d01);
		}
	}

	public static class CurbVerticalTop extends Tile {

		public CurbVerticalTop() {
			super(Sprite.curbVerticalTopSprite, false, 0xff9d9d02);
		}
	}

	public static class CurbVerticalBottom extends Tile {

		public CurbVerticalBottom() {
			super(Sprite.curbVerticalBottomSprite, false, 0xff9d9d03);
		}
	}

	public static class CoastTopRight extends Tile {

		public CoastTopRight() {
			super(Sprite.coastTopRightSprite, false, 0xff0001ff);
		}
	}

	public static class CoastTopLeft extends Tile {

		public CoastTopLeft() {
			super(Sprite.coastTopLeftSprite, false, 0xff0002ff);
		}
	}

	public static class CoastBottomRight extends Tile {

		public CoastBottomRight() {
			super(Sprite.coastBottomRightSprite, false, 0xff0003ff);
		}
	}

	public static class CoastBottomLeft extends Tile {

		public CoastBottomLeft() {
			super(Sprite.coastBottomLeftSprite, false, 0xff0004ff);
		}
	}

	public static class PosterBrickTile extends Tile {

		public PosterBrickTile() {
			super(Sprite.posterBrickSprite, true, 0xff8c8c8d);
		}
	}
}
