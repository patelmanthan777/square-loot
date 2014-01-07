package entity.npc;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import rendering.TextureManager;
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
		Player player = new Player(new Vector2f(0,0));
		players.add(player);
		return player;
	}
}