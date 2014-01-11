package entity.projectile;

import org.lwjgl.util.vector.Vector2f;
import rendering.Drawable;
import entity.Entity;

public abstract class Projectile extends Entity implements Drawable{

	protected boolean destroyed;

	public Projectile(){
		super(new Vector2f(),new Vector2f());
	}
	
	public Projectile(Vector2f pos, Vector2f rot) {
		super(pos, rot);
		destroyed = false;
	}
	
	/**
	 * Reset a projectile according the method parameters.
	 * @param pos is the new position
	 * @param rot id the new orientation 
	 */
	public void reset(Vector2f pos, Vector2f rot)
	{
		this.setPosition(pos);
		this.setDirection(rot);
		destroyed = false;
	}
	
	/**
	 * Handle the case where the projectile should be destroyed.
	 * @return true if the projectile should be destroyed, false otherwise
	 */
	public boolean shouldBeDestroyed(){
		return destroyed;
	}
	
	/**
	 * A cloning interface to save memory.
	 * 
	 * @param pos
	 * @param rot
	 * @return 
	 */ 
	abstract public Projectile Clone(Vector2f pos, Vector2f rot);
}
