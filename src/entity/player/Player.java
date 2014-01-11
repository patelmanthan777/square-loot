package entity.player;

import item.weapon.LaserRifle;
import item.weapon.Weapon;
import light.Laser;
import light.Light;
import static org.lwjgl.opengl.GL11.*;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import physics.PhysicsEntity;
import rendering.MiniMapDrawable;
import rendering.TextureManager;
import userInterface.MiniMap;
import entity.LivingEntity;
import environment.Map;

public class Player extends LivingEntity implements MiniMapDrawable, PhysicsEntity {
	private Laser laser;
	private Light light;
	private Body body;

	private Weapon weapon = new LaserRifle(250);

	public Player(Vector2f pos) {
		super(pos);
		Vector3f col = new Vector3f(0, 0, 0);
		setColor(col);
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
		l.setDirection(getDirection());
		l.setPosition(position);
	}

	@Override
	public void draw() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1, 1, 1);
		glBindTexture(GL_TEXTURE_2D, TextureManager.playerTexture()
				.getTextureID());
		glBegin(GL_QUADS);
		glTexCoord2f(1, 1);
		glVertex2f(points[0].x, points[0].y);
		glTexCoord2f(1, 0);
		glVertex2f(points[3].x, points[3].y);
		glTexCoord2f(0, 0);
		glVertex2f(points[2].x, points[2].y);
		glTexCoord2f(0, 1);
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
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy + persoRatio
				* MiniMap.roomSize.y);
		glVertex2f(posx, posy + persoRatio * MiniMap.roomSize.y);
		glEnd();
	}

	@Override
	public void setDirection(float orix, float oriy) {
		super.setDirection(orix, oriy);
		if (laser != null) {
			laser.setDirection(orix, oriy);
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

	public void primaryWeapon(float directionX, float directionY) {
		weapon.Fire(new Vector2f(position),
				new Vector2f(directionX, directionY));
	}

	@Override
	public void initPhysics(World w) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x, position.y);
		body = w.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(halfSize.x, halfSize.y);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.1f;
		body.createFixture(fixtureDef);
	}

	public void update() {
		Vec2 position = body.getPosition();
		setPosition(position.x, position.y);
	}

	@Override
	public void updatePostion(long dt, Map m) {
		if (dt != 0) {
			Vec2 point = new Vec2(position.x, position.y);
			Vec2 vel = body.getLinearVelocity();
			if (translation.length() != 0) {
				translation.normalise(translation);
				translation.scale(dt * 100);
				Vec2 impulse = new Vec2(translation.x, translation.y);
				body.applyLinearImpulse(impulse, point);
			} else {
				float factor = (float) Math.max(0., 1 - dt / 1);
				body.setLinearVelocity(new Vec2(vel.x * factor, vel.y * factor));
			}
		}
		translation.x = 0;
		translation.y = 0;
	}

}
