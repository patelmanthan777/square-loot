package item.weapon;

import org.lwjgl.util.vector.Vector2f;
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

	public void drawInventory(float x,
			   				  float y,
			   				  float width,
			                  float height){
		glColor3f(1f, 0.00f, 0.00f);
		
		glVertex2f(x + width, y);
		glVertex2f(x, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);		
	}
	
}
