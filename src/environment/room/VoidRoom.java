package environment.room;

import environment.blocks.BlockFactory;

public class VoidRoom extends Room{
	public VoidRoom(int width, int height, int posX, int posY, int blockSize){
		super(width, height, posX, posY,blockSize);
	}

	@Override
	protected void construct() {
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height; j++){
				grid[i][j] = BlockFactory.createVoidBlock();
			}
		}
	}

	@Override
	public void drawOnMiniMap() {
		// TODO Auto-generated method stub
		
	}
}
