package environment;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import rendering.Drawable;
import environment.room.Room;

public class Door implements Drawable {
	private boolean opened = false;

	private Room r1;
	private Room r2;
	private Orientation orientation;
	private int thickness = (int) Map.blockPixelSize.x;
	private int width = (int) (Map.blockPixelSize.x * 2);
	private int posx;
	private int posy;
	private static Vector4f color = new Vector4f(0.1f,0.1f,0.1f,1f);

	public Door(Room r1, Room r2, Orientation orientation) {
		this.r1 = r1;
		this.r2 = r2;
		this.orientation = orientation;
		if (orientation == Orientation.HORIZONTAL) {
			posx = r1.getX();
			posx += Map.roomPixelSize.x / 2 - width / 2;
			posy = Math.max(r1.getY(), r2.getY());
			posy -= thickness / 2;
		} else {
			posy = r1.getY();
			posy += Map.roomPixelSize.y / 2 - width / 2;
			posx = Math.max(r1.getX(), r2.getX());
			posx -= thickness / 2;
		}

	}

	public boolean isOpened(){
		return opened;
	}
	
	public void open(){
		opened = true;
	}
	
	public void close(){
		opened = false;
	}
	
	public void toggle(){
		if(opened)
			close();
		else
			open();
	}
	
	@Override
	public void draw() {
		glColor4f(color.x,color.y,color.z,color.w);
		if (!opened) {
			if (orientation == Orientation.HORIZONTAL) {
				glBegin(GL_QUADS);
				glVertex2f(posx, posy + thickness);
				glVertex2f(posx + width, posy + thickness);
				glVertex2f(posx + width, posy);
				glVertex2f(posx, posy);
				glEnd();
			} else {
				glBegin(GL_QUADS);
				glVertex2f(posx, posy + width);
				glVertex2f(posx + thickness, posy + width);
				glVertex2f(posx + thickness, posy);
				glVertex2f(posx, posy);
				glEnd();
			}
		}
	}
}
