package entity.npc;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import rendering.MiniMapDrawable;
import userInterface.MiniMap;
import entity.player.Player;
import environment.Map;

public class Zombie extends Npc implements MiniMapDrawable {

	private static int scentDistanceBlk = 5;
	//private static int scentDistancePx = (int) (scentDistanceBlk * Map.blockPixelSize.x);
	private ZombieState zombieState;
	private float orientationSpeed = 0;
	private float orientationDesc = 0.00001f;
	private boolean running = false;
	
	
	private SpriteSheet headSprites;
	
	
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

	private void init() {
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10);
		this.accFactor = 0.001f;
		this.descFactor = 0.2f;
		this.halfSize.x = 40;
		this.halfSize.y = 40;
		zombieState = ZombieState.IDLE;
		try {
			headSprites = new SpriteSheet("assets/textures/zombie.png",256,256);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void updatePosition(long delta, Map m){
		super.updatePosition(delta, m);
		/* animation update stuff */
		
	}
	
	
	@Override
	public void draw() {
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		
		/* BODY */
		Image tile = headSprites.getSprite(0,0);
		tile.setCenterOfRotation(halfSize.x, halfSize.y);
		tile.setRotation(-(this.getDegreAngle()+90));	
		tile.draw(this.getX()*Map.blockPixelSize.x-halfSize.x, this.getY()*Map.blockPixelSize.y-halfSize.y,halfSize.x*2,halfSize.y*2);
		glDisable(GL_BLEND);
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
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy + persoRatio
				* MiniMap.roomSize.y);
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
		float dst = scentDistanceBlk;
		float length;
		zombieState = ZombieState.IDLE;
		for (Player p : players) {
			Vector2f.sub(p.getPosition(), this.getPosition(), thisToPlayer);
			length = thisToPlayer.length();
			thisToPlayer.normalise(thisToPlayer);
			if (length < dst || dst == -1) {
				// chase the nearest player
				zombieState = ZombieState.CHASING;
				dst = length;
				this.setDirection(thisToPlayer);
				this.translate(thisToPlayer.x, thisToPlayer.y);
				updatePoints();
			}

		}
		if (zombieState == ZombieState.IDLE) {
			// randomly moves

			for (int i = 0; i < deltaT; i++) {
				running = (Math.random() < 0.001) ? !running : running;

				if (running)
					this.translate(this.getDirection().x, this.getDirection().y);

				float deltaOrientation = (float) ((Math.random() - 0.5f) * 0.005);
				orientationSpeed += deltaOrientation;
				orientationSpeed = orientationSpeed > 0 ? (float) Math.max(0,
						orientationSpeed - orientationDesc) : (float) Math.min(
						0, orientationSpeed + orientationDesc);
				orientationSpeed = (float) Math.min(3, orientationSpeed);
				orientationSpeed = (float) Math.max(-3, orientationSpeed);
				this.rotateDegree(orientationSpeed);
			}
		}
	}
}
