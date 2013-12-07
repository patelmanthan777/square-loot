package entity.projectile;

import org.lwjgl.util.vector.Vector2f;
import rendering.Drawable;
import entity.Entity;

public abstract class Projectile extends Entity implements Drawable{

	protected boolean toDestroy;

	public Projectile(){
		super(new Vector2f(),new Vector2f());
	}
	
	/**
	 * Projectile class constructor
	 * @param pos Initial position of the projectile
	 * @param rot Initial direction of the projectile
	 */
	public Projectile(Vector2f pos, Vector2f rot) {
		super(pos, rot);
		toDestroy = false;
	}
	
	
	/**
	 * does the projectile must be destroy?
	 * @return true if the projectile must be destroy, else false
	 */
	public boolean mustBeDestroy(){
		return toDestroy;
	}
	
	abstract public Projectile Clone(Vector2f pos, Vector2f rot);
}
