package item.weapon;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
import static org.lwjgl.opengl.GL11.*;

import entity.projectile.ProjectileManager;

public class LaserRifle extends Weapon {

	public LaserRifle(long fireRate)
	{
		super(fireRate);
	}

	@Override
	public void Fire(Vector2f pos, Vector2f target) {	
		if(this.readyToFire())
		{
			ProjectileManager.createBullet(pos, target);
			this.updateLastShot();
		}		
	}

	/**
	 * Draw a representation of the weapon in the inventory.
	 */
	public void drawInventory(float x,
			   				  float y,
			   				  float width,
			                  float height){
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		glBindTexture(GL_TEXTURE_2D, TextureManager.laserRifleTexture().getTextureID());
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
		glDisable(GL_TEXTURE_2D);				
	}
	
}
