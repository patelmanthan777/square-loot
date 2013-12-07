package userInterface;

import org.lwjgl.util.vector.Vector2f;

import environment.Map;
import environment.room.Room;
import game.GameLoop;

public class MiniMap extends Overlay{
	public static Vector2f position;
	public static Vector2f screenRegion = new Vector2f(0.2f,0.2f);
	public static Vector2f roomSize;
	private Room[][] rooms;
	public MiniMap(Room[][] rooms){
		this.rooms = rooms;
		MiniMap.position = new Vector2f((float)GameLoop.WIDTH*0.7f,(float)GameLoop.HEIGHT*0.05f);
		MiniMap.roomSize = new Vector2f((screenRegion.x * GameLoop.WIDTH)/Map.mapRoomSize.x,(screenRegion.y * GameLoop.HEIGHT)/Map.mapRoomSize.y);
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
	}
}
