package environment.blocks;

public class DoorBlock extends ShadowCasterBlock{
	private Block underBlock;
	private boolean opened;
	/* blocks position in the map */
	private int i;
	private int j;
	
	public DoorBlock(Block underBlock, int i, int j){
		this.underBlock = underBlock;
		this.opened = false;
		this.color.x = 0.5f;
		this.color.y = 0.5f;
		this.color.z = 0.5f;
		this.color.w = 1;
		this.layer = 1;
		this.i = i;
		this.j = j;
	}
	
	public boolean isOpened(){
		return opened;
	}
	
	public void open(){
		opened = true;
	}
	
	public void close(){
		opened = false;
	}
	
	public void toggle(){
		if(opened)
			close();
		else
			open();
	}
	@Override
	public  boolean testCollision(){
		return !opened;
	}
	@Override
	public boolean castShadows(){
		return !opened;
	}
	@Override
	public void drawAt(float posX, float posY) {
		if(opened)
			underBlock.drawAt(posX, posY);
		else
			super.drawAt(posX, posY);
			
	}
	@Override
	public int getLayer(){
		return opened?underBlock.getLayer():this.layer;
	}
	
	public int getI(){
		return i;
	}
	
	public int getJ(){
		return j;
	}
}
