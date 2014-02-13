package sound;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import configuration.ConfigManager;

public class SoundManager {
	
	private static Music backgroundMusic;
	private static Sound gunshot;
	static public  void init(){
		try {
			backgroundMusic = new Music("assets/souds/soundeffects/bg1.ogg");
			backgroundMusic.loop();
			backgroundMusic.setVolume(ConfigManager.musicVolume);
			gunshot = new Sound("assets/souds/soundeffects/gunshot.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static public void gunShot(float x, float y){
		gunshot.playAt(0.8f, ConfigManager.musicVolume, 0, 0, 0);
	}
}
