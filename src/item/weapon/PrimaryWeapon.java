package item.weapon;

public abstract class PrimaryWeapon extends Weapon {
	
	public PrimaryWeapon(long fireRate, float x, float y, float projectileSpeed, float projectileSize, int damage)
	{
		super(fireRate,x,y, projectileSpeed, projectileSize, damage);		
	}
}