package userInterface;

import static org.lwjgl.opengl.GL11.*;

import item.Item;
import userInterface.Overlay;


public class Inventory extends Overlay{
	private boolean open = false;
	
	private int[] coord = new int[2];
	private int[] inventoryPixelSize = new int[2];
	private int[] itemPixelSize = new int[2];
	private int borderPixelSize;
	
	private int colNb = 5;
	private int dispRowNb = 2;
	
	private float weight;
	
	private Item[][] items;
	private	int[] itemCurs = new int[2];
	private int size;
	private int sizeMax;
	
	private int[] cursor = new int[2];
	
	private int rowIndex;
	
	
	public Inventory(int size){
		coord[0] = 200;
		coord[1] = 200;
		
		itemPixelSize[0] = 100;
		itemPixelSize[1] = 100;
		
		borderPixelSize = 5;
		
		inventoryPixelSize[0] = colNb * (itemPixelSize[0] + borderPixelSize) + borderPixelSize;
		inventoryPixelSize[1] = dispRowNb * (itemPixelSize[1] + borderPixelSize) + borderPixelSize;
		
		weight = 0f;		
				
		items = new Item[(int)(size / colNb)+1][colNb];
		for (int i = 0; i < (int)(size / colNb)+1; i++){
			for (int j = 0; j < colNb; j++){
				items[i][j] = null;
			}
		}
		
		itemCurs[0] = 0;
		itemCurs[1] = 0;
		this.size = 0;
		sizeMax  = size;
		
		cursor[0] = -1;
		cursor[1] = -1;
		
		rowIndex = 0;	
	}
	
	/**
	 * Add an item to the inventory.
	 * 
	 * @param i is the item to added
	 * @return null if the item was added successfully, <b>i</b> otherwise.
	 */		
	public Item add(Item i){
		if (size < sizeMax){
			items[itemCurs[0]][itemCurs[1]] = i;			
			size ++;
			weight += i.getWeight();
			
			if (size < sizeMax){
				boolean update = false;
				while(!update){					 
					itemCurs[1] = (itemCurs[1] + 1) % colNb;
					if(itemCurs[1] == 0)
						itemCurs[0]++;
					
					if(items[itemCurs[0]][itemCurs[1]] == null)
						update = true;
				}
			}
			else{
				itemCurs[0] = 4;
				itemCurs[1] = (int)(size / colNb)+1;
			}
			
			return null;
		}
		
		return i;
	}
	
	/**
	 * Return the item displayed at (<b>x</b>, <b>y</b>).
	 * 
	 * @param x
	 * @param y
	 * @return the considered item
	 */
	public Item access(float x, float y){
		getIdx((int) x, (int) y);
		
		return items[cursor[0]][cursor[1]];
	}
	
	/**
	 * Remove the object displayed at (<b>x</b>, <b>y</b>) from the inventory.
	 * 
	 * @param x
	 * @param y
	 */
	public Item remove(float x, float y){
		getIdx((int) x, (int) y);
		
		if(cursor[0] != -1 && cursor[1] != -1){
			Item i = items[cursor[0]][cursor[1]];
				
			weight -= i.getWeight();
			size--;
			
			if(itemCurs[0] >= cursor[0] && itemCurs[1] >= cursor[1]){
				itemCurs[0] = cursor[0];
				itemCurs[1] = cursor[1];
			}
			
			items[cursor[0]][cursor[1]] = null;
		
			return i;
		}
		
		return null;
	}
	 
	
	public float getWeight(){
		return weight;
	}
	
	/**
	 * Triggers the drawing of the inventory and capture the relevant
	 * mouse events.
	 * 
	 */
	public void open(){
		open = true;
	}
	
	/**
	 * Return the inventory in its hiding state.
	 */
	public void close(){
		open = false;
	}
	
	/**
	 * Test whether the inventory should be displayed.
	 * @return <b>true</b> if the inventory is expected
	 * to be displayed.
	 */
	public boolean isOpen(){
		return open;
	}
	
	/**
	 * Obtain the indices in the inventory corresponding to
	 * the coordinates (<b>x</b>,<b>y</b>).
	 * @param x
	 * @param y
	 */
	private void getIdx(int x, int y){
		
	}
	
	public void draw(){		
		if(isOpen()){
			glPushMatrix();		
			glLoadIdentity();
			glDisable(GL_BLEND);
			glDisable(GL_TEXTURE_2D);
		
			glBegin(GL_QUADS);
			glColor3f(0.03f, 0.04f, 0.29f);
			

			glVertex2f(coord[0], coord[1]+inventoryPixelSize[1]);
			glVertex2f(coord[0]+inventoryPixelSize[0],
					coord[1]+inventoryPixelSize[1]);
			glVertex2f(coord[0]+inventoryPixelSize[0], coord[1]);
			glVertex2f(coord[0], coord[1]);
			
								
						
			for(int i = 0; i< dispRowNb; i++){
				for(int j = 0; j < colNb; j++){
				
					if(items[rowIndex+i][j] != null){
						glColor3f(0.00f, 0.00f, 0.18f);
						glVertex2f(coord[0] + borderPixelSize +
								j * (borderPixelSize + itemPixelSize[0]),
								coord[1] +
								(i+1) * (borderPixelSize + itemPixelSize[1]));
						glVertex2f(coord[0] + 
								(j+1) * (borderPixelSize + itemPixelSize[0]),
								coord[1] +
								(i+1) * (borderPixelSize + itemPixelSize[1]));												
						glVertex2f(coord[0] +
								(j+1) * (borderPixelSize + itemPixelSize[0]),
								coord[1]+ borderPixelSize +
								i * (borderPixelSize + itemPixelSize[1]));
						glVertex2f(coord[0] + borderPixelSize +
								j * (borderPixelSize + itemPixelSize[0]),
								coord[1]+ borderPixelSize +
								i * (borderPixelSize + itemPixelSize[1]));
						glEnd();
						items[rowIndex+i][j].draw(coord[0]+ borderPixelSize +
								j * (borderPixelSize + itemPixelSize[0]),
								coord[1]+ borderPixelSize +
								i * (borderPixelSize + itemPixelSize[1]),
								itemPixelSize[0],
								itemPixelSize[1]);
						glBegin(GL_QUADS);
					}
					else{
						glColor3f(0.00f, 0.00f, 0.24f);
						glVertex2f(coord[0] + borderPixelSize +
								j * (borderPixelSize + itemPixelSize[0]),
								coord[1] +
								(i+1) * (borderPixelSize + itemPixelSize[1]));
						glVertex2f(coord[0] + 
								(j+1) * (borderPixelSize + itemPixelSize[0]),
								coord[1] +
								(i+1) * (borderPixelSize + itemPixelSize[1]));												
						glVertex2f(coord[0] +
								(j+1) * (borderPixelSize + itemPixelSize[0]),
								coord[1]+ borderPixelSize +
								i * (borderPixelSize + itemPixelSize[1]));
						glVertex2f(coord[0] + borderPixelSize +
								j * (borderPixelSize + itemPixelSize[0]),
								coord[1]+ borderPixelSize +
								i * (borderPixelSize + itemPixelSize[1]));

					}											
				}
			}
								
			glEnd();
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
		
			glPopMatrix();
		}
	}
}