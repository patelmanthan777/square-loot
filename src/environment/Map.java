package environment;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Drawable;

public class Map implements Drawable{

	private Vector3f blockColor = new Vector3f(1,1,1);
	private int width;
	private int height;
	private float probability = 0.10f;
	private Vector2f halfBlockSize = new Vector2f(20,20);
	
	
	private int[][] blockGrid;  
	private int spawnPoint_x;
	private int spawnPoint_y;
	private Vector2f spawnPosition;
	
	
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		this.blockGrid = new int[width][height];
	}
	
	public boolean testCollision(float x, float y)
	{
		int x_grid = (int) Math.floor(x/(halfBlockSize.x*2));
		int y_grid = (int) Math.floor(y/(halfBlockSize.y*2));
		if( x_grid < 0 || y_grid < 0 || x_grid > height || y_grid > width || blockGrid[x_grid][y_grid] == 1){
			return true;
		}
    return false;
	}
	
	public Vector2f getSpawnPosition(){
		return spawnPosition;
	}
	
	public void generate(){
		int i;
		int j;
		for(i = 0 ; i < width ; i++){
			for(j = 0 ; j < height ; j++){
				if(Math.random() < probability){
					blockGrid[i][j] = 1; 
				}else{
					blockGrid[i][j] = 0;
				}
			}
		}
		for(i = 0 ; i < width ; i++){
			blockGrid[i][0] = 1;
			blockGrid[i][height-1] = 1;
		}
		for(j = 0 ; j < height ; j++){
			blockGrid[0][j] = 1;
			blockGrid[width-1][j] = 1;
		}
		
		spawnPoint_x = (int)(Math.random()*(width-2)+1);
		spawnPoint_y = (int)(Math.random()*(height-2)+1);
		blockGrid[spawnPoint_x][spawnPoint_y] = 0;
		spawnPosition = new Vector2f((spawnPoint_x * 2 + 1)* halfBlockSize.x, (spawnPoint_y * 2 +1) * halfBlockSize.y);
	}
	
	
	@Override
	public void draw() {
		GL11.glMatrixMode( GL11.GL_MODELVIEW );
		GL11.glColor3f(this.blockColor.x,this.blockColor.y,this.blockColor.z);
		// draw quad
		int i;
		int j;
		float posX;
		float posY;
		for(i = 0 ; i < width ; i++){
			for(j = 0 ; j < width ; j++){
				if(blockGrid[i][j] == 1){
					GL11.glBegin(GL11.GL_LINE_STRIP);
					posX = i * halfBlockSize.x * 2 + halfBlockSize.x;
					posY = j * halfBlockSize.y * 2 + halfBlockSize.y;
					GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
				    GL11.glVertex2f(posX-halfBlockSize.x,posY-halfBlockSize.y);
				    GL11.glVertex2f(posX-halfBlockSize.x,posY+halfBlockSize.y);
				    GL11.glVertex2f(posX+halfBlockSize.x,posY+halfBlockSize.y);
				    GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
				    GL11.glEnd();
				}
			}
		}
		
	}
}