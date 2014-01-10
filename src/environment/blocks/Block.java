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
	protected int layer;
	
	
	
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

	/**
	 * Modify <b>pos</b> so that it is located in the next block in
	 * the direction <b>dir</b>.  
	 * 
	 * @param pos
	 * @param dir
	 * @param ix
	 * @param iy
	 */
	public void nextBlock(Vector2f pos, Vector2f dir, int ix, int iy){
		float x =  (ix * Map.blockPixelSize.x);
		float y =  (iy * Map.blockPixelSize.y);
		Vector2f edge = new Vector2f();
		Vector2f normal = new Vector2f();
		Vector2f toVertex = new Vector2f();			
		
		initBlock(x, y);
		
		for (int i = 0; i < nb_points; i++){
			Vector2f currentVertex = points[i];
			Vector2f nextVertex = points[(i + 1) % 4];
			Vector2f.sub(nextVertex,currentVertex, edge);
			normal.x = edge.getY();
			normal.y = -edge.getX();			
	
			Vector2f vert = new Vector2f(0f, 1.0f);
			
			if (Vector2f.dot(normal, dir) > 0){
				if (Vector2f.dot(vert, normal) > 0) {
					toVertex.y = nextVertex.y - pos.y;					
				}
				else if (Vector2f.dot(vert, normal) < 0) {
					toVertex.y = pos.y - currentVertex.y;
				}
				else if (Vector2f.dot(vert, edge) < 0) {
					toVertex.x = nextVertex.x - pos.x;
				}
				else if (Vector2f.dot(vert, edge) > 0) {
					toVertex.x = pos.x - currentVertex.x;
				}
			}									
		}
		
		if (toVertex.x / dir.x < toVertex.y / dir.y) {
			pos.x += toVertex.x;
			pos.y += toVertex.y * toVertex.x / dir.x;
			pos.scale(1.01f);
		}
		else{
			pos.x += toVertex.x * toVertex.y / dir.y;
			pos.y += toVertex.y;
			pos.scale(1.01f);
		}
			
	}
	
	public boolean castShadows(){
		return false;
	}

	public void setColor(float r, float g, float b) {
		this.color.x = r;
		this.color.y = g;
		this.color.z = b;
	}
	
	public int getLayer(){
		return layer;
	}
}
