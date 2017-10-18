package graphics;

import java.awt.Color;
import java.util.ArrayList;

import entity.Entity;
import level.Level;

public class Popup extends Entity {

	private ArrayList<Sprite> messages;

	private int timer;

	public Popup() {
		super(5, 255, null);
		messages = new ArrayList<Sprite>();
	}

	public void say(String text) {
		if (messages.size() >= 10) return;
		messages.add(new Sprite(text, Color.yellow, Color.gray, 10));
	}

	public void render(Render render) {
		if (messages.isEmpty()) return;
		for (int i = 0; i < messages.size(); i++) {
			Sprite sprite = messages.get(i);
			render.renderSprite(x, y - sprite.height * i, sprite);
		}
	}

	public void tick(Level level) {
		if (messages.isEmpty()) return;
		if (++timer % 500 == 0) {
			if (messages.size() == 1) messages = new ArrayList<Sprite>();
			else
				messages = new ArrayList<Sprite>(messages.subList(0, messages.size() - 2));
			timer = 0;
		}
	}
}
