package item.weapon;


import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;

import rendering.TextureManager;

import entity.projectile.ProjectileManager;

public class LaserRifle extends PrimaryWeapon {

	public LaserRifle(long fireRate, float x, float y, float projectileSpeed, float projectileSize, int damage)
	{
		super(fireRate, x, y, projectileSpeed, projectileSize, damage);
		position.x = x;
		position.y = y;	
	}

	@Override
	public void fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			ProjectileManager.createBullet(pos, target, projectileSpeed, projectileSize, damage);
			this.updateLastShot();
		}		
	}
	
	public int getTextureID(){
		return TextureManager.laserRifleTexture().getTextureID();
	}
}
