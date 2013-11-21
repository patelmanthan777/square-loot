package environment;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

import org.lwjgl.util.vector.Vector2f;

import environment.blocks.Block;
import environment.blocks.BlockFactory;
import environment.blocks.SolidBlock;
import rendering.Drawable;
import rendering.ShadowCaster;
import rendering.LightTaker;

public class Map implements Drawable, ShadowCaster, LightTaker{
	private int width;
	private int height;
	private float probability = 0.10f;
	private Vector2f halfBlockSize = new Vector2f(20, 20);
	private Block[][] blockGrid;
	private int spawnPoint_x;
	private int spawnPoint_y;
	private Vector2f spawnPosition;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		this.blockGrid = new Block[width][height];
	}

	public boolean testCollision(float x, float y) {
		int x_grid = (int) Math.floor(x / (halfBlockSize.x * 2));
		int y_grid = (int) Math.floor(y / (halfBlockSize.y * 2));
		if (x_grid < 0 || y_grid < 0 || x_grid > height || y_grid > width)
			return true;
		else
			return blockGrid[x_grid][y_grid].testCollision();
	}

	public Vector2f getSpawnPosition() {
		return spawnPosition;
	}

	public void generate() {
		int i;
		int j;
		for (i = 0; i < width; i++) {
			for (j = 0; j < height; j++) {
				if (Math.random() < probability) {
					blockGrid[i][j] = BlockFactory.createSolidBlock(i,j,halfBlockSize);
				} else {
					blockGrid[i][j] = BlockFactory.createEmptyBlock();
				}
			}
		}
		for (i = 0; i < width; i++) {
			blockGrid[i][0] = BlockFactory.createBorderBlock();
			blockGrid[i][height - 1] = BlockFactory.createBorderBlock();
		}
		for (j = 0; j < height; j++) {
			blockGrid[0][j] = BlockFactory.createBorderBlock();
			blockGrid[width - 1][j] = BlockFactory.createBorderBlock();
		}

		spawnPoint_x = (int) (Math.random() * (width - 2) + 1);
		spawnPoint_y = (int) (Math.random() * (height - 2) + 1);
		blockGrid[spawnPoint_x][spawnPoint_y] = BlockFactory.createEmptyBlock();
		spawnPosition = new Vector2f((spawnPoint_x * 2 + 1) * halfBlockSize.x,
				(spawnPoint_y * 2 + 1) * halfBlockSize.y);
	}

	@Override
	public void draw() {

		// draw quad
		int i;
		int j;
		float posX;
		float posY;
		for (i = 0; i < width; i++) {
			for (j = 0; j < width; j++) {
				posX = i * halfBlockSize.x * 2 + halfBlockSize.x;
				posY = j * halfBlockSize.y * 2 + halfBlockSize.y;
				blockGrid[i][j].drawAt(posX, posY, halfBlockSize);
			}
		}
	}


	@Override
	public LinkedList<Shadow> computeShadow(Light light) {
		LinkedList<Shadow> l = new LinkedList<Shadow>();
		for(SolidBlock b : BlockFactory.getSolidBlocks()){
			l.addAll(b.computeShadow(light));
		}
		return l;
	}
}