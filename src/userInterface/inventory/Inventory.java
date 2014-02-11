package userInterface.inventory;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.lwjgl.util.vector.Vector2f;

import entity.LivingEntity;
import item.Battery;
import item.Item;
import item.ItemListEnum;
import item.Key;
import item.weapon.EnergyWeapon;
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
	private InventorySlot<Key> key;
	
	private LinkedList<Battery> batteries;
	private int batteryNbMax;
	
	/*Active Equipment*/
	
	
	private InventorySlot<PrimaryWeapon> pweapon;
	private InventorySlot<SecondaryWeapon> sweapon;
	private InventorySlot<Shield> shield;
	private InventorySlot<Accessory> accessory;
	private InventorySlot<MotionGear> mgear;
	private EnergyWeapon eweapon;
	
	
	private LivingEntity owner;
	
	

	public Inventory(int size, LivingEntity owner){	
		weight = 0f;					
			
		key = new InventorySlot<Key>();				
		
		batteries = new LinkedList<Battery>();
		batteryNbMax = size;
		
		pweapon = new InventorySlot<PrimaryWeapon>();
		sweapon = new InventorySlot<SecondaryWeapon>();
		shield = new InventorySlot<Shield>();
		accessory = new InventorySlot<Accessory>();
		mgear = new InventorySlot<MotionGear>();
		
		eweapon = new EnergyWeapon(1000,200,200,0.02f,10,50);
		
		HUD.registerInventory(this);
		
		this.owner = owner;
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
			if(batteries.size() < batteryNbMax){
				batteries.addLast((Battery) i);
				tmp = null;
			}
			else
				tmp = i;					
		else if (i instanceof Key)
			tmp = key.add((Key) i);
		
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
		default:
			break;
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
		default:
			break;
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
			try{
				tmp = batteries.removeLast();
			}
			catch(NoSuchElementException e){
				tmp = null;
			}			
			break;
		case KEY:
			tmp = key.remove();
		case NOITEM:
		default:
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
						                new Vector2f(dirx, diry),owner.getSpeed());			
			break;
		case SWEAPON:
			if(pweapon.access() != null)
				sweapon.access().action(new Vector2f(x   , y   ),
                                        new Vector2f(dirx, diry),owner.getSpeed());			
			break;
		case SHIELD:			
			if(pweapon.access() != null)
				shield.access().action(new Vector2f(x   , y   ),
									   new Vector2f(dirx, diry),owner.getSpeed());			
			break;
		case ACCESSORY:			
			if(pweapon.access() != null)
				accessory.access().action(new Vector2f(x   , y   ),
                                          new Vector2f(dirx, diry),owner.getSpeed());			
			break;
		case MGEAR:			
			if(pweapon.access() != null)
				mgear.access().action(new Vector2f(x   , y   ),
									  new Vector2f(dirx, diry),owner.getSpeed());			
			break;
		case BATTERY:
		case NOITEM:
		default:
		}	
		
	}
	
	public void energyShot(Vector2f pos, Vector2f target){
		if(batteries.size() > 0 && eweapon.action(pos, target,owner.getSpeed()))		
			batteries.removeLast();
		
	}

	/**
	 * Test whether there is a battery in the inventory.
	 */
	public boolean isCarryingKey(){
		return !key.isEmpty(); 
	}
	
	
	public void draw(){}
	
}