package environment.room;

import environment.Map;
import environment.blocks.BlockFactory;
import environment.element.EndGate;

public class EndRoom extends Room{
	private int endGateSize = 2;
	private EndGate endGate;
	private int lock = 4;
	
	public EndRoom(float posX, float posY) {
		super(posX, posY);
		miniMapColor.x = 0;
		miniMapColor.y = 0;
		miniMapColor.z = 1;
		endGate = new EndGate(endGateSize, lock, posX/Map.blockPixelSize.x + Map.roomBlockSize.x/2, posY/Map.blockPixelSize.y+Map.roomBlockSize.y/2);
		System.out.println();
		endGate.initPhysics();
	}

	@Override
	public void construct() {
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
		
		for(int i = (int)Map.roomBlockSize.x/2-endGateSize; i < (int)Map.roomBlockSize.x/2+endGateSize; i++){
			for(int j = (int)Map.roomBlockSize.y/2-endGateSize; j < (int)Map.roomBlockSize.y/2+endGateSize; j++){
				grid[i][j] = BlockFactory.createEndBlock();
			}
		}	
	}
	
	@Override
	public void destroy(){
	}
	
	

}
