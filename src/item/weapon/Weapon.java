package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import event.Timer;
import item.Item;

public abstract class Weapon extends Item {
	protected long fireRate;
	protected long lastShot;
	
	/**
	 * Weapon class constructor
	 * @param fireRate The weapon fire rate 
	 */
	public Weapon(long fireRate)
	{
		this.fireRate = fireRate;
		this.lastShot = 0;
	}
	
	/**
	 * Is the weapon ready to fire?
	 * @return true if the weapon is ready to fire, else false
	 */
	protected boolean readyToFire(){
		long currentTime = Timer.getTime();
		return fireRate + lastShot < currentTime;
	}
	
	/**
	 * Update the last shot time
	 */
	protected void updateLastShot(){
		lastShot = Timer.getTime();
	}
	
	/**
	 * Fire the weapon
	 * @param pos the position where the weapon has been fire
	 * @param target the target area
	 */
	abstract public void Fire(Vector2f pos, Vector2f target);
	
}
