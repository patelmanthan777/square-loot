package environment.blocks;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

public class BlockFactory {
	static EmptyBlock emptyBlock = new EmptyBlock();
	static BorderBlock borderBlock = new BorderBlock();
	static SolidBlock solidBlock = new SolidBlock();
	static VoidBlock voidBlock = new VoidBlock();
	static LinkedList <SolidBlock> solidBlocks = new LinkedList<SolidBlock>();
	
	
	private BlockFactory(){
	}
	
	static public Block createEmptyBlock(){
		return emptyBlock;
	}
	
	static public Block createSolidBlock(Vector2f position, Vector2f size){
		SolidBlock b = new SolidBlock(position.x, position.y, size);
		solidBlocks.add(b);
		return b;
	}
	
	static public Block createSolidBlock(int i, int j, Vector2f size){
		SolidBlock b = new SolidBlock(i,j, size);
		solidBlocks.add(b);
		return b;
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
	public static LinkedList<SolidBlock> getSolidBlocks(){
		return solidBlocks;
	}
	
}
