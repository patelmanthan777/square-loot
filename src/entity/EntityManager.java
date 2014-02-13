package entity;

import item.Battery;
import item.weapon.LaserRifle;

import java.util.Iterator;
import java.util.LinkedList;

import light.Laser;
import light.Light;
import light.LightManager;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;

import entity.npc.Npc;
import entity.npc.Shopkeeper;
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
	
	public static void reinitNPCS() {
		npcs.clear();
	}
	
	public static Player reinitPlayers() {
		players.clear();
		
		return createPlayer();
	}

	public static void createZombie(float posx, float posy) {
		Npc npc = new Zombie(posx, posy, 1, 1);
		npcs.add(npc);
		npc.initPhysics();
	}
	
	public static void createShopkeeper(float posx, float posy) {
		Npc npc = new Shopkeeper(posx, posy, 1, 1);
		npcs.add(npc);
		npc.initPhysics();
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
		Player player = new Player(new Vector2f(0, 0));
		players.add(player);
		player.initPhysics();
		
		Light playerLight = LightManager.addPointLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 20,2*(int)ConfigManager.resolution.x,true);
		Laser playerLaser = LightManager.addLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), player.getDirection());
		
		player.setLight(playerLight);

		player.setLaser(playerLaser);
		player.pickUp(new LaserRifle(100,
									 0f,
				 0f,
				 100f,
				 10,
				 10));
		player.pickUp(new Battery(200,200));
		player.pickUp(new Battery(200,200));
		player.pickUp(new Battery(200,200));
		player.pickUp(new Battery(200,200));
		player.pickUp(new Battery(200,200));
		
		return player;
	}
}
