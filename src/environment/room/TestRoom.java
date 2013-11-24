package environment.room;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import light.LightManager;
import environment.blocks.BlockFactory;

public class TestRoom extends Room{
	
	public TestRoom(int width, int height, int posX, int posY, int blockSize){
		super(width, height, posX, posY,blockSize);
		construct();
	}

	@Override
	protected void construct() {
		for(int i = 1; i < width-1;i++){
			for(int j = 1; j < height-1; j++){
				if(Math.random() > 0.02){
					grid[i][j] = BlockFactory.createEmptyBlock();
				}else{
					grid[i][j] = BlockFactory.createSolidBlock();
				}
				
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
		
		LightManager.addActivatedLight("l"+x+y, new Vector2f((x+width/2)*blockSize,(y+height/2)*blockSize), new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random()),(float)Math.random()*20,1 * blockSize * width);
		
	}

	@Override
	public void drawOnMiniMap() {
		// GL11.glColor3f(color.x,color.y,color.z);
		glColor3f(1, 1, 1);
		// draw quad
		glPushMatrix();
		glLoadIdentity();
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f(x/width, y/height);
		//glVertex2f(position.x - halfSize.x, position.y - halfSize.y);
		//glVertex2f(position.x + halfSize.x, position.y + halfSize.y);
		//glVertex2f(position.x - halfSize.x, position.y + halfSize.y);
		//glEnd();
		
	}
	
	
	
	
}
