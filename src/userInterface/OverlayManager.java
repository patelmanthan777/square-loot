package userInterface;

import java.awt.Font;
import java.util.LinkedList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import entity.player.Player;
import environment.room.Room;

public  class OverlayManager {
	
	private static LinkedList<Overlay> overlays = new LinkedList<Overlay>();
	public static  UnicodeFont font;
	
	static public void init(){
		font = new UnicodeFont(new Font("Times New Roman", Font.BOLD, 14));
		font.addAsciiGlyphs();
		font.addGlyphs(400, 600);
		font.getEffects().add(new ColorEffect(java.awt.Color.WHITE)); // FIXME ?
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
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
	
	static public void createMiniMap(Room[][] rooms, Player p){
		MiniMap mm = new MiniMap(rooms,p);
		addOverlay(mm);
	}
	
	static public void createPlayerStatsOverlay(Player p){
		PlayerStatsOverlay pso = new PlayerStatsOverlay(p);
		addOverlay(pso);
	}
}
