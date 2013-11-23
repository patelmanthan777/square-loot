package environment.blocks;

public class BlockFactory {
	static EmptyBlock emptyBlock = new EmptyBlock();
	static BorderBlock borderBlock = new BorderBlock();
	static SolidBlock solidBlock = new SolidBlock();
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
	
}
