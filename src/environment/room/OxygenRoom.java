package environment.room;

import configuration.ConfigManager;
import environment.Map;
import environment.blocks.BatteryReceptacleBlock;
import environment.blocks.BlockFactory;
import event.Timer;

public class OxygenRoom extends Room{
	
	public static final int maxPressure = 100;
	private long timeLoadMax;
	private long timeLoadLimit = 0;
	private BatteryReceptacleBlock block = null;
	
	public OxygenRoom(float posX, float posY) {
		super(posX, posY);
		this.pressure = maxPressure;
		this.newPressure = maxPressure;
		this.timeLoadMax = ConfigManager.oxygenTime * Timer.unitInOneSecond;
	}

	@Override
	protected void construct() {
		super.construct();
		grid[(int) (Map.roomBlockSize.x/2)+1][(int) (Map.roomBlockSize.y/2)+1] = BlockFactory.createBatteryReceptacleBlock(this,(int) ((Map.roomBlockSize.x/2)+1+x/Map.blockPixelSize.x),(int) ((Map.roomBlockSize.y/2)+1+y/Map.blockPixelSize.y));
	}
	
	
	public void update(long delta){
		if(Timer.getRunningTime() > timeLoadLimit){
			if(this.block != null){
				this.block.unpowering();
				this.block = null;
			}
			this.pressure = this.newPressure;
		}
	}
	
	public void powering(BatteryReceptacleBlock block){
		this.pressure = maxPressure;
		this.timeLoadLimit = Timer.getRunningTime() + timeLoadMax;
		if(this.block != null){
			this.block.unpowering();
		}
		this.block = block;
	}
	
}
