package entity;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import engine.Main;
import entity.strangers.Lvl2_Dave;
import graphics.Render;
import graphics.Sprite;
import launcher.Launcher;
import launcher.ResourcesDownloader;
import level.Level;

public abstract class NPC extends Mob {

	public static final NPC roadInFrontOfLotStranger = new Lvl2_Dave();

	protected String message;

	public boolean isBusy;

	public NPC(int x, int y, Sprite sprite, int speed, String dataFile) {
		super(x, y, sprite, speed, "Unloaded NPC");
		loadSettings(dataFile);
	}

	public void stopAndLookAt(Entity e) {
		isBusy = true;
	}

	public void release() {
		isBusy = false;
	}

	public void sayMessage() {
		Main.game.popup.say(message);
	}

	public void tick(Level level) {
		if (!isBusy) script(level);
	}

	protected abstract void script(Level level);

	private void loadSettings(String dataFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(ResourcesDownloader.resPath + "text/" + Launcher.lang + "/strangers/" + dataFile + ".npc"))) {
			String line = br.readLine();
			while (line != null) {
				String[] data = line.split("=");
				if (data[0].equals("name")) nameLabel = new Sprite(data[1], Color.black, new Color(Render.ALPHA), 8);
				else if (data[0].equals("message")) message = data[1];
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("No data found for stranger \"" + dataFile + "\" in language " + Launcher.lang);
		}
	}
}