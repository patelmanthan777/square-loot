package entity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;

import environment.Map;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;
import physics.GameBodyType;
import rendering.Drawable;
import utils.MathFunction;

public abstract class DynamicEntity extends Entity implements Drawable, PhysicsObject{
	/**
	 * Represent the direction in which the entity moves
	 */
	protected Vector2f translation = new Vector2f(0, 0);
	//protected float maxSpeed = 0.02f;
	protected float descFactor = 0.5f;
	protected float accFactor = 30f;
	protected Vector2f halfSize = new Vector2f();
	protected Body body;
	protected Vector2f speed = new Vector2f();

	protected GameBodyType  gbtype = GameBodyType.ENTITY;
	protected BodyType       btype =     BodyType.STATIC;


	public DynamicEntity(Vector2f pos) {
		super(pos);
	}

	public DynamicEntity(Vector2f pos, Vector2f rot) {
		super(pos, rot);
	}

	public DynamicEntity(float posx, float posy, float dirx, float diry) {
		super(posx, posy, dirx, diry);
	}

	public DynamicEntity(float posx, float posy) {
		super(posx, posy);
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

	public void normaliseTranslation(){
		this.translation.normalise(translation);
	}
	
	/**
	 * Initialize the physics body
	 * 
	 * @param w
	 *            the physics world
	 */
	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = btype;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x, position.y);
		body = PhysicsManager.createBody(bodyDef);
		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(this.halfSize.x);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicCircle;
		fixtureDef.density = 0.3f;
		fixtureDef.friction = 0.0f;
		body.createFixture(fixtureDef);
		PhysicsDataStructure s = new PhysicsDataStructure(this, gbtype); 
		body.setUserData(s);
	}

	/**
	 * Update the entity position according to its attributes.
	 * 
	 * @param dt
	 *            represents the time passed since last update
	 */
	public void updatePosition(long delta, Map m) {
		Vec2 position = body.getPosition();
		setPosition(position.x, position.y);
		speed.x = this.body.getLinearVelocity().x;
		speed.y = this.body.getLinearVelocity().y;
	}

	public void updatePhysics(long delta) {
		if(delta > 200)
			return;
		
		if (this.translation.length() != 0) {
			//this.translation.normalise(translation);
			this.translation.scale(accFactor);
			this.speed.x += this.translation.x;
			this.speed.y += this.translation.y;
		}
				
		
		float newSpeedx = this.speed.x - this.speed.x * descFactor;
		float newSpeedy = this.speed.y - this.speed.y * descFactor;
		
		
		this.speed.x = MathFunction.sameSigne(speed.x,newSpeedx)? newSpeedx : 0;
		this.speed.y = MathFunction.sameSigne(speed.y,newSpeedy)? newSpeedy : 0;

		
		
		this.translation.x = 0;
		this.translation.y = 0;

		body.setLinearVelocity(new Vec2(this.speed.x,this.speed.y));
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case BLOCK:
			break;
		case ENTITY:
			break;
		case PLAYER:
			break;
		case PROJECTILE:
			break;
		default:
			break;
		}
	}
	
	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		
	}
	
	public void destroy()
	{
		body.getWorld().destroyBody(body);
	}
	
	public Vector2f getSpeed(){
		return new Vector2f(this.speed.x,this.speed.y);
	}
	
	@Override
	public void setPosition(Vector2f pos){
		super.setPosition(pos);
		Vec2 p = new Vec2(pos.x, pos.y);
		body.setTransform(p, 0);
	}

	@Override
	public void setPosition(float posx, float posy){
		super.setPosition(posx,posy);
		Vec2 p = new Vec2(posx, posy);
		body.setTransform(p, 0);
	}
	
	@Override
	public void setX(float x){
		super.setX(x);
		Vec2 p = new Vec2(x, position.y);
		body.setTransform(p, 0);
	}

	@Override
	public void setY(float y){
		super.setY(y);
		Vec2 p = new Vec2(position.x, y);
		body.setTransform(p, 0);
	}
	
	public void setSpeed(float x, float y){
		this.speed.x = x;
		this.speed.y = y;
	}
}
