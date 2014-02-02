package userInterface.inventory;


import org.lwjgl.util.vector.Vector2f;

import item.Item;

import item.weapon.PrimaryWeapon;
import item.weapon.SecondaryWeapon;
import item.shield.Shield;
import item.motionGear.MotionGear;
import item.accessory.Accessory;

import userInterface.Overlay;
import userInterface.inventory.InventoryItemEnum;




public class Inventory extends Overlay{		
	private float weight;
		
	/*Carried*/
	
	/*Active Equipment*/
	class InventorySlot<A> {
		A slot = null;
				
		
		public A add(A a){
			A tmp = slot; 
			slot = a;
			return tmp;
		}
		
		public A access(){
			return slot;
		}
		
		public A remove(){
			return add(null);
		}
	}	
	
	private InventorySlot<PrimaryWeapon> pweapon;
	private InventorySlot<SecondaryWeapon> sweapon;
	private InventorySlot<Shield> shield;
	private InventorySlot<Accessory> accessory;
	private InventorySlot<MotionGear> mgear;
	
	
	

	
	
	public Inventory(int size){	
		weight = 0f;					
			
		pweapon = new InventorySlot<PrimaryWeapon>();
		sweapon = new InventorySlot<SecondaryWeapon>();
		shield = new InventorySlot<Shield>();
		accessory = new InventorySlot<Accessory>();
		mgear = new InventorySlot<MotionGear>();

	
											
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
		
		if(i instanceof PrimaryWeapon)
			tmp = pweapon.add((PrimaryWeapon) i);		
		else if (i instanceof SecondaryWeapon)
			tmp = sweapon.add((SecondaryWeapon) i);
		else if (i instanceof Shield)
			tmp = shield.add((Shield) i);
		else if (i instanceof SecondaryWeapon)
			tmp = accessory.add((Accessory) i);
		else if (i instanceof SecondaryWeapon)
			tmp = mgear.add((MotionGear) i);
		
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
			return pweapon.access();
		case SWEAPON:
			return sweapon.access();
		case SHIELD:
			return shield.access();
		case ACCESSORY:
			return accessory.access();
		case MGEAR:
			return mgear.access();
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
			tmp = pweapon.remove();
			break;
		case SWEAPON:			
			tmp = sweapon.remove();
			break;
		case SHIELD:			
			tmp = shield.remove();
			break;
		case ACCESSORY:			
			tmp = accessory.remove();
			break;
		case MGEAR:			
			tmp = mgear.remove();
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
	public void equippedItemAction(InventoryItemEnum i, float x, float y, float dirx, float diry){
		switch (i){
		case PWEAPON:
			if(pweapon.access() != null)
				pweapon.access().action(new Vector2f(x   , y   ),
						                new Vector2f(dirx, diry));			
			break;
		case SWEAPON:
			if(pweapon.access() != null)
				sweapon.access().action(new Vector2f(x   , y   ),
                                        new Vector2f(dirx, diry));			
			break;
		case SHIELD:			
			if(pweapon.access() != null)
				shield.access().action(new Vector2f(x   , y   ),
									   new Vector2f(dirx, diry));			
			break;
		case ACCESSORY:			
			if(pweapon.access() != null)
				accessory.access().action(new Vector2f(x   , y   ),
                                          new Vector2f(dirx, diry));			
			break;
		case MGEAR:			
			if(pweapon.access() != null)
				mgear.access().action(new Vector2f(x   , y   ),
									  new Vector2f(dirx, diry));			
			break;
		case NOITEM:
		}	
		
	}

	public void draw(){}
	
}