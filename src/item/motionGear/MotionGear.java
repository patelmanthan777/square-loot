package item.motionGear;

import item.Equipment;
import item.ItemListEnum;

public abstract class MotionGear extends Equipment {
	
	public MotionGear(float x, float y, ItemListEnum s, long cd)
	{
		super(x,y,s,cd);		
	}
}