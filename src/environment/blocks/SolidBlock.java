package environment.blocks;


public class SolidBlock extends PhysicalBlock {

	protected SolidBlock() {
		super();
		layer = 1;
	}

	protected SolidBlock(float x, float y) {
		super(x, y);
		layer = 1;
	}
}
