package environment.blocks;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import physics.PhysicsDataStructure;
import physics.PhysicsObject;
import physics.bodyType;
import configuration.ConfigManager;

public class PhysicalBlock extends ShadowCasterBlock implements PhysicsObject{

	public PhysicalBlock(float x, float y) {
		super(x,y);
	}

	public PhysicalBlock() {
		super();
	}

	@Override
	public void initPhysics(World w, float x, float y) {
		BodyDef bodyDef = new BodyDef();			    
	    bodyDef.position.set(x/ConfigManager.unitPixelSize + 0.5f,
	    		y/ConfigManager.unitPixelSize + 0.5f);
	    Body body = w.createBody(bodyDef);
	    PolygonShape box = new PolygonShape();
	    box.setAsBox(0.5f, 0.5f);
	    body.createFixture(box, 0.0f);
		PhysicsDataStructure s = new PhysicsDataStructure(this,bodyType.BLOCK); 
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
		case PROJECTILE:
			break;
		default:
			break;
		}
	}
}
