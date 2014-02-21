package item.accessory;

import item.Equipment;
import item.ItemListEnum;

public abstract class Accessory extends Equipment {
	
	public Accessory(float x, float y, ItemListEnum s, long cd)
	{
		super(x,y,s,cd);		
	}
}