package light;

import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.util.vector.Vector2f;

import rendering.Drawable;

public class Shadow implements Drawable{
	private static int size = 4;
	public Vector2f [] points = new Vector2f[size];
	public Vector2f color = new Vector2f();
	public Shadow(Shadow shadow){
		for(int i = 0; i < size; i++){
			this.points[i] = new Vector2f(shadow.points[i]);
		}
	}
	
	public Shadow(){
		for(int i = 0; i < size; i++){
			this.points[i] = new Vector2f();
		}
	}

	@Override
	public void draw() {
		glVertex2f(points[0].x, points[0].y);
		glVertex2f(points[1].x, points[1].y);
		glVertex2f(points[3].x, points[3].y);
		glVertex2f(points[2].x, points[2].y);
	}
}
