package environment;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

import org.lwjgl.util.vector.Vector2f;

import environment.room.Room;
import game.GameLoop;
import rendering.ShadowCaster;

public class Map implements ShadowCaster{
	
	
	public static Vector2f blockPixelSize;
	public static Vector2f roomBlockSize;
	public static Vector2f roomPixelSize;
	public static Vector2f mapRoomSize;
	public static Vector2f mapBlockSize;
	public static Vector2f mapPixelSize;
	public static Vector2f spawnPixelPosition;
	public static Vector2f spawnRoomPosition;
	
	private Room[][] roomGrid;
	
	
	private Vector2f drawRoomPosition;
	private Vector2f drawRoomDistance;

	
	private boolean fullRender = false;
	
	static boolean updateFrameBuffer = true;
	static private int frameBufferID;
	static private int textureID;
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
		this.drawRoomDistance = new Vector2f(0.5f*GameLoop.WIDTH/Map.roomPixelSize.x,0.5f*GameLoop.HEIGHT/Map.roomPixelSize.y);
		generate();
	}

	public void initTexture(){
		frameBufferID = glGenFramebuffers();
		textureID = glGenTextures();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, (int) Map.mapPixelSize.x,
				(int) Map.mapPixelSize.y, 0, GL_RGBA, GL_INT,
				(java.nio.ByteBuffer) null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
				GL_TEXTURE_2D, textureID, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
	}
	
	private void fullRender(){
		int minX = 0;
		int maxX = (int) Map.mapRoomSize.x;
		int minY = 0;
		int maxY = (int) Map.mapRoomSize.y;
		glBegin(GL_QUADS);
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if(roomGrid[i][j]!= null){
					roomGrid[i][j].draw();
				}
			}
		}
		glEnd();
	}
	
	public void renderMapToFrameBuffer(){
		int texSave =  glGetInteger(GL_TEXTURE_BINDING_2D);
		int frameBufferSave =  glGetInteger(GL_FRAMEBUFFER_BINDING);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, (int) Map.mapPixelSize.x, (int) Map.mapPixelSize.y, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, (int) Map.mapPixelSize.x, (int) Map.mapPixelSize.y);
		glPushMatrix();
		glLoadIdentity();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		fullRender();
		glPopMatrix();
		glPopAttrib();
		updateFrameBuffer = false;
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, GameLoop.displayed_x, GameLoop.displayed_y, 0, 1, -1);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferSave);
		glMatrixMode(GL_MODELVIEW);
		glBindTexture(GL_TEXTURE_2D, texSave);
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
		if (roomI < 0 || roomJ < 0 || roomI > Map.mapRoomSize.x-1 || roomJ > Map.mapRoomSize.y-1){
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
		roomGrid[(int)drawRoomPosition.x][(int)drawRoomPosition.y].discover();
	}

	/**
	 * Compute shadow casted by the map
	 */
	@Override
	public LinkedList<Shadow> computeShadow(Light light) {
		LinkedList<Shadow> l = new LinkedList<Shadow>();
		
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		if(fullRender){
			minX = 0;
			maxX = (int) Map.mapRoomSize.x;
			minY = 0;
			maxY = (int) Map.mapRoomSize.y;
			
		}else{
			minX = (int)Math.max(0,light.getX()/Map.roomPixelSize.x - drawRoomDistance.x);
			maxX = (int)Math.min(Map.mapRoomSize.x,light.getX()/Map.roomPixelSize.x + drawRoomDistance.x+1);
			minY = (int)Math.max(0,light.getY()/Map.roomPixelSize.y - drawRoomDistance.y);
			maxY = (int)Math.min(Map.mapRoomSize.y,light.getY()/Map.roomPixelSize.y + drawRoomDistance.y+1);
		}
		
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
	public void setFullRender(boolean full){
		this.fullRender = full;
	}
	public boolean getFullRender(){
		return this.fullRender;
	}
	public boolean isUpdated(){
		return !updateFrameBuffer;
	}
	public static int getTextureID(){
		return textureID;
	}
}