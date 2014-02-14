package environment.blocks;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import light.Light;
import light.LightManager;
import physics.GameBodyType;


import physics.PhysicsDataStructure;
import environment.room.OxygenRoom;

public class BatteryReceptacleBlock extends SolidBlock{
	
	private OxygenRoom room;
	private Light l;

	
	public BatteryReceptacleBlock(OxygenRoom room){
		super();
		gbtype = GameBodyType.RECEPTACLE;
		this.room = room;
		this.color.set(0.2f, 1f, 1f, 1);
		
	}
	
	public BatteryReceptacleBlock(OxygenRoom room, int posx, int posy){
		super();
		gbtype = GameBodyType.RECEPTACLE;
		this.room = room;
		this.color.set(0.2f, 1f, 1f, 1);
		
		l = LightManager.addPointLight(this.toString(),
									   new Vector2f(
											   (posx+0.5f)*ConfigManager.unitPixelSize,
											   (posy+0.5f)*ConfigManager.unitPixelSize),
									   new Vector3f(color.x,color.y,color.z),
									   100,
									   1000,
									   true);
	
		l.deactivate();
	}
	
	public void powering(){
		room.powering(this);
		l.activate();
	}
	
	public void unpowering(){
		l.deactivate();
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case ENERGYSHOT:			
				powering();
			break;
		default:
			break;
		}
	}
	public void destroy(){
		LightManager.deleteLight(l.toString());
	}
}
