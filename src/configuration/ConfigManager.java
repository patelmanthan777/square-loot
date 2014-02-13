package configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.lwjgl.util.vector.Vector2f;

public class ConfigManager {
	private static final String configFileName = "options.txt";
	
	
	public static boolean fullScreen = false;
	public static Vector2f resolution = new Vector2f(1280,720);
	public static int maxFps = 120;
	public static int unitPixelSize = 48;
	public static int oxygenTime = 10;
	public static float blockPhysicSize = 1;
	public static float playerAcc = 20;
	public static float robotAcc = 10;
	public static float ownPressureCoef = 500;
	public static float playerOxygenConsumption = 25;
	public static float musicVolume = 0.5f;
	
	/**
	 * Initialize the ConfigManager class by reading from the "options.txt"
	 * file.
	 */
	public static void init(){
		Properties prop = new Properties();	
		try{
			InputStream configFile  = new FileInputStream(configFileName);
			prop.load(configFile);
			loadResolution(prop);
			loadFullscreen(prop);
			loadMaxFps(prop);
			loadOxygenTime(prop);
			loadRobotAcc(prop);
			loadPlayerAcc(prop);
			loadOwnPressureCoef(prop);
			loadPlayerOxygenConsumption(prop);
			loadMusicVolume(prop);
		}catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	static private void loadResolution(Properties prop) {
		String str = prop.getProperty("Resolution");
		String [] resSplit = str.split("x");
		resolution.x = Float.parseFloat(resSplit[0]);
		resolution.y = Float.parseFloat(resSplit[1]);
	}
	
	static private void loadFullscreen(Properties prop) {
		String str = prop.getProperty("FullScreen");
		fullScreen = Boolean.parseBoolean(str);
	}
	
	static private void loadMaxFps(Properties prop) {
		String str = prop.getProperty("MaxFPS");
		maxFps = Integer.parseInt(str);
		maxFps = (maxFps == 0) ? 5000 : maxFps;
	}
	
	static private void loadOxygenTime(Properties prop) {
		String str = prop.getProperty("OxygenTime");
		oxygenTime = Integer.parseInt(str);
	}
	
	static private void loadRobotAcc(Properties prop) {
		String str = prop.getProperty("RobotAcc");
		robotAcc = Float.parseFloat(str);
	}
	
	static private void loadPlayerAcc(Properties prop) {
		String str = prop.getProperty("PlayerAcc");
		playerAcc = Float.parseFloat(str);
	}
	static private void loadOwnPressureCoef(Properties prop) {
		String str = prop.getProperty("OwnPressureCoef");
		ownPressureCoef = Float.parseFloat(str);
	}
	static private void loadPlayerOxygenConsumption(Properties prop) {
		String str = prop.getProperty("PlayerOxygenConsumption");
		playerOxygenConsumption = Float.parseFloat(str);
	}
	static private void loadMusicVolume(Properties prop) {
		String str = prop.getProperty("MusicVolume");
		musicVolume = Float.parseFloat(str);
	}
}
