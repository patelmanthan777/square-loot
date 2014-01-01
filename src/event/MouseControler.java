package event;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;

public class MouseControler {
	public Vector2f position;
	public Vector2f direction;
	
	private KeyState [] buttons;
	private int buttonsNumber;
	
	public MouseControler(){
		position = new Vector2f();
		direction = new Vector2f();
		buttonsNumber = Mouse.getButtonCount();
		buttons = new KeyState[buttonsNumber];
		for(int i =0 ; i < buttonsNumber; i++){
			buttons[i] = KeyState.INACTIVE;
		}
	}
	
	/**
	 * Update state for each mouse buttons
	 */
	public void update(){
		position.x = Mouse.getX(); 
		position.y = ConfigManager.resolution.y-Mouse.getY();
		
		direction.x = position.x-(int)ConfigManager.resolution.x/2.0f;
		direction.y = position.y-(int)ConfigManager.resolution.y/2.0f ;
		
		for (int i = 0; i < buttonsNumber ; i++){
			if(Mouse.isButtonDown(i)){
				if(buttons[i] == KeyState.RELEASED || buttons[i] == KeyState.INACTIVE){
					buttons[i] = KeyState.PRESSED;
				}else{
					buttons[i] = KeyState.HELD;
				}
			}else{
				if(buttons[i] == KeyState.RELEASED){
					buttons[i] = KeyState.INACTIVE;
				}else if(buttons[i] != KeyState.INACTIVE){
					buttons[i] = KeyState.RELEASED;
				}
			}
		}
	}
	
	public KeyState getState(int button){
		return buttons[button];
	}
	
}
