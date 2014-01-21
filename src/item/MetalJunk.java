package item;

import rendering.TextureManager;

import item.Item;


public class MetalJunk extends Item {
	public MetalJunk(float x, float y){
		super(x,y);
		weight = 100;
		drawSize[0] = 15;
		drawSize[1] = 30;
	}
	
	public int getTextureID(){
		return TextureManager.metalJunkTexture().getTextureID();
	}	
	
}