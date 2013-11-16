package entity;
import org.lwjgl.util.vector.Vector2f;


public class Node {
	Vector2f position;
	Vector2f rotation;
	
	public Node(Vector2f pos, Vector2f rot){
		position = pos;
		rotation = rot;
	}
	
	public void setPosition(Vector2f pos){
		position = pos;
	}
	
	public void setOrientation(Vector2f ori){
		rotation = ori;
	}
	
	public Node(Vector2f pos){
		position = new Vector2f(pos);
		rotation = new Vector2f(0f,0f);
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public Vector2f getRotation(){
		return position;
	}
	
	public float getX(){
		return position.x;
	}
	public float getY(){
		return position.y;
	}
	
	public float getRotationX(){
		return rotation.x;
	}
	public float getRotationY(){
		return rotation.y;
	}
	
	public void setX(float x){
		position.x = x;
	}
	public void setY(float y){
		position.y = y;
	}
	
}
