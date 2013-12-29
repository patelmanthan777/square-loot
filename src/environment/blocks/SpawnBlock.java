package environment.blocks;

public class SpawnBlock extends Block{
	public SpawnBlock(){
		super();
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
