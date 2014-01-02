package entity;
import org.lwjgl.util.vector.Vector2f;


public class Node {
	protected Vector2f position;
	protected Vector2f direction;
	protected Vector2f tangent;
	protected String name = null;

	
	public Node(Vector2f pos, Vector2f rot){
		position = pos;
		direction = rot;
		tangent = new Vector2f();
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
		setOrientation(ori.x,ori.y);
	}
	
	public void setOrientation(float orix, float oriy){
		direction.x = orix;
		direction.y = oriy;
		tangent.x = oriy;
		tangent.y = -orix;
	}
	
	public Node(Vector2f pos){
		position = new Vector2f(pos);
		direction = new Vector2f();
		tangent = new Vector2f();
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public Vector2f getRotation(){
		return direction;
	}
	
	public float getX(){
		return position.x;
	}
	public float getY(){
		return position.y;
	}
	
	public float getRotationX(){
		return direction.x;
	}
	public float getRotationY(){
		return direction.y;
	}
	
	public void setX(float x){
		position.x = x;
	}
	public void setY(float y){
		position.y = y;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
