package environment.blocks;



public class BorderBlock extends ShadowCasterBlock{
	
	@Override
	public boolean testCollision(){
		return true;
	}

	@Override
	public boolean castShadows(){
		return true;
	}
	
	

}
