package engine;

import launcher.Launcher;

public class Main {

	public static GameEngine game;

	public static void main(String[] args) {
		if (args.length == 1) Launcher.begin(args[0].trim().toLowerCase().equals("-noupdate"));
		else
			Launcher.begin(false);
	}
}
