package environment.blocks;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import environment.Map;



public class BorderBlock extends ShadowCasterBlock{
	
	protected BorderBlock(){
		super();
		layer = 1;
	}
	
	protected BorderBlock(float x, float y){
		super(x,y);
		layer = 1;
	}
	
	@Override
	public boolean testCollision(){
		return true;
	}

	@Override
	public void initPhysics(World w, float x, float y) {
		BodyDef bodyDef = new BodyDef();			    
	    bodyDef.position.set(x+ Map.blockPixelSize.x/2, y + Map.blockPixelSize.y/2);
	    Body body = w.createBody(bodyDef);
	    PolygonShape box = new PolygonShape();
	    box.setAsBox(Map.blockPixelSize.x/2, Map.blockPixelSize.y/2);
	    body.createFixture(box, 0.0f);
	}

}
