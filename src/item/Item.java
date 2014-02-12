package item;




import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;




import org.jbox2d.dynamics.BodyType;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import configuration.ConfigManager;
import physics.GameBodyType;
import physics.PhysicsObject;
import rendering.Drawable;
import utils.GraphicsAL;
import entity.DynamicEntity;
import environment.Map;

public abstract class Item extends DynamicEntity implements Drawable, PhysicsObject{
	protected float weight = 0;	
	Vector2f [] points = new Vector2f[4];
	public final ItemListEnum self;
	public boolean destroyed = false;	
	
	protected SpriteSheet sprites;
	protected Image image;
	public Item(float x, float y, ItemListEnum s){
		super(x, y);
		this.descFactor = 0.01f;
		this.halfSize.x = (float)30/(float)ConfigManager.unitPixelSize;
		this.halfSize.y = (float)30/(float)ConfigManager.unitPixelSize;	
		self = s;
		gbtype = GameBodyType.ITEM;
		this.btype = BodyType.DYNAMIC;
		
		for(int i = 0 ; i < 4 ; i++){
			points[i] = new Vector2f();
		}
		
		
	}
	
	
	public boolean shouldBeDestroyed(){
		return destroyed;
	}	
	
	public float getWeight(){
		return weight;
	}
	
	
	public void draw(){			
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);		
				
		image.draw(this.getX()*Map.blockPixelSize.x-halfSize.x*ConfigManager.unitPixelSize, this.getY()*Map.blockPixelSize.y-halfSize.y*ConfigManager.unitPixelSize,halfSize.x*2*ConfigManager.unitPixelSize,halfSize.y*2*ConfigManager.unitPixelSize);
	
		glDisable(GL_BLEND);
	}
	
	/**
	 * Draw a representation of the weapon in the inventory.
	 */
	public void draw(float x,
			         float y,
					 float width,
					 float height){
		
		
		points[0].x = x + (width - width*halfSize.x/halfSize.y)/2;
		points[0].y = y + height;
		points[1].x = x + (width - width*halfSize.x/halfSize.y)/2 + width*halfSize.x/halfSize.y;
		points[1].y = y + height;
		points[2].x = x + (width - width*halfSize.x/halfSize.y)/2 + width*halfSize.x/halfSize.y;
		points[2].y = y;
		points[3].x = x + (width - width*halfSize.x/halfSize.y)/2;
		points[3].y = y;		
		
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
