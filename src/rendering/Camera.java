package rendering;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import entity.Node;

public class Camera extends Node implements Drawable{
	
	
	
	public Camera(Vector2f pos) {
		super(pos);
	}

	
	@Override
	public void draw() {
		GL11.glTranslatef((int)(-getX()+Display.getWidth()/2.0f), (int)(-getY()+Display.getHeight()/2.0f),0);
	}
	
}
