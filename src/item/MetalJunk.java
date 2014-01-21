package item;

import static org.lwjgl.opengl.GL11.*;

import rendering.TextureManager;
import item.Item;


public class MetalJunk extends Item {
	public MetalJunk(float x, float y){
		super(x,y);
		weight = 100;
		drawSize[0] = 15;
		drawSize[1] = 30;
	}

	
	public void draw(){
		draw(position.x, position.y, drawSize[0], drawSize[1]);
	}

	/**
	 * Draw a representation of the weapon in the inventory.
	 */
	public void draw(float x,
			   		 float y,
			   		 float width,
			         float height){	
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		glBindTexture(GL_TEXTURE_2D, TextureManager.metalJunkTexture().getTextureID());
		glBegin(GL_QUADS);
		glTexCoord2f(0,1);
		glVertex2f(x, y + height);				
		glTexCoord2f(1,1);
		glVertex2f(x + width, y + height);
		glTexCoord2f(1,0);			
		glVertex2f(x + width, y);
		glTexCoord2f(0,0);		
		glVertex2f(x, y);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);					
	}
	
}