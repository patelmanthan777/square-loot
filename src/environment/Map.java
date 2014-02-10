package environment;

import static org.lwjgl.opengl.GL11.*;
import light.Light;
import light.PointLight;
import light.Laser;
import light.LightManager;
import light.ShadowBuffer;
import org.lwjgl.util.vector.Vector2f;
import configuration.ConfigManager;
import environment.blocks.Block;
import environment.room.Room;
import rendering.FBO;
import rendering.ShadowCaster;

public class Map implements ShadowCaster {
	public static final int maxLayer = 2;

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

	/* Avoid dynamic allocation */
	Vector2f cpos = new Vector2f();
	/* ************************* */
	private static int neighboorPressureCoef = 1;
	private static int ownPressureCoef = 500;
	public static int textureSize;
	public static final int textureNb = 5;
	public static int doorLayer = 0;
	public static Vector2f currentBufferPosition;
	private static boolean shouldBeRendered[][][] = new boolean[textureNb][textureNb][maxLayer];
	private static int indx = 0;
	private static int indy = 0;
	/**
	 * FBO stands for <i>Frame Buffer Object</i> it is a rendered view of the
	 * map stored for performance purposes. There is one FBO per layer.
	 */
	public static FBO[][][] mapFBO = new FBO[textureNb][textureNb][maxLayer];

	public Map(int n, Vector2f mapRoomSize, Vector2f roomBlockSize,
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

		Map.textureSize = (int) (Math.max(ConfigManager.resolution.x,
				ConfigManager.resolution.y) / (textureNb - 2));
		for (int layer = 0; layer < maxLayer; layer++) {
			for (int i = 0; i < Map.textureNb; i++) {
				for (int j = 0; j < Map.textureNb; j++) {
					mapFBO[i][j][layer] = new FBO(textureSize, textureSize);
				}
			}
		}
		for (int i = 0; i < textureNb; i++) {
			for (int j = 0; j < textureNb; j++) {
				for (int layer = 0; layer < maxLayer; layer++) {
					shouldBeRendered[i][j][layer] = true;
				}
			}
		}
		roomGrid = new Room[(int) Map.mapRoomSize.x][(int) Map.mapRoomSize.y];
		generate(n);
		currentBufferPosition = new Vector2f(
				(int) (spawnPixelPosition.x - (Map.textureNb / 2.0f)
						* textureSize),
				(int) (spawnPixelPosition.y - (Map.textureNb / 2.0f)
						* textureSize));
	}

	public void render(int i, int j, int layer) {
		int minX = (int) Math.max(0, ((Map.currentBufferPosition.x + i
				* Map.textureSize) / Map.roomPixelSize.x));
		int maxX = (int) Math
				.min(Map.mapRoomSize.x, ((Map.currentBufferPosition.x + (i + 1)
						* Map.textureSize) / Map.roomPixelSize.x) + 1);
		int minY = (int) Math.max(0, ((Map.currentBufferPosition.y + j
				* Map.textureSize) / Map.roomPixelSize.y));
		int maxY = (int) Math
				.min(Map.mapRoomSize.y, ((Map.currentBufferPosition.y + (j + 1)
						* Map.textureSize) / Map.roomPixelSize.y) + 1);

		glBegin(GL_QUADS);
		for (int k = minX; k < maxX; k++) {
			for (int l = minY; l < maxY; l++) {
				if (roomGrid[k][l] != null) {
					roomGrid[k][l].draw(layer);
					roomGrid[k][l].setRenderUpdated(true, layer);
				}
			}
		}
		glEnd();
	}

	/**
	 * Compute a full map render and stores it in mapFBO
	 */
	public void renderMapToFrameBuffers() {
		checkRoomsUpdate();
		for (int i = 0; i < textureNb; i++) {
			for (int j = 0; j < textureNb; j++) {
				for (int layer = 0; layer < maxLayer; layer++) {
					if (shouldBeRendered[i][j][layer]) {
						shouldBeRendered[i][j][layer] = false;
						getFBO(i, j, layer).bind();
						glPushMatrix();
						glLoadIdentity();
						glTranslatef(
								-(currentBufferPosition.x + i * Map.textureSize),
								-(currentBufferPosition.y + j * Map.textureSize),
								0);
						render(i, j, layer);
						glPopMatrix();
						getFBO(i, j, layer).unbind();
						LightManager.needFBOUpdate(i, j);
					}
				}
			}
		}
	}

	private void checkRoomsUpdate() {
		int minX;
		int maxX;
		int minY;
		int maxY;
		for (int i = 0; i < textureNb; i++) {
			for (int j = 0; j < textureNb; j++) {

				minX = (int) Math.max(0, ((Map.currentBufferPosition.x + i
						* Map.textureSize) / Map.roomPixelSize.x));
				maxX = (int) Math.min(Map.mapRoomSize.x,
						((Map.currentBufferPosition.x + (i + 1)
								* Map.textureSize) / Map.roomPixelSize.x) + 1);
				minY = (int) Math.max(0, ((Map.currentBufferPosition.y + j
						* Map.textureSize) / Map.roomPixelSize.y));
				maxY = (int) Math.min(Map.mapRoomSize.y,
						((Map.currentBufferPosition.y + (j + 1)
								* Map.textureSize) / Map.roomPixelSize.y) + 1);

				for (int k = minX; k < maxX; k++) {
					for (int l = minY; l < maxY; l++) {
						if (roomGrid[k][l] != null) {
							for (int layer = 0; layer < maxLayer; layer++) {
								if (!roomGrid[k][l].renderIsUpdated(layer)) {
									shouldBeRendered[i][j][layer] = true;
									
								}
							}
						}
					}
				}
			}
		}
	}

	public Vector2f getSpawnPixelPosition() {
		return spawnPixelPosition;
	}
	
	public Vector2f getSpawnPosition() {
		return  new Vector2f(spawnPixelPosition.x / ConfigManager.unitPixelSize,
				spawnPixelPosition.y / ConfigManager.unitPixelSize );
	}

	/**
	 * Generate the map
	 */
	public void generate(int n) {
		MapGenerator.generate(this, n);
	}

	/**
	 * Set the draw position and update the minimap display.
	 * 
	 * @param pos
	 *            the position
	 */
	public void setDrawPosition(Vector2f pos) {
		drawRoomPosition.x = pos.x / Map.roomPixelSize.x;
		drawRoomPosition.y = pos.y / Map.roomPixelSize.y;

		roomGrid[(int) drawRoomPosition.x][(int) drawRoomPosition.y].discover();
		Sas[] doors = roomGrid[(int) drawRoomPosition.x][(int) drawRoomPosition.y]
				.getSas();
		for (int i = 0; i < 4; i++) {
			if (doors[i] != null)
				doors[i].open();
		}
		int translateMapFBOx = 0;
		int translateMapFBOy = 0;
		if (pos.x - ConfigManager.resolution.x / 2 < currentBufferPosition.x)
			translateMapFBOx = -1;
		else if (pos.x + ConfigManager.resolution.x / 2 > currentBufferPosition.x
				+ Map.textureNb * Map.textureSize)
			translateMapFBOx = 1;

		if (pos.y - ConfigManager.resolution.y / 2 < currentBufferPosition.y)
			translateMapFBOy = -1;
		else if (pos.y + ConfigManager.resolution.y / 2 > currentBufferPosition.y
				+ Map.textureNb * Map.textureSize)
			translateMapFBOy = 1;

		if (translateMapFBOx == -1) {
			currentBufferPosition.x -= Map.textureSize;
			indx = (indx - 1 + textureNb) % textureNb;
			for (int i = 0; i < textureNb; i++) {
				for (int layer = 0; layer < maxLayer; layer++) {
				shouldBeRendered[0][i][layer] = true;
				}
			}
		} else if (translateMapFBOx == 1) {
			currentBufferPosition.x += Map.textureSize;

			for (int i = 0; i < textureNb; i++) {
				for (int layer = 0; layer < maxLayer; layer++) {
				shouldBeRendered[textureNb - 1][i][layer] = true;
				}
			}
			indx = (indx + 1 + textureNb) % textureNb;
		}
		if (translateMapFBOy == -1) {
			currentBufferPosition.y -= Map.textureSize;
			indy = (indy - 1 + textureNb) % textureNb;
			for (int i = 0; i < textureNb; i++) {
				for (int layer = 0; layer < maxLayer; layer++) {
				shouldBeRendered[i][0][layer] = true;
			}}
				
		} else if (translateMapFBOy == 1) {
			currentBufferPosition.y += Map.textureSize;
			for (int i = 0; i < textureNb; i++) {
				for (int layer = 0; layer < maxLayer; layer++) {
				shouldBeRendered[i][textureNb - 1][layer] = true;
				}
			}
			indy = (indy + 1 + textureNb) % textureNb;
		}
		LightManager.needUpdate(translateMapFBOx, translateMapFBOy);

	}

	public void laserIntersect(Laser l) {
		l.setIntersection(null);
		cpos.x = l.getX();
		cpos.y = l.getY();
		while (l.getIntersection() == null) {
			int idxX = (int) (cpos.x / Map.roomPixelSize.x);
			int idxY = (int) (cpos.y / Map.roomPixelSize.y);
			if (roomGrid[idxX][idxY] != null)
				roomGrid[idxX][idxY].laserIntersect(l, cpos);
		}
	}

	/**
	 * Compute the shadows casted by the map
	 */
	@Override
	public void computeShadow(Light l, ShadowBuffer[] shadows) {
		if (l instanceof PointLight) {
			PointLight light = (PointLight) l;
			int roomPosiX = (int) (l.getX() / Map.roomPixelSize.x);
			int roomPosiY = (int) (l.getY() / Map.roomPixelSize.y);

			int minX = (int) Math.max(0, (light.getX() - light.getMaxDst())
					/ Map.roomPixelSize.x);
			int maxX = (int) Math.min(Map.mapRoomSize.x - 1,
					(light.getX() + light.getMaxDst()) / Map.roomPixelSize.x);
			int minY = (int) Math.max(0, (light.getY() - light.getMaxDst())
					/ Map.roomPixelSize.y);
			int maxY = (int) Math.min(Map.mapRoomSize.y - 1,
					(light.getY() + light.getMaxDst()) / Map.roomPixelSize.y);

			int i;
			int j;
			for (i = minX; i <= maxX; i++) {
				if (roomGrid[i][roomPosiY] != null) {
					roomGrid[i][roomPosiY].computeShadow(l, shadows);
				}
			}
			for (j = minY; j <= maxY; j++) {
				if (roomGrid[roomPosiX][j] != null) {
					roomGrid[roomPosiX][j].computeShadow(l, shadows);
				}
			}
		}
	}

	public void update(long delta) {
		for (int frame = 0; frame < delta; frame++) {
			// COMPUTE THE NEW PRESSURES
			for (int i = 0; i < Map.mapRoomSize.x; i++) {
				for (int j = 0; j < Map.mapRoomSize.y; j++) {
					if (roomGrid[i][j] != null) {
						updatePressure(i, j);
					}
				}
			}

			// REPLACE OLD PRESSURES WITH THE NEW PRESSURES
			for (int i = 0; i < Map.mapRoomSize.x; i++) {
				for (int j = 0; j < Map.mapRoomSize.y; j++) {
					if (roomGrid[i][j] != null) {
						if (roomGrid[i][j] != null) {
							roomGrid[i][j].update(delta);
						}
					}
				}
			}
		}
	}

	/**
	 * Update the pressure of the block of index (i,j) in the map
	 * 
	 * @param i
	 *            abcisse in blocks
	 * @param j
	 *            ordinate in blocks
	 */
	private void updatePressure(int i, int j) {
		if (roomGrid[i][j] != null) {
			float pressure = 0;
			float coef = 0;
			Room room;
			Room mainRoom = roomGrid[i][j];
			coef += ownPressureCoef;
			pressure += mainRoom.getPressure() * ownPressureCoef;

			if (i - 1 >= 0 && mainRoom.getDoors()[3] != null
					&& mainRoom.getSas()[3].isOpened()) {
				room = roomGrid[i - 1][j];
				if (room != null) {
					coef += neighboorPressureCoef;
					pressure += room.getPressure() * neighboorPressureCoef;
				}
			}
			if (j - 1 >= 0 && mainRoom.getDoors()[0] != null
					&& mainRoom.getSas()[0].isOpened()) {
				room = roomGrid[i][j - 1];
				if (room != null) {
					coef += neighboorPressureCoef;
					pressure += room.getPressure() * neighboorPressureCoef;
				}
			}
			if (i + 1 < Map.mapRoomSize.x && mainRoom.getDoors()[1] != null
					&& mainRoom.getSas()[1].isOpened()) {
				room = roomGrid[i + 1][j];
				if (room != null) {
					coef += neighboorPressureCoef;
					pressure += room.getPressure() * neighboorPressureCoef;
				}
			}
			if (j + 1 < Map.mapRoomSize.y && mainRoom.getDoors()[2] != null
					&& mainRoom.getSas()[2].isOpened()) {
				room = roomGrid[i][j + 1];
				if (room != null) {
					coef += neighboorPressureCoef;
					pressure += room.getPressure() * neighboorPressureCoef;
				}
			}

			pressure /= coef;
			roomGrid[i][j].setNewPressure(pressure);
		}
	}

	public Room[][] getRooms() {
		return roomGrid;
	}

	public Room getRoom(float x, float y){
		return roomGrid[(int) (x / roomBlockSize.x)]
					   [(int) (y / roomBlockSize.y)];
	}
	
	public static FBO getFBO(int i, int j, int layer) {
		return mapFBO[(i + indx) % textureNb][(j + indy) % textureNb][layer];
	}

	public static int getTextureID(int i, int j, int layer) {
		return getFBO(i, j, layer).getTextureID();
	}

	public void initPhysics() {
		for(Room[] roomArray : roomGrid)
		{
			for(Room r : roomArray)
			{
				if(r != null)
				{
					r.initPhysics();
				}
			}
		}
		
	}

	public Block getBlock(int i, int j) {
		if (i < 0 || j < 0) {
			return null;
		}
		int k = (int) (i / Map.roomBlockSize.x);
		int l = (int) (j / Map.roomBlockSize.y);
		if (roomGrid[k][l] != null) {
			int m = (int) (i - k * Map.roomBlockSize.x);
			int n = (int) (j - l * Map.roomBlockSize.y);
			return roomGrid[k][l].getBlock(m, n);
		} else {
			return null;
		}
	}
}
