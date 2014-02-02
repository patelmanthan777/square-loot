package environment.blocks;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class VoidBlock extends ShadowCasterBlock{

	protected VoidBlock(){
		super();
		layer = 2;
		this.collidable = true;
	}

	@Override
	public Body initPhysics(World w, float x, float y) {
		return null;
	}
}
