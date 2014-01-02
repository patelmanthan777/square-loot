package environment.blocks;

public class VoidBlock extends Block{

	protected VoidBlock(){
		super();
		layer = 2;
	}
	
	@Override
	public boolean testCollision() {
		return true;
	}

	@Override
	public boolean castShadows() {
		return false;
	}

}
