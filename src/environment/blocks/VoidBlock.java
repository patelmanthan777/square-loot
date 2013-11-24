package environment.blocks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class VoidBlock implements Block{

	@Override
	public boolean testCollision() {
		return false;
	}

	@Override
	public void drawAt(float posX, float posY, Vector2f halfBlockSize) {
		GL11.glColor3f(0,0,0);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
		GL11.glVertex2f(posX-halfBlockSize.x,posY-halfBlockSize.y);
		GL11.glVertex2f(posX+halfBlockSize.x,posY+halfBlockSize.y);
		GL11.glVertex2f(posX-halfBlockSize.x,posY+halfBlockSize.y);
		GL11.glEnd();
	}

	@Override
	public boolean castShadows() {
		return false;
	}

}
