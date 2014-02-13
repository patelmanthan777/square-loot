package sound;

import game.GameLoop;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import configuration.ConfigManager;

public class SoundManager {
	private static float dstFactor = 100;
	private static Music backgroundMusic;
	private static Sound gunShot;
	private static Sound robotAttack;
	private static Sound explosion;
	private static Sound footStep;
	private static Sound robotPunched;
	private static Sound playerPunched;
	private static Sound coin;
	
	static public  void init(){
		try {
			backgroundMusic = new Music("assets/sounds/soundeffects/bg1.ogg");
			backgroundMusic.loop();
			backgroundMusic.setVolume(ConfigManager.musicVolume);
			gunShot = new Sound("assets/sounds/soundeffects/gunshot.ogg");
			robotAttack = new Sound("assets/sounds/soundeffects/robotattack.ogg");
			footStep = new Sound("assets/sounds/soundeffects/footstep.ogg");
			robotPunched = new Sound("assets/sounds/soundeffects/robotpunch.ogg");
			playerPunched = new Sound("assets/sounds/soundeffects/punch.ogg");
			explosion = new Sound("assets/sounds/soundeffects/explosion.ogg");
			coin = new Sound("assets/sounds/soundeffects/coin.ogg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	static public void gunShot(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		gunShot.playAt(0.8f, ConfigManager.musicVolume, relativeX, relativeY, 0);
	}
	
	static public void robotAttack(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		robotAttack.playAt(1f, ConfigManager.musicVolume, relativeX, relativeY, 0);
	}
	
	static public void footStep(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		footStep.playAt(1f, ConfigManager.musicVolume, relativeX, relativeY, 0);
	}
	
	static public void explosion(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		explosion.playAt(1f, ConfigManager.musicVolume*3f, relativeX, relativeY, 0);
	}
	
	static public void playerPunched(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		playerPunched.playAt(1f, ConfigManager.musicVolume, relativeX, relativeY, 0);
	}
	
	static public void robotPunched(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		robotPunched.playAt(1f, ConfigManager.musicVolume, relativeX, relativeY, 0);
	}
	
	static public void coin(float x, float y){
		float relativeX =  (x - GameLoop.cam.getX())/dstFactor;
		float relativeY =  (y - GameLoop.cam.getY())/dstFactor;
		coin.playAt(1f, ConfigManager.musicVolume, relativeX, relativeY, 0);
	}
	
	static public void destroy(){
	}
}
