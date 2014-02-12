package item;

import rendering.TextureManager;


public class Key extends Item {
	public Key(float x, float y){
		super(x,y,ItemListEnum.KEY);
		weight = 100;
	}
	
	public int getTextureID(){
		return TextureManager.keyTexture().getTextureID();
	}	
	
}