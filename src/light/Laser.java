package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import light.Light;

public class Laser extends Light{	
	
	public Laser(Vector2f p, Vector3f color){
		super(p, color, true);
	}
	
	public Laser(Vector2f p, Vector3f color, Vector2f dir){
		super(p, color, true);	
	}
	
	@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
	}
}
