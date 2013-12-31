package entity.npc;

import org.lwjgl.util.vector.Vector2f;

import entity.LivingEntity;

public abstract class Npc extends LivingEntity {

	public Npc(Vector2f pos) {
		super(pos);
	}
	
	public abstract void thinkAndAct();

}
