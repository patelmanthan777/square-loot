package entity.projectile;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;
import physics.GameBodyType;
import rendering.Drawable;
import entity.Entity;

public abstract class Projectile extends Entity implements Drawable, PhysicsObject{

	protected boolean destroyed;
	protected Vector3f color;
	protected Body body;
	protected float speedValue;
	protected float size;
	protected int damage;
	protected Vector2f initSpeed = new Vector2f(0,0);
	
	public Projectile() {
		super(0.f,0.f);
		size = 0.f;
		speedValue = 0.f;
		destroyed = false;
		damage = 0;
	}
	
	public Projectile(Vector2f pos, Vector2f rot, Vector2f initSpeed, float speedValue, float size, int damage) {
		super(pos, rot);
		destroyed = false;
		this.speedValue = speedValue;
		this.initSpeed.set(initSpeed.x, initSpeed.y);
		this.size = size;
		this.damage = damage;
	}
	
	/**
	 * Reset a projectile according the method parameters.
	 * @param pos is the new position
	 * @param rot id the new orientation 
	 * @param damage 
	 */
	public void reset(Vector2f pos, Vector2f rot,  Vector2f initSpeed, float speedValue, float size, int damage)
	{
		this.setPosition(pos);
		this.setDirection(rot);
		destroyed = false;
		this.speedValue = speedValue;
		this.initSpeed.set(initSpeed.x, initSpeed.y);
		this.size = size;
		this.damage = damage;
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
	abstract public Projectile Clone(Vector2f pos, Vector2f rot, Vector2f initSpeed, float speedValue, float size, int damage);
	
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
		bodyDef.position.set(position.x*ConfigManager.blockPhysicSize, position.y*ConfigManager.blockPhysicSize);
		body = PhysicsManager.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(0.1f*ConfigManager.blockPhysicSize, 0.1f*ConfigManager.blockPhysicSize);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;		
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.1f;
		body.createFixture(fixtureDef);
		Vec2 vel = new Vec2((direction.x * speedValue+initSpeed.x)*ConfigManager.blockPhysicSize, (direction.y * speedValue+initSpeed.y)*ConfigManager.blockPhysicSize);
		body.setLinearVelocity(vel);
		PhysicsDataStructure s = new PhysicsDataStructure(this,GameBodyType.PROJECTILE); 
		body.setUserData(s);
	}
	
	

	/**
	 * Update the entity position according to its attributes.
	 * 
	 * @param dt
	 *            represents the time passed since last update
	 */
	public void updatePostion() {
		Vec2 position = body.getPosition();
		setPosition(position.x/ConfigManager.blockPhysicSize, position.y/ConfigManager.blockPhysicSize);
		Vec2 vel = new Vec2((direction.x * speedValue+initSpeed.x)*ConfigManager.blockPhysicSize, (direction.y * speedValue+initSpeed.y)*ConfigManager.blockPhysicSize);
		body.setLinearVelocity(vel);
	}

	public void destroy() {
		body.getWorld().destroyBody(body);
	}
	
	public void ContactHandler(PhysicsDataStructure a)
	{
		switch(a.getType())
		{
		case TRIGGERZONE:
			break;
		default:
			this.toDestroy();
			break;
		}
	}
	
	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		
	}
	
	public int getDamage()
	{
		return damage;
		
	}
}
