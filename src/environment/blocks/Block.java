package environment.blocks;

import org.lwjgl.util.vector.Vector2f;


public interface Block {
	public abstract boolean testCollision();
	public abstract void drawAt(float posX, float posY, Vector2f halfBlockSize);
	public abstract boolean castShadows();
}
