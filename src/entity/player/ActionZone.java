package entity.player;

import item.Item;

import java.util.Iterator;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import physics.PhysicsDataStructure;
import configuration.ConfigManager;
import entity.npc.Npc;
import entity.trigger.TriggerZone;

public class ActionZone extends TriggerZone {
	private LinkedList<Item> itemList;
	private LinkedList<Npc> npcList;

	
	public ActionZone(float range, float x, float y) {
		super(range, x, y);
		itemList = new LinkedList<Item>();
		npcList = new LinkedList<Npc>();
	}
	
	public void setPosition(float x, float y) {
		Vec2 p = new Vec2(x*ConfigManager.blockPhysicSize, y*ConfigManager.blockPhysicSize);
		body.setTransform(p, 0);
		body.setAwake(true);
	}
	
	public Item getItem() {
		clearList();
		return  itemList.pollFirst();
	}
	
	public Npc getNpc() {

		return npcList.peekFirst();
	}

	private void clearList() {
		Iterator<Item> ite = itemList.iterator();
		while(ite.hasNext())
		{
			Item i = ite.next();
			if(i.destroyed == true)
			{
				ite.remove();
			}
		}
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case SHOPKEEPER:
			npcList.add((Npc) a.getPhysicsObject());
			break;
		case ITEM:
		case BATTERY:
			itemList.add((Item) a.getPhysicsObject());
			break;
		default:
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
		case BATTERY:
			itemList.remove((Item) a.getPhysicsObject());
			break;
		default:
			break;
		}
		
	}
}
