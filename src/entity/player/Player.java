package entity.player;

import item.weapon.LaserRifle;
import item.weapon.Weapon;
import light.Laser;
import light.Light;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.MiniMapDrawable;
import rendering.TextureManager;
import userInterface.MiniMap;
import entity.LivingEntity;
import environment.Map;

public class Player extends LivingEntity implements MiniMapDrawable {
	private static final int nbPoints = 4;
	private Vector2f[] points = new Vector2f[nbPoints];
	private Vector2f halfSize = new Vector2f(20, 20);
	private Laser laser;
	private Light light;
	
	private Weapon weapon = new LaserRifle(250);
	
	public Player(Vector2f pos) {
		super(pos);
		Vector3f col = new Vector3f(0, 0, 0);
		for(int i = 0 ; i < nbPoints; i++){
			points[i] = new Vector2f();
		}
		setColor(col);
		position.x = pos.x;
		position.y = pos.y;
		this.direction.x = 1;
		this.direction.y = 1;
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10);
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

	public void setLight(Light l) {
		light = l;
		l.setPosition(position);
	}

	public void setLaser(Laser l) {
		laser = l;
		l.setOrientation(direction);
		l.setPosition(position);
	}

	@Override
	public void draw() {
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		glBindTexture(GL_TEXTURE_2D, TextureManager.playerTexture().getTextureID());
		glBegin(GL_QUADS);
		glTexCoord2f(1,1);
		glVertex2f(points[0].x, points[0].y);
		glTexCoord2f(1,0);
		glVertex2f(points[3].x, points[3].y);
		glTexCoord2f(0,0);
		glVertex2f(points[2].x, points[2].y);
		glTexCoord2f(0,1);
		glVertex2f(points[1].x, points[1].y);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);
	}

	@Override
	public void drawOnMiniMap() {
		float persoRatio = 0.5f;
		int posx = (int) (MiniMap.position.x + (getX() / Map.roomPixelSize.x)
				* MiniMap.roomSize.x - persoRatio * MiniMap.roomSize.x / 2);
		int posy = (int) (MiniMap.position.y + (getY() / Map.roomPixelSize.y)
				* MiniMap.roomSize.y - persoRatio * MiniMap.roomSize.y / 2);
		glColor3f(0, 1, 0);
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
		if (laser != null) {
			laser.setOrientation(orix, oriy);
		}
		updatePoints();
	}

	@Override
	public void setPosition(float posx, float posy) {
		super.setPosition(posx, posy);
		if (light != null) {
			light.setPosition(posx, posy);
		}
		if (laser != null) {
			laser.setPosition(posx, posy);
		}
		updatePoints();
	}

	public Light getLight() {
		return light;
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

	public void primaryWeapon(float directionX, float directionY){
		weapon.Fire(new Vector2f(position), new Vector2f(directionX,directionY));
	}

}
