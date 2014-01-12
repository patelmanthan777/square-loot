package entity;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector2f;

import rendering.Drawable;

public abstract class DynamicEntity extends Entity implements Drawable{
	/**
	 * Represent the direction in which the entity moves
	 */
	protected Vector2f translation = new Vector2f(0, 0);
	protected float minSpeed = 0.01f;
	protected float maxSpeed = 20f;
	protected float descFactor = 50;
	protected float accFactor = 0.025f;
	protected Body body;

	public DynamicEntity(Vector2f pos) {
		super(pos);
	}

	public DynamicEntity(Vector2f pos, Vector2f rot) {
		super(pos, rot);
	}
	
	public DynamicEntity(float posx, float posy, float dirx, float diry) {
		super(posx,posy,dirx,diry);
	}
	public DynamicEntity(float posx, float posy) {
		super(posx,posy);
	}


	/**
	 * Update the translation attribute by adding the parameters to the
	 * respective coordinates.
	 * 
	 * @param translationx
	 *            represents horizontal motion
	 * @param translationy
	 *            represents vertical motion
	 */
	public void translate(float translationx, float translationy) {
		this.translation.x += translationx;
		this.translation.y += translationy;
	}

	/**
	 * Initialize the physics body
	 * 
	 * @param w 
	 * 		the physics world
	 */
	public abstract void initPhysics(World w);

	/**
	 * Update the entity position according to its attributes.
	 * 
	 * @param dt
	 *            represents the time passed since last update
	 */
	public void updatePostion() {
		Vec2 position = body.getPosition();
		setPosition(position.x, position.y);
	}
	
	public void updatePhysics(long dt) {
		if (dt != 0) {
			Vec2 point = new Vec2(position.x, position.y);
			Vec2 vel = body.getLinearVelocity();
		
			if (translation.length() != 0) {
				System.out.println(vel);
				translation.normalise(translation);
				translation.scale(dt * 100);
				Vec2 impulse = new Vec2(translation.x, translation.y);
				body.applyLinearImpulse(impulse, point);
			} else {
				float factor = (float) Math.max(0., 1 - dt / 1);
				body.setLinearVelocity(new Vec2(vel.x * factor, vel.y * factor));
			}
		}
		translation.x = 0;
		translation.y = 0;
	}
}
