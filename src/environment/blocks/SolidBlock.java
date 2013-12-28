package environment.blocks;

public class SolidBlock extends ShadowCasterBlock{
	
	
	
	protected SolidBlock(){
		super();
	}
	
	protected SolidBlock(float x, float y){
		super(x,y);
	}

	@Override
	public boolean testCollision() {
		return true;
	}	
}
