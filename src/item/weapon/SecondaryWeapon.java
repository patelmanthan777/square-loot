package item.weapon;

public abstract class SecondaryWeapon extends Weapon {
	
	public SecondaryWeapon(long fireRate, float x, float y, float projectileSpeed, float projectileSize, int damage)
	{
		super(fireRate,x,y, projectileSpeed, projectileSize, damage);		
	}
}