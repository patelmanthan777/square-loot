package userInterface.inventory;

import org.lwjgl.util.vector.Vector2f;

import item.Item;
import item.Equipment;
import userInterface.Overlay;
import userInterface.inventory.InventoryItemEnum;

import utils.NonContinuousTable;


public class Inventory extends Overlay{		
	private float weight;
		
	/*Carried*/
	
	/*Active Equipment*/
	private static final int equippedNbMax = 5;
	private NonContinuousTable<Equipment> equippedItems;	
	


	
	
	public Inventory(int size){	
		weight = 0f;					
		
		equippedItems =
				new NonContinuousTable<Equipment>(4, new Equipment[equippedNbMax]);
											
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
	public Item access(InventoryItemEnum i){
		
		switch (i){
		case PWEAPON:
			return equippedItems.access(0);
		case SWEAPON:
			return equippedItems.access(1);
		case SHIELD:
			return equippedItems.access(2);
		case ACCESSORY:
			return equippedItems.access(3);
		case MGEAR:
			return equippedItems.access(4);
		case NOITEM:
		}
		
		return null;
	}		
	
	/**
	 * Remove the object displayed at (<b>x</b>, <b>y</b>) from the inventory.
	 * 
	 * @param x
	 * @param y
	 */
	public Item remove(InventoryItemEnum i){		
		
		Item tmp = null;
		
		switch (i){
		case PWEAPON:
			tmp = equippedItems.remove(0);
			break;
		case SWEAPON:			
			tmp = equippedItems.remove(1);
			break;
		case SHIELD:			
			tmp = equippedItems.remove(2);
			break;
		case ACCESSORY:			
			tmp = equippedItems.remove(3);
			break;
		case MGEAR:			
			tmp = equippedItems.remove(4);
			break;
		case NOITEM:
		}
							
		if(tmp != null)
			weight -= tmp.getWeight();
			
		return tmp;
	}
	
	
	public float getWeight(){
		return weight;
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
		if(idx < equippedNbMax && equippedItems.access(idx) != null){		
			equippedItems.access(idx).action(new Vector2f(x   , y   ),
											 new Vector2f(dirx, diry));
		}
	}

	public void draw(){}
	
}