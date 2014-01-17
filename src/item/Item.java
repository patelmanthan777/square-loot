package item;

public abstract class Item {
	float weight = 0;
	
	public float getWeight(){
		return weight;
	}
	
	public abstract void drawInventory(float x,
									   float y,
									   float width,
									   float height); 

}
