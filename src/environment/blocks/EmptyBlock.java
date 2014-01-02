package environment.blocks;

public class EmptyBlock extends Block {
	protected EmptyBlock(){
		super();
		layer = 0;
	}

	@Override
	public boolean testCollision() {
		return false;
	}

	@Override
	public boolean castShadows() {
		return false;
	}
	
}
