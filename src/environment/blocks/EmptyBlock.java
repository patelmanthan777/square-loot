package environment.blocks;

import org.jbox2d.dynamics.World;

public class EmptyBlock extends Block {
	protected EmptyBlock(){
		super();
		layer = 0;
	}

	@Override
	public void initPhysics(World w, float x, float y) {
	}
	
}
