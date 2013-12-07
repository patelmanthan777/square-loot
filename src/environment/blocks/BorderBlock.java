package environment.blocks;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import environment.Map;

public class BorderBlock implements Block{
	private Vector3f color = new Vector3f(0,0,0);
	
	@Override
	public void drawAt(float posX, float posY) {
		
		GL11.glColor3f(color.x,color.y,color.z);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(posX+Map.blockPixelSize.x,posY);
		GL11.glVertex2f(posX,posY);
		GL11.glVertex2f(posX+Map.blockPixelSize.x,posY+Map.blockPixelSize.y);
		GL11.glVertex2f(posX,posY+Map.blockPixelSize.y);
		GL11.glEnd();

	}

	@Override
	public boolean testCollision() {
		return true;
	}

	@Override
	public boolean castShadows() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
