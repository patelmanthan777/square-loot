package environment.blocks;

public class VoidBlock extends ShadowCasterBlock{

	protected VoidBlock(){
		super();
		layer = 2;
	}
	
	@Override
	public boolean testCollision() {
		return true;
	}
}
