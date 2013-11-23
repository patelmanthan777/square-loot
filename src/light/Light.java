package light;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entity.Node;


public class Light extends Node{
	protected Vector3f color; // couleur de la lumiere
	protected boolean active = true;
	protected float radius; // distance max d'eclairage
	protected LightManager lm;
	public Light(LightManager lm , Vector2f p, Vector3f color, float radius){
		super(p);
		this.lm = lm;
		this.color = color;
		this.radius = radius;
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
	
	public float getRadius(){
		return radius;
	}
	
	public void activate(){
		active = true;
		lm.activateLight(name);
	}
	
	public void desactivate(){
		active = false;
		lm.desactivateLight(name);
	}
	
	public boolean isActive(){
		return active;
	}
	
	@Override
	public void setPosition(Vector2f position){
		this.position.x = position.x;
		this.position.y = position.y;
		lm.updateLightShadows(this);
	}
	
	@Override
	public void setPosition(float posx, float posy){
		this.position.x = posx;
		this.position.y = posy;
		lm.updateLightShadows(this);
	}
}
