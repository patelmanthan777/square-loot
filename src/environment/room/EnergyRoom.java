package environment.room;

import light.LightManager;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import environment.Map;
import environment.blocks.BlockFactory;

public class EnergyRoom extends Room{
	public EnergyRoom(float posX, float posY){
		super(posX,posY);
	}
	
	@Override
	protected void construct(){
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
		
		/*
		grid[2][2] = BlockFactory.createEnergyBlock();
		grid[3][2] = BlockFactory.createEnergyBlock();
		*/
		grid[4][2] = BlockFactory.createSolidBlock();
		
		grid[11][2] = BlockFactory.createSolidBlock();
		/*
		grid[12][2] = BlockFactory.createEnergyBlock();
		grid[13][2] = BlockFactory.createEnergyBlock();
		*/
		
		/*
		grid[2][(int)Map.roomBlockSize.y-3] = BlockFactory.createEnergyBlock();
		grid[3][(int)Map.roomBlockSize.y-3] = BlockFactory.createEnergyBlock();		
		*/
		grid[4][(int)Map.roomBlockSize.y-3] = BlockFactory.createSolidBlock();
		
		grid[11][(int)Map.roomBlockSize.y-3] = BlockFactory.createSolidBlock();
		/*
		grid[12][(int)Map.roomBlockSize.y-3] = BlockFactory.createEnergyBlock();
		grid[13][(int)Map.roomBlockSize.y-3] = BlockFactory.createEnergyBlock();
		*/
									
		
		
		Vector2f pos = new Vector2f(x+Map.roomPixelSize.x/2,y+Map.roomPixelSize.y/2);
		Vector3f color = new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random());
		float radius = (float)Math.random()*100;

		float dstMax = Map.roomPixelSize.x;
		LightManager.addPointLight("room "+x+" " + y, pos, color, radius, dstMax ,false);
	}
}