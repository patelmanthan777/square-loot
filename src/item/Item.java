package item;



import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.util.vector.Vector2f;

import rendering.Drawable;
import rendering.TextureManager;
import utils.GraphicsAL;
import entity.Node;

public abstract class Item extends Node implements Drawable{
	protected float weight = 0;	
	
	protected int [] drawSize = new int[2];	
	
	public Item(float x, float y){
		super(x, y);
		drawSize[0] = 30;
		drawSize[1] = 30;
	}
	
	public float getWeight(){
		return weight;
	}
	
	
	public void draw(){
		Vector2f [] points = new Vector2f[4];
		points[0] = new Vector2f(position.x,
				 				 position.y + drawSize[1]);
		points[1] = new Vector2f(position.x + drawSize[0],
                				 position.y + drawSize[1]);
		points[2] = new Vector2f(position.x + drawSize[0],
				   				 position.y);
		points[3] = new Vector2f(position.x, position.y);				
		
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
		GraphicsAL.drawQuadTexture(points,
				   				   GraphicsAL.fullTexPoints,
				   				   getTextureID());			
		glDisable(GL_BLEND);
	}
	
	/**
	 * Draw a representation of the weapon in the inventory.
	 */
	public void draw(float x,
			         float y,
					 float width,
					 float height){
		Vector2f [] points = new Vector2f[4];
		
		points[0] = new Vector2f(x + (width - width*drawSize[0]/drawSize[1])/2,
				 				 y + height);
		points[1] = new Vector2f(x + (width - width*drawSize[0]/drawSize[1])/2 + width*drawSize[0]/drawSize[1],
				 				 y + height);
		points[2] = new Vector2f(x + (width - width*drawSize[0]/drawSize[1])/2 + width*drawSize[0]/drawSize[1], y);
		points[3] = new Vector2f(x + (width - width*drawSize[0]/drawSize[1])/2, y);			
		
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		GraphicsAL.drawQuadTexture(points,
								   GraphicsAL.fullTexPoints,
								   getTextureID());		
		glDisable(GL_BLEND);
	}
	

	public abstract int getTextureID();
}
