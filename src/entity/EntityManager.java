package entity;

import java.util.Iterator;
import java.util.LinkedList;

import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
import entity.npc.Npc;
import entity.npc.Zombie;
import entity.player.Player;
import static org.lwjgl.opengl.GL11.*;

public class EntityManager {
	/** Per room management in the future ? **/
	private static LinkedList<Npc> npcs = new LinkedList<Npc>();
	private static LinkedList<Player> players = new LinkedList<Player>();

	public static void init() {

	}

	public static void createZombie(float posx, float posy) {
		npcs.add(new Zombie(posx, posy, 1, 1));
	}

	public static void updateInput(long elapsedTime) {
		for (Npc npc : npcs) {
			npc.thinkAndAct(players, elapsedTime);
		}
	}

	public static void updatePhysics(long elapsedTime) {
		for (Npc npc : npcs) {
			npc.updatePhysics(elapsedTime);
		}
		for (Player p : players) {
			p.updatePhysics(elapsedTime);
		}
	}

	public static void updatePosition() {
		Iterator<Npc> ite = npcs.iterator();
		while(ite.hasNext())
		{
			Npc npc = ite.next();
			if(npc.getHealth() > 0)
			{
				npc.updatePostion();
			}
			else
			{
				ite.remove();
				npc.destroy();
			}
		}
		
		for (Player p : players) {
			p.updatePostion();
		}
	}

	public static void render() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBindTexture(GL_TEXTURE_2D, TextureManager.zombieTexture()
				.getTextureID());
		glBegin(GL_QUADS);
		for (Npc npc : npcs) {
			npc.draw();
		}
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);
	}

	public static Player createPlayer() {
		Player player = new Player(new Vector2f(0, 0));
		players.add(player);
		return player;
	}

	public static void initPhysics(World world) {
		for (Npc npc : npcs) {
			npc.initPhysics(world);
		}
		for (Player p : players) {
			p.initPhysics(world);
		}
		
	}
}
