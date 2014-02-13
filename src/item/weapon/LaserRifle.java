package item.weapon;


import item.ItemListEnum;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
import sound.SoundManager;
import entity.LivingEntity;
import entity.projectile.ProjectileManager;

public class LaserRifle extends PrimaryWeapon {

	public LaserRifle(long fireRate, float x, float y, float projectileSpeed, float projectileSize, int damage){
		super(fireRate, x, y, ItemListEnum.LASERRIFLE, projectileSpeed, projectileSize, damage);
		this.recoil = 1.5f;
	}

	@Override
	public void fire(Vector2f pos, Vector2f target, LivingEntity doer) {	
		ProjectileManager.createBullet(pos, target,  doer.getSpeed(), projectileSpeed, projectileSize, damage);	
		doer.translate(-doer.getDirection().x*recoil, -doer.getDirection().y*recoil);
		SoundManager.gunShot(doer.getX(), doer.getY());
	}
	
	public int getTextureID(){
		return TextureManager.laserRifleTexture().getTexture().getTextureID();
	}
}
