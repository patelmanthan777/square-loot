package entity;
import org.lwjgl.util.vector.Vector2f;


public class Node {
	protected Vector2f position;
	protected Vector2f direction;
	protected Vector2f tangent;
	protected String name = null;

	public Node(Vector2f pos){
		this(pos.x, pos.y);
	}
	
	public Node(Vector2f pos, Vector2f rot){
		this(pos.x,pos.y,rot.x,rot.y);
	}
	
	public Node(float posx, float posy){
		position = new Vector2f(posx, posy);
		direction = new Vector2f();
		tangent = new Vector2f();
	}
	
	
	public Node(float posx, float posy, float dirx, float diry){
		position = new Vector2f(posx, posy);
		direction = new Vector2f(dirx,diry);
		tangent = new Vector2f(diry,-dirx);
	}
	
	public void setPosition(Vector2f pos){
		setPosition(pos.x,pos.y);
	}
	
	public void setPosition(float posx, float posy){
		position.x = posx;
		position.y = posy;
	}
	
	public void setDirection(Vector2f ori){
		setDirection(ori.x,ori.y);
	}
	
	public void setDirection(float orix, float oriy){
		direction.x = orix;
		direction.y = oriy;
		tangent.x = oriy;
		tangent.y = -orix;
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
