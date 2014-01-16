package environment.room;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import light.LightManager;
import environment.Map;
import environment.blocks.BlockFactory;

public class OxygenRoom extends Room{

	private float load;
	public OxygenRoom(float posX, float posY) {
		super(posX, posY);
		this.load = 10000;
		this.pressure = 1000;
		this.newPressure = 1000;
		this.miniMapColor.x = 0;
		this.miniMapColor.y = 0;
		this.miniMapColor.z = 1;
		construct();
	}

	@Override
	protected void construct() {
		for(int i = 1; i < (int)Map.roomBlockSize.x-1;i++){
			for(int j = 1; j < (int)Map.roomBlockSize.y-1; j++){
				grid[i][j] = BlockFactory.createEmptyBlock();
			}
		}
		for(int i = 0; i < (int)Map.roomBlockSize.x;i++){
			grid[i][0] = BlockFactory.createVoidBlock();
			grid[i][(int)Map.roomBlockSize.y-1] = BlockFactory.createVoidBlock();
		}
		for(int i = 0; i < (int)Map.roomBlockSize.y; i++){
			grid[0][i] = BlockFactory.createVoidBlock();
			grid[(int)Map.roomBlockSize.x-1][i] = BlockFactory.createVoidBlock();
		}
		
		for(int i = 1; i < (int)Map.roomBlockSize.x-1;i++){
			grid[i][1] = BlockFactory.createBorderBlock();
			grid[i][(int)Map.roomBlockSize.y-2] = BlockFactory.createBorderBlock();
		}
		for(int i = 1; i < (int)Map.roomBlockSize.y-1; i++){
			grid[1][i] = BlockFactory.createBorderBlock();
			grid[(int)Map.roomBlockSize.x-2][i] = BlockFactory.createBorderBlock();
		}
		Vector2f pos = new Vector2f(x+5*Map.roomPixelSize.x/11,y+Map.roomPixelSize.y/2);
		Vector3f color = new Vector3f(1,0.1f,0.1f);
		float radius = 50;
		float dstMax = Map.roomPixelSize.x;
		LightManager.addPointLight("room "+x+"0 " + y, pos, color, radius, dstMax ,false);
		pos.x += Map.roomPixelSize.x/11;
		LightManager.addPointLight("room "+x+"1 " + y, pos, color, radius, dstMax ,false);
	}

	public void setNewPressure(float pressure){
		this.newPressure = pressure;
	}
	
	public void update(){
		if(load > 0){
			load -= (this.pressure - this.newPressure);
			if(load < 0){
				this.pressure += load;
			}
		}else{
			this.pressure = this.newPressure;
		}
	}
	
}
