package sound;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import configuration.ConfigManager;

public class SoundManager {
	private static float dstFactor = 100;
	private static Music backgroundMusic;
	private static Sound gunshot;
	private static Sound robotattack;
	static public  void init(){
		try {
			backgroundMusic = new Music("assets/sounds/soundeffects/bg1.ogg");
			backgroundMusic.loop();
			backgroundMusic.setVolume(ConfigManager.musicVolume);
			gunshot = new Sound("assets/sounds/soundeffects/gunshot.ogg");
			robotattack = new Sound("assets/sounds/soundeffects/robotattack.ogg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	static public void gunShot(float x, float y){
		gunshot.playAt(0.8f, ConfigManager.musicVolume, x/dstFactor, y/dstFactor, 0);
	}
	
	static public void robotAttack(float x, float y){
		robotattack.playAt(1f, ConfigManager.musicVolume, x/dstFactor, y/dstFactor, 0);
	}
	
	static public void destroy(){
	}
}
