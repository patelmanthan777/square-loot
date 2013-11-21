package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Laser extends Light {
	private Vector2f dir = new Vector2f();
	
	public Laser(LightManager lm, Vector2f p, Vector3f color){
		super(lm, p, color,-1);
		dir.x = 1;
		dir.y = 1;
	}
	
	public Laser(LightManager lm, Vector2f p, Vector3f color, Vector2f dir){
		super(lm, p, color,-1);
		this.dir.x = dir.x;
		this.dir.y = dir.y;
	}
	
	public void setOrientation(Vector2f dir){
		this.dir.x = dir.x;
		this.dir.y = dir.y;
	}
	
	public Vector2f getDirection(){
		return dir;
	}
	
	@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
		lm.updateLaserShadows(this);
	}
	
}
