package item;

import org.lwjgl.util.vector.Vector2f;

public abstract class Equipment extends Item{
	
	public Equipment(float x, float y){
		super(x,y);
	}
	
	public abstract void action(Vector2f pos, Vector2f target);
}