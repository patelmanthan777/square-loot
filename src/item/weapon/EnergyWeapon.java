package item.weapon;

import item.ItemListEnum;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
import entity.LivingEntity;
import entity.projectile.ProjectileManager;

public class EnergyWeapon extends Weapon{

	public EnergyWeapon(long fireRate, float x, float y,
			float projectileSpeed, float projectileSize, int damage) {
		super(fireRate, x, y, ItemListEnum.LASERRIFLE, projectileSpeed, projectileSize, damage);
		this.recoil = 3;
	}


	@Override
	public void fire(Vector2f pos, Vector2f target, LivingEntity doer) {	
		ProjectileManager.createEnergyShot(pos, target, doer.getSpeed(), projectileSpeed, projectileSize, damage);	
		doer.translate(-doer.getDirection().x*recoil, -doer.getDirection().y*recoil);
	}
	
	public int getTextureID(){
		return TextureManager.laserRifleTexture().getTexture().getTextureID();
	}
}
