package environment.room;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import userInterface.MiniMap;
import light.LightManager;
import environment.Map;
import environment.blocks.BlockFactory;

public class TestRoom extends Room{
	
	public TestRoom(float posX, float posY){
		super(posX, posY);
		construct();
	}

	@Override
	protected void construct() {
		for(int i = 1; i < (int)Map.roomBlockSize.x-1;i++){
			for(int j = 1; j < (int)Map.roomBlockSize.y-1; j++){
				if(Math.random() > 0.02){
					grid[i][j] = BlockFactory.createEmptyBlock();
				}else{
					grid[i][j] = BlockFactory.createSolidBlock();
				}
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
			grid[i][1] = BlockFactory.createSolidBlock();
			grid[i][(int)Map.roomBlockSize.y-2] = BlockFactory.createSolidBlock();
		}
		for(int i = 1; i < (int)Map.roomBlockSize.y-1; i++){
			grid[1][i] = BlockFactory.createSolidBlock();
			grid[(int)Map.roomBlockSize.x-2][i] = BlockFactory.createSolidBlock();
		}
		
		LightManager.addActivatedLight("l"+x+y, new Vector2f(x+Map.roomPixelSize.x/2,y+Map.roomPixelSize.y/2), new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random()),(float)Math.random()*20,Map.roomPixelSize.x * 2);
		
	}
}
