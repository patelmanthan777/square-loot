package entity.projectile;

import light.Light;
import light.LightManager;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import configuration.ConfigManager;
import rendering.Shader;

public class EnergyShot extends Projectile {

/**
 * Save the shader program associated with the bullets. -1 means
 * not initialized. 
 */
	static private Shader bulletShaderProgram = null;
	private Light l;

	/**
	 * Initialize the bullet shader
	 */
	static public void initBulletShader(){
		if(bulletShaderProgram == null)
		{
			bulletShaderProgram = new Shader("bullet");
		}
	}
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public EnergyShot() {
		super();
	}
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public EnergyShot(Vector2f pos, Vector2f rot, Vector2f initSpeed, float speedValue, float size, int damage) {
		super(pos,rot,initSpeed,speedValue,size,damage);
		color = new Vector3f(0.2f,0.8f,1f);
		l = LightManager.addPointLight(this.toString(), new Vector2f(200, 200), color, 50,2*(int)ConfigManager.resolution.x,true);
	
	}

	@Override
	public void updatePostion() {
		super.updatePostion();
		l.setPosition(this.position.x* ConfigManager.unitPixelSize,this.position.y* ConfigManager.unitPixelSize);
	}
	
	/**
	 * Draw the bullet.
	 */
	@Override
	public void draw() {
	
	}

	@Override
	public Projectile Clone(Vector2f pos, Vector2f rot,Vector2f initSpeed, float speedValue, float size, int damage) {		
		return new EnergyShot(pos, rot, initSpeed, speedValue, size, damage);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		LightManager.deactivateLight(this.toString(), true);
	}
	
	@Override
	public void reset(Vector2f pos, Vector2f rot, Vector2f initSpeed, float speedValue, float size, int damage)
	{
		super.reset(pos, rot, initSpeed,speedValue, size, damage);
		l.activate();
		l.setPosition(pos.x*ConfigManager.unitPixelSize, pos.y*ConfigManager.unitPixelSize);
	}
	
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
		PhysicsDataStructure s = new PhysicsDataStructure(this,GameBodyType.ENERGYSHOT); 
		body.setUserData(s);
	}
}

