package environment.blocks;

public class SolidBlock extends ShadowCasterBlock{
	
	
	
	protected SolidBlock(){
		super();
		isShadowFree = true;
	}
	
	protected SolidBlock(float x, float y){
		super(x,y);
		isShadowFree = true;
	}

	@Override
	public boolean testCollision() {
		return true;
	}	
}
