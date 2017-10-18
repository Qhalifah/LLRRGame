package graphics;

public class Render {

	public static final int ALPHA = 0xffff00dc;

	public int width;
	public int height;
	public int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		clear(0xffa1a1a1);
	}

	public void clear(int c) {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = c;
	}

	public void renderSprite(int x, int y, Sprite sprite) {
		for (int yy = 0; yy < sprite.height; yy++) {
			int yOffs = y + yy;
			if (yOffs < 0 || yOffs >= height) continue;
			for (int xx = 0; xx < sprite.width; xx++) {
				int xOffs = x + xx;
				if (xOffs < 0 || xOffs >= width) continue;
				int c = sprite.pixels[xx + yy * sprite.width];
				if (c != ALPHA) pixels[xOffs + yOffs * width] = c;
			}
		}
	}
}
