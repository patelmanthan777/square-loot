package environment.blocks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class SolidBlock implements Block {
	private Vector3f color = new Vector3f(0,0,0);

	protected SolidBlock(){
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
		/*
		GL11.glColor3f(color.x,color.y,color.z);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
	    GL11.glVertex2f(posX-halfBlockSize.x,posY-halfBlockSize.y);
	    GL11.glVertex2f(posX-halfBlockSize.x,posY+halfBlockSize.y);
	    GL11.glVertex2f(posX+halfBlockSize.x,posY+halfBlockSize.y);
	    GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
	    GL11.glEnd();
		*/
	}

	@Override
	public boolean testCollision() {
		return true;
	}

}
