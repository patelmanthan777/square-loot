package item;

import org.jbox2d.collision.shapes.PolygonShape;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;

import rendering.TextureManager;


public class Energy extends Item {
	int charge;

	
	public Energy(float x, float y, int charge){
		super(x,y,ItemListEnum.ENERGY);
		weight = 100;
		drawSize[0] = 15;
		drawSize[1] = 30;
		
		this.charge = charge;				
	}
	
	public int getTextureID(){
		return TextureManager.energyTexture().getTextureID();
	}	
	
	public int getCharge(){
		return charge;
	}

	@Override	
	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x, position.y);
		body = PhysicsManager.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(0.4f, 0.4f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.1f;
		body.createFixture(fixtureDef);
		PhysicsDataStructure s = new PhysicsDataStructure(this, GameBodyType.ENERGY); 
		body.setUserData(s);
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a)
	{
		
		switch(a.getType())
		{
		case PLAYER:
			destroyed = true;
			break;		
		default:
			break;
		}
	}
}