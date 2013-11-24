package environment.blocks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EmptyBlock implements Block {
	private Vector3f color = new Vector3f(1,1,1);
	protected EmptyBlock(){
	}
	
	@Override
	public void drawAt(float posX, float posY, Vector2f halfBlockSize) {
		GL11.glColor3f(color.x,color.y,color.z);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
		GL11.glVertex2f(posX-halfBlockSize.x,posY-halfBlockSize.y);
		GL11.glVertex2f(posX+halfBlockSize.x,posY+halfBlockSize.y);
		GL11.glVertex2f(posX-halfBlockSize.x,posY+halfBlockSize.y);
		GL11.glEnd();
	}

	@Override
	public boolean testCollision() {
		return false;
	}

	@Override
	public boolean castShadows() {
		// TODO Auto-generated method stub
		return false;
	}

}