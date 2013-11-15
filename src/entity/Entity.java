package entity;

import rendering.Drawable;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Entity implements Drawable{
	protected Vector2f position;
	protected Vector2f orientation;
	protected Vector3f color;
	
	public Vector2f getPosition(){
		return position;
	}
	public Vector2f getOrientation(){
		return orientation;
	}
	
	public void setPosition(Vector2f position){
		this.position = position;
	}
	
	public void setOrientation(Vector2f orientation){
		this.orientation = orientation;
	}
	
	public Vector3f getColor(){
		return color;
	}
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
}
