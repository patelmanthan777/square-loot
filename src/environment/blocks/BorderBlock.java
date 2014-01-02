package environment.blocks;



public class BorderBlock extends ShadowCasterBlock{
	
	protected BorderBlock(){
		super();
		isShadowFree = true;
	}
	
	protected BorderBlock(float x, float y){
		super(x,y);
		isShadowFree = true;
	}
	
	@Override
	public boolean testCollision(){
		return true;
	}

	@Override
	public boolean castShadows(){
		return true;
	}
	
	

}
