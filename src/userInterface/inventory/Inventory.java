package userInterface.inventory;


import org.lwjgl.util.vector.Vector2f;

import item.Battery;
import item.Item;
import item.ItemListEnum;

import item.weapon.PrimaryWeapon;
import item.weapon.SecondaryWeapon;
import item.shield.Shield;
import item.motionGear.MotionGear;
import item.accessory.Accessory;

import userInterface.HUD;
import userInterface.Overlay;
import userInterface.inventory.InventoryItemEnum;




public class Inventory extends Overlay{		
	private float weight;
		
	class InventorySlot<A extends Item> {
		A slot = null;
				
		public boolean isEmpty(){
			return slot == null;
		}
		
		public A add(A a){
			A tmp = slot; 
			slot = a;
			return tmp;
		}
		
		public A access(){
			return slot;
		}
		
		public ItemListEnum getInfo(){
			return slot.self;
		}
		
		public A remove(){
			return add(null);
		}
	}	
	
	
	/*Carried*/
	private InventorySlot<Battery> battery;
	
	/*Active Equipment*/
	
	
	private InventorySlot<PrimaryWeapon> pweapon;
	private InventorySlot<SecondaryWeapon> sweapon;
	private InventorySlot<Shield> shield;
	private InventorySlot<Accessory> accessory;
	private InventorySlot<MotionGear> mgear;
	
	
	

	
	

	public Inventory(){	
		weight = 0f;					
			
		battery = new InventorySlot<Battery>();
		
		pweapon = new InventorySlot<PrimaryWeapon>();
		sweapon = new InventorySlot<SecondaryWeapon>();
		shield = new InventorySlot<Shield>();
		accessory = new InventorySlot<Accessory>();
		mgear = new InventorySlot<MotionGear>();
		
		HUD.registerInventory(this);
	}
	
	/**
	 * Add an item to the inventory, and drop the relevant item
	 * if any was already being carried or equipped.
	 * 
	 * @param i is the item to be added
	 * @return <b>null</b> or the old item if the item was added
	 * successfully, <b>i</b> otherwise.
	 */		
	public Item add(Item i){
		Item tmp = i;
		
		if(i instanceof PrimaryWeapon)
			tmp = pweapon.add((PrimaryWeapon) i);		
		else if (i instanceof SecondaryWeapon)
			tmp = sweapon.add((SecondaryWeapon) i);
		else if (i instanceof Shield)
			tmp = shield.add((Shield) i);
		else if (i instanceof Accessory)
			tmp = accessory.add((Accessory) i);
		else if (i instanceof MotionGear)
			tmp = mgear.add((MotionGear) i);
		else if (i instanceof Battery)
			tmp = battery.add((Battery) i);
		
		if(tmp == null){
			weight += i.getWeight();
		}
		
		return tmp;		
	}
	
	/**
	 * Return the requested equipped item.
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
	 *  Return the requested information.
	 */
	public ItemListEnum getInfo(InventoryItemEnum i){
		switch (i){
		case PWEAPON:
			if (!pweapon.isEmpty())
				return pweapon.getInfo();
		case SWEAPON:
			if (!sweapon.isEmpty())
				return sweapon.getInfo();
		case SHIELD:
			if (!shield.isEmpty())
				return shield.getInfo();
		case ACCESSORY:
			if (!accessory.isEmpty())
				return accessory.getInfo();
		case MGEAR:
			if (!mgear.isEmpty())
				return mgear.getInfo();
		case NOITEM:
		}
		
		return null;
	}
		
	
	/**
	 * Remove the requested equipment from the inventory.
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
		case BATTERY:				
			tmp = battery.remove();
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
	 * Triggers the action of the requested equipment.
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
		case BATTERY:
		case NOITEM:
		}	
		
	}

	/**
	 * Test whether there is a battery in the inventory.
	 */
	public boolean isCarryingBattery(){
		return !battery.isEmpty(); 
	}
	
	
	public void draw(){}
	
}