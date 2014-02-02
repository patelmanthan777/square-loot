package entity.projectile;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import physics.PhysicsManager;
import rendering.Drawable;
import entity.Entity;

public abstract class Projectile extends Entity implements Drawable{

	protected boolean destroyed;
	protected Vector3f color;
	protected Body body;
	protected float speedValue;
	protected float size;
	
	public Projectile() {
		super(0.f,0.f);
		size = 0.f;
		speedValue = 0.f;
		destroyed = false;
	}
	
	public Projectile(Vector2f pos, Vector2f rot, float speedValue, float size) {
		super(pos, rot);
		destroyed = false;
		this.speedValue = speedValue;
		this.size = size;
	}
	
	/**
	 * Reset a projectile according the method parameters.
	 * @param pos is the new position
	 * @param rot id the new orientation 
	 */
	public void reset(Vector2f pos, Vector2f rot,  float speedValue, float size)
	{
		this.setPosition(pos);
		this.setDirection(rot);
		destroyed = false;
		this.speedValue = speedValue;
		this.size = size;
	}
	
	public void toDestroy()
	{
		destroyed = true;
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
	abstract public Projectile Clone(Vector2f pos, Vector2f rot, float speedValue, float size);
	
	/**
	 * Initialize the physics body
	 * 
	 * @param w
	 *            the physics world
	 */
	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.bullet = true;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x, position.y);
		body = PhysicsManager.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(0.1f, 0.1f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;		
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.1f;
		body.createFixture(fixtureDef);
		Vec2 vel = new Vec2(direction.x * speedValue, direction.y * speedValue);
		body.setLinearVelocity(vel);
		body.setUserData(this);
	}

	/**
	 * Update the entity position according to its attributes.
	 * 
	 * @param dt
	 *            represents the time passed since last update
	 */
	public void updatePostion() {
		Vec2 position = body.getPosition();
		setPosition(position.x, position.y);
		Vec2 vel = new Vec2(direction.x * speedValue, direction.y * speedValue);
		body.setLinearVelocity(vel);
	}

	public void destroy() {
		body.getWorld().destroyBody(body);
	}
}
