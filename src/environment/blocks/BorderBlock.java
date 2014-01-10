package environment.blocks;



public class BorderBlock extends ShadowCasterBlock{
	
	protected BorderBlock(){
		super();
		layer = 1;
	}
	
	protected BorderBlock(float x, float y){
		super(x,y);
		layer = 1;
	}
	
	@Override
	public boolean testCollision(){
		return true;
	}

}
