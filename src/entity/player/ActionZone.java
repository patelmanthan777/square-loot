package entity.player;

import item.Item;

import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import physics.PhysicsManager;
import physics.PhysicsObject;
import configuration.ConfigManager;
import entity.npc.Npc;

public class ActionZone implements PhysicsObject {
	private LinkedList<Item> itemList;
	private LinkedList<Npc> npcList;
	private Body body;
	private float range;
	
	public ActionZone(float range) {
		this.range = range;
		itemList = new LinkedList<Item>();
		npcList = new LinkedList<Npc>();
	}
	
	public void init() {
		initPhysics();
	}
	
	public void setPosition(float x, float y) {
		Vec2 p = new Vec2(x*ConfigManager.blockPhysicSize, y*ConfigManager.blockPhysicSize);
		body.setTransform(p, 0);
		body.setAwake(true);
	}
	
	public Item getItem() {
		return  itemList.pollFirst();
	}
	
	public Npc getNpc() {
		return npcList.peekFirst();
	}
	
	public void initPhysics(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type =  BodyType.DYNAMIC;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(0, 0);
		body = PhysicsManager.createBody(bodyDef);
		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(range*ConfigManager.blockPhysicSize);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicCircle;
		fixtureDef.density = 0.3f;
		fixtureDef.friction = 0.0f;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef);
		PhysicsDataStructure s = new PhysicsDataStructure(this, GameBodyType.TRIGGER); 
		body.setUserData(s);
	}

	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
			case SHOPKEEPER:
				npcList.add((Npc) a.getPhysicsObject());
				break;
			case ITEM:

				itemList.add((Item) a.getPhysicsObject());
				System.out.println("Item : " + itemList.size());
				break;
		}

	}

	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
			case SHOPKEEPER:
				npcList.remove((Npc) a.getPhysicsObject());
			break;
			case ITEM:
				itemList.remove((Item) a.getPhysicsObject());
				System.out.println("Item : " + itemList.size());
				break;
		}
		
	}
}
