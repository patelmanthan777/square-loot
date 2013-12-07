package environment.room;

import java.util.LinkedList;

import light.Light;
import light.Shadow;
import rendering.Drawable;
import rendering.ShadowCaster;
import environment.Map;
import environment.blocks.Block;
import environment.blocks.BlockFactory;
import environment.blocks.ShadowCasterBlock;

public abstract class Room implements Drawable, ShadowCaster {
	protected Block[][] grid;
	protected float x;
	protected float y;
	protected boolean[] doors = new boolean[4];

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

	public abstract void drawOnMiniMap();

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void createDoor(int wall) {
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
	public LinkedList<Shadow> computeShadow(Light light) {
		LinkedList<Shadow> l = new LinkedList<Shadow>();
		boolean[] neighbours = new boolean[4];
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
					l.addAll(((ShadowCasterBlock) grid[i][j]).computeShadow(light, (int)(x/Map.blockPixelSize.x) + i, (int)(y/Map.blockPixelSize.y) + j, neighbours));
				}
			}
		}
		return l;
	}

}
