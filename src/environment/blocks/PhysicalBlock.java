package environment.blocks;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;
import physics.GameBodyType;
import configuration.ConfigManager;

public class PhysicalBlock extends ShadowCasterBlock implements PhysicsObject{
	GameBodyType gbtype;
	
	public PhysicalBlock(float x, float y) {
		super(x,y);
		gbtype = GameBodyType.BLOCK;
	}

	public PhysicalBlock() {
		super();
		gbtype = GameBodyType.BLOCK;
	}
	
	@Override
	public void init(float x, float y)
	{
		initBlock(x, y);
		initPhysics();
	}

	public void initPhysics() {
		BodyDef bodyDef = new BodyDef();			    
	    bodyDef.position.set((points[0].x/ConfigManager.unitPixelSize + 0.5f)*ConfigManager.blockPhysicSize,
	    		(points[0].y/ConfigManager.unitPixelSize + 0.5f)*ConfigManager.blockPhysicSize);
	   
	    Body body = PhysicsManager.createBody(bodyDef);
	    PolygonShape box = new PolygonShape();
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.friction = 0;
	    fixtureDef.density = 0;
	    fixtureDef.shape = box;
	    fixtureDef.restitution = 0.0f;
	    box.setAsBox(0.5f*ConfigManager.blockPhysicSize, 0.5f*ConfigManager.blockPhysicSize);
	    body.createFixture(fixtureDef);
		PhysicsDataStructure s = new PhysicsDataStructure(this,gbtype); 
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
	
	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		
	}
}
