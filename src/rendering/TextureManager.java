package rendering;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {
	private static int nbTextures = 2;
	
	private static final int player = 0;
	private static final int zombie = 1;
	
	private static Texture[] textures = new Texture[nbTextures];
	
	public static void init(){
		try {
			textures[player] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/player.png"));
			textures[zombie] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/zombie.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	
	public static Texture playerTexture(){
		return textures[player];
	}
	
	public static Texture zombieTexture(){
		return textures[zombie];
	}
}
