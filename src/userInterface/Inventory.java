package userInterface;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import item.Item;
import item.Equipment;
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
		STORED,
		NOITEM
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
	
	/*Moving feature*/
	
	
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
								
		
		selectedItem = itemState.NOITEM;
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
		case NOITEM:
		}
		
		unselect();
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
				
			
		switch (selectedItem){
		case EQUIPPED:
			tmp = equippedItems.remove(cursor);
			break;
		case STORED:
			tmp = items.remove(cursor);
			break;
		case NOITEM:
		}
		
		if(tmp != null)
			weight -= tmp.getWeight();
	
		unselect();
		return tmp;
	}
	 
	public void select(float x, float y){
		setCursor((int) x, (int)y);
	}
	
	public void unselect(){
		selectedItem = itemState.NOITEM;
	}
	
	public void move(float destx, float desty){				
		itemState srcitem = selectedItem;
		int srcCurs = cursor;
		
		setCursor((int) destx, (int) desty);
		
		if(srcitem != itemState.NOITEM &&
		   selectedItem != itemState.NOITEM)
			if(srcitem == itemState.EQUIPPED &&
			   selectedItem == itemState.EQUIPPED)
				equippedItems.move(srcCurs, cursor);
			else if(srcitem == itemState.STORED &&
				    selectedItem == itemState.STORED)
				items.move(srcCurs, cursor);
			else if(srcitem == itemState.EQUIPPED &&
				    selectedItem == itemState.STORED){
				Item tmp = equippedItems.remove(srcCurs);
				tmp = items.add(tmp, cursor);
								
				if(tmp instanceof Equipment || tmp == null)
					equippedItems.add((Equipment) tmp, srcCurs);
				else{
					tmp = items.add(tmp);
					
					/*if there is no size left return to the original state*/
					if(tmp != null){
						equippedItems.add((Equipment)
										   items.remove(cursor), srcCurs);
						items.add(tmp, cursor);
					}						
				}				
			}
			else{
				Item tmp = items.remove(srcCurs);
				
				if(tmp instanceof Equipment)
					tmp = equippedItems.add((Equipment) tmp, cursor);

				items.add(tmp, srcCurs);
					
			}
		else
			unselect();
				
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
	 * Test whether the cursor is inside the inventory window
	 * 
	 * @param x
	 * @param y
	 * @return <b>true</b> if the cursor is inside the inventory window.
	 */
	public boolean isInsideWindow(float x, float y){
		return coord[0] <= x && x <= coord[0] + inventoryPixelSize[0] &&
			   coord[1] <= y && y <= coord[1] + inventoryPixelSize[1];
	}
	
	/**
	 * Test whether the cursor is inside the item cell whose upper
	 * left corner is locater at 
	 * 
	 * @param cx is the cursor horizontal coordinate
	 * @param cy is the cursor vertical coordinate
	 * @param ix is the item horizontal coordinate
	 * @param iy is the item vertical coordinate
	 * @return <b>true</b> if the cursor is inside the item cell.
	 */
	private boolean isInsideItem(float cx, float cy, float ix, float iy){
		return ix <= cx && cx <= ix + itemPixelSize[0] &&
			   iy <= cy && cy <= iy + itemPixelSize[1];
	}
	
	/**
	 * Set <b>cursor</b> and <b>selectedItem</b> to the values
	 * corresponding to the item displayed at (<b>x</b>,<b>y</b>).
	 * @param x
	 * @param y
	 */
	private void setCursor(int x, int y){
		unselect();
		if(isInsideWindow(x, y)){
			for(int i = 0; i < equippedNbMax; i++){
				if(isInsideItem(x,
						  		y,
						  		coord[0]+ borderPixelSize +
								(borderPixelSize + itemPixelSize[0])/2 +
								i * (borderPixelSize + itemPixelSize[0]),
								coord[1]+ borderPixelSize)){
					selectedItem = itemState.EQUIPPED;
					cursor = i; 
				}					
			}
			
			for(int i = 0; i< dispRowNb; i++){
				for(int j = 0; j < colNb; j++){
					if(isInsideItem(x,
					  		y,
					  		coord[0] + borderPixelSize +
							j * (borderPixelSize + itemPixelSize[0]),
							coord[1]+ borderPixelSize +
							(i+1) * (borderPixelSize + itemPixelSize[1]))){
						selectedItem = itemState.STORED;
						cursor = colNb*(rowIndex+i)+j; 
					}
				}				
			}			
		}
	}
	
	/**
	 * Triggers the action of the item equipped in the <b>idx</b> cell.
	 * 
	 * @param idx
	 * @param x
	 * @param y
	 * @param dirx
	 * @param diry
	 */
	public void equippedItemAction(int idx, float x, float y, float dirx, float diry){
		if(idx < equippedNbMax && equippedItems.access(idx) != null)		
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
										(borderPixelSize + itemPixelSize[0])/2 +
										i * (borderPixelSize + itemPixelSize[0]), 
										coord[1] + borderPixelSize,
										itemPixelSize[0],
										itemPixelSize[1]);				
					glEnd();
					glEnable(GL_TEXTURE_2D);
					equippedItems.access(i).draw(coord[0]+ borderPixelSize +
												 (borderPixelSize + itemPixelSize[0])/2 +
												 i * (borderPixelSize + itemPixelSize[0]),
									   			 coord[1]+ borderPixelSize,
									   			 itemPixelSize[0],
									   			 itemPixelSize[1]);
					glDisable(GL_TEXTURE_2D);
					glBegin(GL_QUADS);
				}
				else{
					glColor3f(0.00f, 0.00f, 0.24f);
					GraphicsAL.drawQuad(coord[0] + borderPixelSize +
										(borderPixelSize + itemPixelSize[0])/2 +
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
							glEnable(GL_TEXTURE_2D);
							items.access(colNb*(rowIndex+i)+j).draw(coord[0]+ borderPixelSize +
									j * (borderPixelSize + itemPixelSize[0]),
									coord[1]+ borderPixelSize +
									(i+1) * (borderPixelSize + itemPixelSize[1]),
									itemPixelSize[0],
									itemPixelSize[1]);
							glDisable(GL_TEXTURE_2D);
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