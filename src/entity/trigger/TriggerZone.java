package entity.trigger;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;

public abstract class TriggerZone implements PhysicsObject {
	protected Body body;
	protected float range;
	protected Vector2f position;
	
	public TriggerZone(float range, float x, float y) {
		this.range = range;
		this.position = new Vector2f(x,y);
	}
	
	public void init() {
		initPhysics();
	}

	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type =  BodyType.DYNAMIC;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x, position.y);
		body = PhysicsManager.createBody(bodyDef);
		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(range);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicCircle;
		fixtureDef.density = 0.3f;
		fixtureDef.friction = 0.0f;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef);
		PhysicsDataStructure s = new PhysicsDataStructure(this, GameBodyType.TRIGGERZONE); 
		body.setUserData(s);
	}
}
