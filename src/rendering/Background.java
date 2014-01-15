package rendering;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;

public class Background implements Drawable{
	
	private Vector2f speed = new Vector2f(0.00001f,0.00001f);
	private Vector2f pos = new Vector2f(0,0);
	
	public Background(){
	}
	
	public void update(long delta){
		pos.x = pos.x + speed.x * delta;
		pos.y = pos.y + speed.y * delta;
	}
	
	@Override
	public void draw() {

		glPushMatrix();
		glLoadIdentity();
		glColor4f(1,1,1,1);
		glBindTexture(GL_TEXTURE_2D, TextureManager.backgroundTexture().getTextureID());
		glBegin(GL_QUADS);
		glTexCoord2f(1 + ConfigManager.resolution.x/ConfigManager.resolution.y  + pos.x,1 + pos.y);
		glVertex2f(0, 0);
		glTexCoord2f(1 + ConfigManager.resolution.x/ConfigManager.resolution.y + pos.x,pos.y);
		glVertex2f(0, ConfigManager.resolution.y);
		glTexCoord2f(pos.x,pos.y);
		glVertex2f(ConfigManager.resolution.x, ConfigManager.resolution.y);
		glTexCoord2f(pos.x,1 + pos.y);
		glVertex2f(ConfigManager.resolution.x, pos.y);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}
	
}
