package rendering;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import entity.Entity;

public class Camera extends Entity implements Drawable{
	
	
	
	public Camera(Vector2f pos) {
		super(pos);
	}

	
	@Override
	public void draw() {
		GL11.glTranslatef((int)(-getX()*ConfigManager.unitPixelSize+Display.getWidth()/2.0f), (int)(-getY()*ConfigManager.unitPixelSize+Display.getHeight()/2.0f),0);
	}
	
}
