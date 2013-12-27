package userInterface;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import entity.player.Player;
import environment.Map;
import environment.room.Room;
import game.GameLoop;

public class MiniMap extends Overlay{
	public static Vector2f position;
	public static Vector2f screenRegion = new Vector2f(0.2f,0.2f);
	public static Vector2f roomSize;
	private Room[][] rooms;
	private Player player;
	
	
	
	public MiniMap(Room[][] rooms, Player p){
		this.rooms = rooms;
		this.player = p;
		MiniMap.position = new Vector2f(ConfigManager.resolution.x*0.75f,ConfigManager.resolution.y*0.05f);
		MiniMap.roomSize = new Vector2f((screenRegion.x * ConfigManager.resolution.x)/Map.mapRoomSize.x,(screenRegion.y * ConfigManager.resolution.y)/Map.mapRoomSize.y);
	}
	
	
	
	@Override
	public void draw() {
		
		for(int i = 0; i < Map.mapRoomSize.x; i++){
			for(int j = 0; j < Map.mapRoomSize.y; j++){
				if (rooms[i][j]!= null){
					rooms[i][j].drawOnMiniMap();
				}
			}
		}
		player.drawOnMiniMap();
	}
}
