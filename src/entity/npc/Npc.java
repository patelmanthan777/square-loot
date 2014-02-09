package entity.npc;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import rendering.Drawable;
import entity.LivingEntity;
import entity.player.Player;

public abstract class Npc extends LivingEntity implements Drawable {

	public Npc(Vector2f pos) {
		super(pos);
	}

	public Npc(Vector2f pos, Vector2f rot) {
		super(pos, rot);
	}
	
	public Npc(float posx, float posy, float dirx, float diry) {
		super(posx,posy,dirx,diry);
	}
	public Npc(float posx, float posy) {
		super(posx,posy);
	}
	
	public abstract void thinkAndAct(LinkedList<Player> players, long deltaT);

}
