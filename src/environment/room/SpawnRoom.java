package environment.room;

import environment.blocks.BlockFactory;

public class SpawnRoom extends Room{

	public SpawnRoom(int width, int height, int posX, int posY,int blockSize) {
		super(width, height,posX,posY,blockSize);

		
	}

	@Override
	protected void construct() {
		for(int i = 1; i < width-1;i++){
			for(int j = 1; j < height-1; j++){
				grid[i][j] = BlockFactory.createEmptyBlock();
			}
		}
		for(int i = 0; i < width;i++){
			grid[i][0] = BlockFactory.createVoidBlock();
			grid[i][height-1] = BlockFactory.createVoidBlock();
		}
		for(int i = 0; i < height; i++){
			grid[0][i] = BlockFactory.createVoidBlock();
			grid[width-1][i] = BlockFactory.createVoidBlock();
		}
		
		for(int i = 1; i < width-1;i++){
			grid[i][1] = BlockFactory.createSolidBlock();
			grid[i][height-2] = BlockFactory.createSolidBlock();
		}
		for(int i = 1; i < height-1; i++){
			grid[1][i] = BlockFactory.createSolidBlock();
			grid[width-2][i] = BlockFactory.createSolidBlock();
		}
		grid[width/2][height/2] = BlockFactory.createSpawnBlock();
		grid[width/2-1][height/2] = BlockFactory.createSpawnBlock();
		grid[width/2][height/2-1] = BlockFactory.createSpawnBlock();
		grid[width/2-1][height/2-1] = BlockFactory.createSpawnBlock();
	}

	@Override
	public void drawOnMiniMap() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
