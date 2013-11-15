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
	
	
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		this.blockGrid = new int[width][height];
	}
	
	public void generate(){
		int i;
		int j;
		for(i = 0 ; i < width ; i++){
			for(j = 0 ; j < width ; j++){
				if(Math.random() < probability){
					blockGrid[i][j] = 1; 
				}else{
					blockGrid[i][j] = 0;
				}
			}
		}
	}
	
	
	@Override
	public void draw() {
		GL11.glMatrixMode( GL11.GL_MODELVIEW );
		GL11.glLoadIdentity( );
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