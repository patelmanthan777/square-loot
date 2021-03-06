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
		BATTERY,
		PICKUP,
		LIGHT,
		ENERGYSHOT,
	}

	private HashMap<playerActions, LinkedList<Integer>> keyBinding;
	private HashMap<playerActions, LinkedList<Integer>> buttonBinding;
	private KeyboardControler keys;
	private MouseControler mouse;
	private playerActions[] actions;
	private Player p;
	
	public PlayerControl(KeyboardControler keys, MouseControler mouse, Player p) {

		this.keys = keys;
		this.mouse = mouse;
		this.p = p;
		keyBinding = new HashMap<playerActions, LinkedList<Integer>>();
		buttonBinding = new HashMap<playerActions, LinkedList<Integer>>();
		actions = playerActions.values();
		
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

		// BATTERY
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_F);
		keyBinding.put(playerActions.BATTERY, l);
		
		// PICKUP
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_R);
		keyBinding.put(playerActions.PICKUP, l);
		// ENERGYSHOT
		l = new LinkedList<Integer>();
		l.add(Keyboard.KEY_SPACE);
		keyBinding.put(playerActions.ENERGYSHOT, l);
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
		case BATTERY:
			batteryAction(state);
			break;		
		case PICKUP:
			pickUpAction(state);
			break;
		case ENERGYSHOT:
			energyShotAction(state);
			break;
		}
	}

	private void moveForwardAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(0, -1);
			p.normaliseTranslation();
		}
	}

	private void moveBackwardAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(0, 1);
			p.normaliseTranslation();
		}
	}

	private void moveRightAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(1, 0);
			p.normaliseTranslation();
		}
	}

	private void moveLeftAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.translate(-1, 0);
			p.normaliseTranslation();
		}
	}

	private void lightAction(KeyState state) {
		if (state == KeyState.PRESSED) {
			System.out.println(p.getSpeed());
			if (p.getLight().isActive())
				p.getLight().deactivate();
			else
				p.getLight().activate();
		}
	}
	
	private void batteryAction(KeyState state) {
		if (state == KeyState.PRESSED) {			
			p.drop();			
		}
	}
	
	private void pickUpAction(KeyState state) {
		if (state == KeyState.PRESSED) {			
			p.pickUpOrBuy();			
		}
	}
	

	private void primaryWeaponAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.primaryWeapon(mouse.direction.x,mouse.direction.y);
		}
	}
	
	private void energyShotAction(KeyState state) {
		if (state == KeyState.HELD || state == KeyState.PRESSED) {
			p.shootEnergy();
		}
	}

}
