package item;

import java.util.LinkedList;


public class ItemManager {

	private static LinkedList <Item> items = new LinkedList<Item>();
	
	public static void init(){
		
	}
	
	public static void add(Item i){
		items.add(i);
	}
	
	public static void remove(Item i){
		items.remove(items.indexOf(i));
	}
	

	public static void render(){	
		for(Item i : items){
			i.draw();
		}
	}	
}