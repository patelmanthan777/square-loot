package event.control;

import game.GameLoop;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import entity.player.Player;
import event.KeyState;
import event.KeyboardControler;
import event.MouseControler;

public class Control {
	private KeyboardControler keys;
	private MouseControler mouse;
	private PlayerControl pControle;
	
	public Control(Player p) {
		keys = new KeyboardControler();
		mouse = new MouseControler();
		
		pControle = new PlayerControl(keys, mouse, p);
	}
	
	
	/**
	 * Interpret the inputs and modify the game entities accordingly.
	 */
	public void update() {
		keys.update();
		mouse.update();
		
		pControle.update();
		menuControle();
		
		
	}
	
	public void menuControle() {
		if (keys.getState(Keyboard.KEY_ESCAPE) == KeyState.PRESSED|| Display.isCloseRequested()) {
			GameLoop.isRunning = false;
		}
	}

}
