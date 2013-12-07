package environment.blocks;


public interface Block {
	public abstract boolean testCollision();
	public abstract void drawAt(float posX, float posY);
	public abstract boolean castShadows();
}
