package item;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import rendering.TextureManager;
import item.Item;


public class Battery extends Item {
	
	public Battery(float x, float y){
		super(x,y,ItemListEnum.BATTERY);
		weight = 0;
		halfSize.set(0.3f,0.3f);
		gbtype = GameBodyType.BATTERY;
		try {
			sprites = new SpriteSheet("assets/textures/battery.png",64,64);
			image = sprites.getSprite(0, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public int getTextureID(){
		return TextureManager.batteryTexture().getTexture().getTextureID();
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