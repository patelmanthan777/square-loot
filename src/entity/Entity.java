package entity;

import rendering.Drawable;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Entity implements Drawable{
	protected Vector2f position;
	protected Vector2f orientation;
	private Vector2f speed = new Vector2f(0,0);
	private Vector2f translation = new Vector2f(0,0);
	private float minSpeed = 0.01f;
	private float maxSpeed = 10;
	private float descFactor = 5;
	//private float accFactor = 100;
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
	
	public Vector2f getSpeed(){
		return speed;
	}
	
	public void setSpeed(Vector2f speed){
		this.speed = speed;
	}
	
	public void translate(float translationx, float translationy){
		this.translation.x += translationx;
		this.translation.y += translationy;
	}
	
	public void updatePostion(float dt){
			if (translation.length() != 0)
				translation = (Vector2f)translation.normalise();
			speed.x = speed.x + translation.x - speed.x / descFactor;
			speed.y = speed.y + translation.y - speed.y / descFactor;
			System.out.println(speed);
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
			
			/*float x_tmp = position.x + speed.x * dt;
			float y_tmp = position.y + speed.y * dt;
			
			if player.noCollision(x_tmp, y_tmp, map) then
				player._x = x_tmp
				player._y = y_tmp
			else
				if player.noCollision(x_tmp, player._y, map) == false then
					player._speedx = 0
				end
				if player.noCollision(player._x, y_tmp, map) == false then
					player._speedy = 0
				end
				if player.noCollision(player._x, y_tmp, map) and player.noCollision(x_tmp, player._y, map) then
					player._speedx = 0
					player._speedy = 0
				end*/
				position.x = position.x + speed.x * dt;
				position.y = position.y + speed.y * dt;
			//end
			
			translation.x = 0;
			translation.y = 0;

	}
}
