package environment.blocks;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import environment.Map;

public abstract class Block {
	/**
	 * The blocks are squared shaped.
	 */
	protected int nb_points = 4;
	
	protected Vector2f[] points = new Vector2f[4];
	protected Vector3f color = new Vector3f();

	/**
	 * Indicates whether shadows should be drawn on this block.
	 */
	public int layer;
	
	
	
	public abstract boolean testCollision();

	public Block(){
		points[0] = new Vector2f();
		points[1] = new Vector2f();
		points[2] = new Vector2f();
		points[3] = new Vector2f();
		
		layer = 0;
	}
	
	public Block(float x, float y){
		points[0] = new Vector2f(x, y);
		points[1] = new Vector2f(x + Map.blockPixelSize.x, y);
		points[2] = new Vector2f(x + Map.blockPixelSize.x, y + Map.blockPixelSize.y);
		points[3] = new Vector2f(x, y + Map.blockPixelSize.y);
		
		layer = 0;
	}
	
	/**
	 * Set a block position to the given coordinates.
	 * @param positionx
	 * @param positiony
	 */
	protected void initBlock(float positionx, float positiony){
		float x = positionx;
		float y = positiony;
		points[0].x = x;
		points[0].y = y;
		points[1].x = x + Map.blockPixelSize.x;
		points[1].y = y;
		points[2].x = x + Map.blockPixelSize.x;
		points[2].y = y + Map.blockPixelSize.y;
		points[3].x = x ;
		points[3].y = y + Map.blockPixelSize.y;
	}
	
	/**
	 * Only a single instance of any specific block will be stored therefore,
	 * the drawing method needs to be able to draw at multiple locations.
	 * 
	 * @param posX
	 * @param posY
	 */
	public void drawAt(float posX, float posY) {
		glColor3f(color.x,color.y,color.z);
		glVertex2f(posX,posY);
		glVertex2f(posX,posY+Map.blockPixelSize.y);
		glVertex2f(posX+Map.blockPixelSize.x,posY+Map.blockPixelSize.y);
		glVertex2f(posX+Map.blockPixelSize.x,posY);
        
	}

	public abstract boolean castShadows();

	public void setColor(float r, float g, float b) {
		this.color.x = r;
		this.color.y = g;
		this.color.z = b;
	}
}
