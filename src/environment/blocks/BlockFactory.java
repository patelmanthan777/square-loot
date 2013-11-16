package environment.blocks;

public class BlockFactory {
	static EmptyBlock emptyBlock = new EmptyBlock();
	static SolidBlock solidBlock = new SolidBlock();
	
	private BlockFactory(){
	}
	
	static public Block createEmptyBlock(){
		return emptyBlock;
	}
	
	static public Block createSolidBlock(){
		return solidBlock;
	}
}
