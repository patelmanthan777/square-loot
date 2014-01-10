package environment.room;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import light.LightManager;
import entity.npc.LivingEntityManager;
import environment.Map;
import environment.blocks.BlockFactory;

public class TestRoom extends Room{
	private float proba = 0.02f;
	public TestRoom(float posX, float posY){
		super(posX, posY);
		construct();
	}

	@Override
	protected void construct() {
		for(int i = 1; i < (int)Map.roomBlockSize.x-1;i++){
			for(int j = 1; j < (int)Map.roomBlockSize.y-1; j++){
				if(Math.random() > proba){
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
			grid[i][1] = BlockFactory.createBorderBlock();
			grid[i][(int)Map.roomBlockSize.y-2] = BlockFactory.createBorderBlock();
		}
		for(int i = 1; i < (int)Map.roomBlockSize.y-1; i++){
			grid[1][i] = BlockFactory.createBorderBlock();
			grid[(int)Map.roomBlockSize.x-2][i] = BlockFactory.createBorderBlock();
		}
		Vector2f pos = new Vector2f(x+Map.roomPixelSize.x/2,y+Map.roomPixelSize.y/2);
		Vector3f color = new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random());
		float radius = (float)Math.random()*100;

		float dstMax = Map.roomPixelSize.x;
		LightManager.addPointLight("room "+x+" " + y, pos, color, radius, dstMax ,false);
		LivingEntityManager.createZombie(pos.x, pos.y);
	}
}
