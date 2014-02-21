package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import entity.LivingEntity;
import item.Equipment;
import item.ItemListEnum;

public abstract class Weapon extends Equipment {
	protected float projectileSpeed;
	protected float projectileSize;
	protected int damage;
	protected float recoil = 0;

	public Weapon(long fireRate, float x, float y, ItemListEnum s, float projectileSpeed, float projectileSize, int damage)
	{
		super(x,y,s, fireRate);		
		this.projectileSpeed = projectileSpeed;
		this.projectileSize = projectileSize;
		this.damage = damage;
	}
	
	
	
	/**
	 * Fire the weapon
	 * @param pos the position where the weapon has been fire
	 * @param target the target area
	 */
	abstract public void fire(Vector2f pos, Vector2f target, LivingEntity doer);
	
	protected void specificAction(Vector2f pos, Vector2f target, LivingEntity doer){				
			Vector2f direct = new Vector2f();
			target.normalise(direct);
			Vector2f position = new Vector2f(pos.x +(projectileSize/ConfigManager.unitPixelSize)*direct.x,
											 pos.y +(projectileSize/ConfigManager.unitPixelSize)*direct.y);
			
			fire(position,target,doer);					
	}
}
