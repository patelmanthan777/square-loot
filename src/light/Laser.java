package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Laser extends Light {
	private Vector2f dir = new Vector2f();
	
	public Laser(Vector2f p, Vector3f color){
		super(p, color,-1,-1);
		dir.x = 1;
		dir.y = 1;
	}
	
	public Laser(Vector2f p, Vector3f color, Vector2f dir){
		super(p, color,-1,-1);
		this.dir.x = dir.x;
		this.dir.y = dir.y;
	}
	
	public Vector2f getDirection(){
		return dir;
	}
	
	/*@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
		lm.updateLaserShadows(this);
	}*/
	
	@Override
	public void setOrientation(Vector2f ori){
		this.position.x = position.x;
		this.position.y = position.y;
	}
	
	@Override
	public void setOrientation(float posx, float posy){
		this.dir.x = posx;
		this.dir.y = posy;
		LightManager.updateLightShadows(this);
	}
	
}
