package item;

import rendering.TextureManager;
import item.Item;


public class Battery extends Item {
	public Battery(float x, float y){
		super(x,y,ItemListEnum.BATTERY);
		weight = 0;
		drawSize[0] = 15;
		drawSize[1] = 30;
	}
	
	public int getTextureID(){
		return TextureManager.batteryTexture().getTextureID();
	}	
	
}