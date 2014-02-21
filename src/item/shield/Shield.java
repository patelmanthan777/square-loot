package item.shield;

import item.Equipment;
import item.ItemListEnum;

public abstract class Shield extends Equipment {
	
	public Shield(float x, float y, ItemListEnum s, long cd)
	{
		super(x,y,s, cd);		
	}
}