package environment;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

import org.lwjgl.opengl.Display;
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
	
	private Vector2f drawPosition;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		this.blockGrid = new Block[width][height];
		this.drawPosition = new Vector2f(0,0);
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
					blockGrid[i][j] = BlockFactory.createSolidBlock();
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
	
	
	public void setDrawPosition(Vector2f pos) {
		drawPosition = pos;
	}

	@Override
	public void draw() {

		int minX = Math.max(0,(int)Math.floor((drawPosition.x-Display.getWidth()/2)/(halfBlockSize.x*2)));
		int maxX = Math.min(width,(int)Math.floor((drawPosition.x+Display.getWidth()/2)/(halfBlockSize.x*2))+1);
		int minY = Math.max(0,(int)Math.floor((drawPosition.y-Display.getHeight()/2)/(halfBlockSize.y*2)));
		int maxY = Math.min(height,(int)Math.floor((drawPosition.y+Display.getHeight()/2)/(halfBlockSize.y*2))+1);
		
		// draw quad
		int i;
		int j;
		float posX;
		float posY;
		for (i = minX; i < maxX; i++) {
			for (j = minY; j < maxY; j++) {
				posX = i * halfBlockSize.x * 2 + halfBlockSize.x;
				posY = j * halfBlockSize.y * 2 + halfBlockSize.y;
				blockGrid[i][j].drawAt(posX, posY, halfBlockSize);
			}
		}
	}


	@Override
	public LinkedList<Shadow> computeShadow(Light light) {
		LinkedList<Shadow> l = new LinkedList<Shadow>();
		int minX = Math.max(0,(int)Math.floor((light.getX()-Display.getWidth()/2)/(halfBlockSize.x*2)));
		int maxX = Math.min(width,(int)Math.floor((light.getX()+Display.getWidth()/2)/(halfBlockSize.x*2))+1);
		int minY = Math.max(0,(int)Math.floor((light.getY()-Display.getHeight()/2)/(halfBlockSize.y*2)));
		int maxY = Math.min(height,(int)Math.floor((light.getY()+Display.getHeight()/2)/(halfBlockSize.y*2))+1);
			
		int i;
		int j;
		for (i = minX; i < maxX; i++) {
			for (j = minY; j < maxY; j++) {
				if(blockGrid[i][j].castShadows())
					l.addAll(((SolidBlock) blockGrid[i][j]).computeShadow(light,i,j,halfBlockSize));
			}
		}
		return l;
	}
}