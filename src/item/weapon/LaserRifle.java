package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import entity.projectile.ProjectileManager;

public class LaserRifle extends Weapon {
	
	public LaserRifle(long fireRate)
	{
		super(fireRate);
	}

	@Override
	public void Fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			ProjectileManager.createBullet(pos, target);
			this.updateLastShot();
		}		
	}


	
}
