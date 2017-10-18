package engine;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public boolean keyW;
	public boolean keyA;
	public boolean keyS;
	public boolean keyD;
	public boolean keySpace;
	public boolean keyShift;
	public boolean keyEnter;
	public boolean keyEscape;
	public boolean keyInteract;

	public boolean leftClick;
	public boolean rightClick;

	public int mouseX;
	public int mouseY;
	public int mouseTileX;
	public int mouseTileY;

	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		mouseTileX = (p.x + 8) / (GameEngine.TILE_SIZE * GameEngine.SCALE);
		mouseTileY = (p.y + 8) / (GameEngine.TILE_SIZE * GameEngine.SCALE);
		mouseX = p.x;
		mouseY = p.y;
	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		mouseTileX = (p.x + 8) / (GameEngine.TILE_SIZE * GameEngine.SCALE);
		mouseTileY = (p.y + 8) / (GameEngine.TILE_SIZE * GameEngine.SCALE);
		mouseX = p.x;
		mouseY = p.y;
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		leftClick = SwingUtilities.isLeftMouseButton(e);
		rightClick = SwingUtilities.isRightMouseButton(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) leftClick = false;
		if (SwingUtilities.isRightMouseButton(e)) rightClick = false;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			keyW = true;
			break;
		case KeyEvent.VK_A:
			keyA = true;
			break;
		case KeyEvent.VK_S:
			keyS = true;
			break;
		case KeyEvent.VK_D:
			keyD = true;
			break;
		case KeyEvent.VK_SPACE:
			keySpace = true;
			break;
		case KeyEvent.VK_ENTER:
			keyEnter = true;
			break;
		case KeyEvent.VK_SHIFT:
			keyShift = true;
			break;
		case KeyEvent.VK_ESCAPE:
			keyEscape = true;
			break;
		case KeyEvent.VK_E:
			keyInteract = true;
			break;
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			keyW = false;
			break;
		case KeyEvent.VK_A:
			keyA = false;
			break;
		case KeyEvent.VK_S:
			keyS = false;
			break;
		case KeyEvent.VK_D:
			keyD = false;
			break;
		case KeyEvent.VK_SPACE:
			keySpace = false;
			break;
		case KeyEvent.VK_ENTER:
			keyEnter = false;
			break;
		case KeyEvent.VK_SHIFT:
			keyShift = false;
			break;
		case KeyEvent.VK_ESCAPE:
			keyEscape = false;
			break;
		case KeyEvent.VK_E:
			keyInteract = false;
			break;
		default:
			break;
		}
	}
}
