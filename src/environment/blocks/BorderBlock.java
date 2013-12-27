package environment.blocks;



public class BorderBlock extends Block{
	
	@Override
	public boolean testCollision(){
		return true;
	}

	@Override
	public boolean castShadows(){
		return false;
	}
	
	

}
