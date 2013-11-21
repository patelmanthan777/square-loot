package light;

import org.lwjgl.util.vector.Vector2f;

public class Shadow {
	Vector2f [] points = new Vector2f[4];
	public Shadow(Vector2f points[]){
		this.points = points;
	}
	
	public Shadow(Vector2f point1, Vector2f point2, Vector2f point3, Vector2f point4){
		this.points[0] = point1;
		this.points[1] = point2;
		this.points[2] = point3;
		this.points[3] = point4;
	}
}
