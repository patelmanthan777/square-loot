package entity.npc;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
import entity.player.Player;
import game.GameLoop;
import static org.lwjgl.opengl.GL11.*;

public class LivingEntityManager {

	private static LinkedList <Npc> npcs = new LinkedList<Npc>();
	private static LinkedList <Player> players = new LinkedList<Player>();
	
	public static void init(){
		
	}
	
	public static  void createZombie(float posx, float posy){
		npcs.add(new Zombie(posx,posy,1,1,0));
	}
	
	public static void update(long deltaT){
		for(Npc npc : npcs){
			npc.thinkAndAct(players,deltaT);
			npc.updatePostion(deltaT, GameLoop.map);
		}
	}
	
	public static void render(){	
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBindTexture(GL_TEXTURE_2D, TextureManager.zombieTexture().getTextureID());
		glBegin(GL_QUADS);
		for(Npc npc : npcs){
			npc.draw();
		}
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);
	}
	
	public static Player createPlayer(){
		Player player = new Player(new Vector2f(0,0), 5);
		players.add(player);
		return player;
	}
}
