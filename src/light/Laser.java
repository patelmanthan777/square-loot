package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import light.Light;

public class Laser extends Light{	
	private Vector2f intersection = new Vector2f();
	
	public Laser(Vector2f p, Vector3f color){
		super(p, color, true);
	}
	
	public Laser(Vector2f p, Vector3f color, Vector2f dir){
		super(p, color, true);	
	}
	
	public Vector2f getIntersection(){
		return intersection;
	}
	
	public void setIntersection(Vector2f i){
		intersection = i;		
	}
	
	@Override
	public void setDirection(float orix, float oriy) {
		super.setDirection(orix,oriy);
		LightManager.updateLaserIntersect(this);
	}
	
	@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
		LightManager.updateLaserIntersect(this);
	}
}
