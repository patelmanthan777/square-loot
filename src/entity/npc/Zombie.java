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

	private static int scentDistanceBlk = 10;
	private static int scentDistancePx = (int) (scentDistanceBlk * Map.blockPixelSize.x);
	private ZombieState state;
	private float orientationSpeed = 0;
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
		
		Vector3f col = new Vector3f(0.5f, 0.7f, 0);
		setColor(col);
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10); 
		this.accFactor = 0.010f;
		this.descFactor = 30f;
		state = ZombieState.IDLE;
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
		glColor3f(1,1,1);
		glTexCoord2f(1,1);
		glVertex2f(points[0].x, points[0].y);
		glTexCoord2f(1,0);
		glVertex2f(points[3].x, points[3].y);
		glTexCoord2f(0,0);
		glVertex2f(points[2].x, points[2].y);
		glTexCoord2f(0,1);
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
	public void setDirection(float orix, float oriy) {
		super.setDirection(orix, oriy);
		updatePoints();
	}

	@Override
	public void setPosition(float posx, float posy) {
		super.setPosition(posx, posy);
		updatePoints();
	}


	@Override
	public void thinkAndAct(LinkedList<Player> players, long deltaT) {
		float dst = scentDistancePx;
		float length;
		for(Player p : players){
			Vector2f.sub(p.getPosition(),this.getPosition(),thisToPlayer);
			length = thisToPlayer.length();
			thisToPlayer.normalise(thisToPlayer);
			if(length < dst || dst == -1){
				// chase the nearest player
				state = ZombieState.CHASING;
				dst = length;
				this.setDirection(thisToPlayer);
				this.translate(thisToPlayer.x, thisToPlayer.y);
				updatePoints();
			}
			
		}
		if(state == ZombieState.IDLE){
			// randomly moves
			
		}
	}


}
