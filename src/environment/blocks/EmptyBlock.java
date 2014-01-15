package environment.blocks;

public class EmptyBlock extends Block {
	protected int pressure;
	protected int newPressure;
	protected EmptyBlock(){
		super();
		layer = 0;
		this.pressurized = true;
		this.pressure = 100;
	}
	
	public int getPressure(){
		return pressure;
	}
	public void setNewPressure(int pressure){
		this.newPressure = pressure;
	}
	
	public void update(){
		this.pressure = this.newPressure;
	}
}
