package environment.blocks;

public class SpawnBlock extends Block{
	public SpawnBlock(){
		super();
		this.color.x = 1.0f;
		this.color.y = 0.0f;
		this.color.z = 0.5f;
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
