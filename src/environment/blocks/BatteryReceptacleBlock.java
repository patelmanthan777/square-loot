package environment.blocks;

import environment.room.OxygenRoom;
import environment.room.Room;

public class BatteryReceptacleBlock extends SolidBlock{
	
	private Room room;
	
	public BatteryReceptacleBlock(OxygenRoom room){
		this.room = room;
		this.color.set(0, 0, 1, 1);
	}
	
	public void powering(){
		
	}
}
