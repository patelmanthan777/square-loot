package userInterface;

import java.awt.Font;
import java.util.LinkedList;

import org.newdawn.slick.TrueTypeFont;

import entity.player.Player;
import environment.room.Room;

public  class OverlayManager {
	
	private static LinkedList<Overlay> overlays = new LinkedList<Overlay>();
	public static  TrueTypeFont font;
	
	static public void addOverlay(Overlay ov){
		overlays.add(ov);
		font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 14), true);
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
	
	static public void createMiniMap(Room[][] rooms, Player p){
		MiniMap mm = new MiniMap(rooms,p);
		addOverlay(mm);
	}
	
	static public void createPlayerStatsOverlay(Player p){
		PlayerStatsOverlay pso = new PlayerStatsOverlay(p);
		addOverlay(pso);
	}
}
