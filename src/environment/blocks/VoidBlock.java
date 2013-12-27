package environment.blocks;

public class VoidBlock extends Block{

	@Override
	public boolean testCollision() {
		return false;
	}

	@Override
	public boolean castShadows() {
		return false;
	}

}
