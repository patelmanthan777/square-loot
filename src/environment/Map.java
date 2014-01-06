package environment;

import static org.lwjgl.opengl.GL11.*;
import light.Light;
import light.ShadowBuffer;
import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import environment.room.Room;
import rendering.FBO;
import rendering.ShadowCaster;

public class Map implements ShadowCaster {
	public static final int maxLayer = 3;

	/**
	 * Block size in number of pixels.
	 */
	public static Vector2f blockPixelSize;
	/**
	 * Room size in number of block.
	 */
	public static Vector2f roomBlockSize;
	/**
	 * Room size in number of pixels.
	 */
	public static Vector2f roomPixelSize;
	/**
	 * Map size in number of room.
	 */
	public static Vector2f mapRoomSize;
	/**
	 * Map size in number of room.
	 */
	public static Vector2f mapBlockSize;
	/**
	 * Map size in number of pixels.
	 */
	public static Vector2f mapPixelSize;
	/**
	 * Spawn coordinates in term of pixels.
	 */
	public static Vector2f spawnPixelPosition;
	/**
	 * Spawn coordinates in term of rooms.
	 */
	public static Vector2f spawnRoomPosition;

	/**
	 * Map representation as a matrix.
	 */
	private Room[][] roomGrid;

	/**
	 * Concretely the position of the player in the room, more generally the
	 * position on which the screen should be centered.
	 */
	private Vector2f drawRoomPosition;
	/**
	 * The distance from which the map is seen, it is needed to optimize the
	 * shadows drawing.
	 */
	private Vector2f drawRoomDistance;

	/**
	 * FBO stands for <i>Frame Buffer Object</i> it is a rendered view of the
	 * map stored for performance purposes. There is one FBO per layer.
	 */
	public static FBO[] mapFBO = new FBO[maxLayer];
	
	private static boolean mapFBOIsUpdated = false;
	/**
	 * <b>true</b> if the full map needs to be rendered, <b>false</b> otherwise.
	 */
	private boolean fullRender = false;

	public Map(Vector2f mapRoomSize, Vector2f roomBlockSize,
			Vector2f blockPixelSize) {
		Map.mapRoomSize = mapRoomSize;
		Map.roomBlockSize = roomBlockSize;
		Map.blockPixelSize = blockPixelSize;

		Map.roomPixelSize = new Vector2f(roomBlockSize.x * blockPixelSize.x,
				roomBlockSize.y * blockPixelSize.y);
		Map.mapBlockSize = new Vector2f(mapRoomSize.x * roomBlockSize.x,
				mapRoomSize.y * roomBlockSize.y);
		Map.mapPixelSize = new Vector2f(mapRoomSize.x * roomPixelSize.x,
				mapRoomSize.y * roomPixelSize.y);
		this.drawRoomPosition = new Vector2f(0, 0);
		this.drawRoomDistance = new Vector2f(ConfigManager.resolution.x
				/ Map.roomPixelSize.x, ConfigManager.resolution.y
				/ Map.roomPixelSize.y);
		for (int i = 0; i < maxLayer; i++) {
			mapFBO[i] = new FBO((int)Map.mapPixelSize.x,(int)Map.mapPixelSize.y);
		}

		generate();
	}

	/**
	 * Render the full map.
	 */
	/*private void fullRender() {
		int minX = 0;
		int maxX = (int) Map.mapRoomSize.x;
		int minY = 0;
		int maxY = (int) Map.mapRoomSize.y;
		glBegin(GL_QUADS);
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (roomGrid[i][j] != null) {
					roomGrid[i][j].draw();
				}
			}
		}
		glEnd();
	}*/

	private void fullRender(int layer){
		int minX = 0;
		int maxX = (int) Map.mapRoomSize.x;
		int minY = 0;
		int maxY = (int) Map.mapRoomSize.y;
		glBegin(GL_QUADS);
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (roomGrid[i][j] != null) {
					roomGrid[i][j].draw(layer);
				}
			}
		}
		glEnd();
	}
	
	/**
	 * Compute a full map render and stores it in mapFBO
	 */
	public void renderMapToFrameBuffers() {
		for (int i = 0; i < maxLayer; i++) {
			mapFBO[i].bind();
			glPushMatrix();
			glLoadIdentity();
			

			fullRender(i);
			glPopMatrix();
			
			mapFBO[i].unbind();
			
		}
		mapFBOIsUpdated = true;
	}

	/**
	 * Test whether the coordinates are inside the map boundaries.
	 * 
	 * @param x
	 *            the horizontal position
	 * @param y
	 *            the vertical position
	 * @return <b>true</b> if the position is in collision, <b>false</b>
	 *         otherwise
	 */
	public boolean testCollision(float x, float y) {
		int roomI = (int) Math.floor(x / (roomPixelSize.x));
		int roomJ = (int) Math.floor(y / (roomPixelSize.y));
		if (roomI < 0 || roomJ < 0 || roomI > Map.mapRoomSize.x - 1
				|| roomJ > Map.mapRoomSize.y - 1) {
			return true;
		} else {
			if (roomGrid[roomI][roomJ] != null) {
				return roomGrid[roomI][roomJ].testCollision(x - roomPixelSize.x
						* roomI, y - roomPixelSize.y * roomJ);
			} else {
				return true;
			}
		}
	}

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
	 * Set the draw position and update the minimap display.
	 * 
	 * @param pos
	 *            the position
	 */
	public void setDrawPosition(Vector2f pos) {
		drawRoomPosition.x = pos.x/Map.roomPixelSize.x;
		drawRoomPosition.y = pos.y/Map.roomPixelSize.y;
		roomGrid[(int)drawRoomPosition.x][(int)drawRoomPosition.y].discover();
	}

	/**
	 * Compute the shadows casted by the map
	 */
	@Override
	public void computeShadow(Light light, ShadowBuffer[] shadows) {
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		int roomPosiX = (int) (light.getX() / Map.roomPixelSize.x);
		int roomPosiY = (int) (light.getY() / Map.roomPixelSize.y);
		
		System.out.println("x : " + roomPosiX);
		System.out.println("y : " + roomPosiY);
		System.out.println(light);
		if (fullRender) {
			minX = 0;
			maxX = (int) Map.mapRoomSize.x;
			minY = 0;
			maxY = (int) Map.mapRoomSize.y;

		} else {
			minX = 0;
			maxX = (int) Map.mapRoomSize.x;
			minY = 0;
			maxY = (int) Map.mapRoomSize.y;
			/*
			minX = (int) Math.max(0, roomPosiX - drawRoomDistance.x);
			maxX = (int) Math.min(Map.mapRoomSize.x, roomPosiX
					+ drawRoomDistance.x + 1);
			minY = (int) Math.max(0, roomPosiY - drawRoomDistance.y);
			maxY = (int) Math.min(Map.mapRoomSize.y, roomPosiY
					+ drawRoomDistance.y + 1);*/
		}

		int i;
		int j;
		for (i = minX; i < maxX; i++) {
			if (roomGrid[i][roomPosiY] != null) {
				roomGrid[i][roomPosiY].computeShadow(light, shadows);
			}
		}
		for (j = minY; j < maxY; j++) {
			if (roomGrid[roomPosiX][j] != null) {
				roomGrid[roomPosiX][j].computeShadow(light, shadows);
			}
		}
	}

	public Room[][] getRooms() {
		return roomGrid;
	}

	public void setFullRender(boolean full) {
		this.fullRender = full;
	}

	public boolean getFullRender() {
		return this.fullRender;
	}

	public boolean isUpdated() {
		return (mapFBOIsUpdated);
	}

	public static int getTextureID(int layer) {
		return mapFBO[layer].getTextureID();
	}
}