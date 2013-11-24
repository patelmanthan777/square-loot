package environment;

import environment.blocks.BlockFactory;

public class VoidRoom extends Room{
	public VoidRoom(int width, int height, int posX, int posY, int blockSize){
		super(width, height, posX, posY,blockSize);
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height; j++){
				grid[i][j] = BlockFactory.createVoidBlock();
			}
		}
	}

	@Override
	protected void construct() {
		// TODO Auto-generated method stub
		
	}
}
