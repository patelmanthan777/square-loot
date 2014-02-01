package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import entity.projectile.ProjectileManager;

public class LaserRifle extends Weapon {

	public LaserRifle(long fireRate, float projectileSpeed, float projectileSize)
	{
		super(fireRate, projectileSpeed, projectileSize);
	}

	@Override
	public void Fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			ProjectileManager.createBullet(pos, target, projectileSpeed, projectileSize);
			this.updateLastShot();
		}		
	}


	
}
