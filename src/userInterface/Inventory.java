package userInterface;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import item.Item;
import item.Equipment;
import item.weapon.Weapon;
import userInterface.Overlay;
import utils.GraphicsAL;
import utils.NonContinuousTable;


public class Inventory extends Overlay{
	private boolean open = false;
	
	private int[] coord = new int[2];
	private int[] inventoryPixelSize = new int[2];
	private int[] itemPixelSize = new int[2];
	private int borderPixelSize;
	
	private int colNb = 5;
	private int dispRowNb = 2;
	
	private float weight;
	
	private enum itemState {
		EQUIPPED,
		STORED
	}
	
	/*Stored*/
	private int size;
	private NonContinuousTable<Item> items;	
	
	/*Equipped*/
	private static final int equippedNbMax = 4;
	private NonContinuousTable<Equipment> equippedItems;
	
	private itemState selectedItem;
	private int cursor;
	
	private int rowIndex;
	
	
	public Inventory(int size){
		coord[0] = 200;
		coord[1] = 200;
		
		itemPixelSize[0] = 50;
		itemPixelSize[1] = 50;
		
		borderPixelSize = 5;
		
		inventoryPixelSize[0] = colNb * (itemPixelSize[0] + borderPixelSize) + borderPixelSize;
		inventoryPixelSize[1] = (dispRowNb+1) * (itemPixelSize[1] + borderPixelSize) + borderPixelSize;
		
		weight = 0f;		
			
		this.size = size;
		items = new NonContinuousTable<Item>(size, new Item[size]);
		
		equippedItems =
				new NonContinuousTable<Equipment>(4, new Equipment[equippedNbMax]);
								
		
		selectedItem = null;
		cursor = -1;
		
		rowIndex = 0;		
	}
	
	/**
	 * Add an item to the inventory, and automatically equip it
	 * if it is possible.
	 * 
	 * @param i is the item to added
	 * @return null if the item was added successfully, <b>i</b> otherwise.
	 */		
	public Item add(Item i){
		Item tmp = i;
		
		if(i instanceof Equipment){
			tmp = equippedItems.add((Equipment) i);
		}
		
		if(tmp != null){
			tmp = items.add(tmp);								
		}						
		
		if(tmp == null){
			weight += i.getWeight();
		}
		
		return tmp;		
	}
	
	/**
	 * Return the item displayed at (<b>x</b>, <b>y</b>).
	 * 
	 * @param x
	 * @param y
	 * @return the considered item
	 */
	public Item access(float x, float y){
		setCursor((int) x, (int) y);
		
		switch (selectedItem){
		case EQUIPPED:
			return equippedItems.access(cursor);
		case STORED:
			return items.access(cursor);
		}
		
		return null;
	}
	
	/**
	 * Remove the object displayed at (<b>x</b>, <b>y</b>) from the inventory.
	 * 
	 * @param x
	 * @param y
	 */
	public Item remove(float x, float y){
		setCursor((int) x, (int) y);
		
		Item tmp = null;
		
		if(cursor != -1)
			
			switch (selectedItem){
			case EQUIPPED:
				tmp = equippedItems.remove(cursor);
				break;
			case STORED:
				tmp = items.remove(cursor);
				break;
			}
		
		if(tmp != null)
			weight -= tmp.getWeight();
		
		return tmp;
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
	 * Set <b>cursor</b> and <b>selectedItem</b> to the values
	 * corresponding to the item displayed at (<b>x</b>,<b>y</b>).
	 * @param x
	 * @param y
	 */
	private void setCursor(int x, int y){
		
	}
	
	public void equippedItemAction(int idx, float x, float y, float dirx, float diry){
		if(idx < equippedNbMax)		
			equippedItems.access(idx).action(new Vector2f(x   , y   ),
											 new Vector2f(dirx, diry));
	}
	
	public void draw(){		
		if(isOpen()){
			/*Background*/
			glPushMatrix();		
			glLoadIdentity();
			glDisable(GL_BLEND);
			glDisable(GL_TEXTURE_2D);
		
			glBegin(GL_QUADS);
			glColor3f(0.03f, 0.04f, 0.29f);
			
			GraphicsAL.drawQuad(coord[0],
								coord[1],
								inventoryPixelSize[0],
								inventoryPixelSize[1]);				
		
			
			/*Equipped items (weapons, ...)*/
			for(int i = 0; i < equippedNbMax; i++){
				if(equippedItems.access(i) != null){
					glColor3f(0.00f, 0.00f, 0.18f);
					GraphicsAL.drawQuad(coord[0] + borderPixelSize +
										i * (borderPixelSize + itemPixelSize[0]), 
										coord[1] + borderPixelSize,
										itemPixelSize[0],
										itemPixelSize[1]);				
					glEnd();
					equippedItems.access(i).draw(coord[0]+ borderPixelSize +
												 i * (borderPixelSize + itemPixelSize[0]),
									   			 coord[1]+ borderPixelSize,
									   			 itemPixelSize[0],
									   			 itemPixelSize[1]);
					glBegin(GL_QUADS);
				}
				else{
					glColor3f(0.00f, 0.00f, 0.24f);
					GraphicsAL.drawQuad(coord[0] + borderPixelSize +
										i * (borderPixelSize + itemPixelSize[0]), 
										coord[1] + borderPixelSize,
										itemPixelSize[0],
										itemPixelSize[1]);								
				}
			}
				
			/*Other items*/
			for(int i = 0; i< dispRowNb; i++){
				for(int j = 0; j < colNb; j++){
					if(colNb*(rowIndex+i)+j < size)
						if(items.access(colNb*(rowIndex+i)+j) != null){
							glColor3f(0.00f, 0.00f, 0.18f);
							GraphicsAL.drawQuad(coord[0] + borderPixelSize +
												j * (borderPixelSize + itemPixelSize[0]),
												coord[1]+ borderPixelSize +
												(i+1) * (borderPixelSize + itemPixelSize[1]),
												itemPixelSize[0],
												itemPixelSize[1]);
							glEnd();
							items.access(colNb*(rowIndex+i)+j).draw(coord[0]+ borderPixelSize +
									j * (borderPixelSize + itemPixelSize[0]),
									coord[1]+ borderPixelSize +
									(i+1) * (borderPixelSize + itemPixelSize[1]),
									itemPixelSize[0],
									itemPixelSize[1]);
							glBegin(GL_QUADS);
						}
						else{
							glColor3f(0.00f, 0.00f, 0.24f);
							GraphicsAL.drawQuad(coord[0] + borderPixelSize +
												j * (borderPixelSize + itemPixelSize[0]),
												coord[1]+ borderPixelSize +
												(i+1) * (borderPixelSize + itemPixelSize[1]),
												itemPixelSize[0],
												itemPixelSize[1]);
				
						}					
						else{
							glColor3f(0.00f, 0.00f, 0.10f);
							GraphicsAL.drawQuad(coord[0] + borderPixelSize +
												j * (borderPixelSize + itemPixelSize[0]),
												coord[1]+ borderPixelSize +
												(i+1) * (borderPixelSize + itemPixelSize[1]),
												itemPixelSize[0],
												itemPixelSize[1]);
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