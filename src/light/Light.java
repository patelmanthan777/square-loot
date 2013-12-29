package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entity.Node;


public class Light extends Node{
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
	
	public Light(Vector2f p, Vector3f color, float radius, float dstMax,boolean dynamic){
		super(p);
		this.color = color;
		this.radius = radius;
		this.maxDst = dstMax;
		this.dynamic = dynamic;
	}
	
	public void setRadius(float rad){
		radius = rad;
	}
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
	public Vector3f getColor(){
		return color;
	}
	
	public float getMaxDst(){
		return maxDst;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void activate(){
		active = true;
		if(dynamic){
			LightManager.activateLight(name, dynamic);
		}
	}
	
	public void deactivate(){
		active = false;
		LightManager.deactivateLight(name, dynamic);
	}
	
	public boolean isActive(){
		return active;
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
	
	
}
