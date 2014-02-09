package environment.blocks;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import light.Light;
import light.LightManager;
import physics.PhysicsDataStructure;
import environment.room.OxygenRoom;

public class BatteryReceptacleBlock extends SolidBlock{
	
	private OxygenRoom room;
	private Light l;
	private int posx;
	private int posy;
	
	public BatteryReceptacleBlock(OxygenRoom room, int posx, int posy){
		this.room = room;
		this.color.set(0.2f, 1f, 1f, 1);
		this.posx = posx;
		this.posy = posy;
		l = LightManager.addPointLight(this.toString(), new Vector2f((posx+0.5f)*ConfigManager.unitPixelSize,(posy+0.5f)*ConfigManager.unitPixelSize), new Vector3f(color.x,color.y,color.z), 100, 1000, true);
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
