package item;



import rendering.Drawable;
import entity.Node;

public abstract class Item extends Node implements Drawable{
	protected float weight = 0;	
	
	protected int [] drawSize = new int[2];	
	
	public Item(float x, float y){
		super(x, y);
		drawSize[0] = 30;
		drawSize[1] = 30;
	}
	
	public float getWeight(){
		return weight;
	}
	
	public abstract void draw(float x,
							  float y,
							  float width,
							  float height); 

}
