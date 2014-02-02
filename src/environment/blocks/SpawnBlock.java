package environment.blocks;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class SpawnBlock extends EmptyBlock{
	public SpawnBlock(){
		super();
		layer = 0;
	}

	@Override
	public Body initPhysics(World w, float x, float y) {
		return null;
	}
}
