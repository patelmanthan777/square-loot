package rendering;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {
	private static int nbTextures = 4;
	
	private static final int background = 0;
	private static final int player = 1;
	private static final int zombie = 2;
	private static final int laserRifle = 3;
	
	private static Texture[] textures = new Texture[nbTextures];
	
	public static void init(){
		try {
			textures[background] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/background.png"));
			textures[player] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/player.png"));
			textures[zombie] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/zombie.png"));
			textures[laserRifle] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/laserRifle.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	public static Texture laserRifleTexture(){
		return textures[laserRifle];
	}
	
	public static Texture playerTexture(){
		return textures[player];
	}
	
	public static Texture backgroundTexture(){
		return textures[background];
	}
	
	public static Texture zombieTexture(){
		return textures[zombie];
	}
}
