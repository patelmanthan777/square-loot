package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import event.Timer;
import item.Item;

public abstract class Weapon extends Item {
	protected long fireRate;
	/**
	 * Last shot timestamp, allow the handling of the
	 * fire cooldown.
	 */
	protected long lastShot;
	
	public Weapon(long fireRate)
	{
		this.fireRate = fireRate;
		this.lastShot = 0;
	}
	
	/**
	 * Test whether the weapon is ready to fire
	 * @return <b>true</b> if the weapon is ready to fire, <b>false</b> otherwise.
	 */
	protected boolean readyToFire(){
		long currentTime = Timer.getTime();
		return fireRate + lastShot < currentTime;
	}
	
	/**
	 * Update the lastShot
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
