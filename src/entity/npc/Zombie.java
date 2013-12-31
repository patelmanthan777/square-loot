package entity.npc;

import org.lwjgl.util.vector.Vector2f;

import environment.Map;

public class Zombie extends Npc{

	public Zombie(Vector2f pos) {
		super(pos);
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void thinkAndAct() {
		
	}

	@Override
	public boolean isInCollision(float x, float y, Map m) {
		return false;
	}
	
}
