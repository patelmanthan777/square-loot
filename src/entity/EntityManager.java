package entity;

import java.util.Iterator;
import java.util.LinkedList;

import org.jbox2d.dynamics.World;
import org.lwjgl.util.vector.Vector2f;

import entity.npc.Npc;
import entity.npc.Zombie;
import entity.player.Player;
import environment.Map;
import static org.lwjgl.opengl.GL11.*;

public class EntityManager {
	/** Per room management in the future ? **/
	private static LinkedList<Npc> npcs = new LinkedList<Npc>();
	private static LinkedList<Player> players = new LinkedList<Player>();

	public static void init() {

	}

	public static void createZombie(float posx, float posy) {
		npcs.add(new Zombie(posx, posy, 1, 1, 0));
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

	public static void updatePosition(long delta, Map m) {
		Iterator<Npc> ite = npcs.iterator();
		while(ite.hasNext())
		{
			Npc npc = ite.next();
			if(npc.getHealth() > 0)
			{
				npc.updatePosition(delta, m);
			}
			else
			{
				ite.remove();
				npc.destroy();
			}
		}
		
		for (Player p : players) {
			p.updatePosition(delta, m);
		}
	}

	public static void render() {
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);				
		for(Npc npc : npcs){
			npc.draw();
		}		
	
		for (Player p : players) {
			p.draw();
		}
		glDisable(GL_BLEND);
	}

	public static Player createPlayer() {
		Player player = new Player(new Vector2f(0, 0), 10);
		players.add(player);
		return player;
	}

	public static void initPhysics(World world) {
		for (Npc npc : npcs) {
			npc.initPhysics();
		}
		for (Player p : players) {
			p.initPhysics();
		}
		
	}
}
