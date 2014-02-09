package environment.blocks;

public class BorderBlock extends PhysicalBlock{
	
	protected BorderBlock(){
		super();
		layer = 1;
	}
	
	protected BorderBlock(float x, float y){
		super(x,y);
		layer = 1;
	}
}
