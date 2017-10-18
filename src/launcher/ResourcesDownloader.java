package launcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ResourcesDownloader {

	public static final String homePath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/LostAbroad/";
	public static final String resPath = homePath + "res/";
	private static final String githubPath = "https://github.com/daveyognaught/LLRR-Game/raw/master/res/";

	public static final ArrayList<String> files = new ArrayList<String>();

	public static void init() throws MalformedURLException {
		// Textures
		files.add("textures/terrain.png");
		files.add("textures/uiElements.png");
		files.add("textures/player1.png");

		// Levels
		files.add("levels/emptyLot.png");
		files.add("levels/roadInFrontOfLot.png");

		// Text

		// English
		files.add("text/English/headers/emptyLot.header");
		files.add("text/English/headers/roadInFrontOfLot.header");
		files.add("text/English/game.info");
		files.add("text/English/strangers/lvl2_dave.npc");

		// Esperanto
		files.add("text/Esperanto/headers/emptyLot.header");
		files.add("text/Esperanto/headers/roadInFrontOfLot.header");
		files.add("text/Esperanto/game.info");
		files.add("text/Esperanto/strangers/lvl2_dave.npc");

		// Italian
		files.add("text/Italian/game.info");

		// Quenya
		files.add("text/Quenya/game.info");

		// Sindarin
		files.add("text/Sindarin/game.info");
	}

	public static ArrayList<String> getSupportedLanguages() {
		ArrayList<String> getLangs = new ArrayList<String>();
		for (String fileName : files)
			if (fileName.startsWith("text/")) {
				String lang = fileName.substring(fileName.indexOf("/") + 1, fileName.indexOf("/", 5));
				if (!getLangs.contains(lang)) {
					getLangs.add(lang);
					String info = "Found supported language: " + lang;
					System.out.println(info);
					Launcher.console.setText(info);
				}
			}
		return getLangs;
	}

	public static void getFiles() {
		new Thread() {

			public void run() {
				try {
					delete(new File(resPath));
				} catch (IOException e) {
					e.printStackTrace();
				}
				int i = 0;
				float s = files.size();
				for (String fileName : files)
					try {
						int p = (int) (100f * (float) (++i) / s);
						System.out.println("Getting file: \"" + fileName + "\": " + p + "%");
						Launcher.console.setText("Getting files..." + p + "%");
						download(fileName, new URL(githubPath + fileName));
					} catch (IOException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error getting required resources!");
						alert.setHeaderText("There was an error downloading game resources!");
						alert.setContentText("Make sure you have a reliable internet connection and try again. If the problem persists, contact the developer at daveoverzero@gmail.com");
						alert.showAndWait();
						System.exit(1);
					}
				Launcher.languages.setItems(FXCollections.observableArrayList(getSupportedLanguages()));
				Launcher.languages.getSelectionModel().select(0);
				System.out.println("Getting files done");
				Launcher.console.setText("Ready to play");
			}
		}.start();
	}

	public static boolean needsUpdate() {
		for (String fileName : files) {
			File file = new File(resPath + fileName);
			if (!file.exists()) return true;
		}
		return false;
	}

	public static void download(String fileName, URL url) throws MalformedURLException, IOException {
		File file = new File(resPath + fileName);
		if (file.getParentFile() != null) file.getParentFile().mkdirs();
		if (!file.exists()) file.createNewFile();
		FileUtils.copyURLToFile(url, file);
	}

	private static void delete(File file) throws IOException {
		if (!file.exists()) return;
		for (File childFile : file.listFiles()) {
			if (childFile.isDirectory()) {
				delete(childFile);
			} else {
				if (!childFile.delete()) {
					throw new IOException();
				}
			}
		}
		if (!file.delete()) {
			throw new IOException();
		}
	}
}
