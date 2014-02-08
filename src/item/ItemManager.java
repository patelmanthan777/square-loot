package item;

import java.util.LinkedList;


public class ItemManager {

	private static LinkedList <Item> items = new LinkedList<Item>();
	
	public static void init(){
		
	}
	
	public static void add(Item i){
		items.add(i);
	}
	
	private static void remove(Item i){
		items.remove(items.indexOf(i));
	}
	
	public static void update(){
		for(Item i: items){
			if(i.shouldBeDestroyed()){
				i.destroy();
				remove(i);
			}
		}
		
	}
	
	public static void render(){	
		for(Item i : items){
			i.draw();
		}
	}	
}