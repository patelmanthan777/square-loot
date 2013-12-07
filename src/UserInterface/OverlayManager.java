package UserInterface;

import java.util.LinkedList;

import environment.room.Room;

public  class OverlayManager {
	private static LinkedList<Overlay> overlays = new LinkedList<Overlay>();
	
	static public void addOverlay(Overlay ov){
		overlays.add(ov);
	}
	
	static public void render(){
		for(Overlay ov : overlays){
			ov.draw();
		}
	}
	
	static public void createStatsOverlay(){
		StatsOverlay stats = new StatsOverlay();
		addOverlay(stats);
	}
	
	static public void createMiniMap(Room[][] rooms, int width, int height){
		MiniMap mm = new MiniMap(rooms, width, height);
		addOverlay(mm);
	}
}
