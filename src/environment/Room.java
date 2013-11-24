package environment;

import environment.blocks.Block;
import environment.blocks.BlockFactory;

public abstract class Room {
	protected int width;
	protected int height;
	protected Block[][] grid;
	protected int x;
	protected int y;
	
	protected boolean[] doors = new boolean[4];
	
	public Room(int width, int height,int posX, int posY){
		this.width = width;
		this.height = height;
		this.x = posX;
		this.y = posY;
		grid = new Block[width][height];
		for(int i = 0; i < 4 ;i++){
			doors[i] = false;
		}
	}
	
	public void place(Block[][] grid){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				grid[x+i][y+j] = this.grid[i][j];
			}
		}
		if(doors[0]){
			grid[x+width/2-2][y] = BlockFactory.createSolidBlock();
			grid[x+width/2-1][y] = BlockFactory.createEmptyBlock();
			grid[x+width/2+0][y] = BlockFactory.createEmptyBlock();
			grid[x+width/2+1][y] = BlockFactory.createSolidBlock();
			grid[x+width/2-2][y+1] = BlockFactory.createSolidBlock();
			grid[x+width/2-1][y+1] = BlockFactory.createEmptyBlock();
			grid[x+width/2+0][y+1] = BlockFactory.createEmptyBlock();
			grid[x+width/2+1][y+1] = BlockFactory.createSolidBlock();
		}
		if(doors[1]){
			grid[x+width-1][y+height/2-2] = BlockFactory.createSolidBlock();
			grid[x+width-1][y+height/2-1] = BlockFactory.createEmptyBlock();
			grid[x+width-1][y+height/2+0] = BlockFactory.createEmptyBlock();
			grid[x+width-1][y+height/2+1] = BlockFactory.createSolidBlock();
			grid[x+width-2][y+height/2-2] = BlockFactory.createSolidBlock();
			grid[x+width-2][y+height/2-1] = BlockFactory.createEmptyBlock();
			grid[x+width-2][y+height/2+0] = BlockFactory.createEmptyBlock();
			grid[x+width-2][y+height/2+1] = BlockFactory.createSolidBlock();
		}
		if(doors[2]){
			grid[x+width/2-2][y+height-1] = BlockFactory.createSolidBlock();
			grid[x+width/2-1][y+height-1] = BlockFactory.createEmptyBlock();
			grid[x+width/2+0][y+height-1] = BlockFactory.createEmptyBlock();
			grid[x+width/2+1][y+height-1] = BlockFactory.createSolidBlock();
			grid[x+width/2-2][y+height-2] = BlockFactory.createSolidBlock();
			grid[x+width/2-1][y+height-2] = BlockFactory.createEmptyBlock();
			grid[x+width/2+0][y+height-2] = BlockFactory.createEmptyBlock();
			grid[x+width/2+1][y+height-2] = BlockFactory.createSolidBlock();
		}
		if(doors[3]){
			grid[x][y+height/2-2] = BlockFactory.createSolidBlock();
			grid[x][y+height/2-1] = BlockFactory.createEmptyBlock();
			grid[x][y+height/2+0] = BlockFactory.createEmptyBlock();
			grid[x][y+height/2+1] = BlockFactory.createSolidBlock();
			grid[x+1][y+height/2-2] = BlockFactory.createSolidBlock();
			grid[x+1][y+height/2-1] = BlockFactory.createEmptyBlock();
			grid[x+1][y+height/2+0] = BlockFactory.createEmptyBlock();
			grid[x+1][y+height/2+1] = BlockFactory.createSolidBlock();
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void createDoor(int wall){
		doors[wall] = true;
	}
	
}
