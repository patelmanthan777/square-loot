package environment.room.template;

import environment.blocks.Block;

public abstract class PatchTemplate {
	static final public int templateSize = 10;
	
	protected BlockTemplate patch[][];
	
	PatchTemplate()
	{
		patch = new BlockTemplate[templateSize][templateSize];
	}
	
	 public abstract void init();
	
	public void generatePatch(Block grid[][], int offsetX, int offsetY)
	{
		for(int i=0; i<templateSize; i++) {
			for(int j=0; j<templateSize; j++) {
				grid[offsetX+i][offsetY+j] = patch[i][j].contructBlock(); 
			}
		}
	}
}
