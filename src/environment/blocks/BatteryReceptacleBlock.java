package environment.blocks;

import physics.PhysicsDataStructure;
import environment.room.OxygenRoom;

public class BatteryReceptacleBlock extends SolidBlock{
	
	private OxygenRoom room;
	
	public BatteryReceptacleBlock(OxygenRoom room){
		this.room = room;
		this.color.set(0, 0, 1, 1);
	}
	
	public void powering(){
		room.powering();
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case BLOCK:
			break;
		case ENTITY:
			break;
		case PLAYER:
			break;
		case PROJECTILE:
			break;
		case ENERGYSHOT:
			powering();
			break;
		default:
			break;
		}
	}
}
