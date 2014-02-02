package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import entity.projectile.ProjectileManager;

public class LaserRifle extends Weapon {

	public LaserRifle(long fireRate, float projectileSpeed, float projectileSize, int damage)
	{
		super(fireRate, projectileSpeed, projectileSize, damage);
	}

	@Override
	public void Fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			Vector2f direct = new Vector2f();
			target.normalise(direct);
			Vector2f position = new Vector2f(pos.x +(0.5f+ projectileSize/ConfigManager.unitPixelSize)*direct.x,
											 pos.y +(0.5f+ projectileSize/ConfigManager.unitPixelSize)*direct.y);
			ProjectileManager.createBullet(position, target, projectileSpeed, projectileSize, damage);
			this.updateLastShot();
		}		
	}	
}
