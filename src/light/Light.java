package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entity.Node;


public class Light extends Node{
	protected Vector3f color; // couleur de la lumiere
	protected boolean active = true;
	protected float radius; // distance max d'eclairage
	protected float maxDst;
	public Light(Vector2f p, Vector3f color, float radius, float dstMax){
		super(p);
		this.color = color;
		this.radius = radius;
		this.maxDst = dstMax;
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
		LightManager.activateLight(name);
	}
	
	public void desactivate(){
		active = false;
		LightManager.desactivateLight(name);
	}
	
	public boolean isActive(){
		return active;
	}
	
	@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
		LightManager.updateLightShadows(this);
	}
	
	@Override
	public void setPosition(float posx, float posy){
		this.position.x = posx;
		this.position.y = posy;
		LightManager.updateLightShadows(this);
	}
	
	
}
