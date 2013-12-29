package environment.blocks;

public class BlockFactory {
	static EmptyBlock emptyBlock = new EmptyBlock();
	static BorderBlock borderBlock = new BorderBlock();
	static SolidBlock solidBlock = new SolidBlock();
	static SpawnBlock spawnBlock = new SpawnBlock();
	static VoidBlock voidBlock = new VoidBlock();

	
	static public void initBlocks(){
		emptyBlock.setColor(1f, 1f, 1f);
		borderBlock.setColor(0.2f, 0.2f, 0.2f);
		solidBlock.setColor(0.3f, 0.3f, 0.3f);
		spawnBlock.setColor(0f, 1f, 0f);
		voidBlock.setColor(0f, 0f, 0f);
	}
	
	static public Block createEmptyBlock(){
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
