package item.weapon;


import item.ItemListEnum;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;

import entity.projectile.ProjectileManager;

public class LaserRifle extends PrimaryWeapon {

	public LaserRifle(long fireRate, float x, float y, float projectileSpeed, float projectileSize, int damage)
	{
		super(fireRate, x, y, ItemListEnum.LASERRIFLE, projectileSpeed, projectileSize, damage);
	}

	@Override
	public void fire(Vector2f pos, Vector2f target) {	
		ProjectileManager.createBullet(pos, target, projectileSpeed, projectileSize, damage);		
	}
	
	public int getTextureID(){
		return TextureManager.laserRifleTexture().getTextureID();
	}
}
