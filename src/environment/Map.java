package environment;

import static org.lwjgl.opengl.GL11.*;
import light.Light;
import light.LightManager;
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


	public static int textureSize;
	public static final int textureNb = 5;
	public static Vector2f currentBufferPosition;
	private static boolean shouldBeRendered[][] = new boolean[textureNb][textureNb];
	private static int indx = 0;
	private static int indy = 0;
	/**
	 * FBO stands for <i>Frame Buffer Object</i> it is a rendered view of the
	 * map stored for performance purposes. There is one FBO per layer.
	 */
	public static FBO[][][] mapFBO = new FBO[textureNb][textureNb][maxLayer];

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

		Map.textureSize = (int) Math.max(ConfigManager.resolution.x,
				ConfigManager.resolution.y) / (textureNb - 2);
		for (int layer = 0; layer < maxLayer; layer++) {
			for (int i = 0; i < Map.textureNb; i++) {
				for (int j = 0; j < Map.textureNb; j++) {
					mapFBO[i][j][layer] = new FBO(textureSize, textureSize);
				}
			}
		}
		for (int i = 0; i < textureNb; i++) {
			for (int j = 0; j < textureNb; j++) {
				shouldBeRendered[i][j] = true;
			}
		}
		generate();
		currentBufferPosition = new Vector2f(spawnPixelPosition.x
				- (Map.textureNb / 2.0f) * textureSize, spawnPixelPosition.y
				- (Map.textureNb / 2.0f) * textureSize);
	}

	private void render(int i, int j, int layer) {
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
				}
			}
		}
		glEnd();
	}

	/**
	 * Compute a full map render and stores it in mapFBO
	 */
	public void renderMapToFrameBuffers() {
		for (int i = 0; i < textureNb; i++) {
			for (int j = 0; j < textureNb; j++) {
				if (shouldBeRendered[i][j]) {
					shouldBeRendered[i][j] = false;
					for (int layer = 0; layer < maxLayer; layer++) {

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
					}
				}
			}
		}
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
		drawRoomPosition.x = pos.x / Map.roomPixelSize.x;
		drawRoomPosition.y = pos.y / Map.roomPixelSize.y;
		roomGrid[(int) drawRoomPosition.x][(int) drawRoomPosition.y].discover();

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
				shouldBeRendered[0][i] = true;
			}
		} else if (translateMapFBOx == 1) {
			currentBufferPosition.x += Map.textureSize;

			for (int i = 0; i < textureNb; i++) {
				shouldBeRendered[textureNb - 1][i] = true;
			}
			indx = (indx + 1 + textureNb) % textureNb;
		}
		if (translateMapFBOy == -1) {
			currentBufferPosition.y -= Map.textureSize;
			indy = (indy - 1 + textureNb) % textureNb;
			for (int i = 0; i < textureNb; i++) {
				shouldBeRendered[i][0] = true;
			}
		} else if (translateMapFBOy == 1) {
			currentBufferPosition.y += Map.textureSize;
			for (int i = 0; i < textureNb; i++) {
				shouldBeRendered[i][textureNb - 1] = true;
			}
			indy = (indy + 1 + textureNb) % textureNb;
		}
		LightManager.needStaticUpdate(translateMapFBOx, translateMapFBOy);

	}

	/**
	 * Compute the shadows casted by the map
	 */
	@Override
	public void computeShadow(Light light, ShadowBuffer[] shadows) {
		int roomPosiX = (int) (light.getX() / Map.roomPixelSize.x);
		int roomPosiY = (int) (light.getY() / Map.roomPixelSize.y);

		int minX = (int) Math.max(0, (light.getX() - light.getMaxDst())/Map.roomPixelSize.x);
		int maxX = (int) Math.min(Map.mapRoomSize.x-1, (light.getX() + light.getMaxDst())/Map.roomPixelSize.x);
		int minY = (int) Math.max(0, (light.getY() - light.getMaxDst())/Map.roomPixelSize.y);
		int maxY = (int) Math.min(Map.mapRoomSize.y-1, (light.getY() + light.getMaxDst())/Map.roomPixelSize.y + 1);

		int i;
		int j;
		for (i = minX; i <= maxX; i++) {
			if (roomGrid[i][roomPosiY] != null) {
				roomGrid[i][roomPosiY].computeShadow(light, shadows);
			}
		}
		for (j = minY; j <= maxY; j++) {
			if (roomGrid[roomPosiX][j] != null) {
				roomGrid[roomPosiX][j].computeShadow(light, shadows);
			}
		}
	}

	public Room[][] getRooms() {
		return roomGrid;
	}

	public boolean isUpdated(int i, int j) {
		return (shouldBeRendered[i][j]);
	}

	public static FBO getFBO(int i, int j, int layer) {
		return mapFBO[(i + indx) % textureNb][(j + indy) % textureNb][layer];
	}

	public static int getTextureID(int i, int j, int layer) {
		return getFBO(i, j, layer).getTextureID();
	}
}