package environment.room;

import configuration.ConfigManager;
import environment.Map;
import environment.blocks.BlockFactory;
import event.Timer;

public class OxygenRoom extends Room{
	
	public static final int maxPressure = 100;
	private long timeLoadMax;
	private long timeLoadLimit = 0;
	
	public OxygenRoom(float posX, float posY) {
		super(posX, posY);
		this.pressure = maxPressure;
		this.newPressure = maxPressure;
		this.timeLoadMax = ConfigManager.oxygenTime * Timer.unitInOneSecond;
	}

	@Override
	protected void construct() {
		super.construct();
		grid[(int) (Map.roomBlockSize.x/2)][(int) (Map.roomBlockSize.y/2)] = BlockFactory.createBatteryReceptacleBlock(this);
	}
	
	
	public void update(long delta){
		
		if(Timer.getRunningTime() > timeLoadLimit){
			this.pressure = this.newPressure;
		}
	}
	
	public void powering(){
		this.pressure = maxPressure;
		this.timeLoadLimit = Timer.getRunningTime() + timeLoadMax;
	}
	
}
