package environment.blocks;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.GameBodyType;
import configuration.ConfigManager;
import environment.Map;

public class DoorBlock extends PhysicalBlock {
	private Block underBlock;
	private boolean opened;
	/* blocks position in the map */
	private int i;
	private int j;

	private Body body;

	public DoorBlock(Block underBlock, int i, int j) {
		this.underBlock = underBlock;
		this.opened = false;
		this.color.x = 0.3f;
		this.color.y = 0.3f;
		this.color.z = 0.3f;
		this.color.w = 1;
		this.layer = 1;
		this.i = i;
		this.j = j;
		this.layer = Map.doorLayer;
		this.castShadows = false;
	}

	public boolean isOpened() {
		return opened;
	}

	public void open() {
		if (!opened) {
			opened = true;
			body.getWorld().destroyBody(body);
		}
	}

	public void close() {
		if(opened)
		{
			opened = false;
			initPhysics();
		}
	}

	public void toggle() {
		if (opened)
			close();
		else
			open();
	}

	@Override
	public void drawAt(float posX, float posY) {
		if (opened)
			underBlock.drawAt(posX, posY);
		else
			super.drawAt(posX, posY);

	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	@Override
	public void initPhysics() {
		BodyDef bodyDef = new BodyDef();			    
	    bodyDef.position.set(points[0].x/ConfigManager.unitPixelSize + 0.5f,
	    		points[0].y/ConfigManager.unitPixelSize + 0.5f);
	    body = PhysicsManager.createBody(bodyDef);
	    PolygonShape box = new PolygonShape();
	    box.setAsBox(0.5f, 0.5f);
	    body.createFixture(box, 0.0f);
		PhysicsDataStructure s = new PhysicsDataStructure(this,GameBodyType.BLOCK); 
		body.setUserData(s);
	}

	public void disablePhysics() {

	}

	public void enablePhysics() {

	}
}
