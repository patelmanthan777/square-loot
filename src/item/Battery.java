package item;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import rendering.TextureManager;
import item.Item;


public class Battery extends Item {
	
	public Battery(float x, float y){
		super(x,y,ItemListEnum.BATTERY);
		weight = 0;
		drawSize[0] = 15;
		drawSize[1] = 30;		
		
		gbtype = GameBodyType.BATTERY;
	}
	
	public int getTextureID(){
		return TextureManager.batteryTexture().getTextureID();
	}	
	
	
	@Override
	public void ContactHandler(PhysicsDataStructure a)
	{
		
		switch(a.getType())
		{
		case RECEPTACLE:
			destroyed = true;
			break;		
		default:
			break;
		}
	}
}