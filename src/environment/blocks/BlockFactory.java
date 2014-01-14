package environment.blocks;

public class BlockFactory {
	/**
	 * Blocks inside the rooms, do not cast shadows, do not collide.
	 */
	static EmptyBlock emptyBlock = new EmptyBlock();
	static BorderBlock borderBlock = new BorderBlock();
	/**
	 * Solid blocks inside the rooms.
	 */
	static SolidBlock solidBlock = new SolidBlock();
	/**
	 * Player spawning blocks.
	 */
	static SpawnBlock spawnBlock = new SpawnBlock();
	/**
	 * Blocks outside the rooms.
	 */
	static VoidBlock voidBlock = new VoidBlock();

	
	static public void initBlocks(){
		emptyBlock.setColor(1f, 1f, 1f,1f);				
		borderBlock.setColor(0.2f, 0.2f, 0.2f,1f);
		solidBlock.setColor(0.2f, 0.2f, 0.2f,1f);
		spawnBlock.setColor(0f, 1f, 0f,1f);
		voidBlock.setColor(0f, 0f, 0f, 0f);
	}
	
	static public Block createEmptyBlock(){
		EmptyBlock emptyBlock = new EmptyBlock();
		emptyBlock.setColor(1f, 1f, 1f,1f);	
		return emptyBlock;
	}
	
	static public Block createSolidBlock(){
		return solidBlock;
	}
	
	static public Block createBorderBlock(){
		return borderBlock;
	}
	

	static public Block createVoidBlock(){
		return voidBlock;
	}

	static public Block createSpawnBlock(){
		return spawnBlock;
	}
}
