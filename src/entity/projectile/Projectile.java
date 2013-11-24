package entity.projectile;

import org.lwjgl.util.vector.Vector2f;
import rendering.Drawable;
import entity.Entity;

public abstract class Projectile extends Entity implements Drawable{
	protected boolean toDestroy;

	public  Projectile(Vector2f pos, Vector2f rot) {
		super(pos, rot);
		toDestroy = false;
	}
	public boolean mustBeDestroy(){
		return toDestroy;
	}
}
