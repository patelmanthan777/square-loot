package entity;
import org.lwjgl.util.vector.Vector2f;


public class Node {
	protected Vector2f position;
	Vector2f rotation;
	
	public Node(Vector2f pos, Vector2f rot){
		position = pos;
		rotation = rot;
	}
	
	public void setPosition(Vector2f pos){
		position.x = pos.x;
		position.y = pos.y;
	}
	
	public void setPosition(float posx, float posy){
		position.x = posx;
		position.y = posy;
	}
	
	public void setOrientation(Vector2f ori){
		rotation.x = ori.x;
		rotation.y = ori.y;
	}
	
	public void setOrientation(float orix, float oriy){
		rotation.x = orix;
		rotation.y = oriy;
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
