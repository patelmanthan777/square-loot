package event.control;

import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;

import entity.player.Player;
import event.KeyState;
import event.KeyboardControler;
import event.MouseControler;

public class PlayerControl {

	enum playerActions {
		MOVE_FORWARD,
		MOVE_LEFT,
		MOVE_RIGHT,
		MOVE_BACKWARD,
		PRIMARY_WEAPON,
		LIGHT,
		INVENTORY
	}

	private HashMap<playerActions, LinkedList<Integer>> keyBinding;
	private HashMap<playerActions, LinkedList<Integer>> buttonBinding;
	private KeyboardControler keys;
	private MouseControler mouse;
	private playerActions[] actions;
	private Player p;

	private float[] savedMouseCoordinates;
	
	public PlayerControl(KeyboardControler keys, MouseControler mouse, Player p) {

		this.keys = keys;
		this.mouse = mouse;
		this.p = p;
		keyBinding = new HashMap<playerActions, LinkedList<Integer>>();
		buttonBinding = new HashMap<playerActions, LinkedList<Integer>>();
		actions = playerActions.values();

		savedMouseCoordinates= new float[2];
		savedMouseCoordinates[0]=-1;
		savedMouseCoordinates[1]=-1;
		
		// MOVE_FORWARD
		LinkedList<Integer> l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_W);
		l.add(Keyboard.KEY_Z);
		keyBinding.put(playerActions.MOVE_FORWARD, l);

		// MOVE_BACKWARD
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_S);
		keyBinding.put(playerActions.MOVE_BACKWARD, l);

		// MOVE_LEFT
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_Q);
		l.add(Keyboard.KEY_A);
		keyBinding.put(playerActions.MOVE_LEFT, l);

		// MOVE_RIGHT
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_D);
		keyBinding.put(playerActions.MOVE_RIGHT, l);

		// LIGHT
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_E);
		keyBinding.put(playerActions.LIGHT, l);

		// PRIMARY_WEAPON
		
		 l = new LinkedList<Integer>(); 
		 l.add(0);
		 buttonBinding.put(playerActions.PRIMARY_WEAPON,l);
		 
		// LIGHT
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_I);
		keyBinding.put(playerActions.INVENTORY, l);

		 
	}

	/**
	 * Check state of buttons and keys and execute the binding action
	 */
	public void update() {
		LinkedList<Integer> l;

		p.setDirection(mouse.direction.x,mouse.direction.y);
		
		for (playerActions a : actions) {
			l = keyBinding.get(a);
			if (l != null) {
				for (Integer i : l) {
					KeyState state = keys.getState(i);
					action(a,state);
				}
			}
			
			l = buttonBinding.get(a);
			if (l != null) {
				for (Integer i : l) {
					KeyState state = mouse.getState(i);
					action(a,state);
				}
			}		
		}
	}
	
	/**
	 * Execute the action given in parameter
	 * @param a the action
	 * @param state the state of button or the key
	 */
	private void action(playerActions a , KeyState state) {
		switch (a) {
		case MOVE_FORWARD:
			moveForwardAction(state);
			break;
		case MOVE_BACKWARD:
			moveBackwardAction(state);
			break;
		case MOVE_RIGHT:
			moveRightAction(state);
			break;
		case MOVE_LEFT:
			moveLeftAction(state);
			break;
		case LIGHT:
			lightAction(state);
			break;
		case PRIMARY_WEAPON:
			primaryWeaponAction(state);
			break;
		case INVENTORY:
			inventoryAction(state);
		}
	}

	private void moveForwardAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(0, -1);
		}
	}

	private void moveBackwardAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(0, 1);
		}
	}

	private void moveRightAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(1, 0);
		}
	}

	private void moveLeftAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(-1, 0);
		}
	}

	private void lightAction(KeyState state) {
		if (state == KeyState.PRESSED) {
			if (p.getLight().isActive())
				p.getLight().deactivate();
			else
				p.getLight().activate();
		}
	}
	
	private void inventoryAction(KeyState state) {
		if (state == KeyState.PRESSED) {
			if (p.getInventory().isOpen())
				p.getInventory().close();
			else
				p.getInventory().open();
		}
	}
	private void primaryWeaponAction(KeyState state) {
		if (p.getInventory().isOpen() &&
			p.getInventory().isInsideWindow(mouse.position.x,
											mouse.position.y)){
			if(state == KeyState.PRESSED){
				savedMouseCoordinates[0] = mouse.position.x;
				savedMouseCoordinates[1] = mouse.position.y;
			}
			else if(state == KeyState.RELEASED &&
					savedMouseCoordinates[0] != -1 &&
					savedMouseCoordinates[1] != -1)
				p.getInventory().move(savedMouseCoordinates[0],
									  savedMouseCoordinates[1],
									  mouse.position.x,
									  mouse.position.y);
		}
		else if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.primaryWeapon(mouse.direction.x,mouse.direction.y);
		}
	}

}
