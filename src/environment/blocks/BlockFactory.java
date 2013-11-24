package environment.blocks;

public class BlockFactory {
	static EmptyBlock emptyBlock = new EmptyBlock();
	static BorderBlock borderBlock = new BorderBlock();
	static SolidBlock solidBlock = new SolidBlock();
	static SpawnBlock spawnBlock = new SpawnBlock();
	static VoidBlock voidBlock = new VoidBlock();
	static SolidBlock solidBlocks = new SolidBlock();

	
	
	private BlockFactory(){
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
