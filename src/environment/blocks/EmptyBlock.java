package environment.blocks;

public class EmptyBlock extends Block {
	protected int pressure;
	protected EmptyBlock(){
		super();
		layer = 0;
		this.pressurized = true;
		this.pressure = 100;
	}
}
