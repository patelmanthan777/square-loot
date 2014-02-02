package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entity.Entity;


public class Light extends Entity{
	protected Vector3f color;
	protected boolean active = true;
	
	/*
	 * <b>true</b> if the light may move
	 */
	protected boolean dynamic;
	
	public Light(Vector2f p, Vector3f color, boolean dynamic){
		super(p);
		this.color = color;
		this.dynamic = dynamic;
	}
	
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
	public Vector3f getColor(){
		return color;
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
		super.setPosition(position);
		LightManager.updateLightShadows(this);
	}
	
	@Override
	public void setPosition(float posx, float posy){
		super.setPosition(posx,posy);
		LightManager.updateLightShadows(this);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
}
