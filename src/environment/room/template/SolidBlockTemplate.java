package environment.room.template;

import environment.blocks.Block;
import environment.blocks.BlockFactory;

public class SolidBlockTemplate extends BlockTemplate {

	@Override
	public Block contructBlock() {
		return BlockFactory.createSolidBlock();
	}

}
