package level.editor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import graphics.Render;
import graphics.Sprite;
import launcher.Launcher;
import launcher.ResourcesDownloader;
import level.Tile;

public class Editor {

	private static final Sprite box = Sprite.selectorSprite.resize(2);
	private static final Sprite grayBox = Sprite.selectorSpriteGray.resize(2);
	private static final Sprite eventIndicatorSprite = new Sprite("e", Color.cyan, new Color(Render.ALPHA), 24);

	private static final int cWidth = 960;
	private static final int cHeight = 576;

	private int drawX;
	private int drawY;
	private int notifX;
	private int notifY;
	private int width = 30;
	private int height = 18;
	private int brushSize = 3;

	private Tile[][] tiles = new Tile[width][height];
	private String[][] notifiers = new String[width][height];
	private String selectedEvent = null;
	private BufferedImage img = new BufferedImage(cWidth, cHeight, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	private Render render = new Render(cWidth, cHeight);
	private Canvas canvas = new Canvas();
	private JButton tileToDrawPreview;
	private Tile chosenTile = Tile.grassTile;
	private JFrame frmLevelEditor;
	private String loadedDir = ResourcesDownloader.resPath + "levels/";
	private JComboBox<Tile> chooseTile;
	private JRadioButton rdbtnTileSampler;
	private JRadioButton rdbtnRemoveEvent;
	private JRadioButton rdbtnAddEvent;
	private JRadioButton rdbtnSelectEvent;
	private JRadioButton rdbtnDraw;
	private JRadioButton rdbtnFloodFill;
	private JRadioButton rdbtnBrush;
	private JRadioButton rdbtnSquare;
	private JButton btnEditEventScript;

	private boolean isDrawing;
	private boolean mouseInBounds;

	/**
	 * Create the application.
	 */
	public Editor() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				tiles[x][y] = Tile.grassTile;
		new Thread() {

			public void run() {
				while (true) {
					tick();
					render();
				}
			}
		}.start();
	}

	private void tick() {
		if (isDrawing && mouseInBounds) {
			if (rdbtnDraw.isSelected()) {
				tiles[drawX][drawY] = chosenTile;
			} else if (rdbtnBrush.isSelected() || rdbtnSquare.isSelected()) {
				for (int y = -brushSize; y <= brushSize; y++)
					for (int x = -brushSize; x <= brushSize; x++)
						if (boundsCheck(drawX + x, drawY + y) && ((rdbtnBrush.isSelected() && Math.sqrt(x * x + y * y) <= brushSize) || (rdbtnSquare.isSelected()))) tiles[drawX + x][drawY + y] = chosenTile;
			} else if (rdbtnFloodFill.isSelected() && tiles[drawX][drawY] != chosenTile) {
				floodFill(drawX, drawY, tiles[drawX][drawY], chosenTile);
			} else if (rdbtnTileSampler.isSelected()) {
				chosenTile = tiles[drawX][drawY];
				chooseTile.setSelectedItem(chosenTile);
				chooseTile.requestFocus();
				isDrawing = false;
			} else if (rdbtnAddEvent.isSelected()) {
				notifiers[drawX][drawY] = "...";
				isDrawing = false;
			} else if (rdbtnRemoveEvent.isSelected()) {
				notifiers[drawX][drawY] = null;
				isDrawing = false;
			} else if (rdbtnSelectEvent.isSelected()) {
				if (notifiers[drawX][drawY] != null) {
					if (selectedEvent == null) {
						selectedEvent = notifiers[drawX][drawY];
						notifX = drawX;
						notifY = drawY;
					} else
						selectedEvent = null;
					isDrawing = false;
				} else
					selectedEvent = null;
				btnEditEventScript.setEnabled(selectedEvent != null);
			}
		}
	}

	private void floodFill(int x, int y, Tile from, Tile to) {
		tiles[x][y] = to;
		if (boundsCheck(x + 1, y) && tiles[x + 1][y] == from) floodFill(x + 1, y, from, to);
		if (boundsCheck(x - 1, y) && tiles[x - 1][y] == from) floodFill(x - 1, y, from, to);
		if (boundsCheck(x, y + 1) && tiles[x][y + 1] == from) floodFill(x, y + 1, from, to);
		if (boundsCheck(x, y - 1) && tiles[x][y - 1] == from) floodFill(x, y - 1, from, to);
	}

	private boolean boundsCheck(int x, int y) {
		return new Rectangle(0, 0, width, height).contains(new Point(x, y));
	}

	private void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		drawMaps();
		drawBrush();
		for (int i = 0; i < render.pixels.length; i++)
			pixels[i] = render.pixels[i];
		g.drawImage(img, 0, 0, cWidth, cHeight, null);
		g.dispose();
		bs.show();
	}

	private void drawBrush() {
		if (rdbtnBrush.isSelected() || rdbtnSquare.isSelected()) for (int y = -brushSize; y <= brushSize; y++)
			for (int x = -brushSize; x <= brushSize; x++)
				if ((rdbtnBrush.isSelected() && Math.sqrt(x * x + y * y) <= brushSize) || (rdbtnSquare.isSelected())) render.renderSprite(drawX + x << 5, drawY + y << 5, box);

	}

	private void drawMaps() {
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				try {
					render.renderSprite(x << 5, y << 5, tiles[x][y].sprite2x);
					if (notifiers[x][y] != null) {
						render.renderSprite((x << 5) + 9, (y << 5) - 2, eventIndicatorSprite);
						if (selectedEvent == null) render.renderSprite(x << 5, y << 5, grayBox);
						else if (selectedEvent == notifiers[x][y]) render.renderSprite(x << 5, y << 5, box);
						else
							render.renderSprite(x << 5, y << 5, grayBox);
					}
				} catch (NullPointerException e) {
					continue; // Means it's loading and I was too lazy to pause the thread for this.
				}
		if (selectedEvent == null) render.renderSprite(drawX << 5, drawY << 5, box);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLevelEditor = new JFrame();
		frmLevelEditor.setTitle("Level Editor");
		frmLevelEditor.setPreferredSize(new Dimension(985, 732));
		frmLevelEditor.pack();
		frmLevelEditor.setVisible(true);
		frmLevelEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLevelEditor.getContentPane().setLayout(null);

		chooseTile = new JComboBox<Tile>();
		chooseTile.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if ((e.getWheelRotation() < 1 && chooseTile.getSelectedIndex() == 0) || (e.getWheelRotation() > 1 && chooseTile.getSelectedIndex() == chooseTile.getItemCount() - 1)) return;
				try {
					chooseTile.setSelectedIndex(chooseTile.getSelectedIndex() + e.getWheelRotation());
				} catch (IllegalArgumentException ex) {
					return;
				}
			}
		});
		chooseTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Icon icon = new ImageIcon(((Tile) chooseTile.getSelectedItem()).sprite2x.asImage());
					tileToDrawPreview.setIcon(icon);
					tileToDrawPreview.setDisabledIcon(icon);
					chosenTile = (Tile) chooseTile.getSelectedItem();
				} catch (NullPointerException ex) {
					return;
				}
			}
		});
		for (Tile tile : Tile.gameTiles)
			chooseTile.addItem(tile);
		chooseTile.setBounds(12, 636, 150, 22);
		frmLevelEditor.getContentPane().add(chooseTile);
		frmLevelEditor.setLocationRelativeTo(null);
		frmLevelEditor.setResizable(false);

		JLabel chosenTileLabel = new JLabel("Selected Tile:");
		frmLevelEditor.getContentPane().add(chosenTileLabel);
		chosenTileLabel.setBounds(12, 602, 85, 22);

		canvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (rdbtnBrush.isSelected() || rdbtnSquare.isSelected()) {
					brushSize -= e.getWheelRotation();
					if (brushSize < 0) brushSize = 0;
					else if (brushSize > 10) brushSize = 10;
				}
			}
		});

		canvas.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				isDrawing = true;
			}

			public void mouseReleased(MouseEvent e) {
				isDrawing = false;
			}

			public void mouseEntered(MouseEvent e) {
				mouseInBounds = true;
			}

			public void mouseExited(MouseEvent e) {
				mouseInBounds = false;
			}
		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				drawX = p.x >> 5;
				drawY = p.y >> 5;
			}

			public void mouseDragged(MouseEvent e) {
				Point p = e.getPoint();
				drawX = p.x >> 5;
				drawY = p.y >> 5;
			}
		});

		canvas.setBounds(10, 10, cWidth, cHeight);
		frmLevelEditor.getContentPane().add(canvas);

		tileToDrawPreview = new JButton();
		tileToDrawPreview.setEnabled(false);
		tileToDrawPreview.setBounds(105, 597, 32, 32);
		tileToDrawPreview.setIcon(new ImageIcon(Tile.grassTile.sprite2x.asImage()));
		tileToDrawPreview.setDisabledIcon(new ImageIcon(Tile.grassTile.sprite2x.asImage()));
		tileToDrawPreview.addMouseWheelListener(chooseTile.getMouseWheelListeners()[0]);
		frmLevelEditor.getContentPane().add(tileToDrawPreview);

		rdbtnDraw = new JRadioButton("Draw");
		rdbtnDraw.setBounds(180, 601, 127, 25);
		frmLevelEditor.getContentPane().add(rdbtnDraw);

		rdbtnFloodFill = new JRadioButton("Flood Fill");
		rdbtnFloodFill.setBounds(180, 635, 127, 25);
		frmLevelEditor.getContentPane().add(rdbtnFloodFill);

		rdbtnSquare = new JRadioButton("Square");
		rdbtnSquare.setBounds(311, 601, 100, 25);
		frmLevelEditor.getContentPane().add(rdbtnSquare);

		rdbtnBrush = new JRadioButton("Brush");
		rdbtnBrush.setBounds(311, 635, 85, 25);
		frmLevelEditor.getContentPane().add(rdbtnBrush);

		JMenuBar menuBar = new JMenuBar();
		frmLevelEditor.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(loadedDir);
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.png", "png"));
				if (fileChooser.showOpenDialog(mntmOpen) == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fileChooser.getSelectedFile();
						BufferedImage img = ImageIO.read(fileChooser.getSelectedFile());
						if (img.getWidth() != width || img.getHeight() != height) throw new IllegalArgumentException("Level is not right dimension! (30x18)");
						tiles = new Tile[width][height];
						for (int y = 0; y < img.getHeight(); y++)
							for (int x = 0; x < img.getWidth(); x++)
								tiles[x][y] = Tile.getTile(img.getRGB(x, y));
						loadedDir = file.getAbsolutePath();
						File eventsFile = new File(file.getPath().replace("levels", "text/" + Launcher.lang).replace(".png", ".events"));
						System.out.println("Loading Events: " + eventsFile.getPath());
						if (!eventsFile.exists()) System.out.println("No events in language " + Launcher.lang + " found for this level");
						else {
							BufferedReader br = new BufferedReader(new FileReader(eventsFile));
							String line = br.readLine();
							while (line != null) {
								String[] data = line.split("~");
								notifiers[Integer.parseInt(data[0])][Integer.parseInt(data[1])] = data[2];
								line = br.readLine();
							}
							br.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(loadedDir);
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.png", "png"));
				fileChooser.setApproveButtonText("Save");
				if (fileChooser.showOpenDialog(mntmOpen) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (!file.getAbsolutePath().endsWith(".png")) file = new File(file.getAbsolutePath() + ".png");
					try {
						if (!file.exists()) file.createNewFile();
						BufferedImage level = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
						for (int y = 0; y < height; y++)
							for (int x = 0; x < width; x++)
								level.setRGB(x, y, tiles[x][y].colourCode);
						ImageIO.write(level, "png", file);
						File eventsFile = new File(file.getPath().replace("levels", "text/" + Launcher.lang).replace(".png", ".events"));
						System.out.println("Saving Events: " + eventsFile.getPath());
						if (eventsFile.getParentFile() != null) eventsFile.getParentFile().mkdirs();
						if (!eventsFile.exists()) eventsFile.createNewFile();
						PrintWriter w = new PrintWriter(eventsFile);
						int i = 0;
						for (int y = 0; y < height; y++)
							for (int x = 0; x < width; x++)
								if (notifiers[x][y] != null) {
									w.println(x + "~" + y + "~" + notifiers[x][y]);
									i++;
								}
						w.close();
						if (i == 0) eventsFile.delete();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmSave);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		rdbtnSelectEvent = new JRadioButton("Select Notifier");
		rdbtnSelectEvent.setBounds(682, 635, 126, 25);
		frmLevelEditor.getContentPane().add(rdbtnSelectEvent);

		rdbtnDraw.setSelected(true);

		rdbtnTileSampler = new JRadioButton("Tile Sampler");
		rdbtnTileSampler.setBounds(430, 601, 109, 25);
		frmLevelEditor.getContentPane().add(rdbtnTileSampler);

		rdbtnRemoveEvent = new JRadioButton("Remove Notifier");
		rdbtnRemoveEvent.setBounds(554, 635, 127, 25);
		frmLevelEditor.getContentPane().add(rdbtnRemoveEvent);

		rdbtnAddEvent = new JRadioButton("Add Notifier");
		rdbtnAddEvent.setBounds(554, 601, 109, 25);
		frmLevelEditor.getContentPane().add(rdbtnAddEvent);

		ButtonGroup editorButtons = new ButtonGroup();
		editorButtons.add(rdbtnBrush);
		editorButtons.add(rdbtnSquare);
		editorButtons.add(rdbtnFloodFill);
		editorButtons.add(rdbtnDraw);
		editorButtons.add(rdbtnSelectEvent);
		editorButtons.add(rdbtnRemoveEvent);
		editorButtons.add(rdbtnTileSampler);
		editorButtons.add(rdbtnAddEvent);

		JLabel lblEventLanguage = new JLabel("Notification Language:");
		lblEventLanguage.setBounds(672, 605, 136, 16);
		frmLevelEditor.getContentPane().add(lblEventLanguage);

		btnEditEventScript = new JButton("Edit Notification");
		btnEditEventScript.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new TextEditor(frmLevelEditor, notifiers, notifX, notifY);
			}
		});
		btnEditEventScript.setBounds(816, 632, 151, 25);
		btnEditEventScript.setEnabled(false);
		frmLevelEditor.getContentPane().add(btnEditEventScript);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(547, 602, 1, 56);
		frmLevelEditor.getContentPane().add(separator);

		JLabel lblLang = new JLabel(Launcher.lang);
		lblLang.setBounds(816, 605, 151, 16);
		frmLevelEditor.getContentPane().add(lblLang);

		frmLevelEditor.pack();
	}
}
