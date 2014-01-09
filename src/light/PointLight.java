package light;

import light.LightManager;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import light.Light;


public class PointLight extends Light {
	protected Vector3f color;
	protected boolean active = true;
	/**
	 * Light power.
	 */
	protected float radius;
	protected float maxDst;
	/**
	 * <b>true</b> if the light may move
	 */
	protected boolean dynamic;
	
	public PointLight(Vector2f p, Vector3f color, float radius, float dstMax,boolean dynamic){
		super(p, color, dynamic);
		this.radius = radius;
		this.maxDst = dstMax;
		
	}
	
	public void setRadius(float rad){
		radius = rad;
	}
	
	public float getMaxDst(){
		return maxDst;
	}
	
	public float getRadius(){
		return radius;
	}
		
	@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
		LightManager.updateLightShadows(this,dynamic);
	}
	
	@Override
	public void setPosition(float posx, float posy){
		this.position.x = posx;
		this.position.y = posy;
		LightManager.updateLightShadows(this,dynamic);
	}
	
	@Override
	public String toString(){
		return name;
	}
}