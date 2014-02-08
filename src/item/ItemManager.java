package item;

import java.util.LinkedList;


public class ItemManager {

	private static LinkedList <Item> items = new LinkedList<Item>();
	private static LinkedList <Item> removedItems = new LinkedList<Item>();
	
	public static void init(){

	}
	
	public static void add(Item i){
		items.add(i);
		i.initPhysics();
	}
	
	private static void remove(Item i){
		items.remove(items.indexOf(i));
	}
	
	
	public static void update(){
		for(Item i: items){
			if(i.shouldBeDestroyed()){
				i.destroy();
				removedItems.add(i);
			}
		}
		
		for(Item i: removedItems){
			remove(i);
		}
		
		removedItems.clear();		
	}
	
	public static void render(){	
		for(Item i : items){
			i.draw();
		}
	}	
}