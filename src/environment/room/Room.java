package environment.room;

import static org.lwjgl.opengl.GL11.*;

import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import light.Light;
import light.Laser;
import light.Shadow;
import light.ShadowBuffer;
import rendering.Drawable;
import rendering.ShadowCaster;
import userInterface.MiniMap;
import environment.Door;
import environment.Map;
import environment.Sas;
import environment.blocks.Block;
import environment.blocks.BlockFactory;

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
	protected boolean discovered = false;
	protected float pressure;
	protected float newPressure;
	protected Door[] doors = new Door[4];
	protected Sas[] sas = new Sas[4];
	protected int doorLayer = 1;
	protected boolean renderIsUpdated[] = new  boolean[4];
	
	
	/* avoid dynamic allocation in computeShadow */
	boolean[] neighbours = new boolean[4];
	private Vector2f shadowPoints[] = new Vector2f[4];
	/* avoid dynamic allocation in laserIntersect */
	int[] htab = new int[8];
	int[] vtab = new int[8];
	/* ------------------------------------------ */

	public Room(float posX, float posY) {
		this.x = posX;
		this.y = posY;
		int gridSizeX = (int) Map.roomBlockSize.x;
		int gridSizeY = (int) Map.roomBlockSize.y;
		grid = new Block[gridSizeX][gridSizeY];
		for (int i = 0; i < 4; i++) {
			doors[i] = null;
		}
		for (int i = 0; i < 4; i++) {
			shadowPoints[i] = new Vector2f();
		}
		
		construct();
	}

	protected void construct(){
		for(int i = 2; i < (int)Map.roomBlockSize.x-2;i++){
			for(int j = 2; j < (int)Map.roomBlockSize.y-2; j++){
				grid[i][j] = BlockFactory.createEmptyBlock();
			}
		}
		for(int i = 0; i < (int)Map.roomBlockSize.x;i++){
			grid[i][0] = BlockFactory.createVoidBlock();
			grid[i][(int)Map.roomBlockSize.y-1] = BlockFactory.createVoidBlock();
		}
		for(int i = 0; i < (int)Map.roomBlockSize.y; i++){
			grid[0][i] = BlockFactory.createVoidBlock();
			grid[(int)Map.roomBlockSize.x-1][i] = BlockFactory.createVoidBlock();
		}
		
		for(int i = 1; i < (int)Map.roomBlockSize.x-1;i++){
			grid[i][1] = BlockFactory.createBorderBlock();
			grid[i][(int)Map.roomBlockSize.y-2] = BlockFactory.createBorderBlock();
		}
		for(int i = 1; i < (int)Map.roomBlockSize.y-1; i++){
			grid[1][i] = BlockFactory.createBorderBlock();
			grid[(int)Map.roomBlockSize.x-2][i] = BlockFactory.createBorderBlock();
		}
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	/**
	 * Create door on the given wall, clockwise indices starting at noon.
	 * 
	 * @param wall
	 *            is the index of the wall
	 */
	public Door createDoor(int wall) {
		if (wall == 0 || wall == 2) {
			int j;
			if(wall == 0)
			{
				j = 0;
			}
			else
			{
				j = (int) Map.roomBlockSize.y - 2;
			}

			grid[(int) Map.roomBlockSize.x / 2 - 2][j] = BlockFactory
					.createBorderBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 1][j] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 0][j] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 1][j] = BlockFactory
					.createBorderBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 2][j+1] = BlockFactory
					.createBorderBlock();
			grid[(int) Map.roomBlockSize.x / 2 - 1][j+1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 0][j+1] = BlockFactory
					.createEmptyBlock();
			grid[(int) Map.roomBlockSize.x / 2 + 1][j+1] = BlockFactory
					.createBorderBlock();
		}
		else if (wall == 1 || wall == 3) {
			int i;
			if(wall == 3)
			{
				i = 0;
			}
			else
			{
				i = (int) Map.roomBlockSize.x - 2;
			}
			
			grid[i][(int) Map.roomBlockSize.y / 2 - 2] = BlockFactory
					.createBorderBlock();
			grid[i][(int) Map.roomBlockSize.y / 2 - 1] = BlockFactory
					.createEmptyBlock();
			grid[i][(int) Map.roomBlockSize.y / 2 + 0] = BlockFactory
					.createEmptyBlock();
			grid[i][(int) Map.roomBlockSize.y / 2 + 1] = BlockFactory
					.createBorderBlock();
			grid[i+1][(int) Map.roomBlockSize.y / 2 - 2] = BlockFactory
					.createBorderBlock();
			grid[i+1][(int) Map.roomBlockSize.y / 2 - 1] = BlockFactory
					.createEmptyBlock();
			grid[i+1][(int) Map.roomBlockSize.y / 2 + 0] = BlockFactory
					.createEmptyBlock();
			grid[i+1][(int) Map.roomBlockSize.y / 2 + 1] = BlockFactory
					.createBorderBlock();
		}
		Door door = new Door(this, wall);
		doors[wall] = door;
		return door;
		
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

	public void draw(int layer) {
		for (int i = 0; i < Map.roomBlockSize.x; i++) {
			for (int j = 0; j < Map.roomBlockSize.y; j++) {
				float posX = x + i * Map.blockPixelSize.x;
				float posY = y + j * Map.blockPixelSize.y;
				if (grid[i][j].getLayer() == layer) {
					grid[i][j].drawAt(posX, posY);
				}
			}
		}
	}	

	@Override
	public void computeShadow(Light light, ShadowBuffer[] shadows) {
		computeHorizontalEdgeShadows(light, shadows);
		computeVerticalEdgeShadows(light, shadows);			
	}
	
	private void computeHorizontalEdgeShadows(Light light, ShadowBuffer[] shadowBuffer) {
		/* NORTH */
		for (int layer = 1; layer < Map.maxLayer+1; layer++) {
			for (int j = 0; j < Map.roomBlockSize.y; j++) {
				int first = -1;
				for (int i = 0; i < Map.roomBlockSize.x; i++) {
					if (light.getY() > j * Map.blockPixelSize.y + this.y) {
						if (grid[i][j].castShadows()  && (j==0 || !(grid[i][j-1].castShadows() && grid[i][j-1].getLayer() == grid[i][j].getLayer())) && grid[i][j].getLayer() == layer) {
							if (first == -1) {
								beginShadow(light,i,j,0);
								first = i;
							}
							if(i == Map.roomBlockSize.x-1){
								endShadow(light, shadowBuffer[layer-1], i, j, 0);
								first = -1;
							}
						} else {
							if (first != -1) {
								endShadow(light, shadowBuffer[layer-1], i-1, j, 0);
								first = -1;
							}
						}
					}
				}
			}
		}
		/* SOUTH */
		for (int layer = 1; layer < Map.maxLayer+1; layer++) {
			for (int j = 0; j < Map.roomBlockSize.y; j++) {
				int first = -1;
				for (int i = (int) (Map.roomBlockSize.x-1); i >=0; i--) {
					if (light.getY() < (j+1) * Map.blockPixelSize.y + this.y) {
						if (grid[i][j].castShadows() && (j==Map.roomBlockSize.y-1 || !(grid[i][j+1].castShadows() && grid[i][j+1].getLayer() == grid[i][j].getLayer())) && grid[i][j].getLayer() == layer) {
							if (first == -1) {
								beginShadow(light,i,j,2);
								first = i;
							}
							if(i == 0){
								endShadow(light, shadowBuffer[layer-1], i, j, 2);
								first = -1;
							}
						} else {
							if (first != -1) {
								endShadow(light, shadowBuffer[layer-1], i+1, j, 2);
								first = -1;
							}
						}
					}
				}
			}
		}
	}
	private void computeVerticalEdgeShadows(Light light, ShadowBuffer[] shadowBuffer) {
		/* EAST */
		for (int layer = 1; layer < Map.maxLayer+1; layer++) {
			for (int i = 0; i < Map.roomBlockSize.x; i++) {
				int first = -1;
				for (int j = 0; j < Map.roomBlockSize.y; j++) {
					if (light.getX() < (i+1) * Map.blockPixelSize.x + this.x) {
						if (grid[i][j].castShadows() && (i==Map.roomBlockSize.x-1 || !(grid[i+1][j].castShadows() && grid[i+1][j].getLayer() == grid[i][j].getLayer())) && grid[i][j].getLayer() == layer) {
							if (first == -1) {
								beginShadow(light,i,j,1);
								first = i;
							}
							if(j == Map.roomBlockSize.y-1){
								endShadow(light, shadowBuffer[layer-1], i, j, 1);
								first = -1;
							}
						} else {
							if (first != -1) {
								endShadow(light, shadowBuffer[layer-1], i, j-1, 1);
								first = -1;
							}
						}
					}
				}
			}
		}
		/* WEST */
		for (int layer = 1; layer < Map.maxLayer+1; layer++) {
			for (int i = 0; i < Map.roomBlockSize.x; i++) {
				int first = -1;
				for (int j = (int) (Map.roomBlockSize.y-1); j >= 0; j--) {
					if (light.getX() > i * Map.blockPixelSize.x + this.x) {
						if (grid[i][j].castShadows() && (i==0 || !(grid[i-1][j].castShadows() && grid[i-1][j].getLayer() == grid[i][j].getLayer())) && grid[i][j].getLayer() == layer) {
							if (first == -1) {
								beginShadow(light,i,j,3);
								first = i;
							}
							if(j == 0){
								endShadow(light, shadowBuffer[layer-1], i, j, 3);
								first = -1;
							}
						} else {
							if (first != -1) {
								endShadow(light, shadowBuffer[layer-1], i, j+1, 3);
								first = -1;
							}
						}
					}
				}
			}
		}
	}
	private void beginShadow(Light light, int i, int j, int edge){
		int cornerX=0;
		int cornerY=0;
		switch(edge){
		case 0:
			cornerX = 0;
			cornerY = 0;
			break;
		case 1:
			cornerX = 1;
			cornerY = 0;
			break;
		case 2:
			cornerX = 1;
			cornerY = 1;
			break;
		case 3:
			cornerX = 0;
			cornerY = 1;
			break;
		}
		shadowPoints[0].x = this.x + i * Map.blockPixelSize.x + cornerX * Map.blockPixelSize.x;
		shadowPoints[0].y = this.y + j * Map.blockPixelSize.y + cornerY * Map.blockPixelSize.y;
		Vector2f.sub(shadowPoints[0], light.getPosition(), shadowPoints[1]);
		shadowPoints[1].normalise(shadowPoints[1]);
		shadowPoints[1].scale(10000);
		shadowPoints[1] = Vector2f.add(shadowPoints[0], shadowPoints[1], shadowPoints[1]);
	}

	private void endShadow(Light light, ShadowBuffer shadowBuffer, int i, int j, int edge){
		int shadowInd = shadowBuffer.lastShadow + 1;
		int cornerX=0;
		int cornerY=0;
		switch(edge){
		case 0:
			cornerX = 1;
			cornerY = 0;
			break;
		case 1:
			cornerX = 1;
			cornerY = 1;
			break;
		case 2:
			cornerX = 0;
			cornerY = 1;
			break;
		case 3:
			cornerX = 0;
			cornerY = 0;
			break;
		}
		shadowPoints[3].x = this.x + i * Map.blockPixelSize.x + cornerX * Map.blockPixelSize.x;
		shadowPoints[3].y = this.y + j * Map.blockPixelSize.y + cornerY * Map.blockPixelSize.y;
		Vector2f.sub(shadowPoints[3], light.getPosition(), shadowPoints[2]);
		shadowPoints[2].normalise(shadowPoints[2]);
		shadowPoints[2].scale(10000);
		shadowPoints[2] = Vector2f.add(shadowPoints[3], shadowPoints[2],
				shadowPoints[2]);
		Shadow[] shadows = (shadowBuffer.getShadows());
		shadows[shadowInd].points[0].x = shadowPoints[3].x;
		shadows[shadowInd].points[0].y = shadowPoints[3].y;
		shadows[shadowInd].points[1].x = shadowPoints[2].x;
		shadows[shadowInd].points[1].y = shadowPoints[2].y;
		shadows[shadowInd].points[2].x = shadowPoints[1].x;
		shadows[shadowInd].points[2].y = shadowPoints[1].y;
		shadows[shadowInd].points[3].x = shadowPoints[0].x;
		shadows[shadowInd].points[3].y = shadowPoints[0].y;
		shadowBuffer.lastShadow++;
	}
	

	/**
	 * Draws the shadow in the case of a laser. Starts by determining which
	 * block will cast it and return having only modified <b>cpos</b> if it is not
	 * in the room.
	 * 
	 * @param l is the laser source
	 * @param shadows is the shadow storage system
	 * @param cpos represents a fictional position of the laser source.
	 * It is modified by the algorithm to follow the laser direction. 
	 */
	public void laserIntersect(Laser l, Vector2f cpos){		
		int i = (int) (cpos.x / Map.blockPixelSize.x) % ((int) Map.roomBlockSize.x);
		int j = (int) (cpos.y / Map.blockPixelSize.y) % ((int) Map.roomBlockSize.y);
		
		if (grid[i][j].castShadows()){			
			l.setIntersection(cpos);			
		}
		else{
			boolean inRoom = true;
			while(l.getIntersection() == null && inRoom) {
				boolean foundInter = false;
				
				
				htab[0] = 0;
				htab[1] = 1;
				htab[2] = 0;
				htab[3] = -1;
				htab[4] = -1;
				htab[5] = 1;
				htab[6] = 1;
				htab[7] = -1;
				
				vtab[0] = -1;
				vtab[1] = 0;
				vtab[2] = 1;
				vtab[3] = 0;
				vtab[4] = -1;
				vtab[5] = -1;
				vtab[6] = 1;
				vtab[7] = 1;
				
				for(int k = 0; k < 8 && !foundInter; k++){			
					
					Vector2f inter = grid[i][j].
							intersectBlock(cpos,
									       l.getDirection(),
									       (int) (this.x + (i+htab[k]) * Map.blockPixelSize.x),
									       (int) (this.y + (j+vtab[k]) * Map.blockPixelSize.y));
				
					if (inter != null) {
						inRoom = i + htab[k] >= 0 &&
								 i + htab[k] < Map.roomBlockSize.x &&
								 j + vtab[k] >= 0 &&
								 j + vtab[k] < Map.roomBlockSize.y;
						
						foundInter = true;
								
						if( inRoom && grid[i+htab[k]][j+vtab[k]].castShadows()) {
							if (grid[i+htab[k]][j+vtab[k]].isInside(cpos,
																	(int) (this.x + (i+htab[k]) * Map.blockPixelSize.x),
																	(int) (this.y + (j+vtab[k]) * Map.blockPixelSize.y)))
								l.setIntersection(cpos);
							
							else
								l.setIntersection(inter);
						}
						else {
							i = i+htab[k];
							j = j+vtab[k];
							cpos.x = inter.x;
							cpos.y = inter.y;																
						}
					}	
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
			glColor3f(miniMapColor.x, miniMapColor.y*pressure/OxygenRoom.maxPressure, miniMapColor.z*pressure/OxygenRoom.maxPressure);
			glLoadIdentity();
			if (doors[0]!=null) {
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
			if (doors[1]!=null) {
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
			if (doors[2]!=null) {
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
			if (doors[3]!=null) {
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

	public void discover() {
		discovered = true;
	}
	
	public void initPhysics(World w) {
		for (int i = 0; i < Map.roomBlockSize.x; i++) {
			for (int j = 0; j < Map.roomBlockSize.y; j++) {
				float posX = x + i * Map.blockPixelSize.x;
				float posY = y + j * Map.blockPixelSize.y;
				grid[i][j].init(posX, posY);
			}
		}	
	}

	public Block getBlock(int i,int j){
		return grid[i][j];
	}
	
	public void putBlock(Block block, int i,int j){
		grid[i][j] = block;
	}
	
	public Door[] getDoors(){
		return doors;
	}
	
	public float getPressure(){
		return pressure;
	}
	
	public float getNewPressure(){
		return newPressure;
	}
	
	public void setNewPressure(float pressure){
		this.newPressure = pressure;
	}
	
	public void update(long delta){
		this.pressure = this.newPressure;
	}
	
	public void setSas(Sas sas, int wall){
		this.sas[wall] = sas;
	}
	
	public Sas[] getSas(){
		return sas;
	}
	
	public void setRenderUpdated(boolean bool, int layer){
		renderIsUpdated[layer] = bool;
	}
	
	public boolean renderIsUpdated(int layer){
		return renderIsUpdated[layer];
	}
}
