package environment.room;

import item.ItemManager;
import item.Key;
import environment.Map;
import environment.blocks.BlockFactory;
import light.LightManager;

public class KeyRoom extends Room {

	public KeyRoom(float posX, float posY) {
		super(posX, posY);
	}

	@Override
	public void construct(){
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

		Key k = new Key(x/Map.blockPixelSize.x+3,
				y/Map.blockPixelSize.y+(float)2.5);
		ItemManager.add(k);
	}
	
	@Override
	public void destroy() {
		LightManager.deleteLight("room "+x+" " + y);
	}

}
