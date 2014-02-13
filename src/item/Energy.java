package item;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import configuration.ConfigManager;
import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import rendering.TextureManager;
import sound.SoundManager;


public class Energy extends Item {
	int charge;

	
	public Energy(float x, float y, int charge){
		super(x,y,ItemListEnum.ENERGY);
		weight = 100;
		
		gbtype = GameBodyType.ENERGY;
		
		this.charge = charge;	
		this.halfSize.set(0.3f,0.3f);
		
		try {
			sprites = new SpriteSheet("assets/textures/energy.png",64,64);
			image = sprites.getSprite(0, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public int getTextureID(){
		return TextureManager.energyTexture().getTexture().getTextureID();
	}	
	
	public int getCharge(){
		return charge;
	}

	@Override	
	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.fixedRotation = false;
		bodyDef.position.set(position.x*ConfigManager.blockPhysicSize, position.y*ConfigManager.blockPhysicSize);
		body = PhysicsManager.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(halfSize.x*ConfigManager.blockPhysicSize, halfSize.y*ConfigManager.blockPhysicSize);
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
			SoundManager.coin(this.getX(), this.getY());
			destroyed = true;
			break;		
		default:
			break;
		}
	}
}