package item;



import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.jbox2d.dynamics.Body;
import org.lwjgl.util.vector.Vector2f;

import physics.PhysicsDataStructure;
import physics.PhysicsObject;

import rendering.Drawable;
import utils.GraphicsAL;
import entity.Entity;
import environment.Map;

public abstract class Item extends Entity implements Drawable, PhysicsObject{
	protected float weight = 0;	
	Vector2f [] points = new Vector2f[4];
	
	
	protected Body body;
	protected boolean destroyed = false;
	
	protected int [] drawSize = new int[2];	
	
	public Item(float x, float y){
		super(x, y);
		drawSize[0] = 30;
		drawSize[1] = 30;
		
		for(int i = 0 ; i < 4 ; i++){
			points[i] = new Vector2f();
		}
	}
	
	public boolean shouldBeDestroyed(){
		return destroyed;
	}
	
	public void destroy() {
		body.getWorld().destroyBody(body);
	}
	
	public float getWeight(){
		return weight;
	}
	
	
	public void draw(){
		
		points[0].x = position.x*Map.blockPixelSize.x;
		points[0].y = position.y*Map.blockPixelSize.y + drawSize[1];
		points[1].x = position.x*Map.blockPixelSize.x + drawSize[0];
		points[1].y = position.y*Map.blockPixelSize.y + drawSize[1];
		points[2].x = position.x*Map.blockPixelSize.x + drawSize[0];
		points[2].y = position.y*Map.blockPixelSize.y;
		points[3].x = position.x*Map.blockPixelSize.x;
		points[3].y = position.y*Map.blockPixelSize.y;				
		
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
		
		
		points[0].x = x + (width - width*drawSize[0]/drawSize[1])/2;
		points[0].y = y + height;
		points[1].x = x + (width - width*drawSize[0]/drawSize[1])/2 + width*drawSize[0]/drawSize[1];
		points[1].y = y + height;
		points[2].x = x + (width - width*drawSize[0]/drawSize[1])/2 + width*drawSize[0]/drawSize[1];
		points[2].y = y;
		points[3].x = x + (width - width*drawSize[0]/drawSize[1])/2;
		points[3].y = y;		
		
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		GraphicsAL.drawQuadTexture(points,
								   GraphicsAL.fullTexPoints,
								   getTextureID());		
		glDisable(GL_BLEND);
	}
	
	public void initPhysics(){
	}

	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case BLOCK:
			break;
		case ENTITY:
			break;
		case PLAYER:
			break;
		case PROJECTILE:
			break;
		default:
			break;
		}
	}
	
	public abstract int getTextureID();
}
