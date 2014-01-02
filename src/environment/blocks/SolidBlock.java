package environment.blocks;

public class SolidBlock extends ShadowCasterBlock{
	
	
	
	protected SolidBlock(){
		super();
		layer = 1;
	}
	
	protected SolidBlock(float x, float y){
		super(x,y);
		layer = 1;
	}

	@Override
	public boolean testCollision() {
		return true;
	}	
}
