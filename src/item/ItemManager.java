package item;

import java.util.LinkedList;
import environment.Map;


public class ItemManager {

	private static LinkedList <Item> items = new LinkedList<Item>();
	private static LinkedList <Item> removedItems = new LinkedList<Item>();
	
	public static void init(){
		
	}
	
	public static void reinit(){
		items.clear();
	}
	
	
	public static void add(Item i){
		i.destroyed = false;
		items.add(i);
		i.initPhysics();
	}
	
	private static void remove(Item i){
		items.remove(items.indexOf(i));
	}
	
	public static void updatePhysics(long elapsedTime) {
		for(Item i: items){ 
			i.updatePhysics(elapsedTime);
		}
	}
	public static void update(long delta, Map m){		
		for(Item i: items){ 
			i.updatePosition(delta, m);
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