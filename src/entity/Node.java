package entity;
import org.lwjgl.util.vector.Vector2f;


public class Node {
	protected Vector2f position;
	private Vector2f direction;
	private Vector2f tangent;
	protected String name = null;
	private float angle;
	
	
	public Node(Vector2f pos){
		this(pos.x, pos.y);
	}
	
	public Node(Vector2f pos, Vector2f rot){
		this(pos.x,pos.y,rot.x,rot.y);
	}
	
	public Node(float posx, float posy){
		this(posx,posy,1,0);
	}
	
	
	public Node(float posx, float posy, float dirx, float diry){
		position = new Vector2f(posx, posy);
		direction = new Vector2f(dirx,diry);
		direction.normalise(direction);
		tangent = new Vector2f(direction.y,-direction.x);
		angle = computeAngle(direction);
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
		setDirectionNoAngle(orix, oriy);
		angle = computeAngle(direction);
	}
	
	private void setDirectionNoAngle(float orix, float oriy){
		direction.x = orix;
		direction.y = oriy;
		direction.normalise(direction);
		tangent.x = direction.y;
		tangent.y = -direction.x;
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public Vector2f getDirection(){
		return direction;
	}
	
	public float getAngle(){
		return angle;
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
	
	public Vector2f getTangent(){
		return tangent;
	}
	
	public static float computeAngle(Vector2f dir){
		float angle = 0;
		angle = (float) Math.acos(dir.x);
		angle = dir.y < 0 ? angle: -angle;
		return angle;
	}
	
	public void rotateRadian(float deltaAngle){
		setRadianAngle(angle + deltaAngle);
	}
	
	public void rotateDegree(float deltaAngle){
		float delta = (float) Math.toRadians(deltaAngle);
		rotateRadian(delta);
	}
	
	public void setRadianAngle(float angle){
		this.angle = angle;
		updateDirection();
	}
	
	private void updateDirection(){
		setDirectionNoAngle((float) Math.cos(angle),(float) Math.sin(angle));
	}
	
	public float getRadianAngle(){
		return angle;
	}
	
	public float getDegreAngle(){
		return (float) Math.toDegrees(angle);
	}
}
