package entity.npc;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;

import rendering.MiniMapDrawable;
import rendering.TextureManager;
import userInterface.MiniMap;
import utils.GraphicsAL;
import entity.player.Player;
import environment.Map;

public class Zombie extends Npc implements MiniMapDrawable {

	private static int scentDistanceBlk = 5;
	//private static int scentDistancePx = (int) (scentDistanceBlk * Map.blockPixelSize.x);
	private ZombieState state;
	private float orientationSpeed = 0;
	private float orientationDesc = 0.00001f;
	private boolean running = false;
	/*** avoid dynamic allocation in thinkAndAct ***/
	private Vector2f thisToPlayer = new Vector2f();

	/**********************************************/

	public Zombie(Vector2f pos, int inventorySize) {
		super(pos, inventorySize);
		init();
	}

	public Zombie(Vector2f pos, Vector2f rot, int inventorySize) {
		super(pos, rot, inventorySize);
		init();
	}

	public Zombie(float posx, float posy, float dirx, float diry, int inventorySize) {
		super(posx, posy, dirx, diry, inventorySize);
		init();
	}

	public Zombie(float posx, float posy, int inventorySize) {
		super(posx, posy, inventorySize);
		init();
	}

	private void init() {
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10);
		this.accFactor = 0.001f;
		this.descFactor = 0.2f;
		state = ZombieState.IDLE;
	}

	@Override
	public void draw() {
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		GraphicsAL.drawQuadTexture(points,
				   				   GraphicsAL.fullTexPoints,
				   				   TextureManager.zombieTexture().getTextureID());			
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
		state = ZombieState.IDLE;
		for (Player p : players) {
			Vector2f.sub(p.getPosition(), this.getPosition(), thisToPlayer);
			length = thisToPlayer.length();
			thisToPlayer.normalise(thisToPlayer);
			if (length < dst || dst == -1) {
				// chase the nearest player
				state = ZombieState.CHASING;
				dst = length;
				this.setDirection(thisToPlayer);
				this.translate(thisToPlayer.x, thisToPlayer.y);
				updatePoints();
			}

		}
		if (state == ZombieState.IDLE) {
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
