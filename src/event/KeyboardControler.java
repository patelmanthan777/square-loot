package event;

import org.lwjgl.input.Keyboard;


public class KeyboardControler {
	private int size = 256;
	private KeyState [] keys;
	
	public KeyboardControler(){
		
		keys = new KeyState[size];
		for(int i =0 ; i < size; i++){
			keys[i] = KeyState.INACTIVE;
		}
		
	}

	/**
	 * Handles the keys currently pressed.
	 */
	public void update(){
		for (int i = 0; i < size ; i++){
			if(Keyboard.isKeyDown(i)){
				if(keys[i] == KeyState.RELEASED || keys[i] == KeyState.INACTIVE){
					keys[i] = KeyState.PRESSED;
				}else{
					keys[i] = KeyState.HELD;
				}
			}else{
				if(keys[i] == KeyState.RELEASED){
					keys[i] = KeyState.INACTIVE;
				}else if(keys[i] != KeyState.INACTIVE){
					keys[i] = KeyState.RELEASED;
				}
			}
		}
	}
	
	public KeyState getState(int key){
		return keys[key];
	}
}
