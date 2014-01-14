package environment.blocks;



public class BorderBlock extends ShadowCasterBlock{
	
	protected BorderBlock(){
		super();
		layer = 1;
		this.collidable = true;
	}
	
	protected BorderBlock(float x, float y){
		super(x,y);
		layer = 1;
		this.collidable = true;
	}
	
}
