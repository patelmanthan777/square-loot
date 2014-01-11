package environment.blocks;

import org.jbox2d.dynamics.World;


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
