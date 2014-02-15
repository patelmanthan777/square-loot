package entity.trigger;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import configuration.ConfigManager;
import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;

public abstract class TriggerZone implements PhysicsObject {
	protected Body body;
	private float range;
	
	public TriggerZone(float range) {
		this.range = range;
	}

	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type =  BodyType.DYNAMIC;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(0, 0);
		body = PhysicsManager.createBody(bodyDef);
		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(range*ConfigManager.blockPhysicSize);
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
