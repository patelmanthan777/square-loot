package item;

import rendering.TextureManager;


public class Energy extends Item {
	int charge;
	
	public Energy(float x, float y, int charge){
		super(x,y);
		weight = 100;
		drawSize[0] = 15;
		drawSize[1] = 30;
		
		this.charge = charge;
	}
	
	public int getTextureID(){
		return TextureManager.energyTexture().getTextureID();
	}	
	
	public int getCharge(){
		return charge;
	}
	
}