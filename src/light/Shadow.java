package light;

import org.lwjgl.util.vector.Vector2f;

public class Shadow {
	private static int size = 4;
	public Vector2f [] points = new Vector2f[size];
	
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
}
