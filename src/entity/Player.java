package entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import environment.Map;



public class Player extends Entity{
	
	private Vector2f halfSize = new Vector2f(10,10);
	
	
	
	//private Vector2f[] points = new Vector2f[4]; // a initialiser avec l'orientation et la taille
												 // --> override setOrientation et setPosition pour le maj
	
	public Player(Vector2f pos){
		super(pos);
		Vector3f col = new Vector3f(0,0,0); 
		setColor(col);
		setPosition(pos);
		Vector2f ori = new Vector2f(0,0);
		setOrientation(ori);
	}
	
	@Override
	public boolean isInCollision(float x, float y, Map m){
		if(m.testCollision(x-halfSize.x, y-halfSize.y) || m.testCollision(x+halfSize.x, y-halfSize.y) || m.testCollision(x-halfSize.x, y+halfSize.y) || m.testCollision(x+halfSize.x, y+halfSize.y)){
			return false;
		}
		return true;
	}
	
	@Override
	public void draw() {
		// Clear the screen and depth buffer	
				// set the color of the quad (R,G,B,A)

				//GL11.glColor3f(color.x,color.y,color.z);
				GL11.glColor3f(0,0,0);
				// draw quad
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					GL11.glVertex2f(position.x+halfSize.x,position.y-halfSize.y);
				    GL11.glVertex2f(position.x-halfSize.x,position.y-halfSize.y);
				    GL11.glVertex2f(position.x+halfSize.x,position.y+halfSize.y);
				    GL11.glVertex2f(position.x-halfSize.x,position.y+halfSize.y);
				GL11.glEnd();
	}
	
}
