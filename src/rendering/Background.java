package rendering;

import static org.lwjgl.opengl.GL11.*;
import configuration.ConfigManager;

public class Background implements Drawable{
	
	
	@Override
	public void draw() {
		glPushMatrix();
		glLoadIdentity();
		glColor4f(1,1,1,1);
		glBindTexture(GL_TEXTURE_2D, TextureManager.backgroundTexture().getTextureID());
		glBegin(GL_QUADS);
		glTexCoord2f(1,1);
		glVertex2f(0, 0);
		glTexCoord2f(1,0);
		glVertex2f(0, ConfigManager.resolution.y);
		glTexCoord2f(0,0);
		glVertex2f(ConfigManager.resolution.x, ConfigManager.resolution.y);
		glTexCoord2f(0,1);
		glVertex2f(ConfigManager.resolution.x, 0);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}
	
}
