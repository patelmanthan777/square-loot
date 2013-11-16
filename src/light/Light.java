package light;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entity.Node;


public class Light extends Node{
	private Vector3f color; // couleur de la lumiere
	private boolean active = true;
	private float radius; // distance max d'eclairage
	
	public Light(Vector2f p, Vector3f color, float radius){
		super(p);
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
	}
	
	public void desactivate(){
		active = false;
	}
	
	public boolean active(){
		return active;
	}
}
