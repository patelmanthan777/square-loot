package entity.npc;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import configuration.ConfigManager;
import physics.GameBodyType;
import physics.PhysicsDataStructure;
import rendering.MiniMapDrawable;
import userInterface.MiniMap;
import entity.player.Player;
import environment.Map;
import event.Timer;

public class Zombie extends Npc implements MiniMapDrawable {

	private static int scentDistanceBlk = 8;
	private ZombieState zombieState;
	private float orientationSpeed = 0;
	private float orientationDesc = 0.00001f;
	private boolean running = false;
	
	private int attackValue = 5;
	private Player contactPlayer;
	private long attackTimer = -1;
	private long attackTimerMax = 100;
	
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
		this.setMaxHealth(20);
		this.setHealth(20);
		this.accFactor = 15f;
		//this.descFactor = 0.2f;
		this.halfSize.x = (float)40/(float)ConfigManager.unitPixelSize;
		this.halfSize.y = (float)40/(float)ConfigManager.unitPixelSize;	
		gbtype = GameBodyType.ZOMBIE;
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
		
		if(contactPlayer != null && attackTimer !=-1)
			contactPlayer.damage(attack());
		else if(contactPlayer != null){
			contactPlayer.damage(attackValue);
			attackTimer = attackTimerMax*Timer.unitInOneSecond + Timer.getTime();
		}
		else
			attackTimer = -1;
	}
	
	
	public int attack(){
		if(attackTimer < Timer.getTime()){
			attackTimer = attackTimerMax*Timer.unitInOneSecond + Timer.getTime();
			return attackValue;			
		}
		return 0;
	}
	
	@Override
	public void draw() {
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		
		/* BODY */
		Image tile = headSprites.getSprite(0,0);
		tile.setCenterOfRotation(halfSize.x*ConfigManager.unitPixelSize, halfSize.y*ConfigManager.unitPixelSize);
		tile.setRotation(-(this.getDegreAngle()+90));	
		tile.draw(this.getX()*Map.blockPixelSize.x-halfSize.x*ConfigManager.unitPixelSize, this.getY()*Map.blockPixelSize.y-halfSize.y*ConfigManager.unitPixelSize,halfSize.x*2*ConfigManager.unitPixelSize,halfSize.y*2*ConfigManager.unitPixelSize);
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
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		super.ContactHandler(a);
		switch(a.getType())
		{		
		case PLAYER:
			contactPlayer = (Player) a.getPhysicsObject();
			break;
		default:			
		}
	}
	
	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case PLAYER:
			if(contactPlayer == a.getPhysicsObject())
				contactPlayer = null;
			break;
		default:	
		}
	}
}
