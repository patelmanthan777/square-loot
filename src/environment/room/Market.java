package environment.room;

import static org.lwjgl.opengl.GL11.*;
import light.LightManager;
import entity.EntityManager;
import environment.Map;
import environment.blocks.BlockFactory;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.TextureManager;
import userInterface.MiniMap;
import configuration.ConfigManager;

public class Market extends Room{
	
	public Market(float posX, float posY){
		super(posX,posY);
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
				
											
		
		
		Vector2f pos = new Vector2f(x+Map.roomPixelSize.x/2,y+Map.roomPixelSize.y/2);
		Vector3f color = new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random());
		float radius = (float)Math.random()*100;
		float dstMax = Map.roomPixelSize.x;
		LightManager.addPointLight("room "+x+" " + y, pos, color, radius, dstMax ,false);
		EntityManager.createShopkeeper(pos.x / ConfigManager.unitPixelSize, pos.y / ConfigManager.unitPixelSize);
	}
	
	@Override
	public void destroy(){		
		LightManager.deleteLight("room "+x+" " + y);
	}
	
	@Override
	public void drawOnMiniMap() {
		super.drawOnMiniMap();
		int minix = (int) (MiniMap.position.x + (x / Map.roomPixelSize.x)
				* MiniMap.roomSize.x);
		int miniy = (int) (MiniMap.position.y + (y / Map.roomPixelSize.y)
				* MiniMap.roomSize.y);
		if(discovered){
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			TextureManager.shopKeeperTexture().setRotation(0);
			TextureManager.shopKeeperTexture().draw(MiniMap.roomSize.x/4f + minix,MiniMap.roomSize.y/4f+miniy,MiniMap.roomSize.x*1f/2f,MiniMap.roomSize.y*1f/2f);
			glDisable(GL_BLEND);
		}
	}
}