package item.weapon;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
import utils.GraphicsAL;

import entity.projectile.ProjectileManager;

public class LaserRifle extends Weapon {

	

	public LaserRifle(long fireRate, float x ,float y)
	{
		super(fireRate, x, y);
		position.x = x;
		position.y = y;	
	}

	@Override
	public void fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			ProjectileManager.createBullet(pos, target);
			this.updateLastShot();
		}		
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
				   				   TextureManager.laserRifleTexture().getTextureID());			
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
		
		points[0] = new Vector2f(x,
				 				 y + height);
		points[1] = new Vector2f(x + width,
				 				 y + height);
		points[2] = new Vector2f(x + width, y);
		points[3] = new Vector2f(x, y);			
		
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		GraphicsAL.drawQuadTexture(points,
								   GraphicsAL.fullTexPoints,
								   TextureManager.laserRifleTexture().getTextureID());		
		glDisable(GL_BLEND);
	}
	
}
