package environment.blocks;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;
import physics.GameBodyType;
import configuration.ConfigManager;

public class PhysicalBlock extends ShadowCasterBlock implements PhysicsObject{
	
	public PhysicalBlock(float x, float y) {
		super(x,y);
	}

	public PhysicalBlock() {
		super();
	}
	
	@Override
	public void init(float x, float y)
	{
		initBlock(x, y);
		initPhysics();
	}

	public void initPhysics() {
		BodyDef bodyDef = new BodyDef();			    
	    bodyDef.position.set(points[0].x/ConfigManager.unitPixelSize + 0.5f,
	    		points[0].y/ConfigManager.unitPixelSize + 0.5f);
	    Body body = PhysicsManager.createBody(bodyDef);
	    PolygonShape box = new PolygonShape();
	    box.setAsBox(0.5f, 0.5f);
	    body.createFixture(box, 0.0f);
		PhysicsDataStructure s = new PhysicsDataStructure(this,GameBodyType.BLOCK); 
		body.setUserData(s);
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
}
