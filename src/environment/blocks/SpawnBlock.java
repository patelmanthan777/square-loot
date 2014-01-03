package environment.blocks;

public class SpawnBlock extends Block{
	public SpawnBlock(){
		super();
		layer = 0;
	}
	@Override
	public boolean testCollision() {
		return false;
	}
}
