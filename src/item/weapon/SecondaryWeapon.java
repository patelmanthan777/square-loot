package item.weapon;

import item.ItemListEnum;

public abstract class SecondaryWeapon extends Weapon {
	

	public SecondaryWeapon(long fireRate, float x, float y, ItemListEnum s, float projectileSpeed, float projectileSize, int damage)
	{
		super(fireRate,x,y,s, projectileSpeed, projectileSize, damage);		
	}
}