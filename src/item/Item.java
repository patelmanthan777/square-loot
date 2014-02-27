package item;




import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;




import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import configuration.ConfigManager;
import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
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
	
	protected float speedValue = 0;
	protected Vector2f initSpeed = new Vector2f(0,0);
	protected Vector2f throwdirection = new Vector2f(0,0);
	
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
				
		image.draw(this.getX()*Map.blockPixelSize-halfSize.x*ConfigManager.unitPixelSize, this.getY()*Map.blockPixelSize-halfSize.y*ConfigManager.unitPixelSize,halfSize.x*2*ConfigManager.unitPixelSize,halfSize.y*2*ConfigManager.unitPixelSize);
	
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
	
	@Override
	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = btype;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x, position.y);
		body = PhysicsManager.createBody(bodyDef);
		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(this.halfSize.x);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicCircle;
		fixtureDef.density = 0.3f;
		fixtureDef.friction = 0.0f;
		body.createFixture(fixtureDef);
		
		
		
		this.speed.set((float) (throwdirection.x * speedValue + initSpeed.x),
					   (float) (throwdirection.y * speedValue + initSpeed.y));
		
		Vec2 vel = new Vec2((direction.x * speedValue + initSpeed.x),
							(direction.y * speedValue + initSpeed.y));
		body.setLinearVelocity(vel);
		
		PhysicsDataStructure s = new PhysicsDataStructure(this, gbtype); 
		body.setUserData(s);
	}
	
	public void setThrow(float vx, float vy, float dx, float dy){
		initSpeed.set(vx, vy);
		throwdirection.set(dx,dy);
		
		speedValue=30;
	}
	
	public abstract int getTextureID();
}
