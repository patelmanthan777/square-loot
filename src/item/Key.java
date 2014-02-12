package item;

import rendering.TextureManager;


public class Key extends Item {
	public Key(float x, float y){
		super(x,y,ItemListEnum.KEY);
		weight = 100;
		this.image = TextureManager.keyTexture();
	}
	
	public int getTextureID(){
		return TextureManager.keyTexture().getTexture().getTextureID();
	}	
	
}