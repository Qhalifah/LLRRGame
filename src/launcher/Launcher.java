package launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ConcurrentModificationException;

import engine.GameEngine;
import engine.Main;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import level.editor.Editor;

public class Launcher extends Application {

	public static String lang;

	private static boolean noUpdate;

	public static TextField console;
	public static ListView<String> languages;

	public static void begin(boolean noUpdate) {
		Launcher.noUpdate = noUpdate;
		launch();
	}

	private static final int GAME_TITLE = 0;
	private static final int CITY_NAME = 1;
	private static final int COUNTRY_NAME = 2;

	private static String[] loadSettings() {
		String[] settings = new String[3];
		try (BufferedReader br = new BufferedReader(new FileReader(ResourcesDownloader.resPath + "text/" + Launcher.lang + "/" + "game.info"))) {
			String line = br.readLine();
			while (line != null) {
				String[] data = line.split("=");
				if (data[0].equals("title")) settings[GAME_TITLE] = data[1];
				else if (data[0].equals("city")) settings[CITY_NAME] = data[1];
				else if (data[0].equals("country")) settings[COUNTRY_NAME] = data[1];
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("No game settings found in language " + Launcher.lang);
		}
		return settings;
	}

	public void start(Stage primaryStage) throws Exception {

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");
		File defaultDirectory = new File(System.getProperty("user.home"));
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(primaryStage);

		Pane pane = new Pane();
		TextField userName = new TextField();
		Button startButton = new Button("Start");
		Button exitButton = new Button("Close");
		Button editorLaunch = new Button("Level Editor");
		Button forceUpdate = new Button("Validate Files");
		Label langLabel = new Label("Language:");
		Label credits = new Label("By Chris Beimers, Carleton University");
		Label banner = new Label();
		banner.setGraphic(new ImageView(SwingFXUtils.toFXImage(new Sprite(380, 200, 0, 0, new SpriteSheet("/banner.png", true)).asImage(), null)));
		languages = new ListView<String>();

		console = new TextField();
		console.setEditable(false);

		userName.relocate(10, 270);
		userName.setPrefSize(380, 20);

		startButton.relocate(10, 310);
		startButton.setPrefSize(122, 20);

		editorLaunch.relocate(139, 310);
		editorLaunch.setPrefSize(122, 20);

		exitButton.relocate(269, 310);
		exitButton.setPrefSize(122, 20);

		langLabel.relocate(10, 350);
		credits.relocate(10, 240);
		banner.relocate(10, 10);

		forceUpdate.relocate(269, 230);
		forceUpdate.setPrefSize(122, 20);

		languages.relocate(100, 350);
		languages.setPrefSize(290, 210);

		console.relocate(10, 570);
		console.setPrefSize(380, 20);

		userName.setText(System.getProperty("user.name"));

		pane.getChildren().addAll(userName, startButton, editorLaunch, exitButton, langLabel, languages, credits, forceUpdate, banner, console);

		Scene scene = new Scene(pane, 390, 600);

		primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/icon.png")));
		primaryStage.setTitle(GameEngine.TITLE + " Launcher");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

		startButton.setOnAction(e -> {
			primaryStage.hide();
			lang = languages.getSelectionModel().getSelectedItem();
			Main.game = new GameEngine(userName.getText().trim(), loadSettings()[GAME_TITLE]);
		});

		exitButton.setOnAction(e -> {
			System.exit(0);
		});

		editorLaunch.setOnAction(e -> {
			lang = languages.getSelectionModel().getSelectedItem();
			primaryStage.hide();
			new Editor();
		});

		forceUpdate.setOnAction(e -> {
			ResourcesDownloader.getFiles();
		});

		forceUpdate.setTooltip(new Tooltip("This will overwrite any changes you made using level editor!"));

		userName.setOnAction(startButton.getOnAction());

		ResourcesDownloader.init();

		if (!noUpdate && ResourcesDownloader.needsUpdate()) ResourcesDownloader.getFiles();
		else {
			languages.setItems(FXCollections.observableArrayList(ResourcesDownloader.getSupportedLanguages()));
			languages.getSelectionModel().select(0);
		}

		try {
			console.setText("Ready to play");
		} catch (ConcurrentModificationException e) {
			System.out.println("Getting Update");
		}
	}
}
