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
		String str = prop.getProperty("Fullscreen");
		fullScreen = Boolean.parseBoolean(str);
	}
	
	static private void loadMaxFps(Properties prop) {
		String str = prop.getProperty("MaxFPS");
		maxFps = Integer.parseInt(str);
		maxFps = (maxFps == 0) ? 5000 : maxFps;
	}
}
