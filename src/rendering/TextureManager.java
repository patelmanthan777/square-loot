package rendering;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {
	
	private static enum textureEnum {
		BACKGROUND,
		PLAYER,
		ZOMBIE,
		LASERRIFLE,
		BATTERY,
		ENERGY
	}	
	
	private static Texture[] textures = new Texture[textureEnum.values().length];
	
	public static void init(){
		try {
			textures[textureEnum.BACKGROUND.ordinal()] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/background.png"));
			textures[textureEnum.PLAYER.ordinal()] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/player.png"));
			textures[textureEnum.ZOMBIE.ordinal()] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/zombie.png"));
			textures[textureEnum.LASERRIFLE.ordinal()] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/laserRifle.png"));
			textures[textureEnum.BATTERY.ordinal()] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/battery.png"));
			textures[textureEnum.ENERGY.ordinal()] = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/textures/energy.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	
	public static Texture playerTexture(){
		return textures[textureEnum.PLAYER.ordinal()];
	}
	
	public static Texture backgroundTexture(){
		return textures[textureEnum.BACKGROUND.ordinal()];
	}
	
	public static Texture zombieTexture(){
		return textures[textureEnum.ZOMBIE.ordinal()];
	}
	
	public static Texture laserRifleTexture(){
		return textures[textureEnum.LASERRIFLE.ordinal()];
	}
	
	public static Texture batteryTexture(){
		return textures[textureEnum.BATTERY.ordinal()];
	}
	
	public static Texture energyTexture(){
		return textures[textureEnum.ENERGY.ordinal()];
	}
}
