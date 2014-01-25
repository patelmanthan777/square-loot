package environment.blocks;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import environment.Map;

public abstract class Block {
	/**
	 * The blocks are squared shaped.
	 */
	protected int nb_points = 4;
	protected boolean pressurized = false;
	protected boolean castShadows = false;
	protected boolean collidable = false;
	protected Vector2f[] points = new Vector2f[4];
	protected Vector4f color = new Vector4f();
	
	/*  Avoid memory leak */
	Vector2f edge = new Vector2f();
	Vector2f normal = new Vector2f();	
	Vector2f inter = new Vector2f();
	Vector2f norm = new Vector2f();
	
	/* ******************************* */
	
	/**
	 * Indicates whether shadows should be drawn on this block.
	 */
	protected int layer;
	
	
	
	

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
		glColor4f(color.x,color.y,color.z,color.w);
		glVertex2f(posX,posY);
		glVertex2f(posX,posY+Map.blockPixelSize.y);
		glVertex2f(posX+Map.blockPixelSize.x,posY+Map.blockPixelSize.y);
		glVertex2f(posX+Map.blockPixelSize.x,posY);
        
	}
	
	/**
	 * Compute the intersection point between an horizontal or vertical segment
	 * and half line define by a point and a direction, in a non degenerated
	 * case.
	 * 
	 * @param p1 coordinate of a segment extremity
	 * @param p2 coordinate of a segment extremity
	 * @param pos point position
	 * @param dir direction
	 * @return intersection point <b>null</b> if none
	 */
	private Vector2f regularIntersect(Vector2f p1,
									  Vector2f p2,
									  Vector2f pos,
									  Vector2f dir){
		
		
		if(p1.x == p2.x){
			norm.x = pos.x - p1.x;
			norm.y = 0;
			if(Vector2f.dot(norm, dir) < 0){			
				inter.x = p1.x;
				inter.y = pos.y + dir.y * Math.abs((p1.x - pos.x)/dir.x);
			
				if (p1.y >= inter.y && inter.y >= p2.y ||
					p1.y <= inter.y && inter.y <= p2.y)
					return inter;
			}
		}
		else if (p1.y == p2.y){
			norm.x = 0;
			norm.y = pos.y - p1.y;
			if(Vector2f.dot(norm, dir) < 0){
				inter.y = p1.y;
				inter.x = pos.x + dir.x * Math.abs((p1.y - pos.y)/dir.y);
			
				if (p1.x >= inter.x && inter.x >= p2.x ||
					p1.x <= inter.x && inter.x <= p2.x)
					return inter;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Compute the intersection point between an horizontal or vertical segment
	 * and half line define by a point and a direction. 
	 * 
	 * @param p1 coordinate of a segment extremity
	 * @param p2 coordinate of a segment extremity
	 * @param pos point position
	 * @param dir direction
	 * @return intersection point <b>null</b> if none
	 */
	private Vector2f intersect(Vector2f p1,
								Vector2f p2,
								Vector2f pos,
								Vector2f dir){
		
		//if on the segment and moving in the direction of the segment
		if(p1.x == p2.x && p1.x == pos.x && dir.x == 0 && 
		   (p1.y >= pos.y && pos.y >= p2.y ||
		    p1.y <= pos.y && pos.y <= p2.y)||
		   p1.y == p2.y && p1.y == pos.y && dir.y == 0 &&
		   (p1.x >= pos.x && pos.x >= p2.x ||
		    p1.x <= pos.x && pos.x <= p2.x)){			
			inter.x = pos.x;
			inter.y = pos.y;
			return inter;
		}
		//else if moving in the direction of the segment
		else if(p1.x == p2.x && p1.x == pos.x && dir.x == 0 ||
				p1.y == p2.y && p1.y == pos.y && dir.y == 0){
			
			if((p1.y > p2.y && p2.y > pos.y && dir.y > 0 ||
				p1.y < p2.y && p2.y < pos.y && dir.y < 0) ||
			   (p1.x > p2.x && p2.x > pos.x && dir.x > 0 ||
			    p1.x < p2.x && p2.x < pos.x && dir.x < 0)){
				inter.x = p2.x;
				inter.y = p2.y;
				return inter;				
			}
			else if((p2.y > p1.y && p1.y > pos.y && dir.y > 0 ||
					 p2.y < p1.y && p1.y < pos.y && dir.y < 0) ||
					(p2.x > p1.x && p1.x > pos.x && dir.x > 0 ||
					 p2.x < p1.x && p1.x < pos.x && dir.x < 0)){
				inter.x = p1.x;
				inter.y = p1.y;
				return inter;				
			}
			else
				return null;
				
		}
		else 			
			return regularIntersect(p1, p2, pos, dir);		
	}

	/**
	 * Test whether the position <b>pos</b> is within the block whose upper
	 * left corner has the coordinate <b>(x,y)</b>.
	 *  
	 * @param pos considered  position
	 * @param x horizontal block coordinate
	 * @param y vertical block coordinate
	 * @return true if the position is in the block
	 */
	public boolean isInside(Vector2f pos, int x, int y){
		return (((int) (pos.x / Map.blockPixelSize.x) == (int) (x / Map.blockPixelSize.x) &&
				 (int) (pos.y / Map.blockPixelSize.y) == (int) (y / Map.blockPixelSize.y)) ||
				(((int)((pos.x-1) / Map.blockPixelSize.x) == (int) (x / Map.blockPixelSize.x)) &&
				 ((int) (pos.y / Map.blockPixelSize.y) == (int) (y / Map.blockPixelSize.y))) ||
				(((int) (pos.x / Map.blockPixelSize.x) == (int) (x / Map.blockPixelSize.x)) &&
				 ((int) ((pos.y-1) / Map.blockPixelSize.y) == (int) (y / Map.blockPixelSize.y))) ||
				((pos.x == x + Map.blockPixelSize.x) &&
				 (pos.y == y + Map.blockPixelSize.y)));
	}
	
	/**
	 * Compute the intersection point between a block segment and
	 * half line define by a point and a direction. 
	 * 
	 * 
	 * @param pos current position 
	 * @param dir direction in which to find the first edge to intersect
	 * @param x is the horizontal coordinate of the block in pixel in the map
	 * @param y is the vertical coordinate of the block in pixel in the map
	 */
	public Vector2f intersectBlock(Vector2f pos, Vector2f dir, int x, int y){		
		
		
		
		initBlock(x, y);
		
		for (int i = 0; i < nb_points; i++){
			Vector2f currentVertex = points[i];
			Vector2f nextVertex = points[(i + 1) % 4];			
			Vector2f.sub(nextVertex,currentVertex, edge);
			normal.x = edge.getY();
			normal.y = -edge.getX();				
					
			if (isInside(pos, x, y) &&				
				!((currentVertex.x == nextVertex.x && currentVertex.x == pos.x) ||
				  (currentVertex.y == nextVertex.y && currentVertex.y == pos.y)) &&
				  Vector2f.dot(normal, dir) > 0){
				Vector2f inter = intersect(currentVertex, nextVertex, pos, dir);
				if (inter != null)
					return inter;
			}
			else if (!isInside(pos, x, y) &&
					!((currentVertex.x == nextVertex.x && currentVertex.x == pos.x) ||
					  (currentVertex.y == nextVertex.y && currentVertex.y == pos.y)) &&
					 Vector2f.dot(normal, dir) < 0)	{
				Vector2f inter = intersect(currentVertex, nextVertex, pos, dir);
				if (inter != null)
					return inter;
			}
		}
		
		return null;			
	}
	
	
	public boolean castShadows(){
		return castShadows;
	}

	public void setColor(float r, float g, float b, float a) {
		this.color.x = r;
		this.color.y = g;
		this.color.z = b;
		this.color.w = a;
	}
	
	public int getLayer(){
		return layer;
	}
	
	public boolean isPressurized(){
		return pressurized;
	}
	
	public  boolean testCollision(){
		return collidable;
	}
}
