package item.weapon;

import item.ItemListEnum;

public abstract class PrimaryWeapon extends Weapon {
	
	public PrimaryWeapon(long fireRate, float x, float y, ItemListEnum s)
	{
		super(fireRate,x,y,s);		
	}
}