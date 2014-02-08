package environment.room;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import light.LightManager;
import entity.EntityManager;
import environment.Map;
import environment.blocks.BlockFactory;

public class RandomBlockRoom extends OxygenRoom{
	private float proba = 0.01f;
	public RandomBlockRoom(float posX, float posY){
		super(posX, posY);
		pressure = 100;
	}

	@Override
	protected void construct() {
		super.construct();
		for(int i = 2; i < (int)Map.roomBlockSize.x-2;i++){
			for(int j = 2; j < (int)Map.roomBlockSize.y-2; j++){
				if(Math.random() < proba){
					grid[i][j] = BlockFactory.createSolidBlock();
				}
			}
		}
		
		Vector2f pos = new Vector2f(x+Map.roomPixelSize.x/2,y+Map.roomPixelSize.y/2);
		Vector3f color = new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random());
		float radius = (float)Math.random()*100;

		float dstMax = Map.roomPixelSize.x;
		LightManager.addPointLight("room "+x+" " + y, pos, color, radius, dstMax ,false);
		EntityManager.createZombie(pos.x / ConfigManager.unitPixelSize, pos.y / ConfigManager.unitPixelSize);
	}
}
