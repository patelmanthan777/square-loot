package environment.blocks;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import environment.Map;

public class SpawnBlock extends Block{
	public SpawnBlock(){
		super();
		layer = 0;
	}
	@Override
	public boolean testCollision() {
		return false;
	}
	@Override
	public void initPhysics(World w, float x, float y) {
	}
}
