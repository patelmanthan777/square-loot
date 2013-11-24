package environment;

import environment.blocks.BlockFactory;

public class VoidRoom extends Room{
	public VoidRoom(int width, int height, int posX, int posY){
		super(width, height, posX, posY);
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height; j++){
				grid[i][j] = BlockFactory.createVoidBlock();
			}
		}
	}
}
