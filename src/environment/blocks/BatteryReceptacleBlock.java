package environment.blocks;

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
}
