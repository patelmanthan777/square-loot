package rendering;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Camera {
	static public void setPosition(Vector2f pos){		
		GL11.glTranslatef(-pos.x+Display.getWidth()/2, -pos.y+Display.getHeight()/2,0);
	}
	
}
