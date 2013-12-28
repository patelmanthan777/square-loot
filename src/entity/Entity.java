package entity;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Drawable;
import environment.Map;

public abstract class Entity extends Node implements Drawable{
	protected Vector2f speed = new Vector2f(0,0);
	/**
	 * Represents the direction in which the entity moves
	 */
	private Vector2f translation = new Vector2f(0,0);
	protected float minSpeed = 0.01f;
	protected float maxSpeed = 2f;
	protected float descFactor = 5;
	protected float accFactor = 0.2f;
	protected Vector3f color;
	
	public Entity(Vector2f pos) {
		super(pos);
	}
	
	public Entity(Vector2f pos, Vector2f rot) {
		super(pos, rot);
	}

	public Vector3f getColor(){
		return color;
	}
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
	public Vector2f getSpeed(){
		return speed;
	}
	
	public void setSpeed(Vector2f speed){
		this.speed = speed;
	}
	
	/**
	 * Updates the translation attribute by adding the parameters to the
	 * respective coordinates. 
	 * 
	 * @param translationx represents horizontal motion
	 * @param translationy represents vertical motion
	 */
	public void translate(float translationx, float translationy){
		this.translation.x += translationx;
		this.translation.y += translationy;
	}
	
	/**
	 * Computes whether the point defined by the first and second argument
	 * is within the boundaries of the map.
	 * 
	 * @param x is horizontal coordinate
	 * @param y is vertical coordinate
	 * @param m is the world representation
	 * @return true if the coordinate are outside the map boundaries,
	 * false otherwise.
	 */
	public abstract boolean isInCollision(float x, float y, Map m);
	
	/**
	 * Update the entity position according to its attributes. 
	 * @param dt represents the time passed since last update 
	 * @param m is the world representation needed in case of collision
	 */
	public void updatePostion(long dt, Map m){
			if (translation.length() != 0)
				translation = (Vector2f)translation.normalise().scale(accFactor);
			speed.x = speed.x + translation.x - speed.x / descFactor;
			speed.y = speed.y + translation.y - speed.y / descFactor;
			float normSpeed = speed.length();
			
			if (normSpeed != 0){
				if (normSpeed < minSpeed){
					speed.x = 0;
					speed.y = 0;
				}else if (normSpeed > maxSpeed) {
					speed.x = maxSpeed * speed.x / normSpeed;
					speed.y = maxSpeed * speed.y / normSpeed;
				}
			}
			
			float x_tmp = position.x + speed.x * dt;
			float y_tmp = position.y + speed.y * dt;
			
			if(!isInCollision(x_tmp, y_tmp, m)){
				setPosition(x_tmp, y_tmp);
			}
			else
			{
				if(isInCollision(x_tmp, position.y, m))
					speed.x = 0;
				if(isInCollision(position.x, y_tmp, m))
					speed.y = 0;
				if(!isInCollision(position.x, y_tmp, m) && !isInCollision(x_tmp, position.y, m)){
					speed.x = 0;
					speed.y = 0;
				}
			
				setPosition(position.x + speed.x * dt, position.y + speed.y * dt);
			}
			
			translation.x = 0;
			translation.y = 0;
	}
}
