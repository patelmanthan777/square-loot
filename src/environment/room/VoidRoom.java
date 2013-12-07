package environment.room;

import environment.Map;
import environment.blocks.BlockFactory;

public class VoidRoom extends Room{
	public VoidRoom(int posX, int posY){
		super(posX, posY);
	}

	@Override
	protected void construct() {
		for(int i = 0; i < Map.roomBlockSize.x;i++){
			for(int j = 0; j < Map.roomBlockSize.y; j++){
				grid[i][j] = BlockFactory.createVoidBlock();
			}
		}
	}

	@Override
	public void drawOnMiniMap() {
		// TODO Auto-generated method stub
		
	}
}