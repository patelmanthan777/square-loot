package rendering;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import utils.GraphicsAL;

public class Background implements Drawable{
	private Vector2f[] points = new Vector2f[4];
	private Vector2f[] texPoints = new Vector2f[4];
	private Vector2f speed = new Vector2f(0.00001f,0.00001f);
	private Vector2f pos = new Vector2f(0,0);
	
	public Background(){
		texPoints[0] = new Vector2f(1 + ConfigManager.resolution.x/ConfigManager.resolution.y  + pos.x,
									1 + pos.y);
		points[0] = new Vector2f(0,0);

		texPoints[1] = new Vector2f(1 + ConfigManager.resolution.x/ConfigManager.resolution.y + pos.x,
									pos.y);
		points[1] = new Vector2f(0, ConfigManager.resolution.y);
		texPoints[2] = new Vector2f(pos.x, pos.y);
		points[2] = new Vector2f(ConfigManager.resolution.x, ConfigManager.resolution.y);
		texPoints[3] = new Vector2f(pos.x,1 + pos.y);	
		points[3] = new Vector2f(ConfigManager.resolution.x, pos.y);			
	}
	
	public void update(long delta){
		pos.x = pos.x + speed.x * delta;
		pos.y = pos.y + speed.y * delta;
		texPoints[0].x = 1 + ConfigManager.resolution.x/ConfigManager.resolution.y  + pos.x;
		texPoints[0].y = 1 + pos.y;
		texPoints[1].x = 1 + ConfigManager.resolution.x/ConfigManager.resolution.y + pos.x;
		texPoints[1].y = pos.y;
		texPoints[2].x = pos.x;
		texPoints[2].y = pos.y;
		texPoints[3].x = pos.x;
		texPoints[3].y = 1 + pos.y;	
	}
	
	@Override
	public void draw() {		
		glPushMatrix();
		glLoadIdentity();
		glColor3f(1,1,1);
		
		GraphicsAL.drawQuadTexture(points,
								   texPoints,
								   TextureManager.backgroundTexture().getTexture().getTextureID());		
		
		glPopMatrix();
	}
	
}
