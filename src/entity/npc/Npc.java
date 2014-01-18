package entity.npc;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import rendering.Drawable;
import entity.LivingEntity;
import entity.player.Player;

public abstract class Npc extends LivingEntity implements Drawable {

	public Npc(Vector2f pos, int inventorySize) {
		super(pos, inventorySize);
	}

	public Npc(Vector2f pos, Vector2f rot, int inventorySize) {
		super(pos, rot, inventorySize);
	}
	
	public Npc(float posx, float posy, float dirx, float diry, int inventorySize) {
		super(posx,posy,dirx,diry,inventorySize);
	}
	public Npc(float posx, float posy, int inventorySize) {
		super(posx,posy,inventorySize);
	}
	
	public abstract void thinkAndAct(LinkedList<Player> players, long deltaT);

}
