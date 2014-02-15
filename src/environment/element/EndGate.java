package environment.element;

import game.Game;
import item.Item;
import item.ItemListEnum;
import physics.PhysicsDataStructure;
import entity.trigger.TriggerZone;

public class EndGate extends TriggerZone {
	private int lock;

	public EndGate(float range, int lock, float x, float y) {
		super(range, x, y);
		this.lock = lock;
	}

	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case ITEM:
			if(lock > 0)
			{
				Item i = (Item) a.getPhysicsObject();
				if(i.self == ItemListEnum.KEY)
				{
					i.destroyed = true;
					lock--;
				}
			}
			break;
		case PLAYER:
			if(lock == 0)
			{
				Game.newLevel = true;
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		default:
			break;
		}
		
	}

}
