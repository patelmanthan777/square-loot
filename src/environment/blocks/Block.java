package environment.blocks;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import environment.Map;

public abstract class Block {
	protected Vector3f color = new Vector3f();

	public abstract boolean testCollision();

	public void drawAt(float posX, float posY) {
		glColor3f(color.x,color.y,color.z);
		glVertex2f(posX,posY);
		glVertex2f(posX,posY+Map.blockPixelSize.y);
		glVertex2f(posX+Map.blockPixelSize.x,posY+Map.blockPixelSize.y);
		glVertex2f(posX+Map.blockPixelSize.x,posY);
        
	}

	public abstract boolean castShadows();

	public void setColor(float r, float g, float b) {
		this.color.x = r;
		this.color.y = g;
		this.color.z = b;
	}
}
