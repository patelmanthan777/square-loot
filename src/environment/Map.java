package environment;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import environment.blocks.Block;
import environment.blocks.SolidBlock;
import rendering.Drawable;
import rendering.ShadowCaster;
import rendering.LightTaker;

public class Map implements Drawable, ShadowCaster, LightTaker{
	private int size;
	private Vector2f halfBlockSize = new Vector2f(20, 20);
	private Block[][] blockGrid;
	private Vector2f spawnPosition;
	
	private Vector2f drawPosition;

	public Map(int size) {
		this.size = size;
		this.drawPosition = new Vector2f(0,0);
	}

	public boolean testCollision(float x, float y) {
		int x_grid = (int) Math.floor(x / (halfBlockSize.x * 2));
		int y_grid = (int) Math.floor(y / (halfBlockSize.y * 2));
		if (x_grid < 0 || y_grid < 0 || x_grid > size-1 || y_grid > size-1)
			return true;
		else
			return blockGrid[x_grid][y_grid].testCollision();
	}

	public Vector2f getSpawnPosition() {
		return spawnPosition;
	}

	public void generate() {
		blockGrid = MapGenerator.generate(size,(int)(2*halfBlockSize.x));
		spawnPosition = MapGenerator.getSpawn();
		spawnPosition.x *= halfBlockSize.x * 2;
		spawnPosition.x += halfBlockSize.x;
		spawnPosition.y *= halfBlockSize.y * 2;
		spawnPosition.y += halfBlockSize.y;
		
	}
	
	
	public void setDrawPosition(Vector2f pos) {
		drawPosition = pos;
	}

	@Override
	public void draw() {

		int minX = Math.max(0,(int)Math.floor((drawPosition.x-Display.getWidth()/2)/(halfBlockSize.x*2)));
		int maxX = Math.min(size,(int)Math.floor((drawPosition.x+Display.getWidth()/2)/(halfBlockSize.x*2))+1);
		int minY = Math.max(0,(int)Math.floor((drawPosition.y-Display.getHeight()/2)/(halfBlockSize.y*2)));
		int maxY = Math.min(size,(int)Math.floor((drawPosition.y+Display.getHeight()/2)/(halfBlockSize.y*2))+1);
		
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
		boolean [] neighbours = new boolean[4];
		int minX = Math.max(0,(int)Math.floor((light.getX()-Display.getWidth()/2)/(halfBlockSize.x*2)));
		int maxX = Math.min(size,(int)Math.floor((light.getX()+Display.getWidth()/2)/(halfBlockSize.x*2))+1);
		int minY = Math.max(0,(int)Math.floor((light.getY()-Display.getHeight()/2)/(halfBlockSize.y*2)));
		int maxY = Math.min(size,(int)Math.floor((light.getY()+Display.getHeight()/2)/(halfBlockSize.y*2))+1);
			
		int i;
		int j;
		for (i = minX; i < maxX; i++) {
			for (j = minY; j < maxY; j++) {
				if(blockGrid[i][j].castShadows()){
					if(j==0){
						neighbours[0] = false;
					}else{
						neighbours[0] = blockGrid[i][j-1].castShadows();
					}
					if(i == 0){
						neighbours[3] = false;
					}else{
						neighbours[3] = blockGrid[i-1][j].castShadows();
					}
					if(i == maxX-1){
						neighbours[1] = false;
					}else{
						neighbours[1] = blockGrid[i+1][j].castShadows();
					}
					if (j==maxY -1){
						neighbours[2] = false;
					}else{
						neighbours[2] = blockGrid[i][j+1].castShadows();
					}
					l.addAll(((SolidBlock) blockGrid[i][j]).computeShadow(light,i,j,halfBlockSize,neighbours));
				}
			}
		}
		return l;
	}
}