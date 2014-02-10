package item;

import org.lwjgl.util.vector.Vector2f;

public abstract class Equipment extends Item{
	
	public Equipment(float x, float y, ItemListEnum s){
		super(x,y,s);
	}
	
	public abstract boolean action(Vector2f pos, Vector2f target);
}