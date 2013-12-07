package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import entity.projectile.ProjectileFactory;
import event.Timer;
import item.Item;

public abstract class Weapon extends Item {
	protected long fireRate;
	protected long lastShot;
	
	public Weapon(long fireRate)
	{
		this.fireRate = fireRate;
		this.lastShot = 0;
	}
	
	protected boolean readyToFire(){
		long currentTime = Timer.getTime();
		return fireRate + lastShot < currentTime;
	}
	
	protected void updateLastShot(){
		lastShot = Timer.getTime();
	}
	
	abstract public void Fire(Vector2f pos, Vector2f target);
	
}
