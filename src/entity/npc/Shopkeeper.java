package entity.npc;

import static org.lwjgl.opengl.GL11.*;
import item.Battery;
import item.Item;
import item.Key;

import java.util.LinkedList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import configuration.ConfigManager;
import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import rendering.TextureManager;
import entity.player.Player;
import environment.Map;

public class Shopkeeper extends Npc {
	enum ShopType {
		ENERGYAMMO, KEY
	}

	private ShopType stype;
	private int price;

	public Shopkeeper(Vector2f pos) {
		super(pos);
		init();
	}

	public Shopkeeper(Vector2f pos, Vector2f rot) {
		super(pos, rot);
		init();
	}

	public Shopkeeper(float posx, float posy, float dirx, float diry) {
		super(posx, posy, dirx, diry);
		init();
	}

	public Shopkeeper(float posx, float posy) {
		super(posx, posy);
		init();
	}

	private void init() {
		// this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(20);
		this.accFactor = 0.001f;
		this.descFactor = 0.2f;
		this.halfSize.x = (float) 40 / (float) ConfigManager.unitPixelSize;
		this.halfSize.y = (float) 40 / (float) ConfigManager.unitPixelSize;

		price = 10;

		Double nrand = Math.random();
		if (nrand < 0.2) {
			stype = ShopType.KEY;
		} else {
			stype = ShopType.ENERGYAMMO;
		}
	}

	@Override
	public void initPhysics() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(position.x * ConfigManager.blockPhysicSize, position.y * ConfigManager.blockPhysicSize);
		body = PhysicsManager.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(0.4f * ConfigManager.blockPhysicSize, 0.4f * ConfigManager.blockPhysicSize);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.1f;
		body.createFixture(fixtureDef);
		PhysicsDataStructure s = new PhysicsDataStructure(this, GameBodyType.SHOPKEEPER);
		body.setUserData(s);
	}

	@Override
	public void ContactHandler(PhysicsDataStructure a) {
	}

	@Override
	public void draw() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1, 1, 1);

		Image tile = TextureManager.shopKeeperTexture();

		tile.setCenterOfRotation(halfSize.x * ConfigManager.unitPixelSize, halfSize.y * ConfigManager.unitPixelSize);
		tile.setRotation(-(this.getDegreAngle() + 90));
		tile.draw(this.getX() * Map.blockPixelSize.x - halfSize.x * ConfigManager.unitPixelSize, this.getY() * Map.blockPixelSize.y - halfSize.y * ConfigManager.unitPixelSize, halfSize.x * 2
				* ConfigManager.unitPixelSize, halfSize.y * 2 * ConfigManager.unitPixelSize);
		switch (stype) {
		case ENERGYAMMO:
			tile = TextureManager.batteryTexture();
			break;
		case KEY:
			tile = TextureManager.keyTexture();
			break;
		}
		tile.rotate(180);
		tile.draw((this.getX() + halfSize.x - 0.2f) * ConfigManager.unitPixelSize, (this.getY() - halfSize.y)* ConfigManager.unitPixelSize, halfSize.x * ConfigManager.unitPixelSize, halfSize.y * ConfigManager.unitPixelSize);
		tile.setRotation(0);
		glDisable(GL_BLEND);
	}

	public Item buy(Player p) {
		if (p.discharge(price)) {
			price *= 2;
			switch (stype) {
			case ENERGYAMMO:
				return new Battery(p.getPosition().x, p.getPosition().y);
			case KEY:
				return new Key(p.getPosition().x, p.getPosition().y);
			}
		}
		return null;
	}

	@Override
	public void thinkAndAct(LinkedList<Player> players, long deltaT) {

	}
}