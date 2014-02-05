package item.weapon;

import item.ItemListEnum;

public abstract class SecondaryWeapon extends Weapon {
	
	public SecondaryWeapon(long fireRate, float x, float y, ItemListEnum s)
	{
		super(fireRate,x,y,s);		
	}
}