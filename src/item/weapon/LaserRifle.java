package item.weapon;


import item.ItemListEnum;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;


import entity.projectile.ProjectileManager;

public class LaserRifle extends PrimaryWeapon {

	

	public LaserRifle(long fireRate, float x ,float y)
	{
		super(fireRate, x, y, ItemListEnum.LASERRIFLE);
		position.x = x;
		position.y = y;	
	}

	@Override
	public void fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			ProjectileManager.createBullet(pos, target);
			this.updateLastShot();
		}		
	}
	
	public int getTextureID(){
		return TextureManager.laserRifleTexture().getTextureID();
	}

	
}
