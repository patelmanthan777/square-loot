package environment.room;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import light.Light;
import light.ShadowBuffer;
import rendering.Drawable;
import rendering.ShadowCaster;
import userInterface.MiniMap;
import environment.Map;
import environment.blocks.Block;
import environment.blocks.BlockFactory;
import environment.blocks.ShadowCasterBlock;

public abstract class Room implements Drawable, ShadowCaster {
	protected Block[][] grid;
	/**
	 * Horizontal index of the room on the map, in pixels.
	 */
	protected float x;
	/**
	 * Vertical index of the room on the map, in pixels.
	 */
	protected float y;
	protected Vector3f miniMapColor = new Vector3f(1, 1, 1);
	protected boolean[] doors = new boolean[4];
	protected boolean discovered = false;
	/* avoid dynamic allocation in computeShadow */
	boolean[] neighbours = new boolean[4];
	/* ------------------------------------------*/
	
	public Room(float posX, float posY) {
		this.x = posX;
		this.y = posY;
		grid = new Block[(int) Map.roomBlockSize.x][(int) Map.roomBlockSize.y];
		for (int i = 0; i < 4; i++) {
			doors[i] = false;
		}
		construct();
	}

	protected abstract void construct();

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	/**
	 * Create door on the given wall, clockwise indices starting at noon.
	 *  
	 * @param wall is the index of the wall
	 */
	public void createDoor(int wall) {
		doors[wall] = true;
		if (wall == 0) {
			grid[(int) Map.roomBlockSize.x / 2 - 2][0] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 1][0] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 0][0] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 1][0] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 2][1] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 1][1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 0][1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 1][1] = BlockFactory
					.createSolidBlock();
		}
		if (wall == 1) {
			grid[(int) Map.roomBlockSize.x - 1][(int) Map.roomBlockSize.y / 2 - 2] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x - 1][(int) Map.roomBlockSize.y / 2 - 1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x - 1][(int) Map.roomBlockSize.y / 2 + 0] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x - 1][(int) Map.roomBlockSize.y / 2 + 1] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x - 2][(int) Map.roomBlockSize.y / 2 - 2] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x - 2][(int) Map.roomBlockSize.y / 2 - 1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x - 2][(int) Map.roomBlockSize.y / 2 + 0] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x - 2][(int) Map.roomBlockSize.y / 2 + 1] = BlockFactory
					.createSolidBlock();
		}
		if (wall == 2) {
			grid[(int) Map.roomBlockSize.x / 2 - 2][(int) Map.roomBlockSize.y - 1] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 1][(int) Map.roomBlockSize.y - 1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 0][(int) Map.roomBlockSize.y - 1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 1][(int) Map.roomBlockSize.y - 1] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 2][(int) Map.roomBlockSize.y - 2] = BlockFactory
					.createSolidBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 1][(int) Map.roomBlockSize.y - 2] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 0][(int) Map.roomBlockSize.y - 2] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 1][(int) Map.roomBlockSize.y - 2] = BlockFactory
					.createSolidBlock();
		}
		if (wall == 3) {
			grid[0][(int) Map.roomBlockSize.y / 2 - 2] = BlockFactory
					.createSolidBlock();
			grid[0][(int) Map.roomBlockSize.y / 2 - 1] = BlockFactory
					.createEmptyBlock();
			grid[0][(int) Map.roomBlockSize.y / 2 + 0] = BlockFactory
					.createEmptyBlock();
			grid[0][(int) Map.roomBlockSize.y / 2 + 1] = BlockFactory
					.createSolidBlock();
			grid[1][(int) Map.roomBlockSize.y / 2 - 2] = BlockFactory
					.createSolidBlock();
			grid[1][(int) Map.roomBlockSize.y / 2 - 1] = BlockFactory
					.createEmptyBlock();
			grid[1][(int) Map.roomBlockSize.y / 2 + 0] = BlockFactory
					.createEmptyBlock();
			grid[1][(int) Map.roomBlockSize.y / 2 + 1] = BlockFactory
					.createSolidBlock();
		}
	}

	public boolean testCollision(float x, float y) {
		int blockI = (int) (x / Map.blockPixelSize.x);
		int blockJ = (int) (y / Map.blockPixelSize.y);
		return grid[blockI][blockJ].testCollision();
	}

	@Override
	public void draw() {
		for (int i = 0; i < Map.roomBlockSize.x; i++) {
			for (int j = 0; j < Map.roomBlockSize.y; j++) {
				float posX = x + i * Map.blockPixelSize.x;
				float posY = y + j * Map.blockPixelSize.y;
				grid[i][j].drawAt(posX, posY);
			}
		}
	}

	@Override
	public void computeShadow(Light light,ShadowBuffer shadows) {
		
		
		for (int i = 0; i < Map.roomBlockSize.x; i++) {
			for (int j = 0; j < Map.roomBlockSize.y; j++) {
				if (grid[i][j].castShadows()) {
					if (j == 0) {
						neighbours[0] = true;
					} else {
						neighbours[0] = grid[i][j - 1].castShadows();
					}
					if (i == 0) {
						neighbours[3] = true;
					} else {
						neighbours[3] = grid[i - 1][j].castShadows();
					}
					if (i == Map.roomBlockSize.x - 1) {
						neighbours[1] = true;
					} else {
						neighbours[1] = grid[i + 1][j].castShadows();
					}
					if (j == Map.roomBlockSize.y - 1) {
						neighbours[2] = true;
					} else {
						neighbours[2] = grid[i][j + 1].castShadows();
					}
					((ShadowCasterBlock) grid[i][j]).computeShadow(
							light, (int) (x / Map.blockPixelSize.x) + i,
							(int) (y / Map.blockPixelSize.y) + j, neighbours, shadows);
				}
			}
		}
	}

	/**
	 * Draw the room on the minimap if discovered.
	 */
	public void drawOnMiniMap() {
		if (discovered) {
			int minix = (int) (MiniMap.position.x + (x / Map.roomPixelSize.x)
					* MiniMap.roomSize.x);
			int miniy = (int) (MiniMap.position.y + (y / Map.roomPixelSize.y)
					* MiniMap.roomSize.y);
			float doorRatio = 0.1f;
			glDisable(GL_BLEND);
			glColor3f(miniMapColor.x, miniMapColor.y, miniMapColor.z);
			glLoadIdentity();
			if (doors[0]) {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x + doorRatio
						* MiniMap.roomSize.x, miniy + doorRatio
						* MiniMap.roomSize.y);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x + doorRatio
						* MiniMap.roomSize.x, miniy);
				glEnd();
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x - doorRatio
						* MiniMap.roomSize.x, miniy);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x - doorRatio
						* MiniMap.roomSize.x, miniy + doorRatio
						* MiniMap.roomSize.y);
				glVertex2f(minix + doorRatio * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glEnd();
			} else {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glVertex2f(minix + doorRatio * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glEnd();
			}
			if (doors[1]) {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ (0.5f - doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix + MiniMap.roomSize.x, miniy
						+ (0.5f - doorRatio) * MiniMap.roomSize.y);
				glEnd();
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + MiniMap.roomSize.x, miniy
						+ (0.5f + doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ (0.5f + doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glEnd();
			} else {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glEnd();
			}
			if (doors[2]) {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x + doorRatio
						* MiniMap.roomSize.x, miniy + (1 - doorRatio)
						* MiniMap.roomSize.y);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x + doorRatio
						* MiniMap.roomSize.x, miniy + MiniMap.roomSize.y);
				glEnd();
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x - doorRatio
						* MiniMap.roomSize.x, miniy + MiniMap.roomSize.y);
				glVertex2f(minix + 0.5f * MiniMap.roomSize.x - doorRatio
						* MiniMap.roomSize.x, miniy + (1 - doorRatio)
						* MiniMap.roomSize.y);
				glVertex2f(minix + doorRatio * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glEnd();
			} else {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (1 - doorRatio) * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix + doorRatio * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glEnd();
			}
			if (doors[3]) {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (doorRatio) * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glVertex2f(minix + (doorRatio) * MiniMap.roomSize.x, miniy
						+ (0.5f - doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix, miniy + (0.5f - doorRatio)
						* MiniMap.roomSize.y);
				glEnd();
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix, miniy + (0.5f + doorRatio)
						* MiniMap.roomSize.y);
				glVertex2f(minix + (doorRatio) * MiniMap.roomSize.x, miniy
						+ (0.5f + doorRatio) * MiniMap.roomSize.y);
				glVertex2f(minix + (doorRatio) * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glEnd();
			} else {
				glBegin(GL_LINE_STRIP);
				glVertex2f(minix + (doorRatio) * MiniMap.roomSize.x, miniy
						+ doorRatio * MiniMap.roomSize.y);
				glVertex2f(minix + (doorRatio) * MiniMap.roomSize.x, miniy
						+ (1 - doorRatio) * MiniMap.roomSize.y);
				glEnd();
			}
		}
	}
	
	public void discover(){
		discovered = true;
	} 
}
