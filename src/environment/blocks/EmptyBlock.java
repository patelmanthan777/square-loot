package environment.blocks;

public class EmptyBlock extends Block {
	protected EmptyBlock(){
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
