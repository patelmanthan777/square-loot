package environment;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import environment.blocks.Block;
import environment.blocks.SolidBlock;
import environment.room.Room;
import rendering.Drawable;
import rendering.ShadowCaster;
import rendering.LightTaker;

public class Map implements Drawable, ShadowCaster, LightTaker{
	
	
	public static Vector2f blockPixelSize;
	public static Vector2f roomBlockSize;
	public static Vector2f roomPixelSize;
	public static Vector2f mapRoomSize;
	public static Vector2f mapBlockSize;
	public static Vector2f mapPixelSize;
	public static Vector2f spawnPixelPosition;
	public static Vector2f spawnRoomPosition;
	
	//private Block[][] blockGrid;
	private Room[][] roomGrid;
	
	
	private Vector2f drawRoomPosition;
	private Vector2f drawRoomDistance;

	
	
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	
	/**
	 * Map class constructor
	 * @param size
	 */
	public Map(Vector2f mapRoomSize, Vector2f roomBlockSize, Vector2f blockPixelSize) {
		Map.mapRoomSize = mapRoomSize;
		Map.roomBlockSize = roomBlockSize;
		Map.blockPixelSize = blockPixelSize;
		Map.roomPixelSize = new Vector2f(roomBlockSize.x*blockPixelSize.x,roomBlockSize.y*blockPixelSize.y);
		Map.mapBlockSize = new Vector2f(mapRoomSize.x*roomBlockSize.x,mapRoomSize.y*roomBlockSize.y);
		Map.mapPixelSize = new Vector2f(mapRoomSize.x*roomPixelSize.x,mapRoomSize.y*roomPixelSize.y);
		this.drawRoomPosition = new Vector2f(0,0);
		this.drawRoomDistance = new Vector2f(2,2);
	}

	/**
	 * Is the given position in collision?
	 * @param x the position abscissa
	 * @param y the position ordinate
	 * @return true if the position is in collision else false
	 */
	public boolean testCollision(float x, float y) {
		int roomI = (int) Math.floor(x / (roomPixelSize.x));
		int roomJ = (int) Math.floor(y / (roomPixelSize.y));
		if (roomI < 0 || roomJ < 0 || roomI > Map.roomBlockSize.x-1 || roomJ > Map.roomBlockSize.y-1){
			return true;
		}else{
			if(roomGrid[roomI][roomJ]!=null){
				return roomGrid[roomI][roomJ].testCollision(x-roomPixelSize.x*roomI,y-roomPixelSize.y*roomJ);
			}else{
				return true;
			}
		}
	}

	/**
	 * Get the map spawn point position
	 * @return the spawn point position
	 */
	public Vector2f getSpawnPixelPosition() {
		return spawnPixelPosition;
	}

	/**
	 * Generate the map
	 */
	public void generate() {
		roomGrid = MapGenerator.generate();
	}
	
	/**
	 * Set the draw position (map will be drawn only around this position)
	 * @param pos the position 
	 */
	public void setDrawPosition(Vector2f pos) {
		drawRoomPosition.x = pos.x/Map.roomPixelSize.x;
		drawRoomPosition.y = pos.y/Map.roomPixelSize.y;
		minX = (int)Math.max(0,drawRoomPosition.x - drawRoomDistance.x);
		maxX = (int)Math.min(Map.mapRoomSize.x,drawRoomPosition.x + drawRoomDistance.x+1);
		minY = (int)Math.max(0,drawRoomPosition.y - drawRoomDistance.y);
		maxY = (int)Math.min(Map.mapRoomSize.y,drawRoomPosition.y + drawRoomDistance.y+1);
	}

	/**
	 * Draw the map
	 */
	@Override
	public void draw() {
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if(roomGrid[i][j]!= null){
					roomGrid[i][j].draw();
				}
			}
		}
	}

	/**
	 * Compute shadow casted by the map
	 */
	@Override
	public LinkedList<Shadow> computeShadow(Light light) {
		LinkedList<Shadow> l = new LinkedList<Shadow>();
		
		
		minX = (int)Math.max(0,light.getX()/Map.roomPixelSize.x - drawRoomDistance.x);
		maxX = (int)Math.min(Map.mapRoomSize.x,light.getX()/Map.roomPixelSize.x + drawRoomDistance.x+1);
		minY = (int)Math.max(0,light.getY()/Map.roomPixelSize.y - drawRoomDistance.y);
		maxY = (int)Math.min(Map.mapRoomSize.y,light.getY()/Map.roomPixelSize.y + drawRoomDistance.y+1);
			
		int i;
		int j;
		for (i = minX; i < maxX; i++) {
			for (j = minY; j < maxY; j++) {
				if(roomGrid[i][j]!= null){
					l.addAll(((Room)roomGrid[i][j]).computeShadow(light));
				}
			}
		}
		return l;
	}
	
	public Room[][] getRooms(){
		return roomGrid;
	}
}