package environment.blocks;

import org.lwjgl.opengl.GL11;

import environment.Map;

public class VoidBlock implements Block{

	@Override
	public boolean testCollision() {
		return false;
	}

	@Override
	public void drawAt(float posX, float posY) {
		GL11.glColor3f(0,0,0);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(posX+Map.blockPixelSize.x,posY);
		GL11.glVertex2f(posX,posY);
		GL11.glVertex2f(posX+Map.blockPixelSize.x,posY+Map.blockPixelSize.y);
		GL11.glVertex2f(posX,posY+Map.blockPixelSize.y);
		GL11.glEnd();
	}

	@Override
	public boolean castShadows() {
		return false;
	}

}
