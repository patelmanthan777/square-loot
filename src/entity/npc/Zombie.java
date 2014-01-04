package entity.npc;

import java.util.LinkedList;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.MiniMapDrawable;
import userInterface.MiniMap;
import entity.player.Player;
import environment.Map;

public class Zombie extends Npc implements MiniMapDrawable{

	
	private static final int nbPoints = 4;
	private Vector2f[] points = new Vector2f[nbPoints];
	private Vector2f halfSize = new Vector2f(15, 15);
	private static int scentDistanceBlk = 10;
	private static int scentDistancePx = (int) (scentDistanceBlk * Map.blockPixelSize.x);
	
	/*** avoid dynamic allocation in thinkAndAct ***/
	private Vector2f thisToPlayer = new Vector2f();
	/**********************************************/
	
	public Zombie(Vector2f pos) {
		super(pos);
		init();
	}

	public Zombie(Vector2f pos, Vector2f rot) {
		super(pos, rot);
		init();
	}

	public Zombie(float posx, float posy, float dirx, float diry) {
		super(posx, posy, dirx, diry);
		init();
	}

	public Zombie(float posx, float posy) {
		super(posx, posy);
		init();
	}
	
	
	private void init(){
		for(int i = 0 ; i < nbPoints; i++){
			points[i] = new Vector2f();
		}
		Vector3f col = new Vector3f(0.5f, 0.7f, 0);
		setColor(col);
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10); 
		this.accFactor = 0.010f;
		this.descFactor = 30f;
		//		protected float descFactor = 50;
		//protected float accFactor = 0.025f;
	}
	
	

	
	
	@Override
	public boolean isInCollision(float x, float y, Map m) {
		if (m.testCollision(x - halfSize.x, y - halfSize.y)
				|| m.testCollision(x + halfSize.x, y - halfSize.y)
				|| m.testCollision(x - halfSize.x, y + halfSize.y)
				|| m.testCollision(x + halfSize.x, y + halfSize.y)) {
			return true;
		}
		return false;
	}


	@Override
	public void draw() {
		
		glColor3f(color.x,color.y,color.z);
		glVertex2f(points[0].x, points[0].y);
		glVertex2f(points[3].x, points[3].y);
		glVertex2f(points[2].x, points[2].y);
		glVertex2f(points[1].x, points[1].y);
		
	}

	@Override
	public void drawOnMiniMap() {
		float persoRatio = 0.5f;
		int posx = (int) (MiniMap.position.x + (getX() / Map.roomPixelSize.x)
				* MiniMap.roomSize.x - persoRatio * MiniMap.roomSize.x / 2);
		int posy = (int) (MiniMap.position.y + (getY() / Map.roomPixelSize.y)
				* MiniMap.roomSize.y - persoRatio * MiniMap.roomSize.y / 2);
		glColor3f(1, 0, 0);
		// draw quad
		glLoadIdentity();
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy);
		glVertex2f(posx, posy);
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy
				+ persoRatio * MiniMap.roomSize.y);
		glVertex2f(posx, posy + persoRatio * MiniMap.roomSize.y);
		glEnd();
	}


	@Override
	public void setOrientation(float orix, float oriy) {
		super.setOrientation(orix, oriy);
		updatePoints();
	}

	@Override
	public void setPosition(float posx, float posy) {
		super.setPosition(posx, posy);
		updatePoints();
	}

	
	/**
	 * Compute the coordinates of the 4 points
	 * using the position the size and the orientation of the player
	 * 
	 * 0      1
	 * +------+
	 * |      |
	 * |      |
	 * +------+
	 * 3	  2
	 */
	
	private void updatePoints(){
		this.direction.normalise(direction);
		this.direction.scale(halfSize.y);
		this.tangent.normalise(tangent);
		this.tangent.scale(halfSize.x);
		points[0].x = this.position.x - this.tangent.x - this.direction.x;
		points[0].y = this.position.y - this.tangent.y - this.direction.y;
		points[1].x = this.position.x + this.tangent.x - this.direction.x;
		points[1].y = this.position.y + this.tangent.y - this.direction.y;
		points[3].x = this.position.x - this.tangent.x + this.direction.x;
		points[3].y = this.position.y - this.tangent.y + this.direction.y;
		points[2].x = this.position.x + this.tangent.x + this.direction.x;
		points[2].y = this.position.y + this.tangent.y + this.direction.y;
	}

	@Override
	public void thinkAndAct(LinkedList<Player> players) {
		float dst = scentDistancePx;
		float length;
		for(Player p : players){
			Vector2f.sub(p.getPosition(),this.getPosition(),thisToPlayer);
			length = thisToPlayer.length();
			thisToPlayer.normalise(thisToPlayer);
			System.out.println(length);
			if(length < dst || dst == -1){
				dst = length;
				this.setOrientation(thisToPlayer);
				this.translate(thisToPlayer.x, thisToPlayer.y);
				updatePoints();
				
			}
		}
	}


}
