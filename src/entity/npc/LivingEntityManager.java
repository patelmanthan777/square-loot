package entity.npc;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import entity.player.Player;
import event.Timer;
import game.GameLoop;
import static org.lwjgl.opengl.GL11.*;

public class LivingEntityManager {
	/** Per room management in the future ? **/
	private static LinkedList <Npc> npcs = new LinkedList<Npc>();
	private static LinkedList <Player> players = new LinkedList<Player>();
	
	public static void init(){
		
	}
	
	public static  void createZombie(float posx, float posy){
		npcs.add(new Zombie(posx,posy,1,1));
	}
	
	public static void update(){
		for(Npc npc : npcs){
			npc.thinkAndAct(players);
			npc.updatePostion(Timer.getDelta(), GameLoop.map);
		}
	}
	
	public static void render(){
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		for(Npc npc : npcs){
			npc.draw();
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
	
	public static Player createPlayer(){
		Player player = new Player(new Vector2f(0,0));
		players.add(player);
		return player;
	}
}
